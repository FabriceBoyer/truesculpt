package truesculpt.mesh;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import truesculpt.utils.MatrixUtils;

public class OctreeNode
{
	public OctreeNode NodeParent = null;
	public ArrayList<OctreeNode> NodeChilds = new ArrayList<OctreeNode>();
	public ArrayList<Vertex> Vertices = new ArrayList<Vertex>();
	public float[] Center = new float[3];
	public float[] Min = new float[3];
	public float[] Max = new float[3];
	public float Radius = -1;

	private float MAX_VERTICES = 100;

	private ShortBuffer mDrawIndexBuffer = null;
	private FloatBuffer mDrawVertexBuffer = null;

	public void RecurseSubdivide()
	{
		if (Vertices.size() > MAX_VERTICES)
		{
			NodeChilds.clear();

			float[] newCenter = new float[3];
			float newRadius = Radius / 2f;

			MatrixUtils.copy(Center, newCenter);
			newCenter[0] += newRadius;
			newCenter[1] += newRadius;
			newCenter[2] += newRadius;
			NodeChilds.add(new OctreeNode(this, newCenter, newRadius));

			MatrixUtils.copy(Center, newCenter);
			newCenter[0] += newRadius;
			newCenter[1] -= newRadius;
			newCenter[2] += newRadius;
			NodeChilds.add(new OctreeNode(this, newCenter, newRadius));

			MatrixUtils.copy(Center, newCenter);
			newCenter[0] -= newRadius;
			newCenter[1] += newRadius;
			newCenter[2] += newRadius;
			NodeChilds.add(new OctreeNode(this, newCenter, newRadius));

			MatrixUtils.copy(Center, newCenter);
			newCenter[0] -= newRadius;
			newCenter[1] -= newRadius;
			newCenter[2] += newRadius;
			NodeChilds.add(new OctreeNode(this, newCenter, newRadius));

			MatrixUtils.copy(Center, newCenter);
			newCenter[0] += newRadius;
			newCenter[1] += newRadius;
			newCenter[2] -= newRadius;
			NodeChilds.add(new OctreeNode(this, newCenter, newRadius));

			MatrixUtils.copy(Center, newCenter);
			newCenter[0] += newRadius;
			newCenter[1] -= newRadius;
			newCenter[2] -= newRadius;
			NodeChilds.add(new OctreeNode(this, newCenter, newRadius));

			MatrixUtils.copy(Center, newCenter);
			newCenter[0] -= newRadius;
			newCenter[1] += newRadius;
			newCenter[2] -= newRadius;
			NodeChilds.add(new OctreeNode(this, newCenter, newRadius));

			MatrixUtils.copy(Center, newCenter);
			newCenter[0] -= newRadius;
			newCenter[1] -= newRadius;
			newCenter[2] -= newRadius;
			NodeChilds.add(new OctreeNode(this, newCenter, newRadius));

			int nCount = 0;
			for (Vertex vertex : Vertices)
			{
				for (OctreeNode subBox : NodeChilds)
				{
					if (subBox.IsVertexInsideBox(vertex))
					{
						subBox.AddVertex(vertex);
						nCount++;
					}
				}
			}

			// Assert.assertTrue(Vertices.size()==nCount);

			// all vertices transfered in child, parent must be empty
			Vertices.clear();

			// recurse
			for (OctreeNode subBox : NodeChilds)
			{
				subBox.RecurseSubdivide();
			}
		}
	}

	public void draw(GL10 gl)
	{
		mDrawIndexBuffer.position(0);
		mDrawVertexBuffer.position(0);

		gl.glFrontFace(GL10.GL_CCW);// counter clock wise is specific to
									// previous format
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mDrawVertexBuffer);

