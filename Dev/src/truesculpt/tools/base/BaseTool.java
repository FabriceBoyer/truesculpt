package truesculpt.tools.base;

import truesculpt.main.Managers;
import truesculpt.mesh.Mesh;
import android.graphics.drawable.Drawable;

abstract public class BaseTool implements ITools
{
	protected long mLastSculptDurationMs = -1;
	protected long tSculptStart = -1;
	private Managers mManagers = null;
	protected Mesh mMesh = null;

	public BaseTool(Managers managers)
	{
		mManagers = managers;
	}

	@Override
	public void Start(float xScreen, float yScreen)
	{
		mMesh = getManagers().getMeshManager().getMesh();
	}

	@Override
	public void Pick(float xScreen, float yScreen)
	{
	}

	@Override
	public void Stop(float xScreen, float yScreen)
	{
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
	public Drawable GetIcon()
	{
		return null;
	}
}
