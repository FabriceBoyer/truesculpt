package truesculpt.tools;

import truesculpt.actions.ColorizeAction;
import truesculpt.main.Managers;
import truesculpt.mesh.Face;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.PaintingTool;
import android.graphics.Color;
import android.util.Log;

public class ColorizeTool extends PaintingTool
{
	private ColorizeAction mAction = null;

	public ColorizeTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		super.Pick(xScreen, yScreen);

		ColorizePaintAction(xScreen, yScreen);

		EndPick();
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);

		mAction = new ColorizeAction();

		ColorizePaintAction(xScreen, yScreen);
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		ColorizePaintAction(xScreen, yScreen);

		if (mAction != null)
		{
			getManagers().getActionsManager().AddUndoAction(mAction);
			mAction.DoAction();
			mAction = null;
		}
		else
		{
			Log.e("COLORIZETOOL", "Anormal null action");
		}

		super.Stop(xScreen, yScreen);
	}

	private void ColorizePaintAction(float xScreen, float yScreen)
	{
		int nTriangleIndex = getManagers().getMeshManager().Pick(xScreen, yScreen);

		if (nTriangleIndex >= 0)
		{
			Mesh mesh = getManagers().getMeshManager().getMesh();

			int targetColor = getManagers().getToolsManager().getColor();
			Face face = mesh.mFaceList.get(nTriangleIndex);
			int nOrigVertex = face.E0.V0;// TODO choose closest point in triangle from pick point
			Vertex origVertex = mesh.mVertexList.get(nOrigVertex);
			float sqMaxDist = (float) Math.pow((MAX_RADIUS - MIN_RADIUS) * getManagers().getToolsManager().getRadius() / 100f + MIN_RADIUS, 2);
			float MaxDist = (float) Math.sqrt(sqMaxDist);
			verticesRes.clear();
			mesh.GetVerticesAtDistanceFromSegment(origVertex, mLastVertex, sqMaxDist, verticesRes);
			cumulatedVerticesRes.addAll(verticesRes);

			float[] VNewCol = new float[3];
			float[] VTargetCol = new float[3];
			Color.colorToHSV(targetColor, VTargetCol);

			for (Vertex vertex : verticesRes)
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
				mAction.AddVertexColorChange(vertex.Index, newColor, vertex);

				// preview
				for (RenderFaceGroup renderGroup : mesh.mRenderGroupList)
				{
					renderGroup.UpdateVertexColor(vertex.Index, newColor);
				}
			}
			mLastVertex = origVertex;

			getManagers().getMeshManager().NotifyListeners();
		}

	}
}
