package truesculpt.mesh;

import java.util.ArrayList;

import junit.framework.Assert;

import truesculpt.utils.MatrixUtils;

public class OctreeNode
{
	public OctreeNode NodeParent=null;
	public ArrayList<OctreeNode> NodeChilds= new ArrayList<OctreeNode>(); 
	public ArrayList<Vertex> Vertices= new ArrayList<Vertex>();
	public float[] Center=new float[3];
	public float[] Min=new float[3];
	public float[] Max=new float[3];
	public float Radius=-1;
	
	private float MAX_VERTICES=500;
	
	public void RecurseSubdivide()
	{
		if (Vertices.size()>MAX_VERTICES)
		{
			NodeChilds.clear();
			
			float[] newCenter=new float[3];
			float newRadius=Radius/2f;
			
			MatrixUtils.copy(Center,newCenter);
			newCenter[0]+=newRadius;
			newCenter[1]+=newRadius;
			newCenter[2]+=newRadius;
			NodeChilds.add(new OctreeNode(this,newCenter, newRadius));
			
			MatrixUtils.copy(Center,newCenter);
			newCenter[0]+=newRadius;
			newCenter[1]-=newRadius;
			newCenter[2]+=newRadius;
			NodeChilds.add(new OctreeNode(this,newCenter, newRadius));
			
			MatrixUtils.copy(Center,newCenter);
			newCenter[0]-=newRadius;
			newCenter[1]+=newRadius;
			newCenter[2]+=newRadius;
			NodeChilds.add(new OctreeNode(this,newCenter, newRadius));
			
			MatrixUtils.copy(Center,newCenter);
			newCenter[0]-=newRadius;
			newCenter[1]-=newRadius;
			newCenter[2]+=newRadius;
			NodeChilds.add(new OctreeNode(this,newCenter, newRadius));
						
			MatrixUtils.copy(Center,newCenter);
			newCenter[0]+=newRadius;
			newCenter[1]+=newRadius;
			newCenter[2]-=newRadius;
			NodeChilds.add(new OctreeNode(this,newCenter, newRadius));
			
			MatrixUtils.copy(Center,newCenter);
			newCenter[0]+=newRadius;
			newCenter[1]-=newRadius;
			newCenter[2]-=newRadius;
			NodeChilds.add(new OctreeNode(this,newCenter, newRadius));
			
			MatrixUtils.copy(Center,newCenter);
			newCenter[0]-=newRadius;
			newCenter[1]+=newRadius;
			newCenter[2]-=newRadius;
			NodeChilds.add(new OctreeNode(this,newCenter, newRadius));
			
			MatrixUtils.copy(Center,newCenter);
			newCenter[0]-=newRadius;
			newCenter[1]-=newRadius;
			newCenter[2]-=newRadius;
			NodeChilds.add(new OctreeNode(this,newCenter, newRadius));
			
			int nCount=0;
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
			
			Assert.assertTrue(Vertices.size()==nCount);
			
			//all vertices transfered in child, parent must be empty
			Vertices.clear();
			
			//recurse
			for (OctreeNode subBox : NodeChilds)
			{
				subBox.RecurseSubdivide();
			}
		}
	}

	private void AddVertex(Vertex vertex)
	{
		Vertices.add(vertex);
		vertex.Box=this;		
	}

	public OctreeNode(OctreeNode parent, float[] center, float newRadius)
	{
		super();
		NodeParent = parent;
		MatrixUtils.copy(center,Center);
		Radius=newRadius;
		
		MatrixUtils.copy(center, Min);
		MatrixUtils.scalarAdd(Min, -newRadius);
		
		MatrixUtils.copy(center, Max);
		MatrixUtils.scalarAdd(Max, newRadius);
	}
	
	public boolean IsVertexInsideBox(Vertex vertex)
	{
		if (MatrixUtils.isInferiorOrEqual(vertex.Coord, Max) &&
			MatrixUtils.isStrictlyInferior(Min, vertex.Coord) 
			)
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
		return NodeChilds.size()==0;
	}
	
	public boolean IsEmpty()
	{	
		return Vertices.size()==0;
	}
	
}
