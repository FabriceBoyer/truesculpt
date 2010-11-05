package truesculpt.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import truesculpt.renderer.CubeRenderer;
import truesculpt.ui.ColorPickerDialog;
import truesculpt.ui.ColorPickerDialog.OnColorChangedListener;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TrueSculpt extends Activity implements OnColorChangedListener,
		SensorEventListener {

	private static final String TAG = "TrueSculptMain";

	boolean bOrigSet = false;

	private String fullmsg;

	private Iterator<String> iter;

	private Set<String> keys;

	// sensor data
	float lastX = 0.0f;

	float lastY = 0.0f;

	float lastZ = 0.0f;
	private HashMap<String, Float> m_sensorsValues = new HashMap<String, Float>();
	private int mColor = 0;

	private GLSurfaceView mGLSurfaceView;
	private CubeRenderer mRenderer = null;
	private SensorManager mSensorManager;
	private String msg;
	float origX = 0.0f;
	float origY = 0.0f;
	float origZ = 0.0f;
	private String Tempkey;

	private String Tempname;
	private TextView text;
	// Temp for test debug
	private DecimalFormat twoPlaces = new DecimalFormat("000.00");

	@Override
	public void colorChanged(int color) {
		mColor = color;
		mRenderer.SetColor(mColor);
		String msg = "color is " + Integer.toString(mColor);
		Toast.makeText(TrueSculpt.this, msg, Toast.LENGTH_SHORT).show();
	}

	public int GetColor() {
		return mColor;
	}

	private String GetCurrentVersion() {
		String strCurrVersion = "0.0";

		PackageManager pm = getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			strCurrVersion = info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return strCurrVersion;
	}

	private String GetLatestVersion() {
		String strLatestVersion = "-1.0";

		try {
			// TODO check alternative url list (register url)
			URL url = new URL(
					"http://code.google.com/p/truesculpt/wiki/Version");
			InputStream stream = url.openStream();
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader buffReader = new BufferedReader(reader);
			String strTemp = buffReader.readLine();
			String strFileVersion = "";
			while (strTemp != null) {
				strFileVersion += strTemp;
				strTemp = buffReader.readLine();
			}
			buffReader.close();

			// <p>LATEST_STABLE_VERSION=0_1 </p>
			// LATEST_BETA_VERSION=0_1
			// UPDATE_BASE_URL=http://truesculpt.googlecode.com/files/TrueSculpt_

			Pattern p = Pattern.compile(
					"<p>LATEST_STABLE_VERSION=[0-9]+_[0-9]+ </p>",
					Pattern.CASE_INSENSITIVE);

			Matcher m = p.matcher(strFileVersion);
			if (m.find()) {
				String elem = m.group();
				elem = elem.replace("<p>LATEST_STABLE_VERSION=", "");
				elem = elem.replace("</p>", "");
				elem = elem.replace("_", ".");
				elem = elem.trim();

				strLatestVersion = elem;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return strLatestVersion;
	}

	private void GetMemoryInfo() {
		String msg = "";

		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);

		msg = " memoryInfo.availMem " + memoryInfo.availMem + "\n";
		msg += " memoryInfo.lowMemory " + memoryInfo.lowMemory + "\n";
		msg += " memoryInfo.threshold " + memoryInfo.threshold + "\n";

		Toast.makeText(TrueSculpt.this, msg, Toast.LENGTH_LONG).show();
	}

	private void GetMemoryInfoForAllProcesses() {
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
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

	private void IsUpdateNeeded() {
		String strCurrVersion = GetCurrentVersion();
		String[] tempVer = strCurrVersion.split("\\.");
		int majCurr = -1;
		int minCurr = -1;
		if (tempVer.length == 2) {
			majCurr = Integer.parseInt(tempVer[0]);
			minCurr = Integer.parseInt(tempVer[1]);
		}

		String strLatestVersion = GetLatestVersion();
		tempVer = strLatestVersion.split("\\.");
		int majLat = -1;
		int minLat = -1;
		if (tempVer.length == 2) {
			majLat = Integer.parseInt(tempVer[0]);
			minLat = Integer.parseInt(tempVer[1]);
		}

		if (majCurr >= 0 && minCurr >= 0 && majLat >= 0 && minLat >= 0) {
			boolean bUpdateNeeded = false;
			boolean bIsBeta = false;
			if (majLat > majCurr) {
				bUpdateNeeded = true;
			} else if (majLat == majCurr) {
				if (minLat > minCurr) {
					bUpdateNeeded = true;
				} else if (minLat < minCurr) {
					bIsBeta = true;
				}
			} else if (majLat < majCurr) {
				bIsBeta = true;
			}

			String msg = getString(R.string.current_version_is_)
					+ strCurrVersion + getString(R.string._latest_version_is_)
					+ strLatestVersion + ". ";
			if (bIsBeta) {
				msg += R.string.this_version_is_a_beta_;
			} else {
				msg += R.string.this_version_is_not_a_beta_;
			}

			if (bUpdateNeeded) {
				msg += R.string.an_update_is_needed_;
			} else {
				msg += R.string.no_update_is_needed_;
			}

			Toast.makeText(TrueSculpt.this, msg, Toast.LENGTH_LONG).show();

			// Launch associated web page
			String lastestURL = strLatestVersion.replace(".", "_");
			lastestURL = "https://code.google.com/p/truesculpt/downloads/detail?name=TrueSculpt_"
					+ lastestURL + ".apk";
			Intent myIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(lastestURL));
			startActivity(myIntent);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.edit:
			GetMemoryInfo();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		text = (TextView) findViewById(R.id.text);
		registerForContextMenu(text);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		final Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new ColorPickerDialog(TrueSculpt.this, TrueSculpt.this, 0)
						.show();
			}
		});

		mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glview);
		mRenderer = new CubeRenderer(false);
		mGLSurfaceView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
		mGLSurfaceView.setRenderer(mRenderer);

		keys = m_sensorsValues.keySet();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.show_sensors:
			ShowSensor();
			Intent myIntent = new Intent("com.TrueSculpt.TutorialWizard");
			startActivity(myIntent);
			return true;
		case R.id.check_version:
			IsUpdateNeeded();
			return true;
		case R.id.quit:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();

		mSensorManager.unregisterListener(TrueSculpt.this);

		mGLSurfaceView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		List<Sensor> sensorList = mSensorManager
				.getSensorList(Sensor.TYPE_ORIENTATION);
		for (int i = 0; i < sensorList.size(); i++) {
			mSensorManager.registerListener(TrueSculpt.this, sensorList.get(i),
					SensorManager.SENSOR_DELAY_GAME);
		}

		mGLSurfaceView.onResume();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {

			int n = event.values.length;
			for (int i = 0; i < n; i++) {
				Tempname = event.sensor.getName() + "_" + i;
				m_sensorsValues.put(Tempname, event.values[i]);
			}
			UpdateSensorText();

			float currX = event.values[1];
			float currY = event.values[2];
			float currZ = event.values[0];

			if (!bOrigSet) {
				origX = currX;
				origY = currY;
				origZ = currZ;

				bOrigSet = true;
			}

			float dObjX = currX - origX;
			float dObjY = currY - origY;
			float dObjZ = currZ - origZ;

			mRenderer.SetOrientation(dObjX, dObjY, dObjZ, 0, 0, 0);

			lastX = currX;
			lastY = currY;
			lastZ = currZ;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		mSensorManager.unregisterListener(TrueSculpt.this);

		mGLSurfaceView.onPause();

	}

	private void ShowSensor() {
		WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
		String macAddr = wm.getConnectionInfo().getMacAddress();

		final TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(TELEPHONY_SERVICE);

		String tmDevice = tm.getDeviceId();
		String tmSerial = tm.getSimSerialNumber();
		String androidId = android.provider.Settings.Secure.getString(
				getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);

		String msg = macAddr + "\n" + tmDevice + "\n" + tmSerial + "\n"
				+ androidId + "\n";
		Toast.makeText(TrueSculpt.this, msg, Toast.LENGTH_LONG).show();
	}

	private void UpdateSensorText() {
		fullmsg = "";
		iter = keys.iterator();
		while (iter.hasNext()) {
			Tempkey = iter.next();
			msg = Tempkey + " : "
					+ twoPlaces.format(m_sensorsValues.get(Tempkey));
			fullmsg += msg + "\n";
		}
		text.setText(fullmsg);
	}
}