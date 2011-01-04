package truesculpt.mesh;

import java.util.Vector;


public class Face
{
	//CCW definition and init
	public Face(Vertex v0, Vertex v1, Vertex v2)
	{
		E0=new Edge(v0,v1,this);
		E1=new Edge(v1,v2,this);
		E2=new Edge(v2,v0,this);
	}

	public Edge E0=null;
	public Edge E1=null;
	public Edge E2=null;	
}
