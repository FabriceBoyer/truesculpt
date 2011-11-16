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

	public FlattenTool(Managers managers)
	{
		super(managers);
	}

	@Override
	protected void Work()
	{
		for (Vertex vertex : mVerticesRes)
		{
			MatrixUtils.copy(mOrigVertex.Normal, VOffset);

			MatrixUtils.minus(vertex.mLastIntersectPt, vertex.Coord, temp);
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
