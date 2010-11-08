package truesculpt.managers;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.util.Log;

public class MemoryManager extends BaseManager {

	private static final String TAG = "TrueSculptMemory";

	public MemoryManager(Context baseContext) {
		super(baseContext);
		// TODO Auto-generated constructor stub
	}

	private void getMemoryInfo() {
		String msg = "";

		ActivityManager activityManager = (ActivityManager) getbaseContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);

		msg = " memoryInfo.availMem " + memoryInfo.availMem + "\n";
		msg += " memoryInfo.lowMemory " + memoryInfo.lowMemory + "\n";
		msg += " memoryInfo.threshold " + memoryInfo.threshold + "\n";

		// Toast.makeText(TrueSculpt.this, msg, Toast.LENGTH_LONG).show();
	}

	private void getMemoryInfoForAllProcesses() {
		ActivityManager activityManager = (ActivityManager) getbaseContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);

		Log.i(TAG, " memoryInfo.availMem " + memoryInfo.availMem + "\n");
		Log.i(TAG, " memoryInfo.lowMemory " + memoryInfo.lowMemory + "\n");
		Log.i(TAG, " memoryInfo.threshold " + memoryInfo.threshold + "\n");

		List<RunningAppProcessInfo> runningAppProcesses = activityManager
				.getRunningAppProcesses();

		Map<Integer, String> pidMap = new TreeMap<Integer, String>();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			pidMap.put(runningAppProcessInfo.pid,
					runningAppProcessInfo.processName);
		}

		Collection<Integer> keys = pidMap.keySet();

		for (int key : keys) {
			int pids[] = new int[1];
			pids[0] = key;
			android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager
					.getProcessMemoryInfo(pids);
			for (android.os.Debug.MemoryInfo pidMemoryInfo : memoryInfoArray) {
				Log.i(TAG, String.format("** MEMINFO in pid %d [%s] **\n",
						pids[0], pidMap.get(pids[0])));
				Log.i(TAG, " pidMemoryInfo.getTotalPrivateDirty(): "
						+ pidMemoryInfo.getTotalPrivateDirty() + "\n");
				Log.i(TAG,
						" pidMemoryInfo.getTotalPss(): "
								+ pidMemoryInfo.getTotalPss() + "\n");
				Log.i(TAG, " pidMemoryInfo.getTotalSharedDirty(): "
						+ pidMemoryInfo.getTotalSharedDirty() + "\n");
			}
		}
	}

}
