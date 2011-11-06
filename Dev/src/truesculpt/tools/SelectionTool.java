package truesculpt.tools;

import java.util.HashSet;

import truesculpt.main.Managers;
import truesculpt.mesh.Face;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import android.graphics.Color;
import android.os.SystemClock;

public class SelectionTool extends ToolsBase
{

	protected HashSet<Vertex> cumulatedVerticesRes = new HashSet<Vertex>();

	public SelectionTool(Managers managers)
	{
		super(managers);

	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);

		cumulatedVerticesRes.clear();
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

		getManagers().getMeshManager().NotifyListeners();
	}

	@Override
	public void Stop()
	{
		super.Stop();

		Mesh mesh = getManagers().getMeshManager().getMesh();

		// switch back to initial color
		for (Vertex vertex : cumulatedVerticesRes)
		{
			for (RenderFaceGroup renderGroup : mesh.mRenderGroupList)
			{
				renderGroup.UpdateVertexColor(vertex.Index, vertex.Color);
			}
		}

		cumulatedVerticesRes.clear();

		getManagers().getMeshManager().NotifyListeners();

		// getManagers().getActionsManager().AddUndoAction(action);
		// action.DoAction();
	}

	private void SelectAction(int triangleIndex)
	{
		if (triangleIndex >= 0)
		{
			Mesh mesh = getManagers().getMeshManager().getMesh();

			int highlightColor = Color.rgb(200, 200, 0);// some kind of yellow

			Face face = mesh.mFaceList.get(triangleIndex);
			int nOrigVertex = face.E0.V0;// TODO choose closest point in triangle from pick point

			Vertex origVertex = mesh.mVertexList.get(nOrigVertex);
			float sqMaxDist = (float) Math.pow((MAX_RADIUS - MIN_RADIUS) * getManagers().getToolsManager().getRadius() / 100f + MIN_RADIUS, 2);
			verticesRes.clear();
			mesh.GetVerticesAtDistanceFromVertex(origVertex, sqMaxDist, verticesRes);

			cumulatedVerticesRes.addAll(verticesRes);

			// temporary highlight
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
