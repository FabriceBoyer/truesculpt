package truesculpt.tools.other;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.SelectionTool;
import android.graphics.Color;

public class HighlightTool extends SelectionTool
{
	int highlightColor = Color.rgb(200, 200, 0);// some kind of yellow

	public HighlightTool(Managers managers)
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
		// selection preview highlight (not in data mesh only in gpu)
		for (Vertex vertex : mVerticesRes)
		{
			for (RenderFaceGroup renderGroup : mMesh.mRenderGroupList)
			{
				int val = 255 - (int) (255.f * vertex.mLastTempSqDistance / mSquareMaxDistance);
				highlightColor = Color.rgb(val, val, 0);
				renderGroup.UpdateVertexColor(vertex.Index, highlightColor);
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
		return "Highlight";
	}
}
