package truesculpt.tools.base;

import java.util.ArrayList;

public class Path
{
	public ArrayList<PathNode> mNodesList = new ArrayList<PathNode>();

	public void AddNode(int nTriangleIndex, float xScreen, float yScreen)
	{
		mNodesList.add(new PathNode(nTriangleIndex, xScreen, yScreen));
	}

	public void Clear()
	{
		mNodesList.clear();
	}
}
