package truesculpt.mesh;

import java.util.ArrayList;

//not oriented
public class HalfEdge
{
	public int V0 = -1;
	public int V1 = -1;
	public int Face = -1;

	public ArrayList<Integer> LinkedEdges = new ArrayList<Integer>();
	float dLength=-1.0f;

	public HalfEdge(int v0, int v1, int face)
	{
		V0 = v0;
		V1 = v1;

		Face=face;		
	}
}
