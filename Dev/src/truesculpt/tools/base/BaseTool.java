package truesculpt.tools.base;

import truesculpt.main.Managers;

abstract public class BaseTool implements ITools
{
	protected long mLastSculptDurationMs = -1;
	protected long tSculptStart = -1;
	private Managers mManagers = null;

	public BaseTool(Managers managers)
	{
		mManagers = managers;
	}

	protected Managers getManagers()
	{
		return mManagers;
	}

	public long getLastSculptDurationMs()
	{
		return mLastSculptDurationMs;
	}
}
