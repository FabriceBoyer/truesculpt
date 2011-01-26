package truesculpt.mesh;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import truesculpt.main.Managers;
import truesculpt.utils.MatrixUtils;
import static junit.framework.Assert.*;

public class Mesh
{
	ArrayList<Edge> mEdgeList = new ArrayList<Edge>();
	ArrayList<Face> mFaceList = new ArrayList<Face>();
	ArrayList<Vertex> mVertexList = new ArrayList<Vertex>();
	
	ArrayList<RenderFaceGroup> mRenderGroupList= new ArrayList<RenderFaceGroup>();

	Managers mManagers;
	
	public Mesh(Managers managers)
	{
		mManagers=managers;
		
		mEdgeList.ensureCapacity(30000);
		mFaceList.ensureCapacity(30000);
		mVertexList.ensureCapacity(30000);
		
		InitAsSphere(1);		
		
		//String strFileName=getManagers().getUtilsManager().CreateObjExportFileName();
		//ExportToOBJ(strFileName);
		
		mRenderGroupList.add(new RenderFaceGroup(this));
	}
	
	public Managers getManagers()
	{
		return mManagers;
	}
	
	void InitAsSphere(int nSubdivionLevel)
	{
		Reset();
		InitAsIcosahedron();
		RegroupSimilarEdges();
		FinalizeSubdivide();
		for (int i=0;i<nSubdivionLevel;i++)
		{
			SubdivideAllFaces();
			FinalizeSubdivide();
		}
		FinalizeInit();
		NormalizeAllVertices();		
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
				
		assertEquals(mFaceList.size(),20);
		assertEquals(mVertexList.size(),12);
		
		// n_vertices = 12;
		// n_faces = 20;
		// n_edges = 30;
	}
	
