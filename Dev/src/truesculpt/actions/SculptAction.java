package truesculpt.actions;

import java.util.ArrayList;

import truesculpt.main.R;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.Vertex;
import truesculpt.utils.MatrixUtils;


public class SculptAction extends BaseAction
{
	private class VertexChange
	{
		public VertexChange(int nIndex, float[] offset, Vertex vertex)
		{
			super();
			this.nIndex = nIndex;			
			MatrixUtils.copy(offset, Voffset);
			this.vertex=vertex;
		}
		public int nIndex=-1;
		float[] Voffset=new float[3];
		Vertex vertex=null;
	}
	
	private ArrayList<VertexChange> mVertexChanges=new ArrayList<VertexChange>() ;
	
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
		for (VertexChange change : mVertexChanges)
		{
			Vertex vertex=change.vertex;
			MatrixUtils.plus(vertex.Coord, change.Voffset, vertex.Coord);	
		}
		for (VertexChange change : mVertexChanges)
		{
			mesh.ComputeVertexNormal(change.vertex);
			mesh.UpdateVertexValue(change.nIndex);
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
		for (VertexChange change : mVertexChanges)
		{
			Vertex vertex=change.vertex;
			MatrixUtils.minus(vertex.Coord, change.Voffset, vertex.Coord);	
		}
		for (VertexChange change : mVertexChanges)
		{
			mesh.ComputeVertexNormal(change.vertex);
			mesh.UpdateVertexValue(change.nIndex);
		}
		return true;
	}

	public void AddVertexOffset(Integer i, float[] vOffset, Vertex vertex)
	{
		mVertexChanges.add(new VertexChange(i,vOffset,vertex));		
	}
}
