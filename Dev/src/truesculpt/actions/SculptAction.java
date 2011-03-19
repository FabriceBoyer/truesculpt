package truesculpt.actions;

import java.util.ArrayList;
import java.util.HashSet;

import truesculpt.main.R;
import truesculpt.mesh.HalfEdge;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.Vertex;
import truesculpt.utils.MatrixUtils;


public class SculptAction extends BaseAction
{
	private class VertexCoordChange
	{
		public VertexCoordChange(int nIndex, float[] offset, Vertex vertex)
		{
			super();
			this.nIndex = nIndex;
			MatrixUtils.copy(vertex.Coord, Vorig);
			MatrixUtils.plus(vertex.Coord, offset, Vnew);	
			this.vertex=vertex;
		}
		public int nIndex=-1;
		float[] Vorig=new float[3];
		float[] Vnew=new float[3];
		Vertex vertex=null;
	}
	
	private ArrayList<VertexCoordChange> mVertexChanges=new ArrayList<VertexCoordChange>() ;
	
	public SculptAction()
	{
		super();
		setDescription("Sculpting");
	}
	
	private HashSet <Integer> faces= new HashSet<Integer>();
	private HashSet <Integer> vertices= new HashSet<Integer>();
	@Override
	public boolean DoAction()
	{ 		
		
		Mesh mesh=getManagers().getMeshManager().getMesh();
		for (VertexCoordChange change : mVertexChanges)
		{
			MatrixUtils.copy(change.Vnew, change.vertex.Coord);
		}
		
		//update normals and publish value after all updates
		faces.clear();
		vertices.clear();
		for (VertexCoordChange change : mVertexChanges)
		{
			vertices.add(change.vertex.Index);
			for (HalfEdge edge : change.vertex.OutLinkedEdges)
			{
				faces.add(edge.Face);
				vertices.add(edge.V1);
			}
		}
		for (Integer index: faces)
		{
			mesh.ComputeFaceEdgesNormal(index);
		}
		//outer bound of modified vertices must update it's normal too (face changed)
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
		//update normals and publish value after all updates
		Mesh mesh=getManagers().getMeshManager().getMesh();
		for (VertexCoordChange change : mVertexChanges)
		{
			MatrixUtils.copy(change.Vorig, change.vertex.Coord);
		}
		//update normals and publish value after all updates
		faces.clear();
		vertices.clear();
		for (VertexCoordChange change : mVertexChanges)
		{
			vertices.add(change.vertex.Index);
			for (HalfEdge edge : change.vertex.OutLinkedEdges)
			{
				faces.add(edge.Face);
				vertices.add(edge.V1);
			}
		}
		for (Integer index: faces)
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

	public void AddVertexOffset(Integer i, float[] vOffset, Vertex vertex)
	{
		mVertexChanges.add(new VertexCoordChange(i,vOffset,vertex));		
	}
}
