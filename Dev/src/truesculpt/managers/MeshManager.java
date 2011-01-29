package truesculpt.managers;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Collection;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import truesculpt.mesh.Mesh;
import truesculpt.renderer.Face;
import truesculpt.renderer.GeneratedObject;
import truesculpt.renderer.NodeRelation;
import truesculpt.renderer.NodeRelationList;
import truesculpt.renderer.PickHighlight;
import truesculpt.renderer.RayPickDebug;
import truesculpt.utils.MatrixUtils;
import truesculpt.utils.Utils;
import android.content.Context;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

//for mesh storage, computation and transformation application
public class MeshManager extends BaseManager
{
	// ray vectors
	static float[] dir = new float[3];
	static float[] n = new float[3];

	static float SMALL_NUM = 0.00000001f; // anything that avoids division

	// overflow
	// triangle vectors
	static float[] u = new float[3];
	static float[] v = new float[3];
	static float[] w = new float[3];

	static float[] w0 = new float[3];
	static float[] zero = { 0, 0, 0 };

	// intersect_RayTriangle(): intersect a ray with a 3D triangle
	// Input: a ray R (R0 and R1), and a triangle T (V0,V1)
	// Output: *I = intersection point (when it exists)
	// Return: -1 = triangle is degenerate (a segment or point)
	// 0 = disjoint (no intersect)
	// 1 = intersect in unique point I1
	// 2 = are in the same plane
	static int intersect_RayTriangle(float[] R0, float[] R1, float[] V0, float[] V1, float[] V2, float[] Ires)
	{
		float r, a, b; // params to calc ray-plane intersect

		// get triangle edge vectors and plane normal
		MatrixUtils.minus(V1, V0, u);
		MatrixUtils.minus(V2, V0, v);

		MatrixUtils.cross(u, v, n); // cross product
		if (n == zero)
		{
			return -1; // do not deal with this case
		}

		MatrixUtils.minus(R1, R0, dir); // ray direction vector

		boolean bBackCullTriangle = MatrixUtils.dot(dir, n) > 0;// ray dir and normal have same direction
		if (bBackCullTriangle)
		{
			return 0;
		}

		MatrixUtils.minus(R0, V0, w0);
		a = -MatrixUtils.dot(n, w0);
		b = MatrixUtils.dot(n, dir);
		if (Math.abs(b) < SMALL_NUM)
		{ // ray is parallel to triangle plane
			if (a == 0)
			{
				return 2;
			} else
			{
				return 0; // ray disjoint from plane
			}
		}

		// get intersect point of ray with triangle plane
		r = a / b;
		if (r < 0.0)
		{
			return 0; // => no intersect
			// for a segment, also test if (r > 1.0) => no intersect
		}

		MatrixUtils.scalarMultiply(dir, r);
		MatrixUtils.plus(R0, dir, Ires);

		// is I inside T?
		float uu, uv, vv, wu, wv, D;
		uu = MatrixUtils.dot(u, u);
		uv = MatrixUtils.dot(u, v);
		vv = MatrixUtils.dot(v, v);
		MatrixUtils.minus(Ires, V0, w);
		wu = MatrixUtils.dot(w, u);
		wv = MatrixUtils.dot(w, v);
		D = uv * uv - uu * vv;

		// get and test parametric coords
		float s, t;
		s = (uv * wv - vv * wu) / D;
		if (s < 0.0 || s > 1.0)
		{
			return 0;
		}
		t = (uv * wu - uu * wv) / D;
		if (t < 0.0 || s + t > 1.0)
		{
			return 0;
		}

		return 1; // I is in T
	}

	private boolean bInitOver = false;

	float[] intersectPt = new float[3];
	FloatBuffer mColorBuffer = null;
	private HashMap<Integer, Face> mFaceMap = new HashMap<Integer, Face>();
	ShortBuffer mIndexBuffer = null;
	float mBoundingSphereRadius = 1.0f;
	float[] mBoundingSphereVector = new float[3];

