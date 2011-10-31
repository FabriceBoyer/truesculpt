package truesculpt.mesh;


public class Face
{
	public HalfEdge E0 = null;
	public HalfEdge E1 = null;
	public HalfEdge E2 = null;

	// CCW definition	
	public Face(int v0, int v1, int v2, int nFaceIndex, int nSubdivisionLevel)
	{
		E0=new HalfEdge(v0, v1, nFaceIndex);
		E1=new HalfEdge(v1, v2, nFaceIndex);
		E2=new HalfEdge(v2, v0, nFaceIndex);	
		
		E0.nSubdivisionLevel=nSubdivisionLevel;
		E1.nSubdivisionLevel=nSubdivisionLevel;
		E2.nSubdivisionLevel=nSubdivisionLevel;
	}
	
	public HalfEdge GetNextEdge(HalfEdge edge)
	{
		HalfEdge res=null;
		
		if (edge==E0) res=E1;
		else if (edge==E1) res=E2;
		else if (edge==E2) res=E0;
		
		return res;
	}
	
	public HalfEdge GetPreviousEdge(HalfEdge edge)
	{
		HalfEdge res=null;
		
		if (edge==E0) res=E2;
		else if (edge==E1) res=E0;
		else if (edge==E2) res=E1;
		
		return res;
	}
}
