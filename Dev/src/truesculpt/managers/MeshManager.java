package truesculpt.managers;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import truesculpt.mesh.Mesh;
import truesculpt.renderer.PickHighlight;
import truesculpt.renderer.RayPickDebug;
import truesculpt.utils.MatrixUtils;
import android.content.Context;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

//for mesh storage, computation and transformation application
public class MeshManager extends BaseManager
{
	private String Name="";
	private boolean bInitOver = true;
	float[] intersectPt = new float[3];
	
	class MeshInitTash implements Runnable
	{
		public int nSubdivionLevel=0;
		@Override
		public void run()
		{
			NewMeshBlocking(nSubdivionLevel);						
		}
	}
	
	public void NewMeshBlocking(int nSubdivionLevel)
	{
		bInitOver=false;
		
		mMesh=new Mesh(getManagers(),nSubdivionLevel);
		
		Name="MyTrueSculpture";
		
		bInitOver = true;
	}
	
	MeshInitTash mInitTask = new MeshInitTash();	

	long mLastPickDurationMs = -1;
	long mLastSculptDurationMs = -1;
	private float[] mModelView = new float[16];	
	private PickHighlight mPickHighlight = new PickHighlight();
	private float[] mProjection = new float[16];
	private RayPickDebug mRay = new RayPickDebug();	
	private int[] mViewPort = new int[4];
	float[] rayPt1 = new float[3];
	float[] rayPt2 = new float[3];

	// Main Mesh test
	Mesh mMesh = null;
	
	public Mesh getMesh()
	{
		return mMesh;
	}

	public MeshManager(Context baseContext)
	{
		super(baseContext);
		
		intersectPt[0]=0f;
		intersectPt[1]=0f;
		intersectPt[2]=1f;
	}

	public void draw(GL10 gl)
	{
		synchronized (this)
		{
			if (mMesh != null  && bInitOver)
			{
				mMesh.draw(gl);
				
				if (getManagers().getOptionsManager().getDisplayDebugInfos())
				{
					mMesh.drawNormals(gl);
					mMesh.drawOctree(gl);
					
					//pick debug
					//mRay.draw(gl);
					//mPickHighlight.draw(gl);
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
	
	public long getLastSculptDurationMs()
	{
		return mLastSculptDurationMs;
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



	@Override
	public void onCreate()
	{
		NewMeshThreaded(5);
	}
	
	public boolean NewMeshThreaded(int nSubdivionLevel)
	{
		boolean bRes=false;
		mInitTask.nSubdivionLevel=nSubdivionLevel;
		
		if (bInitOver)
		{
			Thread thr = new Thread(null, mInitTask, "Mesh_Init");
			thr.start();
			bRes=true;
		}
		
		return bRes;
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
		if (mMesh!=null)
		{
			synchronized (this)
			{	
				GetWorldCoords(rayPt2, screenX, screenY, 1.0f);// normalized z between -1 and 1
				GetWorldCoords(rayPt1, screenX, screenY, -1.0f);
	
				mRay.setRayPos(rayPt1, rayPt2);
	
				//handle symmetry
				if (bInitOver)
				{					
					switch (getManagers().getToolsManager().getSymmetryMode())
					{
					case X:
						rayPt1[0]*=-1;
						rayPt2[0]*=-1;						
						PickRay();
						rayPt1[0]*=-1;
						rayPt2[0]*=-1;
						nIndex=PickRay();
						break;
					case Y:
						rayPt1[1]*=-1;
						rayPt2[1]*=-1;
						PickRay();
						rayPt1[1]*=-1;
						rayPt2[1]*=-1;
						nIndex=PickRay();
						break;
					case Z:
						rayPt1[2]*=-1;
						rayPt2[2]*=-1;
						PickRay();
						rayPt1[2]*=-1;
						rayPt2[2]*=-1;
						nIndex=PickRay();
						break;			
					case NONE:
						nIndex=PickRay();
						break;
					}
				}
				
				NotifyListeners();	
			}
		}

		return nIndex;
	}
	
	private int PickRay()
	{
		long tPickStart = SystemClock.uptimeMillis();
		int nIndex = mMesh.Pick(rayPt1, rayPt2,intersectPt);
		long tPickStop = SystemClock.uptimeMillis();
		mLastPickDurationMs = tPickStop - tPickStart;
		
		if (nIndex >= 0)
		{
			mPickHighlight.setPickHighlightPosition(intersectPt);

			long tSculptStart = SystemClock.uptimeMillis();
			// TODO place in actionManager
			switch (getManagers().getToolsManager().getToolMode())
			{
				case SCULPT:
				{
					switch (getManagers().getToolsManager().getSculptSubMode())
					{
					case DRAW:
						mMesh.RiseSculptAction(nIndex);
						break;
					case GRAB:
						mMesh.InitGrabAction(nIndex);
						break;	
					case SMOOTH:
						break;
					case COLOR:
						mMesh.ColorizePaintAction(nIndex);
						break;
					case TEXTURE:
						break;
					case PICK_COLOR:
						mMesh.PickColorAction(nIndex);
						break;								
					}
				}
			}		
			long tSculptStop = SystemClock.uptimeMillis();
			mLastSculptDurationMs = tSculptStop - tSculptStart;
		}
		else
		{
			float[] zero = { 0, 0, 0 };
			mPickHighlight.setPickHighlightPosition(zero);
		}	
		return nIndex;
	}


	// TODO test for GL11 instance of to handle not GL11 devices
	// TODO use GL11ES calls independent of redraw with gl param
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
	
	public void getLastPickingPoint(float [] point)
	{
		MatrixUtils.copy(intersectPt, point);
	}

	public void setName(String name)
	{
		Name = name;
	}

	public String getName()
	{
		return Name;
	}
	
	public boolean IsInitOver()
	{		
		return bInitOver;
	}
}
