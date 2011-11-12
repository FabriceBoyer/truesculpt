package truesculpt.tools.other;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.SelectionTool;
import android.graphics.Color;

public class BorderTool extends SelectionTool
{
	int highlightColor = Color.rgb(200, 200, 0);// some kind of yellow

	public BorderTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		// switch back to initial color
		for (Vertex vertex : mCumulatedVerticesRes)
		{
			for (RenderFaceGroup renderGroup : mMesh.mRenderGroupList)
			{
				renderGroup.UpdateVertexColor(vertex.Index, vertex.Color);
			}
		}

		super.Stop(xScreen, yScreen);
	}

	@Override
	protected void Work()
	{
		for (Vertex vertex : mVerticesRes)
		{
			for (RenderFaceGroup renderGroup : mMesh.mRenderGroupList)
			{
				if (vertex.mLastIsBorder)
				{
					renderGroup.UpdateVertexColor(vertex.Index, highlightColor);
				}
				else
				{
					renderGroup.UpdateVertexColor(vertex.Index, 0);// black
				}
			}
		}
	}

	@Override
	public int GetIcon()
	{
		return R.drawable.flash;
	}

	@Override
	public String GetName()
	{
		return "Border";
	}
}