	Runnable mInitTask = new Runnable()
	{
		@Override
		public void run()
		{
			mObject = new GeneratedObject(getManagers().getToolsManager().getColor(), 4);

			mIndexBuffer = mObject.getIndexBuffer();
			mColorBuffer = mObject.getColorBuffer();
			mNormalBuffer = mObject.getNormalBuffer();
			mVertexBuffer = mObject.getVertexBuffer();

			BuildRelationMapFromMesh();
			BuildFaceMapFromMesh();

			bInitOver = true;

			mHandler.post(mNotifyTask); // to come back in UI thread
		}
	};

	private Handler mHandler = new Handler();
	Runnable mNotifyTask = new Runnable()
	{
		@Override
		public void run()
		{
			ComputeBoundingSphereRadius();
			NotifyListeners();
		}
	};

	long mLastPickDurationMs = -1;
	private float[] mModelView = new float[16];
	private HashMap<Integer, NodeRelationList> mNodeRelationMap = new HashMap<Integer, NodeRelationList>();
	FloatBuffer mNormalBuffer = null;
	private GeneratedObject mObject = null;
	private PickHighlight mPickHighlight = new PickHighlight();
	private float[] mProjection = new float[16];
	private RayPickDebug mRay = new RayPickDebug();
	FloatBuffer mVertexBuffer = null;
	private int[] mViewPort = new int[4];
	float[] rayPt1 = new float[3];
	float[] rayPt2 = new float[3];

	// Main Mesh test
	Mesh mMesh = null;

	public MeshManager(Context baseContext)
	{
		super(baseContext);
	}

	private void AddFaceRelationToMap(int nVertexIndexOrig, int triangleIndex)
	{
		NodeRelationList relationList = mNodeRelationMap.get(nVertexIndexOrig);
		if (relationList == null)
		{
			relationList = new NodeRelationList();
			mNodeRelationMap.put(nVertexIndexOrig, relationList);
		}
		relationList.AddFaceRelation(triangleIndex);
	}

	private void AddVertexRelationToMap(int nVertexIndexOrig, int nVertexIndexOther, float fDistance)
	{
		NodeRelationList relationList = mNodeRelationMap.get(nVertexIndexOrig);
		if (relationList == null)
		{
			relationList = new NodeRelationList();
			mNodeRelationMap.put(nVertexIndexOrig, relationList);
		}
		relationList.AddVertexRelation(nVertexIndexOther, fDistance);
	}

	private void BuildFaceMapFromMesh()
	{
		int nIndexCount = mIndexBuffer.capacity();

		for (int i = 0; i < nIndexCount; i = i + 3)
		{
			Face face = new Face();

			int nIndex0 = 3 * mIndexBuffer.get(i);
			mVertexBuffer.position(nIndex0);
			mVertexBuffer.get(face.V0, 0, 3);

			int nIndex1 = 3 * mIndexBuffer.get(i + 1);
			mVertexBuffer.position(nIndex1);
			mVertexBuffer.get(face.V1, 0, 3);

			int nIndex2 = 3 * mIndexBuffer.get(i + 2);
			mVertexBuffer.position(nIndex2);
			mVertexBuffer.get(face.V2, 0, 3);

			face.nIndex0 = nIndex0;
			face.nIndex1 = nIndex1;
			face.nIndex2 = nIndex2;

			mFaceMap.put(i, face);
			mFaceMap.put(i + 1, face);
			mFaceMap.put(i + 2, face);
		}

		mIndexBuffer.position(0);
		mVertexBuffer.position(0);
	}

