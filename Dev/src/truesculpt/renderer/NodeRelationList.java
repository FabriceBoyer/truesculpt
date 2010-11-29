package truesculpt.renderer;

import java.util.Vector;

public class NodeRelationList
{

	public Vector<Integer> mFaceRelationList = new Vector<Integer>();
	public Vector<NodeRelation> mVertexRelationList = new Vector<NodeRelation>();

	public NodeRelationList()
	{

	}
	
	public void AddFaceRelation(int triangleIndex)
	{
		mFaceRelationList.add(triangleIndex);
	}

	public void AddVertexRelation(int mOtherIndex, float mDistance)
	{
		mVertexRelationList.add(new NodeRelation(mOtherIndex, mDistance));
	}
}
