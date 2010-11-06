package truesculpt.managers;

import truesculpt.main.TrueSculpt;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;


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

import truesculpt.renderer.SphereRenderer;
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

public class SensorsManager extends BaseManager implements
SensorEventListener {

	public SensorsManager(Activity mBaseActivity) {
		super(mBaseActivity);
		// TODO Auto-generated constructor stub
	}


	private SensorManager mSensorManager;
	// sensor data
	float lastX = 0.0f;
	float lastY = 0.0f;
	float lastZ = 0.0f;
	private HashMap<String, Float> m_sensorsValues = new HashMap<String, Float>();
	private String msg;
	float origX = 0.0f;
	float origY = 0.0f;
	float origZ = 0.0f;
	private String Tempkey;

	private String Tempname;
	
	boolean bOrigSet = false;

	private String fullmsg;

	private Iterator<String> iter;

	private Set<String> keys;

	// Temp for test debug
	private DecimalFormat twoPlaces = new DecimalFormat("000.00");

	
	private void showSensor() {
		WifiManager wm = (WifiManager)  getBaseActivity().getSystemService(Activity.WIFI_SERVICE);
		String macAddr = wm.getConnectionInfo().getMacAddress();

		final TelephonyManager tm = (TelephonyManager)  getBaseActivity().getBaseContext()
				.getSystemService(Activity.TELEPHONY_SERVICE);

		String tmDevice = tm.getDeviceId();
		String tmSerial = tm.getSimSerialNumber();
		String androidId = android.provider.Settings.Secure.getString(
				 getBaseActivity().getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);

		String msg = macAddr + "\n" + tmDevice + "\n" + tmSerial + "\n"
				+ androidId + "\n";
		//Toast.makeText(TrueSculpt.this, msg, Toast.LENGTH_LONG).show();
	}

	private void updateSensorText() {
		fullmsg = "";
		iter = keys.iterator();
		while (iter.hasNext()) {
			Tempkey = iter.next();
			msg = Tempkey + " : "
					+ twoPlaces.format(m_sensorsValues.get(Tempkey));
			fullmsg += msg + "\n";
		}
		//text.setText(fullmsg);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {

			int n = event.values.length;
			for (int i = 0; i < n; i++) {
				Tempname = event.sensor.getName() + "_" + i;
				m_sensorsValues.put(Tempname, event.values[i]);
			}
			updateSensorText();

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

			//mRenderer.setOrientation(dObjX, dObjY, dObjZ, 0, 0, 0);

			lastX = currX;
			lastY = currY;
			lastZ = currZ;
		}
	}
	
	public void Start()	
	{
		mSensorManager = (SensorManager)  getBaseActivity().getSystemService(Activity.SENSOR_SERVICE);
		keys = m_sensorsValues.keySet();
		List<Sensor> sensorList = mSensorManager
		.getSensorList(Sensor.TYPE_ORIENTATION);
for (int i = 0; i < sensorList.size(); i++)
{
	mSensorManager.registerListener(SensorsManager.this, sensorList.get(i),	SensorManager.SENSOR_DELAY_GAME);
}

	}
	
	
	public void Stop()
	{
		mSensorManager.unregisterListener(SensorsManager.this);

	}

	
}
