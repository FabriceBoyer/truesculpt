package truesculpt.tools.sculpting;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.SculptingTool;
import truesculpt.utils.MatrixUtils;

public class FlattenTool extends SculptingTool
{
	private final float[] temp = new float[3];
	private final float[] averageTarget = new float[3];
	private final float[] averageNormal = new float[3];

	public FlattenTool(Managers managers)
	{
		super(managers);
	}

	@Override
	protected void Work()
	{
		// compute average target
		MatrixUtils.zero(averageTarget);
		MatrixUtils.zero(averageNormal);
		for (Vertex vertex : mVerticesRes)
		{
			MatrixUtils.plus(vertex.Coord, averageTarget, averageTarget);
			MatrixUtils.plus(vertex.Coord, averageNormal, averageNormal);
		}
		MatrixUtils.scalarMultiply(averageTarget, 1.0f / mVerticesRes.size());
		MatrixUtils.scalarMultiply(averageNormal, 1.0f / mVerticesRes.size());
		MatrixUtils.normalize(averageNormal);

		// flatten toward this target
		for (Vertex vertex : mVerticesRes)
		{
			MatrixUtils.copy(averageNormal, VOffset);

			MatrixUtils.minus(averageTarget, vertex.Coord, temp);
			float newOffsetFactor = MatrixUtils.dot(VOffset, temp) * (1 - vertex.mLastTempSqDistance / mSquareMaxDistance);
			MatrixUtils.scalarMultiply(VOffset, newOffsetFactor);

			MatrixUtils.plus(VOffset, vertex.Coord, VOffset);
			((SculptAction) mAction).AddNewVertexValue(VOffset, vertex);

			// preview
			MatrixUtils.copy(vertex.Normal, VNormal);
			MatrixUtils.scalarMultiply(VNormal, newOffsetFactor);
			for (RenderFaceGroup renderGroup : mMesh.mRenderGroupList)
			{
				renderGroup.UpdateVertexValue(vertex.Index, VOffset, VNormal);
			}
		}
	}

	@Override
	public String GetName()
	{
		return "Flatten";
	}

	@Override
	public int GetIcon()
	{
		return R.drawable.flatten;
	}

}