	private void BuildRelationMapFromMesh()
	{
		float[] V0 = new float[3];
		float[] V1 = new float[3];
		float[] V2 = new float[3];
		float[] VDiff = new float[3];

		int nIndexCount = mIndexBuffer.capacity();

		for (int i = 0; i < nIndexCount; i = i + 3)
		{
			int nIndex0 = 3 * mIndexBuffer.get(i);
			mVertexBuffer.position(nIndex0);
			mVertexBuffer.get(V0, 0, 3);

			int nIndex1 = 3 * mIndexBuffer.get(i + 1);
			mVertexBuffer.position(nIndex1);
			mVertexBuffer.get(V1, 0, 3);

			int nIndex2 = 3 * mIndexBuffer.get(i + 2);
			mVertexBuffer.position(nIndex2);
			mVertexBuffer.get(V2, 0, 3);

			// 0 to 1
			MatrixUtils.minus(V1, V0, VDiff);
			float fDist1 = MatrixUtils.magnitude(VDiff);
			AddVertexRelationToMap(nIndex0, nIndex1, fDist1);
			AddVertexRelationToMap(nIndex1, nIndex0, fDist1);

			// 1 to 2
			MatrixUtils.minus(V2, V1, VDiff);
			float fDist2 = MatrixUtils.magnitude(VDiff);
			AddVertexRelationToMap(nIndex1, nIndex2, fDist2);
			AddVertexRelationToMap(nIndex2, nIndex1, fDist2);

			// 2 to 0
			MatrixUtils.minus(V0, V2, VDiff);
			float fDist3 = MatrixUtils.magnitude(VDiff);
			AddVertexRelationToMap(nIndex2, nIndex0, fDist3);
			AddVertexRelationToMap(nIndex0, nIndex2, fDist3);

			// same faces for every point
			AddFaceRelationToMap(nIndex0, i);
			AddFaceRelationToMap(nIndex1, i);
			AddFaceRelationToMap(nIndex2, i);
		}

		mIndexBuffer.position(0);
		mVertexBuffer.position(0);
	}

	// TODO place as an action
	private void ColorizePaintAction(int triangleIndex)
	{
		if (triangleIndex >= 0)
		{
			float[] VColor = new float[4];

			int color = getManagers().getToolsManager().getColor();
			Utils.ColorIntToFloatVector(color, VColor);
			VColor[3] = 1.0f;// no alpha

			int nIndex0 = 4 * mIndexBuffer.get(triangleIndex);
			mColorBuffer.position(nIndex0);
			mColorBuffer.put(VColor, 0, 4);

			int nVertexIndex0 = 3 * mIndexBuffer.get(triangleIndex);

			if (getManagers().getToolsManager().getRadius() >= 50)
			{
				// First corona
				NodeRelationList list = mNodeRelationMap.get(nVertexIndex0);
				for (Integer otherTriangle : list.mFaceRelationList)
				{
					nIndex0 = 4 * mIndexBuffer.get(otherTriangle);
					mColorBuffer.position(nIndex0);
					mColorBuffer.put(VColor, 0, 4);

					int nIndex1 = 4 * mIndexBuffer.get(otherTriangle + 1);
					mColorBuffer.position(nIndex1);
					mColorBuffer.put(VColor, 0, 4);

					int nIndex2 = 4 * mIndexBuffer.get(otherTriangle + 2);
					mColorBuffer.position(nIndex2);
					mColorBuffer.put(VColor, 0, 4);
				}
			}

			mIndexBuffer.position(0);
			mColorBuffer.position(0);
		}
	}

	void ComputeBoundingSphereRadius()
	{
		mBoundingSphereRadius = 0.0f;
		Collection<Face> list = mFaceMap.values();
		for (Face face : list)
		{
			float norm = MatrixUtils.magnitude(face.V0);
			if (norm > mBoundingSphereRadius)
			{
				MatrixUtils.copy(face.V0, mBoundingSphereVector);
			}

			norm = MatrixUtils.magnitude(face.V1);
			if (norm > mBoundingSphereRadius)
			{
				MatrixUtils.copy(face.V1, mBoundingSphereVector);
			}

			norm = MatrixUtils.magnitude(face.V2);
			if (norm > mBoundingSphereRadius)
			{
				MatrixUtils.copy(face.V2, mBoundingSphereVector);
			}
		}

		float finalNorm = MatrixUtils.magnitude(mBoundingSphereVector);
		mBoundingSphereRadius = finalNorm;

		getManagers().getPointOfViewManager().setRmin(1 + mBoundingSphereRadius);// takes near clip into accoutn, TODO read from conf
	}

