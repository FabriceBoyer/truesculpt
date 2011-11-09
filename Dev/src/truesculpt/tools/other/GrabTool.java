package truesculpt.tools.other;

import truesculpt.main.Managers;
import truesculpt.tools.base.BaseTool;
import android.graphics.drawable.Drawable;

public class GrabTool extends BaseTool
{
	public GrabTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public Drawable GetIcon()
	{
		return null;
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
