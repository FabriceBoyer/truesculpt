package truesculpt.tools.base;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;

public abstract class SculptingTool extends SelectionTool
{
	protected final float[] VOffset = new float[3];
	protected final float[] VNormal = new float[3];

	public SculptingTool(Managers managers)
	{
		super(managers);
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		super.Start(xScreen, yScreen);

		mAction = new SculptAction();
	}
}
