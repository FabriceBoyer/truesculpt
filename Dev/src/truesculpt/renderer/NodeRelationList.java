package truesculpt.renderer;

import java.util.Vector;

public class NodeRelationList
{

	public Vector<NodeRelation> mRelationList = new Vector<NodeRelation>();

	public NodeRelationList()
	{

	}

	public void AddRelation(int mOtherIndex, float mDistance)
	{
		mRelationList.add(new NodeRelation(mOtherIndex, mDistance));
	}
}
