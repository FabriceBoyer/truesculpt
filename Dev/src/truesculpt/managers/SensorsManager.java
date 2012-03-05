package truesculpt.managers;

import java.util.List;

import truesculpt.utils.MatrixUtils;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorsManager extends BaseManager implements SensorEventListener
{
	boolean bOrigSet = false;
	float[] diffAngles = new float[3];
	float[] lastAngles = new float[3];

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

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		if (accuracy == SensorManager.SENSOR_STATUS_ACCURACY_LOW)
		{
			stop();

			// String msg =
			// getbaseContext().getString(R.string.sensors_have_a_low_accuracy);
			// getManagers().getUtilsManager().ShowToastMessage(msg);
			// Log.i("SensorManager", msg);
		}
		else
		{
			start();
		}
	}

	@Override
	public void onCreate()
	{
		restart();
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
			float fAngleGain = 3;
			MatrixUtils.minus(event.values, lastAngles, diffAngles);

			// eliminate bad points
			if (diffAngles[0] < fAngleThresold && diffAngles[1] < fAngleThresold)
			{
				float rotation = -(event.values[0] - origAngles[0]) * fAngleGain;
				float elevation = +(event.values[1] - origAngles[1]) * fAngleGain;

				MatrixUtils.copy(event.values, lastAngles);

				getManagers().getPointOfViewManager().SetAllAngles(rotation, elevation, 0);// notification done here
			}
		}
	}

	public void restart()
	{
		stop();
		start();
		getManagers().getTouchManager().UpdateUseSensors();
	}

	private void start()
	{
		if (getManagers().getOptionsManager().getUseSensorsToChangePOV() && mSensorManager == null)
		{
			mSensorManager = (SensorManager) getbaseContext().getSystemService(Context.SENSOR_SERVICE);
			List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
			for (int i = 0; i < sensorList.size(); i++)
			{
				mSensorManager.registerListener(SensorsManager.this, sensorList.get(i), SensorManager.SENSOR_DELAY_GAME);
			}
		}
	}

	private void stop()
	{
		if (mSensorManager != null)
		{
			mSensorManager.unregisterListener(SensorsManager.this);
			mSensorManager = null;
		}
	}

}
