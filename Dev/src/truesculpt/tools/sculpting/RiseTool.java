package truesculpt.tools.sculpting;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;
import truesculpt.main.R;
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
	protected void Work()
	{
		if (mAction != null)
		{
			for (Vertex vertex : mVerticesRes)
			{
				MatrixUtils.copy(vertex.Normal, VOffset);

				float newOffsetFactor = 1;

				// Gaussian
				// newOffsetFactor = Gaussian(mSigma, vertex.mLastTempSqDistance) / mMaxGaussian;

				// Linear
				newOffsetFactor = 1 - (vertex.mLastTempSqDistance / mSquareMaxDistance);

				// Saturate
				if (newOffsetFactor > 1)
				{
					newOffsetFactor = 1;
				}
				if (newOffsetFactor < 0)
				{
					newOffsetFactor = 0;
				}
				MatrixUtils.scalarMultiply(VOffset, newOffsetFactor * mMaxDeformation);

				MatrixUtils.plus(VOffset, vertex.Coord, VOffset);
				((SculptAction) mAction).AddNewVertexValue(VOffset, vertex);

				// preview
				MatrixUtils.copy(vertex.Normal, VNormal);
				MatrixUtils.scalarMultiply(VNormal, 1 - newOffsetFactor);
				for (RenderFaceGroup renderGroup : mMesh.mRenderGroupList)
				{
					renderGroup.UpdateVertexValue(vertex.Index, VOffset, VNormal);
				}
			}
		}
	}

	@Override
	public int GetIcon()
	{
		return R.drawable.draw;
	}

	@Override
	public String GetName()
	{
		return "Rise";
	}
}
