package truesculpt.tools.painting;

import truesculpt.actions.ColorizeAction;
import truesculpt.main.Managers;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.PaintingTool;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class ColorizeTool extends PaintingTool
{
	private int mTargetColor = Color.BLACK;
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

		mTargetColor = getManagers().getToolsManager().getColor();
		Color.colorToHSV(mTargetColor, VTargetCol);
	}

	@Override
	protected void Work()
	{
		if (mAction != null)
		{
			for (Vertex vertex : mVerticesRes)
			{
				float dist = (float) Math.sqrt(vertex.mLastTempSqDistance);

				Color.colorToHSV(vertex.Color, VNewCol);

				// barycenter of colors
				float alpha = (mMaxDistance - dist) / mMaxDistance;// [0;1]
				VNewCol[0] = VTargetCol[0];
				VNewCol[1] = VTargetCol[1];
				VNewCol[2] = (1 - alpha) * VNewCol[2] + alpha * VTargetCol[2];

				// int newColor=Color.HSVToColor(VNewCol);
				int newColor = mTargetColor;

				((ColorizeAction) mAction).AddVertexColorChange(vertex.Index, newColor, vertex);

				// preview
				for (RenderFaceGroup renderGroup : mMesh.mRenderGroupList)
				{
					renderGroup.UpdateVertexColor(vertex.Index, newColor);
				}
			}
		}
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
}
