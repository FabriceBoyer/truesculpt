package truesculpt.tools.base;

import truesculpt.actions.ColorizeAction;
import truesculpt.main.Managers;

public abstract class PaintingTool extends ToolsBase
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

}
