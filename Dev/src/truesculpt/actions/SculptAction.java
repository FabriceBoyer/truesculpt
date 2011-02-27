package truesculpt.actions;

import java.util.ArrayList;

import truesculpt.main.R;
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

	@Override
	public boolean DoAction()
	{
		//update normals and publish value after all updates
		Mesh mesh=getManagers().getMeshManager().getMesh();
		for (VertexCoordChange change : mVertexChanges)
		{
			MatrixUtils.copy(change.Vnew, change.vertex.Coord);
		}
		for (VertexCoordChange change : mVertexChanges)
		{
			mesh.ComputeVertexNormal(change.vertex);
			mesh.UpdateVertexValue(change.nIndex, change.vertex);
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
		for (VertexCoordChange change : mVertexChanges)
		{
			mesh.ComputeVertexNormal(change.vertex);
			mesh.UpdateVertexValue(change.nIndex, change.vertex);
		}
		return true;
	}

	public void AddVertexOffset(Integer i, float[] vOffset, Vertex vertex)
	{
		mVertexChanges.add(new VertexCoordChange(i,vOffset,vertex));		
	}
}
