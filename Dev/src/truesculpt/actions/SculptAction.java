package truesculpt.actions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import truesculpt.main.R;
import truesculpt.mesh.HalfEdge;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.Vertex;
import truesculpt.utils.MatrixUtils;

public class SculptAction extends BaseAction
{
	private class VertexCoordChange
	{
		public VertexCoordChange(float[] offset, Vertex vertex)
		{
			super();
			MatrixUtils.copy(vertex.Coord, Vorig);
			MatrixUtils.plus(vertex.Coord, offset, Vnew);
			this.vertex = vertex;
		}

		float[] Vorig = new float[3];
		float[] Vnew = new float[3];
		Vertex vertex = null;
	}

	private final HashMap<Integer, VertexCoordChange> mVertexChanges = new HashMap<Integer, VertexCoordChange>();

	public SculptAction()
	{
		super();
		setDescription("Sculpting " + mnActionCounter++);
	}

	private final HashSet<Integer> faces = new HashSet<Integer>();
	private final HashSet<Integer> vertices = new HashSet<Integer>();

	@Override
	public boolean DoAction()
	{
		Mesh mesh = getManagers().getMeshManager().getMesh();
		for (Map.Entry<Integer, VertexCoordChange> change : mVertexChanges.entrySet())
		{
			MatrixUtils.copy(change.getValue().Vnew, change.getValue().vertex.Coord);
		}

		// update normals and publish value after all updates
		faces.clear();
		vertices.clear();
		for (Map.Entry<Integer, VertexCoordChange> change : mVertexChanges.entrySet())
		{
			vertices.add(change.getValue().vertex.Index);
			for (HalfEdge edge : change.getValue().vertex.OutLinkedEdges)
			{
				faces.add(edge.Face);
				vertices.add(edge.V1);
			}
		}
		for (Integer index : faces)
		{
			mesh.ComputeFaceEdgesNormal(index);
		}
		// outer bound of modified vertices must update it's normal too (face
		// changed)
		for (Integer index : vertices)
		{
			mesh.ComputeVertexNormal(index);
			mesh.UpdateVertexValue(index);
		}
		return true;
	}

	@Override
	public String GetActionName()
	{
		return "Sculpt";
	}

	@Override
	public int GetImageResourceID()
	{
		return R.drawable.grab;
	}

	@Override
	public boolean UndoAction()
	{
		// update normals and publish value after all updates
		Mesh mesh = getManagers().getMeshManager().getMesh();
		for (Map.Entry<Integer, VertexCoordChange> change : mVertexChanges.entrySet())
		{
			MatrixUtils.copy(change.getValue().Vorig, change.getValue().vertex.Coord);
		}
		// update normals and publish value after all updates
		faces.clear();
		vertices.clear();
		for (Map.Entry<Integer, VertexCoordChange> change : mVertexChanges.entrySet())
		{
			vertices.add(change.getValue().vertex.Index);
			for (HalfEdge edge : change.getValue().vertex.OutLinkedEdges)
			{
				faces.add(edge.Face);
				vertices.add(edge.V1);
			}
		}
		for (Integer index : faces)
		{
			mesh.ComputeFaceEdgesNormal(index);
		}
		for (Integer index : vertices)
		{
			mesh.ComputeVertexNormal(index);
			mesh.UpdateVertexValue(index);
		}
		return true;
	}

	public void AddVertexOffset(float[] vOffset, Vertex vertex)
	{
		mVertexChanges.put(vertex.Index, new VertexCoordChange(vOffset, vertex));
	}
}
