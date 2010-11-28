package truesculpt.managers;

import java.util.List;
import java.util.Vector;

import truesculpt.utils.MatrixUtils;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

public class SensorsManager extends BaseManager implements SensorEventListener
{

	public interface OnSensorChangeListener
	{
		void onSensorChanged();
	}

	boolean bOrigSet = false;
	float[] diffAngles = new float[3];
	float[] lastAngles = new float[3];

	private Vector<OnSensorChangeListener> mListeners = new Vector<OnSensorChangeListener>();

	private SensorManager mSensorManager = null;

	float[] origAngles = new float[3];

	public SensorsManager(Context baseContext)
	{
		super(baseContext);

	}

	public void calibrate()
	{
		bOrigSet = false;
	}

	private void NotifyListeners()
	{
		getManagers().getPointOfViewManager().onSensorChanged();

		for (OnSensorChangeListener listener : mListeners)
		{
			listener.onSensorChanged();
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		if (accuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW)
		{
			// getManagers().getOptionsManager().setUseSensorsToChangePOV(false);
			// restart();

			// TODO queue event to come back in UI thread
			String msg = "Disabling sensors due to low accuracy";
			Toast.makeText(getbaseContext(), msg, Toast.LENGTH_LONG);
			Log.i("SensorManager", msg);
		}
	}

	@Override
	public void onCreate()
	{
		if (getManagers().getOptionsManager().getUseSensorsToChangePOV())
		{
			restart();
		}
	}

	@Override
	public void onDestroy()
	{
		stop();
	}

	@Override
	public void onSensorChanged(SensorEvent event)
	{

		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
		{
			if (!bOrigSet)
			{
				MatrixUtils.copy(event.values, origAngles);
				bOrigSet = true;
			}

			float fAngleThresold = 90.0f;
			MatrixUtils.minus(event.values, lastAngles, diffAngles);

			// eliminate bas points
			if (diffAngles[0] < fAngleThresold && diffAngles[1] < fAngleThresold)
			{
				float rotation = -(event.values[0] - origAngles[0]);
				float elevation = +(event.values[1] - origAngles[1]);

				MatrixUtils.copy(event.values, lastAngles);

				getManagers().getPointOfViewManager().setRotationAngle(rotation);
				getManagers().getPointOfViewManager().setElevationAngle(elevation);

				NotifyListeners();
			}
		}
	}

	public void registerOnSensorChangeListener(OnSensorChangeListener listener)
	{
		mListeners.add(listener);
	}

	public void restart()
	{
		stop();
		start();
	}

	public void start()
	{
		mSensorManager = (SensorManager) getbaseContext().getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
		for (int i = 0; i < sensorList.size(); i++)
		{
			mSensorManager.registerListener(SensorsManager.this, sensorList.get(i), SensorManager.SENSOR_DELAY_GAME);
		}
	}

	public void stop()
	{
		if (mSensorManager != null)
		{
			mSensorManager.unregisterListener(SensorsManager.this);
		}
	}

	public void unRegisterOnSensorChangeListener(OnSensorChangeListener listener)
	{
		mListeners.remove(listener);
	}

}
