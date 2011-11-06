package truesculpt.tools;

import truesculpt.main.Managers;
import truesculpt.mesh.Face;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.PaintingTool;

public class PickColorTool extends PaintingTool
{
	public PickColorTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);

		PickColorAction(xScreen, yScreen);
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		super.Stop(xScreen, yScreen);

		PickColorAction(xScreen, yScreen);
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		super.Pick(xScreen, yScreen);

		PickColorAction(xScreen, yScreen);
	}

	private void PickColorAction(float xScreen, float yScreen)
	{
		int nIndex = getManagers().getMeshManager().Pick(xScreen, yScreen);

		if (nIndex >= 0)
		{
			Mesh mesh = getManagers().getMeshManager().getMesh();

			Face face = mesh.mFaceList.get(nIndex);
			Vertex vertex = mesh.mVertexList.get(face.E0.V0);// arbitrarily chosen point in triangle
			int color = vertex.Color;
			getManagers().getToolsManager().setColor(color, true);

			getManagers().getMeshManager().NotifyListeners();
		}
	}
}