	private void RegroupSimilarEdges()
	{
		//Create vertex list
		//regroup common vertices and delete useless ones
		int nCount=mFaceList.size();
		for (int i=0;i<nCount;i++)
		{
			Face face = mFaceList.get(i);
			
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
	}
			
	private void FinalizeSubdivide()
	{
		for (Edge edge : mEdgeList)
		{
			edge.V0.mCloseEdgeList.clear();
			edge.V1.mCloseEdgeList.clear();
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
				edge.mLinkedEdgeList.clear();
				edge.mLinkedEdgeList.clear();
			}
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
	}
	
	private void FinalizeInit()
	{
		//Set default vertex color
		int color=getManagers().getToolsManager().getColor();
		for (Vertex vertex : mVertexList)
		{
			vertex.Color=color;
		}
		
		//check triangle normals are outside and correct if necessary		
		float[] u = new float[3];
		float[] v = new float[3];
		float[] n = new float[3];
		float[] dir = new float[3];
		
		for (Face face : mFaceList)
		{
			Vertex V0 = face.getV0();
			Vertex V1 = face.getV1();			
			Vertex V2 = face.getV2();
			
			// get triangle edge vectors and plane normal
			MatrixUtils.minus(V1.Coord, V0.Coord, u);
			MatrixUtils.minus(V2.Coord, V0.Coord, v);
	
			MatrixUtils.cross(u, v, n); // cross product
			
			dir=V0.Coord;

			boolean bCollinear = MatrixUtils.dot(dir, n) > 0;// dir and normal have same direction
			if (!bCollinear)//swap two edges
			{
				assertTrue(true);
			}
		}
	}
	
	//TODO update function updating all lists	
	//return an equivalent edge from the list (with equal vertex not faces)
	private Edge RegroupEdge(Edge e)
	{
		Edge res=e;		
		int n=mEdgeList.size();
		for (int i=0;i<n;i++)
		{
			Edge edge=mEdgeList.get(i);
			if (AreEdgeVertexEqual(e, edge))
			{
				res=edge;
				break;
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
		mVertexList.clear();
		mFaceList.clear();
		mEdgeList.clear();
	}

	// makes a sphere
	void NormalizeAllVertices()
	{
		int n=mVertexList.size();
		for (int i=0;i<n;i++)
		{
			Vertex vertex=mVertexList.get(i);
			MatrixUtils.normalize(vertex.Coord);
			MatrixUtils.copy(vertex.Coord, vertex.Normal);//Normal is coord because sphere is radius 1			
		}
	}

	void SubdivideFace(Face face)
	{
		boolean bDoesFaceContainsHalfSplitterEdge=face.E0.bHalfSplitter || face.E1.bHalfSplitter || face.E2.bHalfSplitter;
		
		Vertex V0 = face.getV0();
		Vertex V1 = face.getV1();			
		Vertex V2 = face.getV2();
				
		Vertex mid0=new Vertex(V0,V1);
		Vertex mid1=new Vertex(V1,V2);
		Vertex mid2=new Vertex(V2,V0);	
		
		mVertexList.add(mid0);
		mVertexList.add(mid1);
		mVertexList.add(mid2);
	}
	
	void SubdivideFacePartialWithVertexOnEdge(Face face)
	{
		Vertex A = face.getV0();
		Vertex B = face.getV1();			
		Vertex C = face.getV2();
				
		Vertex v0=new Vertex(A,B);//takes mid point
		Vertex v1=new Vertex(B,C);
		Vertex v2=new Vertex(C,A);	
		
		mVertexList.add(v0);
		mVertexList.add(v1);
		mVertexList.add(v2);
		
		Face f0 = new Face(A, v0, v2);
		Face f1 = new Face(v0, B, v1);
		Face f2 = new Face(v1, C, v2);
		Face f3 = new Face(v0, v1, v2);
		
		mFaceList.add(f0);
		mFaceList.add(f1);
		mFaceList.add(f2);
		mFaceList.add(f3);
		
		mFaceList.remove(face);		
	}

	void SubdivideAllFaces()
	{
		/*
		ArrayList<Face> mOrigFaceList = new ArrayList<Face>(mFaceList);
		int n=mOrigFaceList.size();
		for (int i=0;i<n;i++)
		{
			SubdivideFacePartialWithVertexOnEdge(mOrigFaceList.get(i));
		}
		*/
		
		
		ArrayList<Face> mOrigFaceList = new ArrayList<Face>(mFaceList);
		int n=mOrigFaceList.size();
		for (int i=0;i<n;i++)
		{		
			Face face = mOrigFaceList.get(i);					

			Vertex A = face.getV0();
			Vertex B = face.getV1();			
			Vertex C = face.getV2();
					
			Vertex v0=new Vertex(A,B);//takes mid point
			Vertex v1=new Vertex(B,C);
			Vertex v2=new Vertex(C,A);	
			
			mVertexList.add(v0);
			mVertexList.add(v1);
			mVertexList.add(v2);
			
			Face f0 = new Face(A, v0, v2);	
			//f0.E0.F1=
			Face f1 = new Face(v0, B, v1);
			Face f2 = new Face(v1, C, v2);
			Face f3 = new Face(v0, v1, v2);
			
			mFaceList.add(f0);
			mFaceList.add(f1);
			mFaceList.add(f2);
			mFaceList.add(f3);
			
			mFaceList.remove(face);				
		}		
				
	}
	
	
	void SubdivideFaceFromSplittedEdge(Face face)
	{
		
		
	}
	
	private Vertex DivideEdge(Edge edge)
	{
		Face face0 = edge.F0;
		Face face1 = edge.F1;
		Vertex vMid=new Vertex(edge.V0,edge.V1);
		Vertex v0=edge.V0;
		Vertex v1=edge.V1;
		mVertexList.add(vMid);
		Edge e0=new Edge(v0,vMid,face0);
		Edge e1=new Edge(vMid,v1,face0);
		mEdgeList.remove(edge);
		e0.F1=face1;
		e1.F1=face1;		
		mEdgeList.add(e0);
		mEdgeList.add(e1);		
		vMid.mCloseEdgeList.add(e0);
		vMid.mCloseEdgeList.add(e1);
		
		return vMid;
	}

	Face Pick(float[] rayPt1, float[] rayPt2)
	{
		return null;
	}

	void ComputeAllVertexNormals()
	{
		int n=mVertexList.size();
		for (int i=0;i<n;i++)
		{
			Vertex vertex = mVertexList.get(i);
			ComputeVertexNormal(vertex);
		}
	}
	
	//Based on close triangles normals * sin of their angle and normalize
	void ComputeVertexNormal(Vertex vertex)
	{
		
	}
	
	void ComputeBoundingSphereRadius()
	{
		float mBoundingSphereRadius=0.0f;
		int n=mVertexList.size();
		for (int i=0;i<n;i++)
		{
			Vertex vertex = mVertexList.get(i);
			float norm=MatrixUtils.magnitude(vertex.Coord);
			if (norm>mBoundingSphereRadius)
			{
				mBoundingSphereRadius=norm;
			}			
		}
	}

	//From http://en.wikipedia.org/wiki/Wavefront_.obj_file
	void ExportToOBJ(String strFileName)
	{		
		 try
		{
			BufferedWriter file = new BufferedWriter(new FileWriter(strFileName));
			
			file.write("#Generated by Truesculpt version "+getManagers().getUpdateManager().getCurrentVersion().toString()+"\n");
			file.write("http://code.google.com/p/truesculpt/\n");
			
			file.write("\n");
			file.write("# List of Vertices, with (x,y,z[,w]) coordinates, w is optional\n");			  
			for (Vertex vertex : mVertexList)
			{
				String str="v "+String.valueOf(vertex.Coord[0])+" "+String.valueOf(vertex.Coord[1])+" "+String.valueOf(vertex.Coord[2])+"\n";
				file.write(str);
			}
				
			file.write("\n");
			file.write("# Texture coordinates, in (u,v[,w]) coordinates, w is optional\n");
			file.write("\n");			
			
			file.write("# Normals in (x,y,z) form; normals might not be unit\n");
			for (Vertex vertex : mVertexList)
			{
				String str="vn "+String.valueOf(vertex.Normal[0])+" "+String.valueOf(vertex.Normal[1])+" "+String.valueOf(vertex.Normal[2])+"\n";
				file.write(str);
			}			

			file.write("\n");
			file.write("# Face Definitions\n");
			for(Face face : mFaceList)
			{
				int n0=mVertexList.indexOf(face.getV0());
				int n1=mVertexList.indexOf(face.getV1());
				int n2=mVertexList.indexOf(face.getV2());
				
				assertTrue(n0>=0);
				assertTrue(n1>=0);
				assertTrue(n2>=0);
				
				//A valid vertex index starts from 1 and match first vertex element of vertex list previously defined. Each face can contain more than three elements.
				String str="f "+
				String.valueOf(n0+1)+"//"+String.valueOf(n0+1)+" "+
				String.valueOf(n1+1)+"//"+String.valueOf(n1+1)+" "+
				String.valueOf(n2+1)+"//"+String.valueOf(n2+1)+"\n";
				
				file.write(str);
			}
			  
			file.write("\n");
			file.close();
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getManagers().getUtilsManager().ShowToastMessage("Sculpture successfully exported to " + strFileName);
	}

	void ImportFromOBJ(String strFileName) throws IOException
	{
		Reset();

		int nCount = 0;
		float[] coord = new float[2];

		LineNumberReader input = new LineNumberReader(new InputStreamReader(new FileInputStream(strFileName)));
		String line = null;
		try
		{
			for (line = input.readLine(); line != null; line = input.readLine())
			{
				if (line.length() > 0)
				{
					if (line.startsWith("v "))
					{
						float[] vertex = new float[3];
						StringTokenizer tok = new StringTokenizer(line);
						tok.nextToken();
						vertex[0] = Float.parseFloat(tok.nextToken());
						vertex[1] = Float.parseFloat(tok.nextToken());
						vertex[2] = Float.parseFloat(tok.nextToken());
						// m.addVertex(vertex);
					}
					else if (line.startsWith("vt "))
					{
						StringTokenizer tok = new StringTokenizer(line);
						tok.nextToken();
						coord[0] = Float.parseFloat(tok.nextToken());
						coord[1] = Float.parseFloat(tok.nextToken());
						// m.addTextureCoordinate(coord);
					} 
					else if (line.startsWith("f "))
					{
						int[] face = new int[3];
						int[] face_n_ix = new int[3];
						int[] face_tx_ix = new int[3];
						int[] val;

						StringTokenizer tok = new StringTokenizer(line);
						tok.nextToken();
						val = parseIntTriple(tok.nextToken());
						face[0] = val[0];
						if (val.length > 1 && val[1] > -1)
							face_tx_ix[0] = val[1];
						if (val.length > 2 && val[2] > -1)
							face_n_ix[0] = val[2];

						val = parseIntTriple(tok.nextToken());
						face[1] = val[0];
						if (val.length > 1 && val[1] > -1)
							face_tx_ix[1] = val[1];
						if (val.length > 2 && val[2] > -1)
							face_n_ix[1] = val[2];

						val = parseIntTriple(tok.nextToken());
						face[2] = val[0];
						if (val.length > 1 && val[1] > -1)
						{
							face_tx_ix[2] = val[1];
							// m.addTextureIndices(face_tx_ix);
						}
						if (val.length > 2 && val[2] > -1)
						{
							face_n_ix[2] = val[2];
							// m.addFaceNormals(face_n_ix);
						}
						// m.addFace(face);
						if (tok.hasMoreTokens())
						{
							val = parseIntTriple(tok.nextToken());
							face[1] = face[2];
							face[2] = val[0];
							if (val.length > 1 && val[1] > -1)
							{
								face_tx_ix[1] = face_tx_ix[2];
								face_tx_ix[2] = val[1];
								// m.addTextureIndices(face_tx_ix);
							}
							if (val.length > 2 && val[2] > -1)
							{
								face_n_ix[1] = face_n_ix[2];
								face_n_ix[2] = val[2];
								// m.addFaceNormals(face_n_ix);
							}
							// m.addFace(face);
						}

					} 
					else if (line.startsWith("vn "))
					{
						nCount++;
						float[] norm = new float[3];
						StringTokenizer tok = new StringTokenizer(line);
						tok.nextToken();
						norm[0] = Float.parseFloat(tok.nextToken());
						norm[1] = Float.parseFloat(tok.nextToken());
						norm[2] = Float.parseFloat(tok.nextToken());
						// m.addNormal(norm);
					}
				}
			}
		} catch (Exception ex)
		{
			System.err.println("Error parsing file:");
			System.err.println(input.getLineNumber() + " : " + line);
		}
		// if (!file_normal) {
		// m.calculateFaceNormals(coordinate_hand);
		// m.calculateVertexNormals();
		// // m.copyNormals();
	}

	protected static int parseInt(String val)
	{
		if (val.length() == 0)
		{
			return -1;
		}
		return Integer.parseInt(val);
	}

	protected static int[] parseIntTriple(String face)
	{
		int ix = face.indexOf("/");
		if (ix == -1)
			return new int[] { Integer.parseInt(face) - 1 };
		else
		{
			int ix2 = face.indexOf("/", ix + 1);
			if (ix2 == -1)
			{
				return new int[] { Integer.parseInt(face.substring(0, ix)) - 1, Integer.parseInt(face.substring(ix + 1)) - 1 };
			} else
			{
				return new int[] { parseInt(face.substring(0, ix)) - 1, parseInt(face.substring(ix + 1, ix2)) - 1, parseInt(face.substring(ix2 + 1)) - 1 };
			}
		}
	}
	
	public void draw(GL10 gl)
	{
		for (RenderFaceGroup renderGroup : mRenderGroupList)
		{
			renderGroup.draw(gl);
		}
	}
	
	public int getVertexCount()
	{
		return mVertexList.size();
	}
	public int getFaceCount()
	{
		return mFaceList.size();
	}
	public int getEdgeCount()
	{
		return mEdgeList.size();
	}
}
