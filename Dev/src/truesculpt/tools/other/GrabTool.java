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
	protected final float[] VScreenNormal = new float[3];
	private float mxOrig = -1;
	private float myOrig = -1;

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
		if (!mbOrigSet)
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
			float dist = (float) Math.sqrt((xScreen - mxOrig) * (xScreen - mxOrig) + (yScreen - myOrig) * (yScreen - myOrig));

			getManagers().getMeshManager().getPickRayVector(VScreenNormal);
			float vectProd = 1 - MatrixUtils.dot(VScreenNormal, mVOrigVertex.Normal);

			for (Vertex vertex : mVerticesRes)
			{
				// only along normal at present time
				MatrixUtils.copy(mVOrigVertex.Normal, VOffset);
				MatrixUtils.normalize(VOffset);
				MatrixUtils.scalarMultiply(VOffset, vectProd * (1 - vertex.mLastTempSqDistance / mSquareMaxDistance) * dist / 500);

				// Do only at the end
				((SculptAction) mAction).AddNewVertexValue(VOffset, vertex);

				// preview
				MatrixUtils.plus(VOffset, vertex.Coord, VOffset);
				MatrixUtils.copy(vertex.Normal, VNormal);
				MatrixUtils.scalarMultiply(VNormal, 1 - vertex.mLastTempSqDistance / mSquareMaxDistance);
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

}