	private void ComputeNormalOfTriangle(int nTriangleIndex, float[] normal)
	{
		Face face = mFaceMap.get(nTriangleIndex);

		// get triangle edge vectors and plane normal
		MatrixUtils.minus(face.V1, face.V0, u);
		MatrixUtils.minus(face.V2, face.V0, v);

		MatrixUtils.cross(u, v, n); // cross product
		MatrixUtils.normalize(n);

		MatrixUtils.copy(n, normal);
	}

	public void draw(GL10 gl)
	{
		synchronized (this)
		{
			if (mMesh != null)
			{
				mMesh.draw(gl);
			}

			if (mObject != null && bInitOver)
			{
				mObject.draw(gl);

				if (getManagers().getOptionsManager().getDisplayDebugInfos())
				{
					mObject.drawNormals(gl);
					mRay.draw(gl);
					mPickHighlight.draw(gl);
				}
			}

		}
	}

	public int getFacesCount()
	{
		int nCount = -1;
		if (mMesh != null)
		{
			nCount = mMesh.getFaceCount();
		}
		return nCount;
	}

	public long getLastPickDurationMs()
	{
		return mLastPickDurationMs;
	}

	int GetPickedTriangleIndex()
	{
		int nRes = -1;

		float[] R0 = new float[3];
		float[] R1 = new float[3];
		float[] Ires = new float[3];

		MatrixUtils.copy(rayPt1, R0);
		MatrixUtils.copy(rayPt2, R1);

		MatrixUtils.minus(R1, R0, dir);
		float fSmallestDistanceToR0 = MatrixUtils.magnitude(dir);// ray is R0 to R1

		int nIndexCount = mIndexBuffer.capacity();
		for (int i = 0; i < nIndexCount; i = i + 3)
		{
			Face face = mFaceMap.get(i);

			int nCollide = intersect_RayTriangle(R0, R1, face.V0, face.V1, face.V2, Ires);

			if (nCollide == 1)
			{
				MatrixUtils.minus(Ires, R0, dir);
				float fDistanceToR0 = MatrixUtils.magnitude(dir);
				if (fDistanceToR0 <= fSmallestDistanceToR0)
				{
					MatrixUtils.copy(Ires, intersectPt);
					nRes = i;
					fSmallestDistanceToR0 = fDistanceToR0;
				}
			}
		}

		mIndexBuffer.position(0);
		mVertexBuffer.position(0);

		return nRes;
	}

	public int getVertexCount()
	{
		int nCount = -1;
		if (mMesh != null)
		{
			nCount = mMesh.getVertexCount();
		}
		return nCount;
	}

	/**
	 * Calculates the transform from screen coordinate system to world coordinate system coordinates for a specific point, given a camera position.
	 * 
	 * @return position in WCS.
	 */
	public void GetWorldCoords(float[] worldPos, float touchX, float touchY, float z)
	{
		// SCREEN height & width (ej: 320 x 480)
		float screenW = mViewPort[2];
		float screenH = mViewPort[3];

		// Auxiliary matrix and vectors
		// to deal with ogl.
		float[] invertedMatrix, transformMatrix, normalizedInPoint, outPoint;
		invertedMatrix = new float[16];
		transformMatrix = new float[16];
		normalizedInPoint = new float[4];
		outPoint = new float[4];

		// Invert y coordinate, as android uses
		// top-left, and ogl bottom-left.
		int oglTouchY = (int) (screenH - touchY);

		/*
		 * Transform the screen point to clip space in ogl (-1,1)
		 */
		normalizedInPoint[0] = (float) (touchX * 2.0f / screenW - 1.0);
		normalizedInPoint[1] = (float) (oglTouchY * 2.0f / screenH - 1.0);
		normalizedInPoint[2] = z;
		normalizedInPoint[3] = 1.0f;

		/*
		 * Obtain the transform matrix and then the inverse.
		 */
		// MatrixUtils.PrintMat("Proj", mProjection);
		// MatrixUtils.PrintMat("Model", mModelView);
		Matrix.multiplyMM(transformMatrix, 0, mProjection, 0, mModelView, 0);
		Matrix.invertM(invertedMatrix, 0, transformMatrix, 0);

		/*
		 * Apply the inverse to the point in clip space
		 */
		Matrix.multiplyMV(outPoint, 0, invertedMatrix, 0, normalizedInPoint, 0);

		if (outPoint[3] == 0.0)
		{
			// Avoid /0 error.
			Log.e("World coords", "ERROR!");
			return;
		}

		// Divide by the 3rd component to find
		// out the real position.
		worldPos[0] = outPoint[0] / outPoint[3];
		worldPos[1] = outPoint[1] / outPoint[3];
		worldPos[2] = outPoint[2] / outPoint[3];
	}

