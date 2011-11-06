package truesculpt.mesh;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import javax.microedition.khronos.opengles.GL10;

import junit.framework.Assert;
import truesculpt.main.Managers;
import truesculpt.managers.ToolsManager;
import truesculpt.utils.MatrixUtils;

public class Mesh
{
	public ArrayList<Face> mFaceList = new ArrayList<Face>();
	public ArrayList<Vertex> mVertexList = new ArrayList<Vertex>();
	public ArrayList<RenderFaceGroup> mRenderGroupList = new ArrayList<RenderFaceGroup>();
	public OctreeNode mRootBoxNode = null;
	static float[] u = new float[3];
	static float[] v = new float[3];
	static float[] dir = new float[3];
	private final Managers mManagers;

	public Mesh(Managers managers, int nSubdivisionLevel)
	{
		mManagers = managers;

		Reset();

		InitAsSphere(nSubdivisionLevel);

		ComputeBoundingSphereRadius();
		InitOctree();
		mRenderGroupList.add(new RenderFaceGroup(this));
	}

	private void InitOctree()
	{

		mRootBoxNode = new OctreeNode(null, new float[] { 0f, 0f, 0f }, 4f);
		mRootBoxNode.Vertices.addAll(mVertexList);
		mRootBoxNode.RecurseSubdivide();
		// CheckOctree();
	}

	private void CheckOctree()
	{
		// check all vertices have a box
		for (Vertex vertex : mVertexList)
		{
			Assert.assertTrue(vertex.Box != null);
		}
		// count boxes
		ArrayList<OctreeNode> boxes = new ArrayList<OctreeNode>();
		RecurseBoxes(mRootBoxNode, boxes);
		int nVertexCount = 0;
		int nNonEmptyBoxes = 0;
		for (OctreeNode box : boxes)
		{
			int n = box.Vertices.size();
			nVertexCount += n;
			if (n > 0)
			{
				nNonEmptyBoxes++;
			}
		}
		Assert.assertTrue(nVertexCount == mVertexList.size());
	}

	private void RecurseBoxes(OctreeNode currBox, ArrayList<OctreeNode> boxes)
	{
		if (!currBox.IsLeaf())
		{
			boxes.addAll(currBox.NodeChilds);
			for (OctreeNode box : currBox.NodeChilds)
			{
				RecurseBoxes(box, boxes);
			}
		}
	}

	float mBoundingSphereRadius = 0.0f;

	public void ComputeBoundingSphereRadius()
	{
		for (Vertex vertex : mVertexList)
		{
			float norm = MatrixUtils.magnitude(vertex.Coord);
			if (norm > mBoundingSphereRadius)
			{
				mBoundingSphereRadius = norm;
			}
		}
		getManagers().getPointOfViewManager().setRmin(1 + mBoundingSphereRadius);
	}

	void ComputeAllVertexNormals()
	{
		ComputeAllFaceEdgesNormals();

		for (Vertex vertex : mVertexList)
		{
			ComputeVertexNormal(vertex);
		}
	}

	public void ComputeVertexNormal(Integer vertex)
	{
		ComputeVertexNormal(mVertexList.get(vertex));
	}

	// Based on close triangles normals * sin of their angle and normalize
	// averaging normals of triangles around
	public static void ComputeVertexNormal(Vertex vertex)
	{
		// reset normal
		vertex.Normal[0] = 0f;
		vertex.Normal[1] = 0f;
		vertex.Normal[2] = 0f;

		// not ordered
		for (HalfEdge edge : vertex.OutLinkedEdges)
		{
			MatrixUtils.plus(edge.Normal, vertex.Normal, vertex.Normal);
		}

		// unit normal
		MatrixUtils.normalize(vertex.Normal);
	}

	public void ComputeAllFaceEdgesNormals()
	{
		for (Face face : mFaceList)
		{
			ComputeFaceEdgesNormal(face);
		}
	}

	public void ComputeFaceEdgesNormal(Integer nFaceIndex)
	{
		ComputeFaceEdgesNormal(mFaceList.get(nFaceIndex));
	}

	public void ComputeFaceEdgesNormal(Face face)
	{
		Vertex A = mVertexList.get(face.E0.V0);
		Vertex B = mVertexList.get(face.E0.V1);
		Vertex C = mVertexList.get(face.E1.V1);

		// E0
		MatrixUtils.minus(B.Coord, A.Coord, u);
		MatrixUtils.minus(C.Coord, A.Coord, v);
		MatrixUtils.cross(u, v, face.E0.Normal);

		// E1
		MatrixUtils.minus(C.Coord, B.Coord, u);
		MatrixUtils.minus(A.Coord, B.Coord, v);
		MatrixUtils.cross(u, v, face.E1.Normal);

		// E2
		MatrixUtils.minus(A.Coord, C.Coord, u);
		MatrixUtils.minus(B.Coord, C.Coord, v);
		MatrixUtils.cross(u, v, face.E2.Normal);
	}

