package truesculpt.mesh;

import java.util.ArrayList;

public class Edge
{
	public int V0 = -1;
	public int V1 = -1;
	public int F0 = -1;
	public int F1 = -1;
	public ArrayList<Integer> LinkedEdges = new ArrayList<Integer>();

	public Edge(int v0, int v1, int F0, int F1)
	{
		V0 = v0;
		V1 = v1;

		this.F0 = F0;
		this.F1 = F1;
	}
}