	private void InitMorphAction(int nTriangleIndex)
	{

	}

	private void NotifyListeners()
	{
		setChanged();
		notifyObservers(this);
	}

	@Override
	public void onCreate()
	{
		// Thread thr = new Thread(null, mInitTask, "Mesh_Init");
		// thr.start();

		// mMesh= new Mesh(getManagers());
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub

	}

	// TODO threaded to improve GUI reactivity
	// pick is not an action
	public int Pick(float screenX, float screenY)
	{
		int nIndex = -1;
		synchronized (this)
		{

			long tStart = SystemClock.uptimeMillis();

			GetWorldCoords(rayPt2, screenX, screenY, 1.0f);// normalized z between -1 and 1
			GetWorldCoords(rayPt1, screenX, screenY, -1.0f);

			mRay.setRayPos(rayPt1, rayPt2);

			// String msg = "";
			// msg="Pt1 : x="+Float.toString(rayPt1[0])+"; y="+Float.toString(rayPt1[1])+"; z="+Float.toString(rayPt1[2])+"\n";
			// msg+=
			// "Pt2 : x="+Float.toString(rayPt2[0])+"; y="+Float.toString(rayPt2[1])+"; z="+Float.toString(rayPt2[2])+"\n";
			// Log.i("Picking",msg);

			if (bInitOver)
			{

				nIndex = GetPickedTriangleIndex();
				if (nIndex >= 0)
				{
					mPickHighlight.setPickHighlightPosition(intersectPt);

					// TODO place in actionManager
					switch (getManagers().getToolsManager().getToolMode())
					{
					case SCULPT:
					{
						switch (getManagers().getToolsManager().getSculptSubMode())
						{
						case RISE:
							RiseSculptAction(nIndex);
							break;
						case MORPH:
							InitMorphAction(nIndex);
							break;
						}
						break;
					}
					case PAINT:
					{
						ColorizePaintAction(nIndex);
						break;
					}
					}

					// msg = "Picked Triangle Index =" +
					// Integer.toString(nIndex) + "\n";
					// msg += "intersectPt : x=" +
					// Float.toString(intersectPt[0]) + "; y=" +
					// Float.toString(intersectPt[1]) + "; z=" +
					// Float.toString(intersectPt[2]) + "\n";
					// Log.i("Picking", msg);
				} else
				{
					mPickHighlight.setPickHighlightPosition(zero);
				}

			}

			NotifyListeners();

			long tStop = SystemClock.uptimeMillis();
			mLastPickDurationMs = tStop - tStart;
			// msg="Picking duration = "+Float.toString(mLastPickDurationMs)+" ms\n";
			// Log.i("Picking", msg);
		}

		return nIndex;
	}

