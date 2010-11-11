package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.managers.PointOfViewManager.OnPointOfViewChangeListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

//TODO transparent to view result immediately on lower activity
public class PointOfViewPanel extends Activity implements OnPointOfViewChangeListener {

	public Managers getManagers() {	
		return ((TrueSculptApp)getApplicationContext()).getManagers();
	}

	private SeekBar elevation;
	private SeekBar rotation;
	private SeekBar distance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pointofview);
		
		
		elevation=(SeekBar)findViewById(R.id.Elevation);
		elevation.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {				
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				getManagers().getmPointOfViewManager().setElevationAngle(progress);				
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		

		rotation=(SeekBar)findViewById(R.id.Rotation);
		rotation.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {				
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				getManagers().getmPointOfViewManager().setRotationAngle(progress);				
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		
		distance=(SeekBar)findViewById(R.id.Distance);
		distance.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {				
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				getManagers().getmPointOfViewManager().setZoomDistance(progress);				
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		UpdateUI();
		
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
		UpdateUI();
	}
	
	private void UpdateUI()
	{		
		elevation.setProgress((int) getManagers().getmPointOfViewManager().getElevationAngle());
		rotation.setProgress((int) getManagers().getmPointOfViewManager().getRotationAngle());
		distance.setProgress((int) getManagers().getmPointOfViewManager().getZoomDistance());		
	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
}
