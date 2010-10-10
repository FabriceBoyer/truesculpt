package com.TrueSculpt;

import com.TrueSculpt.ColorPickerDialog;
import com.TrueSculpt.ColorPickerDialog.OnColorChangedListener;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@SuppressWarnings("deprecation")
public class TrueSculpt extends Activity implements OnColorChangedListener, SensorEventListener {

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
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	  super.onCreateContextMenu(menu, v, menuInfo);
	  MenuInflater inflater = getMenuInflater();
	  inflater.inflate(R.menu.context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	  switch (item.getItemId()) {
	  case R.id.edit:
	    //editNote(info.id);
	    return true;
	  default:
	    return super.onContextItemSelected(item);
	  }
	}
	
	private void IsUpdateNeeded()
	{
		String strCurrVersion=GetCurrentVersion();
		String[] tempVer=strCurrVersion.split(".");
		int majCurr=Integer.parseInt(tempVer[0]);
		int minCurr=Integer.parseInt(tempVer[1]);
		
		String strLatestVersion=GetLatestVersion();	
		tempVer=strLatestVersion.split(".");
		int majLat=Integer.parseInt(tempVer[0]);
		int minLat=Integer.parseInt(tempVer[1]);
		
		boolean bUpdateNeeded=false;
		boolean bIsBeta=false;
		if (majLat>majCurr)
		{
			bUpdateNeeded=true;
		}
		else if (majLat==majCurr)
		{
			if (minLat>minCurr)
			{
				bUpdateNeeded=true;
			}
			else if (minLat<minCurr)
			{
				bIsBeta=true;			
			}
		}	
		else if (majLat<majCurr) 
		{
			bIsBeta=true;
		}
		
		
		String msg="Current version is "+ strCurrVersion +". Latest version is "+strLatestVersion+".";
		if (bIsBeta)
		{
			msg+="This version is a beta";
		}
		if (bUpdateNeeded)
		{
			msg+="An update is needed.";
		}
		else
		{
			msg+="No update is needed.";
		}

		Toast.makeText(TrueSculpt.this, msg, Toast.LENGTH_LONG).show();
	}

	private String GetLatestVersion()
	{
		String strLatestVersion="0.0";
		
		try {
			URL url=new URL("http://code.google.com/p/truesculpt/wiki/Version");
			InputStream stream=url.openStream();
			InputStreamReader reader=new InputStreamReader(stream);
			BufferedReader buffReader= new BufferedReader(reader);
			String strTemp=buffReader.readLine();
			String strFileVersion="";
			while (strTemp!=null)
			{					
				strFileVersion+=strTemp;
				strTemp=buffReader.readLine();
			}
			buffReader.close();
						
			// <p>LATEST_STABLE_VERSION=0_1 </p>
			// LATEST_BETA_VERSION=0_1 
			// UPDATE_BASE_URL=http://truesculpt.googlecode.com/files/TrueSculpt_
						
			Pattern p = Pattern.compile("<p>LATEST_STABLE_VERSION=[0-9]+_[0-9]+ </p>",Pattern.CASE_INSENSITIVE);
			  
			Matcher m = p.matcher(strFileVersion);
			if (m.find())
			{
				String elem=m.group();
				elem=elem.replace("<p>LATEST_STABLE_VERSION=", "");
				elem=elem.replace("</p>", "");
				elem=elem.replace("_", ".");
				elem=elem.trim();
				
				strLatestVersion=elem;				
			}			
		} 
		catch (MalformedURLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strLatestVersion;		
	}
	
	
	private String GetCurrentVersion() 
	{		
		String strCurrVersion="0.0";
		
		PackageManager pm = getPackageManager();		
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			strCurrVersion=info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return strCurrVersion;
	}	 
}