package truesculpt.tools;

import truesculpt.actions.ColorizeAction;
import truesculpt.main.Managers;
import truesculpt.mesh.Face;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.Vertex;
import truesculpt.utils.MatrixUtils;
import android.graphics.Color;

public class ColorizeTool extends PaintingTool
{
	public ColorizeTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		super.Pick(xScreen, yScreen);
		int nIndex = getManagers().getMeshManager().Pick(xScreen, yScreen);
		ColorizePaintAction(nIndex);
	}

	private void ColorizePaintAction(int triangleIndex)
	{
		if (triangleIndex >= 0)
		{
			Mesh mesh = getManagers().getMeshManager().getMesh();

			int targetColor = getManagers().getToolsManager().getColor();
			Face face = mesh.mFaceList.get(triangleIndex);
			int nOrigVertex = face.E0.V0;// TODO choose closest point in triangle from pick point
			Vertex origVertex = mesh.mVertexList.get(nOrigVertex);
			float sqMaxDist = (float) Math.pow((MAX_RADIUS - MIN_RADIUS) * getManagers().getToolsManager().getRadius() / 100f + MIN_RADIUS, 2);
			float MaxDist = (float) Math.sqrt(sqMaxDist);
			mesh.GetVerticesAtDistanceFromVertex(origVertex, sqMaxDist, verticesRes);

			float[] VNewCol = new float[3];
			float[] VTargetCol = new float[3];
			Color.colorToHSV(targetColor, VTargetCol);
			float[] temp = new float[3];

			ColorizeAction action = new ColorizeAction();
			for (Vertex vertex : verticesRes)
			{
				MatrixUtils.minus(vertex.Coord, origVertex.Coord, temp);
				float dist = MatrixUtils.magnitude(temp);

				Color.colorToHSV(vertex.Color, VNewCol);

				// barycenter of colors
				float alpha = (MaxDist - dist) / MaxDist;// [0;1]
				VNewCol[0] = VTargetCol[0];
				VNewCol[1] = VTargetCol[1];
				VNewCol[2] = (1 - alpha) * VNewCol[2] + alpha * VTargetCol[2];

				// int newColor=Color.HSVToColor(VNewCol);
				int newColor = targetColor;
				action.AddVertexColorChange(vertex.Index, newColor, vertex);
			}
			getManagers().getActionsManager().AddUndoAction(action);
			action.DoAction();
		}
	}
}
