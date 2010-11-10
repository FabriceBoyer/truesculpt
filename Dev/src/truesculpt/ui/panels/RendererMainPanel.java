package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.main.R.id;
import truesculpt.main.R.layout;
import truesculpt.main.R.menu;
import truesculpt.utils.Utils;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class RendererMainPanel extends Activity {

	private static final String TAG = "TrueSculptMain";

	private GLSurfaceView mGLSurfaceView = null;

	public Managers getManagers() {	
		return ((TrueSculptApp)getApplicationContext()).getManagers();
	}
	

	public void ShowSplashScreen()
	{	
		if (getManagers().getmOptionsManager().getDisplaySplashScreenAtStartup()==true)
		{
			Utils.StartMyActivity(this, truesculpt.ui.panels.SplashPanel.class);
		}
	}
	
	public void CheckUpdate()
	{
		if (getManagers().getmOptionsManager().getCheckUpdateAtStartup()==true)
		{
			Utils.StartMyActivity(this, truesculpt.ui.panels.UpdatePanel.class);
		}
	}
	
	public void ShowTutorial()
	{
		if (getManagers().getmOptionsManager().getViewTutorialAtStartup()==true)
		{
			Utils.StartMyActivity(this, truesculpt.ui.panels.TutorialWizardPanel.class);
		}
	}
	
	public void NotifyStartupStat()
	{
		if (getManagers().getmOptionsManager().getGatherUsageData()==true)
		{
			getManagers().getmUsageStatisticsManager().incrementStartupCount();
		}
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getManagers().Init(this);		
		
		ShowSplashScreen();		
		CheckUpdate();
		ShowTutorial();
		NotifyStartupStat();
		
		setContentView(R.layout.main);		
		
		// mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glview);
		// mGLSurfaceView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
		// mGLSurfaceView.setRenderer(mRenderer);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
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
			getManagers().getmOptionsManager().showOptionsPanel();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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

	@Override
	protected void onPause() {
		super.onPause();

		if (mGLSurfaceView != null)
			mGLSurfaceView.onPause();
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

		if (mGLSurfaceView != null)
			mGLSurfaceView.onPause();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.context_menu, menu);
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

}