	public void draw(GL10 gl)
	{
		for (RenderFaceGroup renderGroup : mRenderGroupList)
		{
			renderGroup.draw(gl);
		}
	}

	public void drawNormals(GL10 gl)
	{
		for (RenderFaceGroup renderGroup : mRenderGroupList)
		{
			// renderGroup.drawNormals(gl);
		}
	}

	public void drawOctree(GL10 gl)
	{
		ArrayList<OctreeNode> boxes = new ArrayList<OctreeNode>();
		if (mRootBoxNode != null)
		{
			RecurseBoxes(mRootBoxNode, boxes);
			for (OctreeNode box : boxes)
			{
				if (!box.IsEmpty())
				{
					box.draw(gl);
				}
			}
		}
	}

	// From http://en.wikipedia.org/wiki/Wavefront_.obj_file
	public void ExportToOBJ(String strFileName)
	{
		MeshSerializer.Export(strFileName, this);
	}

	private void FinalizeSphereInit()
	{
		getManagers().getToolsManager();
		setAllVerticesColor(ToolsManager.getDefaultColor());

		normalizeAllVertices();

		computeVerticesLinkedEdges();
		linkNeighbourEdges();

		ComputeAllVertexNormals();

		checkFacesNormals();
	}

	private void checkFacesNormals()
	{
		// check triangle normals are outside and correct if necessary
		float[] u = new float[3];
		float[] v = new float[3];
		float[] n = new float[3];
		float[] dir = new float[3];

		for (Face face : mFaceList)
		{
			Vertex V0 = mVertexList.get(face.E0.V0);
			Vertex V1 = mVertexList.get(face.E1.V0);
			Vertex V2 = mVertexList.get(face.E2.V0);

			// get triangle edge vectors and plane normal
			MatrixUtils.minus(V1.Coord, V0.Coord, u);
			MatrixUtils.minus(V2.Coord, V0.Coord, v);

			MatrixUtils.cross(u, v, n); // cross product

			dir = V0.Coord;

			boolean bColinear = MatrixUtils.dot(dir, n) > 0;// dir and normal
															// have same
															// direction
			if (!bColinear)// swap two edges
			{
				assertTrue(false);
			}
		}
	}

	private void setAllVerticesColor(int color)
	{
		for (Vertex vertex : mVertexList)
		{
			vertex.Color = color;
		}
	}

	public int getFaceCount()
	{
		return mFaceList.size();
	}

	public Managers getManagers()
	{
		return mManagers;
	}

	public int getVertexCount()
	{
		return mVertexList.size();
	}

	public void ImportFromOBJ(String strFileName) throws IOException
	{
		Reset();

		MeshSerializer.Import(strFileName, this);

		computeVerticesLinkedEdges();
		linkNeighbourEdges();

		ComputeAllVertexNormals();

		ComputeBoundingSphereRadius();
		InitOctree();

		mRenderGroupList.add(new RenderFaceGroup(this));

		getManagers().getMeshManager().NotifyListeners();
	}

