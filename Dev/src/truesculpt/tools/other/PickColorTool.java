package truesculpt.tools.other;

import android.graphics.drawable.Drawable;
import truesculpt.main.Managers;
import truesculpt.mesh.Face;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.ToolsBase;

public class PickColorTool extends ToolsBase
{
	public PickColorTool(Managers managers)
	{
		super(managers);
	}

	@Override
	protected void Work()
	{
		Face face = mMesh.mFaceList.get(nTriangleIndex);
		Vertex vertex = mMesh.mVertexList.get(face.E0.V0);// arbitrarily chosen point in triangle
		int color = vertex.Color;
		getManagers().getToolsManager().setColor(color, true, false);

		getManagers().getMeshManager().NotifyListeners();
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
