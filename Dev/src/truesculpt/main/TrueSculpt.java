package truesculpt.main;

import truesculpt.managers.ManagersManager;
import truesculpt.ui.dialogs.SplashDialog;
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

public class TrueSculpt extends Activity {

	private static final String TAG = "TrueSculptMain";

	private GLSurfaceView mGLSurfaceView = null;

	private ManagersManager mManagers = new ManagersManager(this);

	/**
	 * @return the mManagers
	 */
	public ManagersManager getManagers() {
		return mManagers;
	}

	public void ShowSplashScreen()
	{		
		SplashDialog splash= new SplashDialog(this);
		splash.show();
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ShowSplashScreen();
		
		setContentView(R.layout.main);
		
		getManagers().getmOptionsManager().onCreate();
		
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
			Utils.StartMyActivity(this, "truesculpt.ui.panels.ToolsPanel");
			return true;
		}
		case R.id.show_point_of_view_panel: {
			Utils.StartMyActivity(this, "truesculpt.ui.panels.PointOfViewPanel");
			return true;
		}
		case R.id.show_debug_panel: {
			Utils.StartMyActivity(this, "truesculpt.ui.debug.DebugPanel");
			return true;
		}
		case R.id.show_check_version_panel: {
			Utils.StartMyActivity(this, "truesculpt.ui.panels.UpdatePanel");
			return true;
		}
		case R.id.show_tutorial_wizard_panel: {
			Utils.StartMyActivity(this,
					"truesculpt.ui.panels.TutorialWizardPanel");
			return true;
		}
		case R.id.show_options: {
			getManagers().getmOptionsManager().showOptions();
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