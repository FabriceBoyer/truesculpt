package truesculpt.mesh;

import java.util.ArrayList;

import truesculpt.utils.MatrixUtils;

public class Vertex
{
	public float[] Coord = new float[3];
	public float[] Normal = new float[3];
	public ArrayList<HalfEdge> InLinkedEdges= new ArrayList<HalfEdge>();
	public ArrayList<HalfEdge> OutLinkedEdges= new ArrayList<HalfEdge>();
	public int Color = 0;
	public OctreeNode Box=null;

	public Vertex(float x, float y, float z)
	{
		Coord[0] = x;
		Coord[1] = y;
		Coord[2] = z;
	}

	public Vertex(float[] coord)
	{
		MatrixUtils.copy(coord, Coord);
	}

	//midVertex
	public Vertex(Vertex V0, Vertex V1)
	{
		float[] temp = new float[3];
		MatrixUtils.plus(V0.Coord, V1.Coord, temp);
		MatrixUtils.scalarMultiply(temp, 0.5f);
		MatrixUtils.copy(temp, Coord);
	}
	public Vertex(Vertex V0, Vertex V1, Vertex V2)
	{
		float[] temp = new float[3];
		MatrixUtils.plus(V0.Coord, V1.Coord, temp);
		MatrixUtils.plus(temp, V2.Coord, temp);
		MatrixUtils.scalarMultiply(temp, 1.0f/3.0f);
		MatrixUtils.copy(temp, Coord);
	}
}
