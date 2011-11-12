package truesculpt.tools.other;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.managers.ToolsManager.ESymmetryMode;
import truesculpt.mesh.Face;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.BaseTool;

public class PickColorTool extends BaseTool
{
	public PickColorTool(Managers managers)
	{
		super(managers);
	}

	private void Work(float xScreen, float yScreen)
	{
		int nTriangleIndex = getManagers().getMeshManager().Pick(xScreen, yScreen, ESymmetryMode.NONE);
		if (nTriangleIndex > 0)
		{
			Face face = mMesh.mFaceList.get(nTriangleIndex);
			Vertex vertex = mMesh.mVertexList.get(face.E0.V0);// arbitrarily chosen point in triangle
			int color = vertex.Color;
			getManagers().getToolsManager().setColor(color, true, false);

			getManagers().getMeshManager().NotifyListeners();
		}
	}

	@Override
	public int GetIcon()
	{
		return R.drawable.colorpicker;
	}

	@Override
	public String GetName()
	{
		return "Pick Color";
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);
		Work(xScreen, yScreen);
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		super.Pick(xScreen, yScreen);
		Work(xScreen, yScreen);
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		super.Stop(xScreen, yScreen);
		Work(xScreen, yScreen);
	}
}
