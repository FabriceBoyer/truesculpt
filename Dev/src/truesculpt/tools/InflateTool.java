package truesculpt.tools;

import truesculpt.main.Managers;
import truesculpt.tools.base.SculptingTool;

public class InflateTool extends SculptingTool
{

	public InflateTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		super.Pick(xScreen, yScreen);

		EndPick();
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		super.Stop(xScreen, yScreen);
	}

}
