package truesculpt.main;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class TrueSculpt extends Activity {

	private static final String TAG = "TrueSculptMain";

	private GLSurfaceView mGLSurfaceView;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mGLSurfaceView = (GLSurfaceView) findViewById(R.id.glview);
		
		mGLSurfaceView.setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);
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
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.show_debug_panel: {
			Intent myIntent = new Intent("truesculpt.debug.DebugPanel");
			startActivity(myIntent);
			return true;
		}
		case R.id.show_check_version_panel: {
			Intent myIntent = new Intent("truesculpt.ui.UpdatePanel");
			startActivity(myIntent);
			return true;
		}
		case R.id.show_tutorial_wizard_panel: {
			Intent myIntent = new Intent("truesculpt.ui.TutorialWizard");
			startActivity(myIntent);
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