	void InitAsIcosahedron()
	{
		float t = (float) ((1 + Math.sqrt(5)) / 2);
		float tau = (float) (t / Math.sqrt(1 + t * t));
		float one = (float) (1 / Math.sqrt(1 + t * t));

		mVertexList.add(new Vertex(tau, one, 0.0f, mVertexList.size()));
		mVertexList.add(new Vertex(-tau, one, 0.0f, mVertexList.size()));
		mVertexList.add(new Vertex(-tau, -one, 0.0f, mVertexList.size()));
		mVertexList.add(new Vertex(tau, -one, 0.0f, mVertexList.size()));
		mVertexList.add(new Vertex(one, 0.0f, tau, mVertexList.size()));
		mVertexList.add(new Vertex(one, 0.0f, -tau, mVertexList.size()));
		mVertexList.add(new Vertex(-one, 0.0f, -tau, mVertexList.size()));
		mVertexList.add(new Vertex(-one, 0.0f, tau, mVertexList.size()));
		mVertexList.add(new Vertex(0.0f, tau, one, mVertexList.size()));
		mVertexList.add(new Vertex(0.0f, -tau, one, mVertexList.size()));
		mVertexList.add(new Vertex(0.0f, -tau, -one, mVertexList.size()));
		mVertexList.add(new Vertex(0.0f, tau, -one, mVertexList.size()));

		// Counter clock wise (CCW) face definition
		mFaceList.add(new Face(4, 8, 7, mFaceList.size(), 0));
		mFaceList.add(new Face(4, 7, 9, mFaceList.size(), 0));
		mFaceList.add(new Face(5, 6, 11, mFaceList.size(), 0));
		mFaceList.add(new Face(5, 10, 6, mFaceList.size(), 0));
		mFaceList.add(new Face(0, 4, 3, mFaceList.size(), 0));
		mFaceList.add(new Face(0, 3, 5, mFaceList.size(), 0));
		mFaceList.add(new Face(2, 7, 1, mFaceList.size(), 0));
		mFaceList.add(new Face(2, 1, 6, mFaceList.size(), 0));
		mFaceList.add(new Face(8, 0, 11, mFaceList.size(), 0));
		mFaceList.add(new Face(8, 11, 1, mFaceList.size(), 0));
		mFaceList.add(new Face(9, 10, 3, mFaceList.size(), 0));
		mFaceList.add(new Face(9, 2, 10, mFaceList.size(), 0));
		mFaceList.add(new Face(8, 4, 0, mFaceList.size(), 0));
		mFaceList.add(new Face(11, 0, 5, mFaceList.size(), 0));
		mFaceList.add(new Face(4, 9, 3, mFaceList.size(), 0));
		mFaceList.add(new Face(5, 3, 10, mFaceList.size(), 0));
		mFaceList.add(new Face(7, 8, 1, mFaceList.size(), 0));
		mFaceList.add(new Face(6, 1, 11, mFaceList.size(), 0));
		mFaceList.add(new Face(7, 2, 9, mFaceList.size(), 0));
		mFaceList.add(new Face(6, 10, 2, mFaceList.size(), 0));

		assertEquals(mFaceList.size(), 20);
		assertEquals(mVertexList.size(), 12);
	}

	private void computeVerticesLinkedEdges()
	{
		// clear all
		for (Vertex vertex : mVertexList)
		{
			vertex.InLinkedEdges.clear();
			vertex.OutLinkedEdges.clear();
		}

		// compute all
		for (Face face : mFaceList)
		{
			UpdateVertexLinkedEdge(face.E0);
			UpdateVertexLinkedEdge(face.E1);
			UpdateVertexLinkedEdge(face.E2);
		}
	}

	private void UpdateVertexLinkedEdge(HalfEdge edge)
	{
		mVertexList.get(edge.V0).OutLinkedEdges.add(edge);
		mVertexList.get(edge.V1).InLinkedEdges.add(edge);
	}

	// suppose linked edges of vertices are correct
	// suboptimal, links made several times
	private void linkNeighbourEdges()
	{
		int n = mVertexList.size();
		for (int i = 0; i < n; i++)
		{
			Vertex vertex = mVertexList.get(i);
			for (HalfEdge e0 : vertex.OutLinkedEdges)
			{
				for (HalfEdge e1 : vertex.InLinkedEdges)
				{
					linkEdgesIfNeighbours(e0, e1);
				}
			}
		}
	}

	private static boolean linkEdgesIfNeighbours(HalfEdge e0, HalfEdge e1)
	{
		boolean bRes = false;

		if ((e0.V0 == e1.V1) && (e0.V1 == e1.V0))
		{
			e0.NeighbourEdge = e1;
			e1.NeighbourEdge = e0;
			bRes = true;
		}

		return bRes;
	}

	void InitAsSphere(int nSubdivionLevel)
	{
		if (nSubdivionLevel >= 0)
		{
			InitAsIcosahedron();
			for (int i = 0; i < nSubdivionLevel; i++)
			{
				SubdivideAllFaces(i);
			}
			FinalizeSphereInit();
		}
	}

	// makes a sphere
	void normalizeAllVertices()
	{
		for (Vertex vertex : mVertexList)
		{
			MatrixUtils.normalize(vertex.Coord);
			MatrixUtils.copy(vertex.Coord, vertex.Normal);// Normal is coord because sphere is radius 1
		}
	}

	private void RecurseBoxesToTest(OctreeNode currBox, ArrayList<OctreeNode> BoxesToTest, final float[] Rinit, final float[] Rdir)
	{
		if (MeshMathsUtils.ray_box_intersect(currBox, Rinit, Rdir, 0, 10))
		{
			if (currBox.IsLeaf())
			{
				if (!currBox.IsEmpty())
				{
					BoxesToTest.add(currBox);
				}
			}
			else
			{
				for (OctreeNode box : currBox.NodeChilds)
				{
					RecurseBoxesToTest(box, BoxesToTest, Rinit, Rdir);
				}
			}
		}
	}

