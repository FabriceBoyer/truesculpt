package truesculpt.tools.other;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.tools.base.BaseTool;

public class GrabTool extends BaseTool
{
	public GrabTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public int GetIcon()
	{
		return R.drawable.grab;
	}

	@Override
	public String GetName()
	{
		return "Grab";
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
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		super.Stop(xScreen, yScreen);
	}

}
