package truesculpt.tools.sculpting;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.mesh.HalfEdge;
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

	private final float[] dirCurr = new float[3];
	private final float[] dirOther = new float[3];

	@Override
	protected void Work()
	{
		for (Vertex vertex : mVerticesRes)
		{
			MatrixUtils.copy(vertex.Coord, VOffset);// default value

			MatrixUtils.minus(vertex.mLastIntersectPt, vertex.Coord, dirCurr);

			Vertex VertexCloserToCenter = null;
			float bestDist = vertex.mLastTempSqDistance;
			for (HalfEdge edge : vertex.OutLinkedEdges)
			{
				Vertex other = mMesh.mVertexList.get(edge.V1);
				if (other.mLastTempSqDistance < bestDist)
				{
					// check side
					MatrixUtils.minus(other.mLastIntersectPt, other.Coord, dirOther);
					float side = MatrixUtils.dot(dirCurr, dirOther);
					if (side > 0)
					{
						VertexCloserToCenter = other;
						bestDist = other.mLastTempSqDistance;
					}
				}
			}

			if (VertexCloserToCenter != null)
			{
				MatrixUtils.copy(VertexCloserToCenter.Coord, VOffset);
			}
			else
			{
				MatrixUtils.copy(vertex.mLastIntersectPt, VOffset);
			}

			((SculptAction) mAction).AddNewVertexValue(VOffset, vertex);

			// preview
			MatrixUtils.copy(vertex.Normal, VNormal);
			MatrixUtils.scalarMultiply(VNormal, 1 - vertex.mLastTempSqDistance / mSquareMaxDistance);
			for (RenderFaceGroup renderGroup : mMesh.mRenderGroupList)
			{
				renderGroup.UpdateVertexValue(vertex.Index, VOffset, VNormal);
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
