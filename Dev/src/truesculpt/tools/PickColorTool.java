package truesculpt.tools;

import truesculpt.main.Managers;
import truesculpt.mesh.Face;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.PaintingTool;

public class PickColorTool extends PaintingTool
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
}
