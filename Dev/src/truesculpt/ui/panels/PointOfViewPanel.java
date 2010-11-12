package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.managers.PointOfViewManager.OnPointOfViewChangeListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
				getManagers().getmPointOfViewManager().setElevationAngle(progress-90);				
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		mElevationSeekBar.setMax(180);		
		mElevationText=(TextView)findViewById(R.id.ElevationText);
				

		mRotationSeekBar=(SeekBar)findViewById(R.id.Rotation);
		mRotationSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {				
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				getManagers().getmPointOfViewManager().setRotationAngle(progress-180);				
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		mRotationSeekBar.setMax(360);
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
		mDistanceSeekBar.setMax((int) getManagers().getmPointOfViewManager().getRmax());
		mDistanceText=(TextView)findViewById(R.id.DistanceText);		
		
		Button button = (Button) findViewById(R.id.Reset);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ResetPOV();				
			}
		});
		
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
		float elevation=getManagers().getmPointOfViewManager().getElevationAngle();
		mElevationSeekBar.setProgress((int)elevation+90 );//90° offset to center
		mElevationText.setText("Elevation="+Integer.toString((int)elevation)+" °");
		
		float rotation=getManagers().getmPointOfViewManager().getRotationAngle();
		mRotationSeekBar.setProgress((int)rotation+180 );//180° offset to center
		mRotationText.setText("Rotation="+Integer.toString((int) rotation)+" °");
		
		float distance=getManagers().getmPointOfViewManager().getZoomDistance();
		mDistanceSeekBar.setProgress((int)distance );	
		mDistanceText.setText("Distance="+Integer.toString((int)distance)+" m");
	}
	
	private void ResetPOV()
	{
		getManagers().getmPointOfViewManager().ResetPOV();		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
}
