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

public class TrueSculpt extends Activity  {

	private static final String TAG = "TrueSculptMain";



	

	private GLSurfaceView mGLSurfaceView;
	private SphereRenderer mRenderer = null;

	




	

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		default:
			return super.onContextItemSelected(item);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);		

		mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glview);
		mRenderer = new SphereRenderer(false);
		mGLSurfaceView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
		mGLSurfaceView.setRenderer(mRenderer);

			}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.show_debug_panel:	
		{
			Intent myIntent = new Intent("truesculpt.debug.DebugPanel");
			startActivity(myIntent);
			return true;
		}
		case R.id.show_check_version_panel:
		{
			Intent myIntent = new Intent("truesculpt.ui.UpdatePanel");
			startActivity(myIntent);
			return true;
		}
		case R.id.show_tutorial_wizard_panel:
		{
			Intent myIntent = new Intent("truesculpt.ui.TutorialWizard");
			startActivity(myIntent);
			return true;
		}
		case R.id.quit:
		{
			this.finish();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();

		
		mGLSurfaceView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();	
		
		mGLSurfaceView.onResume();
	}


	@Override
	protected void onStop() {
		super.onStop();
		
		mGLSurfaceView.onPause();

	}

}