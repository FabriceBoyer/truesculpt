package truesculpt.tools;

import android.os.SystemClock;
import truesculpt.main.Managers;

public class SelectionTool extends ToolsBase
{

	public SelectionTool(Managers managers)
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
		long tSculptStart = SystemClock.uptimeMillis();
		super.Pick(xScreen, yScreen);
		int nIndex = getManagers().getMeshManager().Pick(xScreen, yScreen);

		SelectAction(nIndex);

		long tSculptStop = SystemClock.uptimeMillis();
		mLastSculptDurationMs = tSculptStop - tSculptStart;
	}

	@Override
	public void Stop()
	{
		super.Stop();

	}

	private void SelectAction(int triangleIndex)
	{

	}

}
