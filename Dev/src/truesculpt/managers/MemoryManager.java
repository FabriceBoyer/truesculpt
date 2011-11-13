package truesculpt.managers;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class MemoryManager extends BaseManager
{

	private static final String TAG = "TrueSculptMemory";

	public MemoryManager(Context baseContext)
	{
		super(baseContext);
	}

	public String getGeneralMemoryInfo()
	{
		String msg = "";

		ActivityManager activityManager = (ActivityManager) getbaseContext().getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);

		msg = " memoryInfo.availMem " + memoryInfo.availMem / 1e6 + " Mo\n";
		msg += " memoryInfo.lowMemory " + memoryInfo.lowMemory + "\n";
		msg += " memoryInfo.threshold " + memoryInfo.threshold / 1e6 + " Mo\n";

		return msg;
		// getManagers().getUtilsManager().ShowToastMessage(msg);
	}

	public String getMemoryInfoForCurrentProcesse()
	{
		String msg = "";

		ActivityManager activityManager = (ActivityManager) getbaseContext().getSystemService(Context.ACTIVITY_SERVICE);

		msg += getGeneralMemoryInfo();

		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

		Map<Integer, String> pidMap = new TreeMap<Integer, String>();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses)
		{
			if (runningAppProcessInfo.processName.contains("truesculpt"))
			{
				pidMap.put(runningAppProcessInfo.pid, runningAppProcessInfo.processName);
			}
		}

		Collection<Integer> keys = pidMap.keySet();

		for (int key : keys)
		{
			int pids[] = new int[1];
			pids[0] = key;
			android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(pids);
			for (android.os.Debug.MemoryInfo pidMemoryInfo : memoryInfoArray)
			{
				msg += String.format("** MEMINFO in pid %d [%s] **\n", pids[0], pidMap.get(pids[0]));
				msg += " pidMemoryInfo.getTotalPrivateDirty(): " + pidMemoryInfo.getTotalPrivateDirty() + " ko\n";
				msg += " pidMemoryInfo.getTotalPss(): " + pidMemoryInfo.getTotalPss() + " ko\n";
				msg += " pidMemoryInfo.getTotalSharedDirty(): " + pidMemoryInfo.getTotalSharedDirty() + " ko\n";
			}
		}

		return msg;
	}

	@Override
	public void onCreate()
	{

	}

	@Override
	public void onDestroy()
	{

	}

}
