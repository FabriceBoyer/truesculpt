package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.managers.PointOfViewManager.OnPointOfViewChangeListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

//TODO transparent to view result immediately on lower activity
public class PointOfViewPanel extends Activity implements OnPointOfViewChangeListener {

	public Managers getManagers() {	
		return ((TrueSculptApp)getApplicationContext()).getManagers();
	}

	private SeekBar mElevationSeekBar;
	private SeekBar mRotationSeekBar;
	private SeekBar mDistanceSeekBar;
	
	private TextView mElevationText;
	private TextView mRotationText;
	private TextView mDistanceText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pointofview);
		
		
		mElevationSeekBar=(SeekBar)findViewById(R.id.Elevation);
		mElevationSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {				
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				getManagers().getmPointOfViewManager().setElevationAngle(progress);				
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		mElevationText=(TextView)findViewById(R.id.ElevationText);
				

		mRotationSeekBar=(SeekBar)findViewById(R.id.Rotation);
		mRotationSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {				
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				getManagers().getmPointOfViewManager().setRotationAngle(progress);				
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		mRotationText=(TextView)findViewById(R.id.RotationText);		
		
		
		mDistanceSeekBar=(SeekBar)findViewById(R.id.Distance);
		mDistanceSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {				
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				getManagers().getmPointOfViewManager().setZoomDistance(progress);				
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		mDistanceText=(TextView)findViewById(R.id.DistanceText);		
		
		UpdateUI();
		
		getManagers().getmPointOfViewManager().registerPointOfViewChangeListener(PointOfViewPanel.this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getManagers().getmPointOfViewManager().unRegisterPointOfViewChangeListener(PointOfViewPanel.this);
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
		mElevationSeekBar.setProgress((int) getManagers().getmPointOfViewManager().getElevationAngle());
		mElevationText.setText("Elevation="+Integer.toString((int) getManagers().getmPointOfViewManager().getElevationAngle())+" °");
		
		mRotationSeekBar.setProgress((int) getManagers().getmPointOfViewManager().getRotationAngle());
		mRotationText.setText("Rotation="+Integer.toString((int) getManagers().getmPointOfViewManager().getRotationAngle())+" °");
		
		mDistanceSeekBar.setProgress((int) getManagers().getmPointOfViewManager().getZoomDistance());	
		mDistanceText.setText("Distance="+Integer.toString((int) getManagers().getmPointOfViewManager().getZoomDistance())+" m");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
}
