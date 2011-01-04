package truesculpt.mesh;

import java.util.Vector;

import truesculpt.utils.MatrixUtils;
import static junit.framework.Assert.*;

public class Mesh
{
	public Vector<Edge> mEdgeList = new Vector<Edge>();
	public Vector<Face> mFaceList = new Vector<Face>();
	public Vector<Vertex> mVertexList = new Vector<Vertex>();

	public Mesh()
	{
		InitAsIcosahedron();
	}
	
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
				
		mVertexList.add(v0);
		mVertexList.add(v1);
		mVertexList.add(v2);
		mVertexList.add(v3);
		mVertexList.add(v4);
		mVertexList.add(v5);
		mVertexList.add(v6);
		mVertexList.add(v7);
		mVertexList.add(v8);
		mVertexList.add(v9);
		mVertexList.add(v10);
		mVertexList.add(v11);

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
		
		mFaceList.add(f0);
		mFaceList.add(f1);
		mFaceList.add(f2);
		mFaceList.add(f3);
		mFaceList.add(f4);
		mFaceList.add(f5);
		mFaceList.add(f6);
		mFaceList.add(f7);
		mFaceList.add(f8);
		mFaceList.add(f9);
		mFaceList.add(f10);
		mFaceList.add(f11);
		mFaceList.add(f12);
		mFaceList.add(f13);
		mFaceList.add(f14);
		mFaceList.add(f15);
		mFaceList.add(f16);
		mFaceList.add(f17);
		mFaceList.add(f18);
		mFaceList.add(f19);
		
		//Create vertex list
		//regroup common vertices and delete useless ones
		for (Face face : mFaceList)
		{
			{
				Edge e=face.E0;
				Edge res=RegroupEdge(e);
				if (res!=e)//regrouped
				{
					face.E0=res;
					face.E0.F1=face;//F0 only set at startup (can be twice the same or different if regrouped but don't care)				
				}
				else
				{
					mEdgeList.add(e);
				}	
			}
			
			{
				Edge e=face.E1;
				Edge res=RegroupEdge(e);
				if (res!=e)//regrouped
				{
					face.E1=res;
					face.E1.F1=face;//F0 only set at startup (can be twice the same or different if regrouped but don't care)				
				}
				else
				{
					mEdgeList.add(e);
				}	
			}
			
			{
				Edge e=face.E2;
				Edge res=RegroupEdge(e);
				if (res!=e)//regrouped
				{
					face.E2=res;
					face.E2.F1=face;//F0 only set at startup (can be twice the same or different if regrouped but don't care)				
				}
				else
				{
					mEdgeList.add(e);
				}	
			}
		}
				
		//update edge list in each vertex
		for (Edge edge : mEdgeList)
		{
			edge.V0.mCloseEdgeList.add(edge);
			edge.V1.mCloseEdgeList.add(edge);
		}
		
		//update close edge list in each edge;
		for (Vertex vertex : mVertexList)
		{
			for (Edge edge : vertex.mCloseEdgeList)
			{
				//TODO filter common edges (use add function or dedicated data structure)
				edge.mLinkedEdgeList.addAll(edge.V0.mCloseEdgeList);
				edge.mLinkedEdgeList.addAll(edge.V1.mCloseEdgeList);
			}
		}
		
		
		assertEquals(mEdgeList.size(),30);
		
		// n_vertices = 12;
		// n_faces = 20;
		// n_edges = 30;
	}
	
	//TODO update function updating all lists
	
	//return an equivalent edge from the list (with equal vertex not faces)
	private Edge RegroupEdge(Edge e)
	{
		Edge res=e;		
		for (Edge edge : mEdgeList)
		{
			if (AreEdgeVertexEqual(e, edge))
			{
				res=edge;
			}
		}
		return res;
	}
	
	private boolean AreEdgeVertexEqual(Edge e0,Edge e1)
	{
		boolean bRes=false;
		
		if 	(((e0.V0==e1.V0)&&(e0.V1==e1.V1)) || ((e0.V1==e1.V0)&&(e0.V0==e1.V1)))
		{ 
			bRes=true;
		}		
		
		return bRes;		
	}
	
	private boolean AreEdgeFaceEqual(Edge e0,Edge e1)
	{
		boolean bRes=false;
		
		if (((e0.F0==e1.F0)&&(e0.F1==e1.F1)) || ((e0.F1==e1.F0)&&(e0.F0==e1.F1)))
		{ 
			bRes=true;
		}		
		
		return bRes;		
	}

	void Reset()
	{

	}

	// makes a sphere
	void NormalizeAllVertices()
	{
		for (Vertex vertex : mVertexList)
		{
			MatrixUtils.normalize(vertex.Coord);
			MatrixUtils.copy(vertex.Coord, vertex.Normal);//Normal is coord because sphere is radius 1			
		}
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
		Reset();

	}
}
