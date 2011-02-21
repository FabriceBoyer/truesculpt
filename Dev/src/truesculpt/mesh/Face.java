package truesculpt.mesh;


public class Face
{
	public HalfEdge E0 = null;
	public HalfEdge E1 = null;
	public HalfEdge E2 = null;

	// CCW definition
	public Face(HalfEdge e0, HalfEdge e1, HalfEdge e2)
	{
		E0=e0;
		E1=e1;
		E2=e2;		
	}
	
	public Face(int v0, int v1, int v2, int nFaceIndex)
	{
		E0=new HalfEdge(v0, v1, nFaceIndex);
		E1=new HalfEdge(v1, v2, nFaceIndex);
		E2=new HalfEdge(v2, v0, nFaceIndex);	
	}
}
