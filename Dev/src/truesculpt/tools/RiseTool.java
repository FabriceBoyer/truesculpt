package truesculpt.tools;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.SculptingTool;
import truesculpt.utils.MatrixUtils;

public class RiseTool extends SculptingTool
{
	public RiseTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);

		mAction = new SculptAction();
	}

	@Override
	protected void Work()
	{
		for (Vertex vertex : mVerticesRes)
		{
			// Rise
			MatrixUtils.copy(vertex.Normal, VOffset);

			// Gaussian
			MatrixUtils.scalarMultiply(VOffset, (Gaussian(sigma, vertex.mLastTempSqDistance) / maxGaussian * fMaxDeformation));

			// Linear
			// MatrixUtils.scalarMultiply(VOffset, (1 - (vertex.mLastTempSqDistance / sqMaxDist)) * fMaxDeformation);

			if (mAction != null)
			{
				((SculptAction) mAction).AddVertexOffset(vertex.Index, VOffset, vertex);

				// preview
				MatrixUtils.plus(VOffset, vertex.Coord, VOffset);
				MatrixUtils.scalarMultiply(VNormal, vertex.mLastTempSqDistance / sqMaxDist);
				for (RenderFaceGroup renderGroup : mMesh.mRenderGroupList)
				{
					renderGroup.UpdateVertexValue(vertex.Index, VOffset, VNormal);
				}
			}
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