	private static void SortBoxesByDistance(ArrayList<OctreeNode> BoxesToTest, final float[] R0)
	{
		Comparator<OctreeNode> comperator = new Comparator<OctreeNode>()
		{
			@Override
			public int compare(OctreeNode box1, OctreeNode box2)
			{
				float[] diff = new float[3];
				MatrixUtils.minus(box1.Center, R0, diff);
				float dist1 = MatrixUtils.magnitude(diff);
				MatrixUtils.minus(box2.Center, R0, diff);
				float dist2 = MatrixUtils.magnitude(diff);
				if (dist1 < dist2)
				{
					return -1;
				}
				else if (dist1 == dist2)
				{
					return 0;
				}
				else
				{
					return 1;
				}
			}
		};
		Collections.sort(BoxesToTest, comperator);
	}

	ArrayList<OctreeNode> BoxesToTest = new ArrayList<OctreeNode>();
	HashSet<Integer> boxFaces = new HashSet<Integer>();

	public int Pick(float[] R0, float[] R1, float[] intersectPtReturn)
	{
		int nRes = -1;
		float[] Ires = new float[3];

		MatrixUtils.minus(R1, R0, dir);
		float fSmallestSqDistanceToR0 = MatrixUtils.squaremagnitude(dir);// ray is R0 to R1

		BoxesToTest.clear();
		RecurseBoxesToTest(mRootBoxNode, BoxesToTest, R0, dir);
		SortBoxesByDistance(BoxesToTest, R0);
		boxFaces.clear();
		for (OctreeNode box : BoxesToTest)
		{
			// fill face list of the box
			boxFaces.clear();
			for (Vertex vertex : box.Vertices)
			{
				for (HalfEdge edge : vertex.OutLinkedEdges)
				{
					boxFaces.add(edge.Face);
				}
			}

			// intersection with triangles of the box
			for (Integer i : boxFaces)
			{
				Face face = mFaceList.get(i);

				int nCollide = MeshMathsUtils.intersect_RayTriangle(R0, R1, mVertexList.get(face.E0.V0).Coord, mVertexList.get(face.E1.V0).Coord, mVertexList.get(face.E2.V0).Coord, Ires);

				if (nCollide == 1)
				{
					MatrixUtils.minus(Ires, R0, dir);
					float fSqDistanceToR0 = MatrixUtils.squaremagnitude(dir);
					if (fSqDistanceToR0 <= fSmallestSqDistanceToR0)
					{
						MatrixUtils.copy(Ires, intersectPtReturn);
						nRes = i;
						fSmallestSqDistanceToR0 = fSqDistanceToR0;
					}
				}
			}

			// intersection found stop loop
			if (nRes >= 0)
			{
				break;
			}
		}
		return nRes;
	}

	ArrayList<Vertex> verticesToTest = new ArrayList<Vertex>();

	public void GetVerticesAtDistanceFromVertex(Vertex origVertex, float sqDistance, HashSet<Vertex> res)
	{
		res.add(origVertex);// add at least this point

		verticesToTest.clear();
		for (HalfEdge edge : origVertex.OutLinkedEdges)
		{
			verticesToTest.add(mVertexList.get(edge.V1));
		}

		float[] temp = new float[3];
		int nCount = verticesToTest.size();
		while (nCount > 0)
		{
			Vertex currVertex = verticesToTest.get(nCount - 1);
			verticesToTest.remove(nCount - 1);

			MatrixUtils.minus(currVertex.Coord, origVertex.Coord, temp);
			float currSqDistance = MatrixUtils.squaremagnitude(temp);
			if (currSqDistance < sqDistance)
			{
				res.add(currVertex);
				for (HalfEdge edge : currVertex.OutLinkedEdges)
				{
					Vertex vertexToAdd = mVertexList.get(edge.V1);
					if (!res.contains(vertexToAdd))// avoids looping
					{
						verticesToTest.add(vertexToAdd);
					}
				}
			}

			nCount = verticesToTest.size();
		}
	}

