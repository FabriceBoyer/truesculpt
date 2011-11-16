package truesculpt.tools.sculpting;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.SculptingTool;
import truesculpt.utils.MatrixUtils;

public class InflateTool extends SculptingTool
{
	public InflateTool(Managers managers)
	{
		super(managers);
	}

	@Override
	protected void Work()
	{
		for (Vertex vertex : mVerticesRes)
		{
			// Inflate
			MatrixUtils.copy(vertex.Coord, VNormal);
			MatrixUtils.normalize(VNormal);
			MatrixUtils.copy(VNormal, VOffset);

			float newOffsetFactor = 1;

			// Gaussian
			// newOffsetFactor= (Gaussian(sigma, vertex.mLastTempSqDistance) / maxGaussian * fMaxDeformation);

			// Linear
			newOffsetFactor = (1 - (vertex.mLastTempSqDistance / mSquareMaxDistance));

			newOffsetFactor = MatrixUtils.saturateBetween0And1(newOffsetFactor);

			MatrixUtils.scalarMultiply(VOffset, newOffsetFactor * mMaxDeformation);

			MatrixUtils.plus(VOffset, vertex.Coord, VOffset);
			((SculptAction) mAction).AddNewVertexValue(VOffset, vertex);

			// preview
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
		return R.drawable.inflate;
	}

	@Override
	public String GetName()
	{
		return "Inflate";
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		if (mAction != null)
		{
			((SculptAction) mAction).DoSmoothAll(mMesh);// just a test
		}

		super.Stop(xScreen, yScreen);
	}
}
