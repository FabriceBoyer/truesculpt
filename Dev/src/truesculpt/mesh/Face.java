package truesculpt.mesh;

import java.util.Vector;


public class Face
{
	//CCW definition and init
	public Face(Vertex v0, Vertex v1, Vertex v2)
	{
		E0=new Edge(v0,v1);
		E1=new Edge(v1,v2);
		E2=new Edge(v2,v0);
	}

	Edge E0=null;
	Edge E1=null;
	Edge E2=null;	
}
