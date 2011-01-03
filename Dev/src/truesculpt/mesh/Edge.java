package truesculpt.mesh;

import java.util.Vector;

public class Edge
{
	public Edge(Vertex v0, Vertex v1)
	{
		V0=v0;
		V1=v1;
	}

	public Vertex V0=null;
	public Vertex V1=null;
	
	public Face F0=null;
	public Face F1=null;
	
	public Vector<Edge> mEdgeList= new Vector<Edge>();
}
