package truesculpt.tools;

import truesculpt.main.Managers;
import truesculpt.mesh.Face;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import android.graphics.Color;
import android.os.SystemClock;

public class SelectionTool extends ToolsBase
{

	public SelectionTool(Managers managers)
	{
		super(managers);

	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);

	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		long tSculptStart = SystemClock.uptimeMillis();
		super.Pick(xScreen, yScreen);
		int nIndex = getManagers().getMeshManager().Pick(xScreen, yScreen);

		SelectAction(nIndex);

		long tSculptStop = SystemClock.uptimeMillis();
		mLastSculptDurationMs = tSculptStop - tSculptStart;
	}

	@Override
	public void Stop()
	{
		super.Stop();

		// getManagers().getActionsManager().AddUndoAction(action);
		// action.DoAction();
	}

	private void SelectAction(int triangleIndex)
	{
		if (triangleIndex >= 0)
		{
			Mesh mesh = getManagers().getMeshManager().getMesh();

			int highlightColor = Color.argb(100, 255, 255, 0);

			Face face = mesh.mFaceList.get(triangleIndex);
			int nOrigVertex = face.E0.V0;// TODO choose closest point in triangle from pick point

			Vertex origVertex = mesh.mVertexList.get(nOrigVertex);
			float sqMaxDist = (float) Math.pow((MAX_RADIUS - MIN_RADIUS) * getManagers().getToolsManager().getRadius() / 100f + MIN_RADIUS, 2);
			mesh.GetVerticesAtDistanceFromVertex(origVertex, sqMaxDist, verticesRes);

			for (Vertex vertex : verticesRes)
			{
				for (RenderFaceGroup renderGroup : mesh.mRenderGroupList)
				{
					renderGroup.UpdateVertexColor(vertex.Index, highlightColor);
				}
			}
		}
	}

}
