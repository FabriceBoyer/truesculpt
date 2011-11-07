package truesculpt.tools;

import truesculpt.main.Managers;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.ToolsBase;
import android.graphics.Color;

public class SelectionTool extends ToolsBase
{
	int highlightColor = Color.rgb(200, 200, 0);// some kind of yellow

	public SelectionTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		// switch back to initial color
		for (Vertex vertex : cumulatedVerticesRes)
		{
			for (RenderFaceGroup renderGroup : mesh.mRenderGroupList)
			{
				renderGroup.UpdateVertexColor(vertex.Index, vertex.Color);
			}
		}

		super.Stop(xScreen, yScreen);
	}

	@Override
	protected void Work()
	{
		// selection preview highlight (not in data mesh only in gpu)
		for (Vertex vertex : verticesRes)
		{
			for (RenderFaceGroup renderGroup : mesh.mRenderGroupList)
			{
				int val = 255 - (int) (255.f * vertex.mLastTempSqDistance / sqMaxDist);
				highlightColor = Color.rgb(val, val, 0);
				renderGroup.UpdateVertexColor(vertex.Index, highlightColor);
			}
		}
	}
}
