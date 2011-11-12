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

	@Override
	protected void PickInternal(float xScreen, float yScreen, ESymmetryMode mode)
	{
		if (mode == ESymmetryMode.NONE)
		{
			int nTriangleIndex = getManagers().getMeshManager().Pick(xScreen, yScreen, ESymmetryMode.NONE);
			if (nTriangleIndex > 0 && mMesh != null)
			{
				Face face = mMesh.mFaceList.get(nTriangleIndex);
				Vertex vertex = mMesh.mVertexList.get(face.E0.V0);// arbitrarily chosen point in triangle
				int color = vertex.Color;
				getManagers().getToolsManager().setColor(color, true, false);
			}
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
		return "Pick color";
	}
}
