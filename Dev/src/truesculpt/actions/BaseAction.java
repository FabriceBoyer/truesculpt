package truesculpt.actions;

import truesculpt.main.Managers;

public abstract class BaseAction
{
	String mStrDescription;
	Managers mManagers = null;

	public BaseAction()
	{
	}

	abstract public boolean DoAction();

	abstract public boolean UndoAction();

	abstract public String GetActionName();

	abstract public int GetImageResourceID();

	public String getDescription()
	{
		return mStrDescription;
	}

	public void setDescription(String strDescription)
	{
		this.mStrDescription = strDescription;
	}

	public void setManagers(Managers managers)
	{
		mManagers = managers;
	}

	public Managers getManagers()
	{
		return mManagers;
	}
}
