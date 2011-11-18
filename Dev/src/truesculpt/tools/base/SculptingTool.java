package truesculpt.tools.base;

import truesculpt.actions.SculptAction;
import truesculpt.main.Managers;

public abstract class SculptingTool extends SelectionTool
{
	public final static float MAX_DEFORMATION = 0.2f;
	protected float mMaxDeformation = -1;

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

		mMaxDeformation = getManagers().getToolsManager().getStrength() / 100.0f * MAX_DEFORMATION;// strength is -100 to 100

		mAction = new SculptAction();
	}

	@Override
	public boolean RequiresToolOverlay()
	{
		return true;
	}

	@Override
	public boolean RequiresStrength()
	{
		return true;
	}

	@Override
	public boolean RequiresRadius()
	{
		return true;
	}

	@Override
	public boolean RequiresColor()
	{
		return false;
	}

	@Override
	public boolean RequiresSymmetry()
	{
		return true;
	}
}
