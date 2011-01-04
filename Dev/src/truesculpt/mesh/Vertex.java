package truesculpt.mesh;

import java.util.Vector;

public class Vertex
{
	public Vertex(float x, float y, float z)
	{
		Coord = new float[3];
		Coord[0]=x;
		Coord[1]=y;
		Coord[2]=z;
	}

	public float[] Coord = null;
	public float[] Normal = null;
	
	public Vector<Edge> mCloseEdgeList= new Vector<Edge>();
}
