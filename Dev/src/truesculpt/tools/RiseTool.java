package truesculpt.tools;

import truesculpt.main.Managers;

public class RiseTool extends SculptingTool
{

	public RiseTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		super.Pick(xScreen, yScreen);
		int nIndex = getManagers().getMeshManager().Pick(xScreen, yScreen);
		getManagers().getMeshManager().getMesh().RiseSculptAction(nIndex);
	}

}
