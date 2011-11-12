package truesculpt.tools.painting;

import truesculpt.actions.ColorizeAction;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.PaintingTool;
import android.graphics.Color;

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
				float currSqDist = vertex.mLastTempSqDistance;

				Color.colorToHSV(vertex.Color, VNewCol);

				// barycenter of colors
				float alpha = 0;
				float sqOffset = mSquareMaxDistance / 2;
				if (currSqDist > sqOffset)
				{
					alpha = ((currSqDist - sqOffset)) / (mSquareMaxDistance - sqOffset);// [0;1]
				}

				// float temp = circularInterp(VNewCol[2], VTargetCol[2], alpha, 1);
				// Log.i("COLORIZETOOL", VNewCol[2] + " to " + VTargetCol[2] + ", with alpha " + alpha + " = " + temp);

				VNewCol[0] = VTargetCol[0];// circularInterp(VTargetCol[0], VNewCol[0], alpha, 1);
				VNewCol[1] = VTargetCol[1];// linearInterp(VTargetCol[1], VNewCol[1], alpha);
				VNewCol[2] = linearInterp(VTargetCol[2], VNewCol[2], alpha);
				// Log.i("COLORIZETOOL", "HSV VTarget=(" + VTargetCol[0] + "," + VTargetCol[1] + "," + VTargetCol[2] + ")" + ", HSV VNewCol=(" + VNewCol[0] + "," + VNewCol[1] + "," + VNewCol[2] + ")" + ", alpha=" + alpha);

				// int newColor = Color.HSVToColor(VNewCol);
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

	private static float linearInterp(float start, float stop, float alpha)
	{
		float res = (1 - alpha) * start + alpha * stop;
		return res;
	}

	// [0;base] based, start/stop may be unordered, alpha is [0;1]
	private float circularInterp(float start, float stop, float alpha, float base)
	{
		float res;
		if (start == stop)
		{
			return start;
		}
		if (alpha == 0)
		{
			return start;
		}
		if (alpha == 1)
		{
			return stop;
		}
		if (Math.abs(stop - start) < base / 2)
		{
			// straight central way
			res = (1 - alpha) * start + alpha * stop;
		}
		else
		{
			// circular way, overflow interp
			if (start < stop)
			{
				start += base;
			}
			else
			{
				stop += base;
			}

			res = (1 - alpha) * start + alpha * stop;

			if (res > base)
			{
				res -= base;
			}
		}

		return res;
	}

	@Override
	public int GetIcon()
	{
		return R.drawable.paint_palette;
	}

	@Override
	public String GetName()
	{
		return "Colorize";
	}
}
