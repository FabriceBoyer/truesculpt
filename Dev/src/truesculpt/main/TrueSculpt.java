package truesculpt.main;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;


import truesculpt.managers.ManagersManager;
import truesculpt.utils.*;

public class TrueSculpt extends Activity {

	private static final String TAG = "TrueSculptMain";

	private GLSurfaceView mGLSurfaceView=null;

	private ManagersManager mManagers=new ManagersManager(this);

	/**
	 * @return the mManagers
	 */
	public ManagersManager getManagers() {
		return mManagers;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glview);		
		//mGLSurfaceView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
		//mGLSurfaceView.setRenderer(mRenderer);

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
			Utils.StartMyActivity(this, "truesculpt.ui.panels.TutorialWizardPanel");
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

		if (mGLSurfaceView!=null) mGLSurfaceView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mGLSurfaceView!=null) mGLSurfaceView.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (mGLSurfaceView!=null) mGLSurfaceView.onPause();
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
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		default:
			return super.onContextItemSelected(item);
		}
	}

}