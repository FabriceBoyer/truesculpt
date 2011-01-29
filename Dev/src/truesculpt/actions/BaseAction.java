package truesculpt.actions;

import truesculpt.main.R;

public abstract class BaseAction
{

	public BaseAction(String strDescription)
	{
		this.mStrDescription = strDescription;
	}
	
	String mStrDescription;
	
	public String getDescription()
	{
		return mStrDescription;
	}
	public void setDescription(String strDescription)
	{
		this.mStrDescription = strDescription;
	}
	
	public abstract boolean DoAction();

	public abstract boolean UndoAction();

	public String getActionName() { return "Undefined"; }
	public int getImageResourceID() { return R.drawable.logo; }
	
}
