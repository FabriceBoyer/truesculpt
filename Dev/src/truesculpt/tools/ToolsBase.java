package truesculpt.tools;

import truesculpt.main.Managers;

public class ToolsBase implements ITools
{
	private Managers mManagers = null;

	public ToolsBase(Managers managers)
	{
		mManagers = managers;
	}

	public void Start()
	{

	}

	public void Pick(float xScreen, float yScreen)
	{

	}

	public void Stop()
	{

	}

	public Managers getManagers()
	{
		return mManagers;
	}

}
