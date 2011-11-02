package truesculpt.mesh;

//oriented edge unique to a triangle
public class HalfEdge
{
	public int V0 = -1;// start
	public int V1 = -1;// end
	public int Face = -1;

	public int nSubdivisionLevel = -1;
	public HalfEdge NeighbourEdge = null;
	public int VNextSplit = -1;

	float Length = -1.0f;

	// unormalized (raw cross product) at V0
	public float[] Normal = new float[3];

	public HalfEdge(int v0, int v1, int face)
	{
		V0 = v0;
		V1 = v1;

		Face = face;
	}

}
