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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
		
		keys=m_sensorsValues.keySet();	
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
                SensorManager.SENSOR_DELAY_GAME);
		}        		

		mGLSurfaceView.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();	
		 mSensorManager.unregisterListener(TrueSculpt.this);
		 
		 mGLSurfaceView.onPause();
		
	}

	@Override
	protected void onPause() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();
		
		mSensorManager.unregisterListener(TrueSculpt.this);
		
		mGLSurfaceView.onPause();
	}


	private GLSurfaceView mGLSurfaceView;
	private SensorManager mSensorManager;
	//Temp for test debug

	private DecimalFormat twoPlaces = new DecimalFormat("000.00");
	private TextView text;
	private String msg;
	private String Tempkey;
	private String fullmsg;
	private Set<String> keys;
	private Iterator<String> iter;
	private String Tempname;

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	private HashMap<String,Float> m_sensorsValues=new HashMap<String,Float>();
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		synchronized (this) {  
			
			int n=event.values.length;
			for (int i=0;i<n;i++)
			{
				Tempname=event.sensor.getName()+"_"+i;
				m_sensorsValues.put(Tempname,event.values[i]);				
			}
			UpdateSensorText();           
		}
	}
	
	private void UpdateSensorText()
	{		
		fullmsg="";		
		iter= keys.iterator();
		while (iter.hasNext())
		{		
			Tempkey=iter.next();
			msg= Tempkey + " : " + twoPlaces.format(m_sensorsValues.get(Tempkey)) ; 				
			fullmsg+=msg+"\n";
		}
		text.setText(fullmsg);  	
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
	        //newGame();
	        return true;
	    case R.id.quit:
	        //quit();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
}