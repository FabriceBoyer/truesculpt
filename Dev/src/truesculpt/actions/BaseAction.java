package truesculpt.actions;

import truesculpt.main.Managers;

public abstract class BaseAction
{
	String mStrDescription;
	Managers mManagers = null;
	protected static int mnActionCounter = 0;

	public BaseAction()
	{
	}

	abstract public boolean DoAction();

	abstract public boolean UndoAction();

	abstract public String GetActionName();

	abstract public int GetImageResourceID();

	abstract public int GetChangeCount();

	public String getDescription()
	{
		return mStrDescription;
	}

	public void setDescription(String strDescription)
	{
		mnActionCounter++;
		this.mStrDescription = strDescription + " " + mnActionCounter;
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
