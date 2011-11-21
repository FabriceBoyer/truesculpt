package truesculpt.tools.sculpting;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.SculptingTool;
import truesculpt.utils.MatrixUtils;

public class DrawTool extends SculptingTool
{
	public DrawTool(Managers managers)
	{
		super(managers);
	}

	@Override
	protected void Work()
	{
		for (Vertex vertex : mVerticesRes)
		{
			MatrixUtils.copy(vertex.Normal, VOffset);

			float newOffsetFactor = 1;

			// Gaussian
			// newOffsetFactor = Gaussian(mSigma, vertex.mLastTempSqDistance) / mMaxGaussian;

			// Quadratic
			newOffsetFactor = 1 - (vertex.mLastTempSqDistance / mSquareMaxDistance);

			// Linear
			// newOffsetFactor = (float) (1 - Math.sqrt(vertex.mLastTempSqDistance / mSquareMaxDistance));

			newOffsetFactor = MatrixUtils.saturateBetween0And1(newOffsetFactor);

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

	@Override
	public int GetIcon()
	{
		return R.drawable.draw;
	}

	@Override
	public String GetName()
	{
		return "Draw";
	}
}
