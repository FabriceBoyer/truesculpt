package truesculpt.tools;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;
import truesculpt.mesh.Face;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.SculptingTool;
import truesculpt.utils.MatrixUtils;
import android.util.Log;

public class RiseTool extends SculptingTool
{
	private final float[] VOffset = new float[3];
	private final float[] VNormal = new float[3];
	private SculptAction mAction = null;

	public RiseTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);

		mAction = new SculptAction();

		RiseSculptAction(xScreen, yScreen);
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		RiseSculptAction(xScreen, yScreen);

		if (mAction != null)
		{
			getManagers().getActionsManager().AddUndoAction(mAction);
			mAction.DoAction();
			mAction = null;
		}
		else
		{
			Log.e("RISETOOL", "Anormal Stop null action");
		}

		super.Stop(xScreen, yScreen);
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		super.Pick(xScreen, yScreen);

		RiseSculptAction(xScreen, yScreen);

		EndPick();
	}

	private void RiseSculptAction(float xScreen, float yScreen)
	{
		int triangleIndex = getManagers().getMeshManager().Pick(xScreen, yScreen);

		if (triangleIndex >= 0)
		{
			Mesh mesh = getManagers().getMeshManager().getMesh();

			float fMaxDeformation = getManagers().getToolsManager().getStrength() / 100.0f * MAX_DEFORMATION;// strength is -100 to 100

			Face face = mesh.mFaceList.get(triangleIndex);
			int nOrigVertex = face.E0.V0;// TODO choose closest point in triangle from pick point
			Vertex origVertex = mesh.mVertexList.get(nOrigVertex);

			float sqMaxDist = (float) Math.pow((MAX_RADIUS - MIN_RADIUS) * getManagers().getToolsManager().getRadius() / 100f + MIN_RADIUS, 2);

			verticesRes.clear();
			mesh.GetVerticesAtDistanceFromSegment(origVertex, mLastVertex, sqMaxDist, verticesRes);
			cumulatedVerticesRes.addAll(verticesRes);

			float sigma = (float) ((Math.sqrt(sqMaxDist) / 1.5f) / FWHM);
			float maxGaussian = Gaussian(sigma, 0);

			// preview only, no data mesh impact, only gpu data modified
			for (Vertex vertex : verticesRes)
			{
				MatrixUtils.copy(vertex.Normal, VOffset);
				MatrixUtils.copy(vertex.Normal, VNormal);
				// sculpting
				MatrixUtils.scalarMultiply(VOffset, (Gaussian(sigma, vertex.mLastTempSqDistance) / maxGaussian * fMaxDeformation));
				if (mAction != null)
				{
					mAction.AddVertexOffset(vertex.Index, VOffset, vertex);
				}
				else
				{
					Log.e("RISETOOL", "Anormal Pick null action");
				}

				// preview
				MatrixUtils.plus(VOffset, vertex.Coord, VOffset);
				MatrixUtils.scalarMultiply(VNormal, vertex.mLastTempSqDistance / sqMaxDist);
				for (RenderFaceGroup renderGroup : mesh.mRenderGroupList)
				{
					renderGroup.UpdateVertexValue(vertex.Index, VOffset, VNormal);
				}
			}
			mLastVertex = origVertex;

			getManagers().getMeshManager().NotifyListeners();
		}
	}
}

// switch (getManagers().getToolsManager().getSymmetryMode())
// {
// case X:
// rayPt1[0] *= -1;
// rayPt2[0] *= -1;
// PickRay();
// rayPt1[0] *= -1;
// rayPt2[0] *= -1;
// nIndex = PickRay();
// break;
// case Y:
// rayPt1[1] *= -1;
// rayPt2[1] *= -1;
// PickRay();
// rayPt1[1] *= -1;
// rayPt2[1] *= -1;
// nIndex = PickRay();
// break;
// case Z:
// rayPt1[2] *= -1;
// rayPt2[2] *= -1;
// PickRay();
// rayPt1[2] *= -1;
// rayPt2[2] *= -1;
// nIndex = PickRay();
// break;
// case NONE:
// nIndex = PickRay();
// break;
// }
// }
