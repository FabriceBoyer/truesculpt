package truesculpt.tools.sculpting;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.SculptingTool;
import truesculpt.utils.MatrixUtils;
import android.graphics.drawable.Drawable;

public class InflateTool extends SculptingTool
{
	public InflateTool(Managers managers)
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
				// Inflate
				MatrixUtils.copy(vertex.Coord, VNormal);
				MatrixUtils.normalize(VNormal);
				MatrixUtils.copy(VNormal, VOffset);

				// Gaussian
				// MatrixUtils.scalarMultiply(VOffset, (Gaussian(sigma, vertex.mLastTempSqDistance) / maxGaussian * fMaxDeformation));

				// Linear
				MatrixUtils.scalarMultiply(VOffset, (1 - (vertex.mLastTempSqDistance / sqMaxDist)) * fMaxDeformation);

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

	@Override
	public String GetDescription()
	{
		return null;
	}

	@Override
	public Drawable GetIcon()
	{
		return null;
	}

	@Override
	public String GetName()
	{
		return null;
	}
}
