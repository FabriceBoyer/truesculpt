package truesculpt.actions;

import truesculpt.main.R;

public abstract class BaseAction
{

	String mStrDescription;

	public BaseAction(String strDescription)
	{
		this.mStrDescription = strDescription;
	}

	public abstract boolean DoAction();

	public String getActionName()
	{
		return "Undefined";
	}

	public String getDescription()
	{
		return mStrDescription;
	}

	public int getImageResourceID()
	{
		return R.drawable.logo;
	}

	public void setDescription(String strDescription)
	{
		this.mStrDescription = strDescription;
	}

	public abstract boolean UndoAction();

}
