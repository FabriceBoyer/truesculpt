package truesculpt.mesh;

public class Face
{
	public Edge E0 = null;

	public Edge E1 = null;
	public Edge E2 = null;
	public int nSubdivionLevel = 1;

	// CCW definition and init
	public Face(Vertex v0, Vertex v1, Vertex v2)
	{
		E0 = new Edge(v0, v1, this);
		E1 = new Edge(v1, v2, this);
		E2 = new Edge(v2, v0, this);
	}

	// vertex not contained in vertex e provided
	public Vertex FindThirdVertex(Edge e)
	{
		Vertex e0 = e.V0;
		Vertex e1 = e.V1;

		if (E0.V0 != e0 && E0.V0 != e1)
		{
			return E0.V0;
		}
		if (E0.V1 != e0 && E0.V1 != e1)
		{
			return E0.V1;
		}
		if (E1.V0 != e0 && E1.V0 != e1)
		{
			return E1.V0;
		}
		if (E1.V1 != e0 && E1.V1 != e1)
		{
			return E1.V1;
		}
		if (E2.V0 != e0 && E2.V0 != e1)
		{
			return E2.V0;
		}
		if (E2.V1 != e0 && E2.V1 != e1)
		{
			return E2.V1;
		}

		return null;
	}

	public Vertex getV0()
	{
		if (E0.F0 == this)// not regrouped
		{
			return E0.V0;
		} else
		{
			return E0.V1;
		}
	}

	public Vertex getV1()
	{
		if (E0.F0 == this)// not regrouped
		{
			return E0.V1;
		} else
		{
			return E0.V0;
		}
	}

	// TODO avoid computing: cache
	public Vertex getV2()
	{
		return FindThirdVertex(E0);
	}
}