		gl.glDrawElements(GL10.GL_LINES, 24, GL10.GL_UNSIGNED_SHORT, mDrawIndexBuffer);
	}

	private void AddVertex(Vertex vertex)
	{
		Vertices.add(vertex);
		vertex.Box = this;
	}

	private void RemoveVertex(Vertex vertex)
	{
		Vertices.remove(vertex);
		vertex.Box = null;
	}

	public OctreeNode(OctreeNode parent, float[] center, float newRadius)
	{
		super();
		NodeParent = parent;
		MatrixUtils.copy(center, Center);
		Radius = newRadius;

		MatrixUtils.copy(center, Min);
		MatrixUtils.scalarAdd(Min, -newRadius);

		MatrixUtils.copy(center, Max);
		MatrixUtils.scalarAdd(Max, newRadius);

		float[] temp = new float[3];
		ByteBuffer ndvbb = ByteBuffer.allocateDirect(8 * 3 * 4);// normals
																// contains 3
																// elem (x,y,z)
																// in float(4
																// bytes)
		ndvbb.order(ByteOrder.nativeOrder());
		mDrawVertexBuffer = ndvbb.asFloatBuffer();

		MatrixUtils.copy(Center, temp);
		temp[0] += Radius;
		temp[1] += Radius;
		temp[2] += Radius;
		mDrawVertexBuffer.put(temp);

		MatrixUtils.copy(Center, temp);
		temp[0] -= Radius;
		temp[1] += Radius;
		temp[2] += Radius;
		mDrawVertexBuffer.put(temp);

		MatrixUtils.copy(Center, temp);
		temp[0] -= Radius;
		temp[1] -= Radius;
		temp[2] += Radius;
		mDrawVertexBuffer.put(temp);

		MatrixUtils.copy(Center, temp);
		temp[0] += Radius;
		temp[1] -= Radius;
		temp[2] += Radius;
		mDrawVertexBuffer.put(temp);

		MatrixUtils.copy(Center, temp);
		temp[0] += Radius;
		temp[1] += Radius;
		temp[2] -= Radius;
		mDrawVertexBuffer.put(temp);

		MatrixUtils.copy(Center, temp);
		temp[0] -= Radius;
		temp[1] += Radius;
		temp[2] -= Radius;
		mDrawVertexBuffer.put(temp);

		MatrixUtils.copy(Center, temp);
		temp[0] -= Radius;
		temp[1] -= Radius;
		temp[2] -= Radius;
		mDrawVertexBuffer.put(temp);

		MatrixUtils.copy(Center, temp);
		temp[0] += Radius;
		temp[1] -= Radius;
		temp[2] -= Radius;
		mDrawVertexBuffer.put(temp);

		ByteBuffer ndibb = ByteBuffer.allocateDirect(24 * 2);// line are 2
																// elements in
																// short ( 2
																// bytes )
		ndibb.order(ByteOrder.nativeOrder());
		mDrawIndexBuffer = ndibb.asShortBuffer();
		mDrawIndexBuffer.put((short) 0);
		mDrawIndexBuffer.put((short) 1);
		mDrawIndexBuffer.put((short) 2);
		mDrawIndexBuffer.put((short) 3);
		mDrawIndexBuffer.put((short) 0);

		mDrawIndexBuffer.put((short) 4);
		mDrawIndexBuffer.put((short) 7);
		mDrawIndexBuffer.put((short) 3);
		mDrawIndexBuffer.put((short) 0);

		mDrawIndexBuffer.put((short) 1);
		mDrawIndexBuffer.put((short) 5);
		mDrawIndexBuffer.put((short) 4);
		mDrawIndexBuffer.put((short) 0);
		mDrawIndexBuffer.put((short) 1);

		mDrawIndexBuffer.put((short) 1);
		mDrawIndexBuffer.put((short) 5);
		mDrawIndexBuffer.put((short) 6);
		mDrawIndexBuffer.put((short) 2);
		mDrawIndexBuffer.put((short) 1);
		mDrawIndexBuffer.put((short) 2);

		mDrawIndexBuffer.put((short) 6);
		mDrawIndexBuffer.put((short) 7);
		mDrawIndexBuffer.put((short) 3);
		mDrawIndexBuffer.put((short) 2);
	}

	public boolean IsVertexInsideBox(Vertex vertex)
	{
		if (MatrixUtils.isInferiorOrEqual(vertex.Coord, Max) && MatrixUtils.isStrictlyInferior(Min, vertex.Coord))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean IsLeaf()
	{
		return NodeChilds.size() == 0;
	}

	public boolean IsEmpty()
	{
		return Vertices.size() == 0;
	}

	public void Reboxing(Vertex vertex)
	{
		if (!IsVertexInsideBox(vertex))
		{
			// find parent going up
			OctreeNode currBox = NodeParent;
			while (currBox != null && !currBox.IsVertexInsideBox(vertex))
			{
				currBox = currBox.NodeParent;
			}

			if (currBox != null)
			{
				currBox.RecursePlaceVertexInChild(vertex);
			}

			RecurseClean();
		}
	}

	private void RecursePlaceVertexInChild(Vertex vertex)
	{
		// find child going down
		for (OctreeNode box : NodeChilds)
		{
			if (box.IsVertexInsideBox(vertex))
			{
				if (box.IsLeaf())
				{
					box.AddVertex(vertex);
					box.RecurseSubdivide();
					break;
				}
				else
				{
					box.RecursePlaceVertexInChild(vertex);
				}
			}
		}
	}

	// resubdividing and box simplification routines called from time to time
	public void CleaningTask()
	{
		RecurseSubdivide();
		RecurseClean();
	}

	// simplification through removing of empty node with only empty child
	public void RecurseClean()
	{
		boolean bAllChildsEmpty = true;
		for (OctreeNode box : NodeChilds)
		{
			if (box.IsLeaf())
			{
				if (!box.IsEmpty())
				{
					bAllChildsEmpty = false;
				}
			}
			else
			{
				bAllChildsEmpty = false;
				box.RecurseClean();
			}
		}

		if (bAllChildsEmpty)
		{
			NodeChilds.clear();
		}
	}

}