	public void GetVerticesAtDistanceFromVertexLine(Vertex vNew, Vertex vLast, float sqDistance, HashSet<Vertex> res)
	{
		res.add(vNew);

		verticesToTest.clear();
		for (HalfEdge edge : vNew.OutLinkedEdges)
		{
			verticesToTest.add(mVertexList.get(edge.V1));
		}

		float[] temp = new float[3];
		int nCount = verticesToTest.size();
		while (nCount > 0)
		{
			Vertex currVertex = verticesToTest.get(nCount - 1);
			verticesToTest.remove(nCount - 1);

			MatrixUtils.minus(currVertex.Coord, vNew.Coord, temp);
			float currSqDistance = MatrixUtils.squaremagnitude(temp);
			if (currSqDistance < sqDistance)
			{
				res.add(currVertex);
				for (HalfEdge edge : currVertex.OutLinkedEdges)
				{
					Vertex vertexToAdd = mVertexList.get(edge.V1);
					if (!res.contains(vertexToAdd))// avoids looping
					{
						verticesToTest.add(vertexToAdd);
					}
				}
			}

			nCount = verticesToTest.size();
		}
	}

	// notification not done, to do in calling thread with post
	void Reset()
	{
		mVertexList.clear();
		mFaceList.clear();
		mRenderGroupList.clear();
		mRootBoxNode = null;
		getManagers().getActionsManager().ClearAll();
		System.gc();
	}

	// to share vertices between edges
	private int getMiddleDivideVertexForEdge(HalfEdge edge)
	{
		int nRes = -1;
		if (edge.VNextSplit != -1)
		{
			nRes = edge.VNextSplit;
		}
		else
		{
			nRes = mVertexList.size();
			mVertexList.add(new Vertex(mVertexList.get(edge.V0), mVertexList.get(edge.V1), nRes));// takes mid point
		}
		return nRes;
	}

	// one triangle become four (cut on middle of each edge)
	void SubdivideAllFaces(int nSubdivionLevel)
	{
		computeVerticesLinkedEdges();
		linkNeighbourEdges();

		// backup original face list and create a brand new one (no face is kept
		// all divided), vertices are only addes none is removed
		ArrayList<Face> mOrigFaceList = mFaceList;
		mFaceList = new ArrayList<Face>();

		for (Face face : mOrigFaceList)
		{
			int nA = face.E0.V0;
			int nB = face.E1.V0;
			int nC = face.E2.V0;

			int nD = getMiddleDivideVertexForEdge(face.E0);
			int nE = getMiddleDivideVertexForEdge(face.E1);
			int nF = getMiddleDivideVertexForEdge(face.E2);

			mFaceList.add(new Face(nA, nD, nF, mFaceList.size(), nSubdivionLevel + 1));
			mFaceList.add(new Face(nD, nB, nE, mFaceList.size(), nSubdivionLevel + 1));
			mFaceList.add(new Face(nE, nC, nF, mFaceList.size(), nSubdivionLevel + 1));
			mFaceList.add(new Face(nD, nE, nF, mFaceList.size(), nSubdivionLevel + 1));

			// update next split of neighbours
			face.E0.NeighbourEdge.VNextSplit = nD;
			face.E1.NeighbourEdge.VNextSplit = nE;
			face.E2.NeighbourEdge.VNextSplit = nF;
		}
	}

	void SetAllEdgesSubdivionLevel(int nLevel)
	{
		for (Face face : mFaceList)
		{
			face.E0.nSubdivisionLevel = nLevel;
			face.E1.nSubdivisionLevel = nLevel;
			face.E2.nSubdivisionLevel = nLevel;
		}
	}

	public void UpdateVertexValue(Integer vertex)
	{
		UpdateVertexValue(mVertexList.get(vertex));
	}

	public void UpdateVertexValue(Vertex vertex)
	{
		vertex.Box.Reboxing(vertex);// update octree

		for (RenderFaceGroup renderGroup : mRenderGroupList)
		{
			renderGroup.UpdateVertexValue(vertex.Index, vertex.Coord, vertex.Normal);
		}
		UpdateBoudingSphereRadius(vertex.Coord);
	}

	public void UpdateVertexColor(Vertex vertex)
	{
		for (RenderFaceGroup renderGroup : mRenderGroupList)
		{
			renderGroup.UpdateVertexColor(vertex.Index, vertex.Color);
		}
	}

	void UpdateBoudingSphereRadius(float[] val)
	{
		float norm = MatrixUtils.magnitude(val);
		if (norm > mBoundingSphereRadius)
		{
			mBoundingSphereRadius = norm;
			getManagers().getPointOfViewManager().setRmin(1 + mBoundingSphereRadius);// takes near clip into account,
			// TODO read from conf
		}
	}

	public ArrayList<Vertex> getVertexList()
	{
		return mVertexList;
	}

	public ArrayList<Face> getFaceList()
	{
		return mFaceList;
	}

}
