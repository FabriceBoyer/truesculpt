package truesculpt.tools;

import truesculpt.actions.ColorizeAction;
import truesculpt.main.Managers;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.PaintingTool;
import android.graphics.Color;

public class ColorizeTool extends PaintingTool
{
	private int targetColor = Color.BLACK;
	private final float[] VNewCol = new float[3];
	private final float[] VTargetCol = new float[3];

	public ColorizeTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);

		targetColor = getManagers().getToolsManager().getColor();
		Color.colorToHSV(targetColor, VTargetCol);

		mAction = new ColorizeAction();
	}

	@Override
	protected void Work()
	{
		for (Vertex vertex : mVerticesRes)
		{
			float dist = (float) Math.sqrt(vertex.mLastTempSqDistance);

			Color.colorToHSV(vertex.Color, VNewCol);

			// barycenter of colors
			float alpha = (MaxDist - dist) / MaxDist;// [0;1]
			VNewCol[0] = VTargetCol[0];
			VNewCol[1] = VTargetCol[1];
			VNewCol[2] = (1 - alpha) * VNewCol[2] + alpha * VTargetCol[2];

			// int newColor=Color.HSVToColor(VNewCol);
			int newColor = targetColor;
			if (mAction != null)
			{
				((ColorizeAction) mAction).AddVertexColorChange(vertex.Index, newColor, vertex);

				// preview
				for (RenderFaceGroup renderGroup : mMesh.mRenderGroupList)
				{
					renderGroup.UpdateVertexColor(vertex.Index, newColor);
				}
			}
		}
	}
}
