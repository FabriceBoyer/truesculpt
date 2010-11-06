package truesculpt.managers;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

public class SensorsManager extends BaseManager implements SensorEventListener {

	boolean bOrigSet = false;

	private String fullmsg;
	private Iterator<String> iter;
	private Set<String> keys;
	// sensor data
	float lastX = 0.0f;
	float lastY = 0.0f;
	float lastZ = 0.0f;
	private HashMap<String, Float> m_sensorsValues = new HashMap<String, Float>();
	private SensorManager mSensorManager;
	private String msg;
	float origX = 0.0f;

	float origY = 0.0f;

	float origZ = 0.0f;

	private String Tempkey;

	private String Tempname;

	// Temp for test debug
	private DecimalFormat twoPlaces = new DecimalFormat("000.00");

	public SensorsManager(Activity mBaseActivity) {
		super(mBaseActivity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		

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

			// mRenderer.setOrientation(dObjX, dObjY, dObjZ, 0, 0, 0);

			lastX = currX;
			lastY = currY;
			lastZ = currZ;
		}
	}

	private void showSensor() {
		WifiManager wm = (WifiManager) getBaseActivity().getSystemService(
				Context.WIFI_SERVICE);
		String macAddr = wm.getConnectionInfo().getMacAddress();

		final TelephonyManager tm = (TelephonyManager) getBaseActivity()
				.getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

		String tmDevice = tm.getDeviceId();
		String tmSerial = tm.getSimSerialNumber();
		String androidId = android.provider.Settings.Secure.getString(
				getBaseActivity().getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);

		String msg = macAddr + "\n" + tmDevice + "\n" + tmSerial + "\n"
				+ androidId + "\n";
		// Toast.makeText(TrueSculpt.this, msg, Toast.LENGTH_LONG).show();
	}

	public void Start() {
		mSensorManager = (SensorManager) getBaseActivity().getSystemService(
				Context.SENSOR_SERVICE);
		keys = m_sensorsValues.keySet();
		List<Sensor> sensorList = mSensorManager
				.getSensorList(Sensor.TYPE_ORIENTATION);
		for (int i = 0; i < sensorList.size(); i++) {
			mSensorManager.registerListener(SensorsManager.this,
					sensorList.get(i), SensorManager.SENSOR_DELAY_GAME);
		}

	}

	public void Stop() {
		mSensorManager.unregisterListener(SensorsManager.this);

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
		// text.setText(fullmsg);
	}

}
