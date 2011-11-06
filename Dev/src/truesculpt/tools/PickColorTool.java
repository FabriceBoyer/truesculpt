package truesculpt.tools;

import truesculpt.main.Managers;
import truesculpt.mesh.Face;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.Vertex;

public class PickColorTool extends PaintingTool
{
	public PickColorTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		super.Pick(xScreen, yScreen);
		int nIndex = getManagers().getMeshManager().Pick(xScreen, yScreen);
		PickColorAction(nIndex);
		getManagers().getMeshManager().NotifyListeners();
	}

	private void PickColorAction(int nIndex)
	{
		if (nIndex >= 0)
		{
			Mesh mesh = getManagers().getMeshManager().getMesh();

			Face face = mesh.mFaceList.get(nIndex);
			Vertex vertex = mesh.mVertexList.get(face.E0.V0);// arbitrarily chosen point in triangle
			int color = vertex.Color;
			getManagers().getToolsManager().setColor(color, true);
		}
	}
}
