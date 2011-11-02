package truesculpt.tools;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;
import truesculpt.mesh.Face;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.Vertex;
import truesculpt.utils.MatrixUtils;
import android.os.SystemClock;

public class RiseTool extends SculptingTool
{

	public RiseTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		long tSculptStart = SystemClock.uptimeMillis();
		super.Pick(xScreen, yScreen);
		int nIndex = getManagers().getMeshManager().Pick(xScreen, yScreen);

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

		RiseSculptAction(nIndex);
		long tSculptStop = SystemClock.uptimeMillis();
		mLastSculptDurationMs = tSculptStop - tSculptStart;
	}

	private void RiseSculptAction(int triangleIndex)
	{
		if (triangleIndex >= 0)
		{
			Mesh mesh = getManagers().getMeshManager().getMesh();
			float fMaxDeformation = getManagers().getToolsManager().getStrength() / 100.0f * MAX_DEFORMATION;// strength is -100 to 100

			Face face = mesh.mFaceList.get(triangleIndex);
			int nOrigVertex = face.E0.V0;// TODO choose closest point in
											// triangle from pick point
			Vertex origVertex = mesh.mVertexList.get(nOrigVertex);

			float sqMaxDist = (float) Math.pow((MAX_RADIUS - MIN_RADIUS) * getManagers().getToolsManager().getRadius() / 100f + MIN_RADIUS, 2);
			mesh.GetVerticesAtDistanceFromVertex(origVertex, sqMaxDist, verticesRes);
			float sigma = (float) ((Math.sqrt(sqMaxDist) / 1.5f) / FWHM);
			float maxGaussian = Gaussian(sigma, 0);

			// separate compute and apply of vertex pos otherwise compute is
			// false
			SculptAction action = new SculptAction();
			float[] VOffset = new float[3];
			float[] temp = new float[3];
			for (Vertex vertex : verticesRes)
			{
				MatrixUtils.copy(origVertex.Normal, VOffset);
				// MatrixUtils.copy(vertex.Normal, VOffset);

				MatrixUtils.minus(vertex.Coord, origVertex.Coord, temp);
				float sqDist = MatrixUtils.squaremagnitude(temp);

				// sculpting functions
				MatrixUtils.scalarMultiply(VOffset, (Gaussian(sigma, sqDist) / maxGaussian * fMaxDeformation));
				// if (MatrixUtils.magnitude(VOffset)>1e-3)
				{
					action.AddVertexOffset(vertex.Index, VOffset, vertex);
				}
			}
			getManagers().getActionsManager().AddUndoAction(action);
			action.DoAction();
		}
	}

	private float Gaussian(float sigma, float sqDist)
	{
		return (float) (oneoversqrttwopi / sigma * Math.exp(-sqDist / (2 * sigma * sigma)));
	}

}
