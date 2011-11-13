package truesculpt.tools.base;

import truesculpt.actions.BaseAction;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.managers.ToolsManager.ESymmetryMode;
import truesculpt.mesh.Mesh;
import android.os.SystemClock;

abstract public class BaseTool implements ITools
{
	protected long mLastSculptDurationMs = -1;
	protected long tSculptStart = -1;
	private Managers mManagers = null;
	protected Mesh mMesh = null;
	protected BaseAction mAction = null;
	protected final float MIN_RADIUS = 0.01f;// meters
	protected final float MAX_RADIUS = 1f;// meters
	protected float mSquareMaxDistance = -1;
	protected float mMaxDistance = -1;

	public BaseTool(Managers managers)
	{
		mManagers = managers;
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		mMesh = getManagers().getMeshManager().getMesh();

		mAction = null;

		mSquareMaxDistance = (float) Math.pow((MAX_RADIUS - MIN_RADIUS) * getManagers().getToolsManager().getRadius() / 100f + MIN_RADIUS, 2);
		mMaxDistance = (float) Math.sqrt(mSquareMaxDistance);
	}

	abstract protected void PickInternal(float xScreen, float yScreen, ESymmetryMode mode);

	@Override
	public void Pick(float xScreen, float yScreen)
	{
		tSculptStart = SystemClock.uptimeMillis();

		// symmetry handling
		switch (getManagers().getToolsManager().getSymmetryMode())
		{
		case NONE:
			// nop
			break;
		case X:
			PickInternal(xScreen, yScreen, ESymmetryMode.X);
			break;
		case Y:
			PickInternal(xScreen, yScreen, ESymmetryMode.Y);
			break;
		case Z:
			PickInternal(xScreen, yScreen, ESymmetryMode.Z);
			break;
		case XY:
		case YZ:
		case XZ:
			// not handled at present time
			break;
		}

		// Regular pick always done
		PickInternal(xScreen, yScreen, ESymmetryMode.NONE);

		mLastSculptDurationMs = SystemClock.uptimeMillis() - tSculptStart;
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
		if (mAction != null)
		{
			getManagers().getActionsManager().AddUndoAction(mAction);
			mAction.DoAction();
			mAction = null;
		}

		mMesh = null;
	}

	protected Managers getManagers()
	{
		return mManagers;
	}

	public long getLastSculptDurationMs()
	{
		return mLastSculptDurationMs;
	}

	@Override
	public String GetName()
	{
		return "BaseTool";
	}

	@Override
	public String GetDescription()
	{
		return GetName();
	}

	@Override
	public int GetIcon()
	{
		return R.drawable.flash;
	}

	@Override
	public boolean RequiresToolOverlay()
	{
		return false;
	}
}
