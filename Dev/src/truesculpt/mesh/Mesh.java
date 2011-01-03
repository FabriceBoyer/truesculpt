package truesculpt.mesh;

import java.util.Vector;

public class Mesh
{
	public Vector<Edge> mEdgeList = new Vector<Edge>();
	public Vector<Face> mFaceList = new Vector<Face>();
	public Vector<Vertex> mVertexList = new Vector<Vertex>();

	void InitAsSphere(int nSubdivionLevel)
	{
		Reset();
		InitAsIcosahedron();
		for (int i=0;i<nSubdivionLevel;i++)
		{
			SubdivideAllFaces();
		}
		NormalizeAllVertices();
		ComputeAllVertexNormals();
		ComputeBoundingSphereRadius();
	}

	void InitAsIcosahedron()
	{
		float t = (float) ((1 + Math.sqrt(5)) / 2);
		float tau = (float) (t / Math.sqrt(1 + t * t));
		float one = (float) (1 / Math.sqrt(1 + t * t));

		// Float icosahedron_vertices[] =
		Vertex v0 = new Vertex(tau, one, 0.0f);
		Vertex v1 = new Vertex(-tau, one, 0.0f);
		Vertex v2 = new Vertex(-tau, -one, 0.0f);
		Vertex v3 = new Vertex(tau, -one, 0.0f);
		Vertex v4 = new Vertex(one, 0.0f, tau);
		Vertex v5 = new Vertex(one, 0.0f, -tau);
		Vertex v6 = new Vertex(-one, 0.0f, -tau);
		Vertex v7 = new Vertex(-one, 0.0f, tau);
		Vertex v8 = new Vertex(0.0f, tau, one);
		Vertex v9 = new Vertex(0.0f, -tau, one);
		Vertex v10 = new Vertex(0.0f, -tau, -one);
		Vertex v11 = new Vertex(0.0f, tau, -one);

		// Counter clock wise (CCVW) face definition
		// Integer icosahedron_faces[] =
		Face f0 = new Face(v4, v8, v7);
		Face f1 = new Face(v4, v7, v9);
		Face f2 = new Face(v5, v6, v11);
		Face f3 = new Face(v5, v10, v6);
		Face f4 = new Face(v0, v4, v3);
		Face f5 = new Face(v0, v3, v5);
		Face f6 = new Face(v2, v7, v1);
		Face f7 = new Face(v2, v1, v6);
		Face f8 = new Face(v8, v0, v11);
		Face f9 = new Face(v8, v11, v1);
		Face f10 = new Face(v9, v10, v3);
		Face f11 = new Face(v9, v2, v10);
		Face f12 = new Face(v8, v4, v0);
		Face f13 = new Face(v11, v0, v5);
		Face f14 = new Face(v4, v9, v3);
		Face f15 = new Face(v5, v3, v10);
		Face f16 = new Face(v7, v8, v1);
		Face f17 = new Face(v6, v1, v11);
		Face f18 = new Face(v7, v2, v9);
		Face f19 = new Face(v6, v10, v2);

		// n_vertices = 12;
		// n_faces = 20;
		// n_edges = 30;
	}

	void Reset()
	{

	}

	// makes a sphere
	void NormalizeAllVertices()
	{

	}

	void SubdivideFace()
	{

	}

	void SubdivideAllFaces()
	{

	}

	Face Pick(float[] rayPt1, float[] rayPt2)
	{
		return null;
	}

	void ComputeAllVertexNormals()
	{

	}

	void ComputeBoundingSphereRadius()
	{

	}

	void ExportToOBJ(String strFileName)
	{

	}

	void ImportFromOBJ(String strFileName)
	{

	}
}
