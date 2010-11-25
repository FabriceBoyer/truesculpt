package truesculpt.renderer;

import java.util.List;
import java.util.Vector;

public class NodeRelationList {

	public NodeRelationList() {

	}
	
	public void AddRelation(int mOtherIndex, float mDistance)
	{
		mRelationList.add(new NodeRelation(mOtherIndex,mDistance));
	}
	public Vector<NodeRelation> mRelationList= new Vector<NodeRelation>();
}