	// TODO place as an action
	private void RiseSculptAction(int triangleIndex)
	{
		if (triangleIndex >= 0)
		{
			float[] V0 = new float[3];
			float[] VTemp = new float[3];
			float[] nOffset = new float[3];

			// first triangle point arbitrarily chosen (should take closest or retessalate)
			int nIndex0 = 3 * mIndexBuffer.get(triangleIndex);
			mVertexBuffer.position(nIndex0);
			mVertexBuffer.get(V0, 0, 3);

			float fMaxDeformation = getManagers().getToolsManager().getStrength() / 100.0f * 0.2f;// strength is -100 to 100

			mNormalBuffer.position(nIndex0);
			mNormalBuffer.get(nOffset, 0, 3);

			MatrixUtils.scalarMultiply(nOffset, fMaxDeformation);

			MatrixUtils.plus(V0, nOffset, V0);
			UpdateVertexBufferValue(nIndex0, V0);

			if (getManagers().getToolsManager().getRadius() >= 50)
			{
				// First corona

				// Rise point
				NodeRelationList list = mNodeRelationMap.get(nIndex0);
				for (NodeRelation relation : list.mVertexRelationList)
				{
					int nOtherIndex = relation.mOtherIndex;

					// TODO read from map
					mVertexBuffer.position(nOtherIndex);
					mVertexBuffer.get(VTemp, 0, 3);

					MatrixUtils.scalarMultiply(nOffset, 1 / 2.0f);

					MatrixUtils.plus(VTemp, nOffset, VTemp);

					UpdateVertexBufferValue(nOtherIndex, VTemp);
				}

				// update normals after rise up
				for (NodeRelation relation : list.mVertexRelationList)
				{
					int nOtherIndex = relation.mOtherIndex;
					UpdateVertexNormal(nOtherIndex);
				}
			}

			UpdateVertexNormal(nIndex0);

			mIndexBuffer.position(0);
			mVertexBuffer.position(0);
			mNormalBuffer.position(0);
		}
	}

	// TODO test for GL11 instanceof to handle not GL11 devices
	// TODO use GL11ES calls indepedent of redraw with gl param
	public void setCurrentModelView(GL10 gl)
	{
		GL11 gl2 = (GL11) gl;
		gl2.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, mModelView, 0);
	}

	public void setCurrentProjection(GL10 gl)
	{
		GL11 gl2 = (GL11) gl;
		gl2.glGetFloatv(GL11.GL_PROJECTION_MATRIX, mProjection, 0);
	}

	public void setViewport(GL10 gl)
	{
		GL11 gl2 = (GL11) gl;
		gl2.glGetIntegerv(GL11.GL_VIEWPORT, mViewPort, 0);
	}

	void UpdateBoudingSphereRadius(float[] val)
	{
		float norm = MatrixUtils.magnitude(val);
		if (norm > mBoundingSphereRadius)
		{
			mBoundingSphereRadius = norm;
			MatrixUtils.copy(val, mBoundingSphereVector);
			getManagers().getPointOfViewManager().setRmin(1 + mBoundingSphereRadius);// takes near clip into accoutn, TODO read from conf
		} else
		{
			// detect if same as limit and recompute to decrement vector if necessary (beware severals vectors can be the max)
		}
	}

	// handles bytearray and map cache
	private void UpdateVertexBufferValue(int nVertexIndex, float[] Value)
	{
		mVertexBuffer.position(nVertexIndex);
		mVertexBuffer.put(Value, 0, 3);
		mVertexBuffer.position(0);

		NodeRelationList list = mNodeRelationMap.get(nVertexIndex);
		for (Integer triangleIndex : list.mFaceRelationList)
		{
			Face face = mFaceMap.get(triangleIndex);

			int nIndex0 = 3 * mIndexBuffer.get(triangleIndex);
			mVertexBuffer.position(nIndex0);
			mVertexBuffer.get(face.V0, 0, 3);

			int nIndex1 = 3 * mIndexBuffer.get(triangleIndex + 1);
			mVertexBuffer.position(nIndex1);
			mVertexBuffer.get(face.V1, 0, 3);

			int nIndex2 = 3 * mIndexBuffer.get(triangleIndex + 2);
			mVertexBuffer.position(nIndex2);
			mVertexBuffer.get(face.V2, 0, 3);
		}

		UpdateBoudingSphereRadius(Value);
	}

	private void UpdateVertexNormal(int nVertexIndex)
	{
		float[] VNormal = { 0, 0, 0 };
		float[] VTempNormal = new float[3];

		// averaging normals of triangles around
		NodeRelationList list = mNodeRelationMap.get(nVertexIndex);
		for (Integer triangleIndex : list.mFaceRelationList)
		{
			ComputeNormalOfTriangle(triangleIndex, VTempNormal);
			MatrixUtils.plus(VNormal, VTempNormal, VNormal);
		}

		MatrixUtils.normalize(VNormal);

		mNormalBuffer.position(nVertexIndex);
		mNormalBuffer.put(VNormal, 0, 3);

		mNormalBuffer.position(0);
	}

}
