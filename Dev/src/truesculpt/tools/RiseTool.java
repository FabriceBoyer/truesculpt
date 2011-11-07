package truesculpt.tools;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;
import truesculpt.mesh.Face;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.SculptingTool;
import truesculpt.utils.MatrixUtils;

public class RiseTool extends SculptingTool
{
	private final float[] VOffset = new float[3];
	private final float[] temp = new float[3];
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
		super.Stop(xScreen, yScreen);

		RiseSculptAction(xScreen, yScreen);

		Mesh mesh = getManagers().getMeshManager().getMesh();

		// final value
		for (Vertex vertex : cumulatedVerticesRes)
		{
			for (RenderFaceGroup renderGroup : mesh.mRenderGroupList)
			{
				vertex.mLastTempSqDistance = -1.f;// reinit
				// renderGroup.UpdateVertexValue(vertex.Index, vertex.Coord);
			}
		}

		// getManagers().getActionsManager().AddUndoAction(action);
		// mAction.DoAction();

		mAction = null;

		getManagers().getMeshManager().NotifyListeners();
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
			if (mLastVertex != null)
			{
				mesh.GetVerticesAtDistanceFromSegment(origVertex, mLastVertex, sqMaxDist, verticesRes);
			}
			else
			{
				mesh.GetVerticesAtDistanceFromVertex(origVertex, sqMaxDist, verticesRes);
			}
			cumulatedVerticesRes.addAll(verticesRes);

			float sigma = (float) ((Math.sqrt(sqMaxDist) / 1.5f) / FWHM);
			float maxGaussian = Gaussian(sigma, 0);

			// preview only, no data mesh impact, only gpu data modified
			for (Vertex vertex : verticesRes)
			{
				MatrixUtils.copy(vertex.Normal, VOffset);

				// sculpting
				MatrixUtils.scalarMultiply(VOffset, (Gaussian(sigma, vertex.mLastTempSqDistance) / maxGaussian * fMaxDeformation));
				MatrixUtils.plus(VOffset, vertex.Coord, VOffset);

				for (RenderFaceGroup renderGroup : mesh.mRenderGroupList)
				{
					renderGroup.UpdateVertexValue(vertex.Index, VOffset);
				}
			}

			// for (Vertex vertex : verticesRes)
			// {
			// MatrixUtils.copy(origVertex.Normal, VOffset);
			// // MatrixUtils.copy(vertex.Normal, VOffset);
			//
			// MatrixUtils.minus(vertex.Coord, origVertex.Coord, temp);
			// float sqDist = MatrixUtils.squaremagnitude(temp);
			//
			// // sculpting functions
			// MatrixUtils.scalarMultiply(VOffset, (Gaussian(sigma, sqDist) / maxGaussian * fMaxDeformation));
			// // if (MatrixUtils.magnitude(VOffset)>1e-3)
			// {
			// action.AddVertexOffset(vertex.Index, VOffset, vertex);
			// }
			// }

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
