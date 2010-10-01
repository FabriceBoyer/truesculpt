package com.TrueSculpt;

import com.TrueSculpt.ColorPickerDialog;
import com.TrueSculpt.ColorPickerDialog.OnColorChangedListener;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;


@SuppressWarnings("deprecation")
public class TrueSculpt extends Activity implements OnColorChangedListener, SensorEventListener {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);        

		text = (TextView) findViewById(R.id.text);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		final Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new ColorPickerDialog(TrueSculpt.this, TrueSculpt.this, 0).show();
			}
		});

		mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glview);
		mGLSurfaceView.setRenderer(new CubeRenderer(false));
	}

	public void colorChanged(int color) {
		String msg="color is" + color;
		Toast.makeText(TrueSculpt.this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		List<Sensor> sensorList = mSensorManager.getSensorList( Sensor.TYPE_ALL );
		for (int i=0;i<sensorList.size();i++)
		{
        mSensorManager.registerListener((SensorEventListener) TrueSculpt.this,
        		sensorList.get(i),
                SensorManager.SENSOR_DELAY_FASTEST);
		}        		

		mGLSurfaceView.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();	
		 mSensorManager.unregisterListener(TrueSculpt.this);
		
	}

	@Override
	protected void onPause() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();
		mGLSurfaceView.onPause();
	}


	private GLSurfaceView mGLSurfaceView;
	private SensorManager mSensorManager;
	//Temp for test debug

	private DecimalFormat twoPlaces = new DecimalFormat("000.00");
	private TextView text;
	private String msg;
	private String fullmsg;



	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {  
			fullmsg="";
			int n=event.values.length;
			for (int i=0;i<n;i++)
			{
				msg= "sensor: " + event.sensor.getName() + " : " + twoPlaces.format(event.values[i]) ; 				
				fullmsg+=msg+"\n";
			}
			text.setText(fullmsg);           
		}
	}
}