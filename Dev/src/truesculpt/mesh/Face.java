package truesculpt.mesh;


public class Face
{
	public int V0 = -1;
	public int V1 = -1;
	public int V2 = -1;

	// CCW definition
	public Face(int v0, int v1, int v2)
	{
		V0=v0;
		V1=v1;
		V2=v2;		
	}
}
