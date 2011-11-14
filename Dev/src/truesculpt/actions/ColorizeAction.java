package truesculpt.actions;

import java.util.ArrayList;

import truesculpt.main.R;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.Vertex;

public class ColorizeAction extends BaseAction
{
	private class VertexColorChange
	{
		public VertexColorChange(int newColor, Vertex vertex)
		{
			super();
			this.newColor = newColor;
			this.oldColor = vertex.Color;
			this.vertex = vertex;
		}

		int newColor = -1;
		int oldColor = -1;
		Vertex vertex = null;
	}

	private final ArrayList<VertexColorChange> mVertexChanges = new ArrayList<VertexColorChange>();

	public ColorizeAction()
	{
		super();
		setDescription("Painting");
	}

	@Override
	public boolean DoAction()
	{
		Mesh mesh = getManagers().getMeshManager().getMesh();
		for (VertexColorChange change : mVertexChanges)
		{
			change.vertex.Color = change.newColor;
			mesh.UpdateVertexColor(change.vertex);
		}
		return true;
	}

	@Override
	public String GetActionName()
	{
		return "Paint";
	}

	@Override
	public int GetImageResourceID()
	{
		return R.drawable.colorpicker;
	}

	@Override
	public boolean UndoAction()
	{
		Mesh mesh = getManagers().getMeshManager().getMesh();
		for (VertexColorChange change : mVertexChanges)
		{
			change.vertex.Color = change.oldColor;
			mesh.UpdateVertexColor(change.vertex);
		}
		return true;
	}

	public void AddVertexColorChange(int newColor, Vertex vertex)
	{
		mVertexChanges.add(new VertexColorChange(newColor, vertex));
	}

	@Override
	public int GetChangeCount()
	{
		return mVertexChanges.size();
	}

}
