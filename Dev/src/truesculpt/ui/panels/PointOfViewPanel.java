package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.managers.PointOfViewManager.OnPointOfViewChangeListener;
import android.app.Activity;
import android.os.Bundle;

//TODO transparent to view result immediatly on lower activity
public class PointOfViewPanel extends Activity implements OnPointOfViewChangeListener {

	public Managers getManagers() {	
		return ((TrueSculptApp)getApplicationContext()).getManagers();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pointofview);
		getManagers().getmPointOfViewManager().registerPointOfViewChangeListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onPointOfViewChange() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
}
