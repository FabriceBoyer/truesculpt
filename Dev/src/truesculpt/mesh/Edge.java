package truesculpt.mesh;

import java.util.ArrayList;

public class Edge
{
	public Vertex V0 = null;
	public Vertex V1 = null;
	public Face F0 = null;
	public Face F1 = null;
	public ArrayList<Edge> mLinkedEdgeList = new ArrayList<Edge>();

	boolean bHalfSplitter = false;

	public Edge(Vertex v0, Vertex v1, Face F)
	{
		V0 = v0;
		V1 = v1;

		V0.mCloseEdgeList.add(this);
		V1.mCloseEdgeList.add(this);

		F0 = F;
		F1 = F;
	}
}
