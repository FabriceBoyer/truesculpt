package truesculpt.tools.other;

import truesculpt.main.Managers;
import truesculpt.managers.ToolsManager.ESymmetryMode;
import truesculpt.mesh.Face;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.BaseTool;
import android.graphics.drawable.Drawable;

public class PickColorTool extends BaseTool
{
	public PickColorTool(Managers managers)
	{
		super(managers);
	}

	private void Work(float xScreen, float yScreen)
	{
		Mesh mMesh = getManagers().getMeshManager().getMesh();
		int nTriangleIndex = getManagers().getMeshManager().Pick(xScreen, yScreen, ESymmetryMode.NONE);
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

	@Override
	public void Start(float xScreen, float yScreen)
	{
		Work(xScreen, yScreen);
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		Work(xScreen, yScreen);
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		Work(xScreen, yScreen);
	}
}
