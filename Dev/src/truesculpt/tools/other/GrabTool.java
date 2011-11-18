package truesculpt.tools.other;

import java.util.HashSet;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.managers.ToolsManager.ESymmetryMode;
import truesculpt.mesh.Face;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.BaseTool;
import truesculpt.utils.MatrixUtils;

public class GrabTool extends BaseTool
{
	private Vertex mVOrigVertex = null;
	private int mnOrigVertex = -1;
	private boolean mbOrigSet = false;
	protected final HashSet<Vertex> mVerticesRes = new HashSet<Vertex>();
	protected final float[] VOffset = new float[3];
	protected final float[] VNormal = new float[3];
	protected final float[] VScreenXNormal = new float[3];
	protected final float[] VScreenYNormal = new float[3];
	protected final float[] temp = new float[3];
	private float mxOrig = -1;
	private float myOrig = -1;
	private final float mPixelRatio = 500;// for 1 meter displacement

	public GrabTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);

		mAction = new SculptAction();

		mVerticesRes.clear();
		mbOrigSet = false;
	}

	@Override
	protected void PickInternal(float xScreen, float yScreen, ESymmetryMode mode)
	{
		// init
		if (!mbOrigSet && mode == ESymmetryMode.NONE)
		{
			if (mMesh != null)
			{
				int nTriangleIndex = getManagers().getMeshManager().Pick(xScreen, yScreen, ESymmetryMode.NONE);
				if (nTriangleIndex >= 0)
				{
					Face face = mMesh.mFaceList.get(nTriangleIndex);
					mnOrigVertex = face.E0.V0;// TODO choose closest point in triangle from pick point
					mVOrigVertex = mMesh.mVertexList.get(mnOrigVertex);

					mMesh.GetVerticesAtDistanceFromSegment(mVOrigVertex, null, mSquareMaxDistance, mVerticesRes);
					mxOrig = xScreen;
					myOrig = yScreen;
					mbOrigSet = true;
				}
			}
		}

		// shaping
		if (mbOrigSet && mAction != null)
		{
			float distX = xScreen - mxOrig;
			float distY = -(yScreen - myOrig);// y is inverted compared to opengl

			// get screen plane in coord world
			getManagers().getMeshManager().GetWorldCoords(VScreenXNormal, xScreen, yScreen, 0);
			getManagers().getMeshManager().GetWorldCoords(temp, xScreen + 10, yScreen, 0);
			MatrixUtils.minus(temp, VScreenXNormal, VScreenXNormal);
			MatrixUtils.normalize(VScreenXNormal);

			getManagers().getMeshManager().GetWorldCoords(VScreenYNormal, xScreen, yScreen, 0);
			getManagers().getMeshManager().GetWorldCoords(temp, xScreen, yScreen - 10, 0);// y is inverted compared to opengl
			MatrixUtils.minus(temp, VScreenYNormal, VScreenYNormal);
			MatrixUtils.normalize(VScreenYNormal);

			for (Vertex vertex : mVerticesRes)
			{
				// only along normal at present time
				MatrixUtils.copy(vertex.Coord, VOffset);

				// Gaussian
				float newOffsetFactor = Gaussian(mSigma, vertex.mLastTempSqDistance) / mMaxGaussian;

				// Quadratic
				// newOffsetFactor = 1 - (vertex.mLastTempSqDistance / mSquareMaxDistance);

				// Linear
				// newOffsetFactor = (float) (1 - Math.sqrt(vertex.mLastTempSqDistance / mSquareMaxDistance));

				newOffsetFactor = MatrixUtils.saturateBetween0And1(newOffsetFactor);

				MatrixUtils.copy(VScreenXNormal, temp);
				MatrixUtils.scalarMultiply(temp, newOffsetFactor * distX / mPixelRatio);
				MatrixUtils.plus(VOffset, temp, VOffset);

				MatrixUtils.copy(VScreenYNormal, temp);
				MatrixUtils.scalarMultiply(temp, newOffsetFactor * distY / mPixelRatio);
				MatrixUtils.plus(VOffset, temp, VOffset);

				// Do only at the end to optimize memory usage, not needed for intermediary results
				((SculptAction) mAction).AddNewVertexValue(VOffset, vertex);

				// preview, todo preview normals
				MatrixUtils.copy(vertex.Normal, VNormal);
				MatrixUtils.scalarMultiply(VNormal, newOffsetFactor);
				for (RenderFaceGroup renderGroup : mMesh.mRenderGroupList)
				{
					renderGroup.UpdateVertexValue(vertex.Index, VOffset, VNormal);
				}
			}

			getManagers().getMeshManager().NotifyListeners();
		}

	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		super.Stop(xScreen, yScreen);

		mVerticesRes.clear();
	}

	@Override
	public int GetIcon()
	{
		return R.drawable.grab;
	}

	@Override
	public String GetName()
	{
		return "Grab";
	}

	@Override
	public boolean RequiresStrength()
	{
		return false;
	}

	@Override
	public boolean RequiresRadius()
	{
		return true;
	}

	@Override
	public boolean RequiresColor()
	{
		return false;
	}

	@Override
	public boolean RequiresSymmetry()
	{
		return true;
	}

}
