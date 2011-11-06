package truesculpt.tools;

import java.util.HashSet;

import truesculpt.main.Managers;
import truesculpt.mesh.Face;
import truesculpt.mesh.Mesh;
import truesculpt.mesh.RenderFaceGroup;
import truesculpt.mesh.Vertex;
import truesculpt.tools.base.Path;
import truesculpt.tools.base.ToolsBase;
import android.graphics.Color;
import android.os.SystemClock;

public class SelectionTool extends ToolsBase
{

	protected HashSet<Vertex> cumulatedVerticesRes = new HashSet<Vertex>();
	private Vertex mLastVertex = null;
	private final Path mPath = new Path();

	public SelectionTool(Managers managers)
	{
		super(managers);

	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);

		cumulatedVerticesRes.clear();
		mLastVertex = null;
		mPath.Clear();

		SelectAction(xScreen, yScreen);
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		long tSculptStart = SystemClock.uptimeMillis();
		super.Pick(xScreen, yScreen);

		SelectAction(xScreen, yScreen);

		long tSculptStop = SystemClock.uptimeMillis();
		mLastSculptDurationMs = tSculptStop - tSculptStart;
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		super.Stop(xScreen, yScreen);

		SelectAction(xScreen, yScreen);

		Mesh mesh = getManagers().getMeshManager().getMesh();

		// switch back to initial color
		for (Vertex vertex : cumulatedVerticesRes)
		{
			for (RenderFaceGroup renderGroup : mesh.mRenderGroupList)
			{
				renderGroup.UpdateVertexColor(vertex.Index, vertex.Color);
			}
		}

		// getManagers().getActionsManager().AddUndoAction(action);
		// action.DoAction();

		getManagers().getMeshManager().NotifyListeners();
	}

	private void SelectAction(float xScreen, float yScreen)
	{
		int nIndex = getManagers().getMeshManager().Pick(xScreen, yScreen);

		if (nIndex >= 0)
		{
			mPath.AddNode(nIndex, xScreen, yScreen);
			Mesh mesh = getManagers().getMeshManager().getMesh();

			int highlightColor = Color.rgb(200, 200, 0);// some kind of yellow

			Face face = mesh.mFaceList.get(nIndex);
			int nOrigVertex = face.E0.V0;// TODO choose closest point in triangle from pick point

			Vertex origVertex = mesh.mVertexList.get(nOrigVertex);
			float sqMaxDist = (float) Math.pow((MAX_RADIUS - MIN_RADIUS) * getManagers().getToolsManager().getRadius() / 100f + MIN_RADIUS, 2);

			verticesRes.clear();
			if (mLastVertex != null)
			{
				mesh.GetVerticesAtDistanceFromSegment(origVertex, mLastVertex, sqMaxDist, verticesRes);
			}
			else
			{
				mesh.GetVerticesAtDistanceFromVertex(origVertex, sqMaxDist, verticesRes);
			}
			cumulatedVerticesRes.addAll(verticesRes);

			// selection preview highlight (not in data mesh only in gpu)
			for (Vertex vertex : verticesRes)
			{
				for (RenderFaceGroup renderGroup : mesh.mRenderGroupList)
				{
					renderGroup.UpdateVertexColor(vertex.Index, highlightColor);
				}
			}

			mLastVertex = origVertex;

			getManagers().getMeshManager().NotifyListeners();
		}
	}

}
