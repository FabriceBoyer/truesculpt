package truesculpt.mesh;

import java.util.Vector;

public class Vertex
{
	public Vertex(float x, float y, float z)
	{		
		Coord[0]=x;
		Coord[1]=y;
		Coord[2]=z;
	}

	public float[] Coord = new float[3];
	public float[] Normal = new float[3];
	
	public Vector<Edge> mCloseEdgeList= new Vector<Edge>();
	
	public int Color=0;
}
