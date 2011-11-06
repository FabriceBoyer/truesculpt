package truesculpt.tools.base;

public class PathNode
{
	public PathNode(int nTriangleIndex, float xScreen, float yScreen)
	{
		super();
		this.nTriangleIndex = nTriangleIndex;
		this.xScreen = xScreen;
		this.yScreen = yScreen;
	}

	public int nTriangleIndex = -1;
	public float xScreen = -1;
	public float yScreen = -1;
}