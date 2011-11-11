package truesculpt.tools.sculpting;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.mesh.HalfEdge;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.SculptingTool;
import truesculpt.utils.MatrixUtils;

public class SmoothTool extends SculptingTool
{
	public SmoothTool(Managers managers)
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
				// Place at average position of all surrounding points
				int nSurroundingVertices = vertex.OutLinkedEdges.size();
				MatrixUtils.zero(VOffset);
				for (HalfEdge edge : vertex.OutLinkedEdges)
				{
					MatrixUtils.plus(mMesh.mVertexList.get(edge.V1).Coord, VOffset, VOffset);
				}
				MatrixUtils.scalarMultiply(VOffset, 1.0f / nSurroundingVertices);

				MatrixUtils.minus(VOffset, vertex.Coord, VOffset);// go to relative offset

				((SculptAction) mAction).AddVertexOffset(vertex.Index, VOffset, vertex);

				// preview
				MatrixUtils.plus(VOffset, vertex.Coord, VOffset);// come back to absolute
				MatrixUtils.copy(vertex.Normal, VNormal);
				MatrixUtils.scalarMultiply(VNormal, vertex.mLastTempSqDistance / mSquareMaxDistance);
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
		return R.drawable.smooth;
	}

	@Override
	public String GetName()
	{
		return "Smooth";
	}
}
