package truesculpt.mesh;

import java.util.Vector;

public class Edge
{
	public Edge(Vertex v0, Vertex v1, Face F)
	{
		V0=v0;
		V1=v1;
		
		F0=F;	
		F1=F;
	}

	public Vertex V0=null;
	public Vertex V1=null;
	
	public Face F0=null;
	public Face F1=null;
	
	public Vector<Edge> mLinkedEdgeList= new Vector<Edge>();
}
