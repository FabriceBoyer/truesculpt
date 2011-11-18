package truesculpt.tools.base;

import truesculpt.actions.ColorizeAction;
import truesculpt.main.Managers;

public abstract class PaintingTool extends SelectionTool
{
	public PaintingTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);

		mAction = new ColorizeAction();
	}

	@Override
	public boolean RequiresStrength()
	{
		return false;
	}

	@Override
	public boolean RequiresRadius()
	{
		return true;
	}

	@Override
	public boolean RequiresColor()
	{
		return true;
	}

	@Override
	public boolean RequiresSymmetry()
	{
		return true;
	}

}
