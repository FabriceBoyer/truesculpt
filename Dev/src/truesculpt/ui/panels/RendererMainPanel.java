package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.managers.PointOfViewManager.OnPointOfViewChangeListener;
import truesculpt.utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class RendererMainPanel extends Activity implements OnPointOfViewChangeListener {

	private static final String TAG = "TrueSculptMain";

	private GLSurfaceView mGLSurfaceView = null;

	public void CheckUpdate()
	{
		if (getManagers().getmOptionsManager().getCheckUpdateAtStartup()==true)
		{
			Utils.StartMyActivity(this, truesculpt.ui.panels.UpdatePanel.class);
		}
	}
	

	public Managers getManagers() {	
		return ((TrueSculptApp)getApplicationContext()).getManagers();
	}
	
	public void NotifyStartupStat()
	{
		if (getManagers().getmOptionsManager().getGatherUsageData()==true)
		{
			getManagers().getmUsageStatisticsManager().incrementStartupCount();
		}
	}
	
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
				
		ShowSplashScreen();		
		CheckUpdate();
		ShowTutorial();
		NotifyStartupStat();
		
		setContentView(R.layout.main);		
		
		 mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glview);
		 mGLSurfaceView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
		 mGLSurfaceView.setRenderer(getManagers().getmRendererManager().getmRenderer());
		 mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		 mGLSurfaceView.requestRender();
		 
		 getManagers().getmPointOfViewManager().registerPointOfViewChangeListener(RendererMainPanel.this);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {		
		super.onDestroy();
		 getManagers().getmPointOfViewManager().unRegisterPointOfViewChangeListener(RendererMainPanel.this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    //Handle the back button
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
	        //Ask the user if they want to quit
	        new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle(R.string.quit)
	        .setMessage(R.string.really_quit)
	        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                //Stop the activity
	                finish();    
	            }
	        })
	        .setNegativeButton(R.string.no, null)
	        .show();

	        return true;
	    }
	    else {
	        return super.onKeyDown(keyCode, event);
	    }
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.show_tools_panel: {
			Utils.StartMyActivity(this, truesculpt.ui.panels.ToolsPanel.class);
			return true;
		}
		case R.id.show_point_of_view_panel: {
			Utils.StartMyActivity(this, truesculpt.ui.panels.PointOfViewPanel.class);
			return true;
		}
		case R.id.show_debug_panel: {
			Utils.StartMyActivity(this, truesculpt.ui.debug.DebugPanel.class);
			return true;
		}
		case R.id.show_check_version_panel: {
			Utils.StartMyActivity(this, truesculpt.ui.panels.UpdatePanel.class);
			return true;
		}
		case R.id.show_tutorial_wizard_panel: {
			Utils.StartMyActivity(this,
					truesculpt.ui.panels.TutorialWizardPanel.class);
			return true;
		}
		case R.id.show_options: {
			getManagers().getmOptionsManager().showOptionsPanel(this);
			return true;
		}
		case R.id.show_about_panel: {
			Utils.StartMyActivity(this,	truesculpt.ui.panels.AboutPanel.class);
			return true;
		}
		case R.id.quit: {
			this.finish();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

		//if (mGLSurfaceView != null)
		//	mGLSurfaceView.onPause();
	}

	@Override
	public void onPointOfViewChange() {		
		mGLSurfaceView.requestRender();		
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mGLSurfaceView != null)
			mGLSurfaceView.onResume();
				 
	}

	@Override
	protected void onStop() {
		super.onStop();
	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		getManagers().getmTouchManager().onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	
	public void ShowSplashScreen()
	{	
		if (getManagers().getmOptionsManager().getDisplaySplashScreenAtStartup()==true)
		{
			Utils.StartMyActivity(this, truesculpt.ui.panels.SplashPanel.class);
		}
	}



	public void ShowTutorial()
	{
		if (getManagers().getmOptionsManager().getViewTutorialAtStartup()==true)
		{
			Utils.StartMyActivity(this, truesculpt.ui.panels.TutorialWizardPanel.class);
		}
	}

}