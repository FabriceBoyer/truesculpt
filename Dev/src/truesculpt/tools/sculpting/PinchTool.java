package truesculpt.tools.sculpting;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.SculptingTool;
import truesculpt.utils.MatrixUtils;

public class PinchTool extends SculptingTool
{

	public PinchTool(Managers managers)
	{
		super(managers);

	}

	@Override
	protected void Work()
	{
		if (mAction != null)
		{
			for (Vertex vertex : mVerticesRes)
			{
				MatrixUtils.minus(vertex.mLastIntersectPt, vertex.Coord, VOffset);
				MatrixUtils.normalize(VOffset);

				float mLastDist = (float) Math.sqrt(vertex.mLastTempSqDistance);
				if (mLastDist > mMaxDistance / 4)
				{
					float newOffsetFactor = 1 - (mLastDist / mMaxDistance) / 2;

					// Saturate
					if (newOffsetFactor > 1)
					{
						newOffsetFactor = 1;
					}
					if (newOffsetFactor < 0)
					{
						newOffsetFactor = 0;
					}
					// MatrixUtils.scalarMultiply(VOffset, newOffsetFactor * mLastDist);
					MatrixUtils.scalarMultiply(VOffset, mLastDist / 2);

					((SculptAction) mAction).AddVertexOffset(vertex.Index, VOffset, vertex);

					// preview
					MatrixUtils.plus(VOffset, vertex.Coord, VOffset);
					MatrixUtils.copy(vertex.Normal, VNormal);
					MatrixUtils.scalarMultiply(VNormal, 1 - newOffsetFactor);
					for (RenderFaceGroup renderGroup : mMesh.mRenderGroupList)
					{
						renderGroup.UpdateVertexValue(vertex.Index, VOffset, VNormal);
					}
				}
			}
		}
	}

	@Override
	public int GetIcon()
	{
		return R.drawable.pinch;
	}

	@Override
	public String GetName()
	{
		return "Pinch";
	}

}
