package truesculpt.ui.panels;

import java.util.Observable;
import java.util.Observer;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

//TODO transparent to view result immediately on lower activity
public class PointOfViewPanel extends Activity implements Observer
{

	private SeekBar mDistanceSeekBar;

	private TextView mDistanceText;
	private SeekBar mElevationSeekBar;
	private TextView mElevationText;

	private SeekBar mRotationSeekBar;
	private TextView mRotationText;

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pointofview);

		mElevationSeekBar = (SeekBar) findViewById(R.id.Elevation);
		mElevationSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				getManagers().getPointOfViewManager().setElevationAngle(progress - 90);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
			}
		});
		mElevationSeekBar.setMax(180);
		mElevationText = (TextView) findViewById(R.id.ElevationText);

		mRotationSeekBar = (SeekBar) findViewById(R.id.Rotation);
		mRotationSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				getManagers().getPointOfViewManager().setRotationAngle(progress - 180);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
			}
		});
		mRotationSeekBar.setMax(360);
		mRotationText = (TextView) findViewById(R.id.RotationText);

		mDistanceSeekBar = (SeekBar) findViewById(R.id.Distance);
		mDistanceSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				getManagers().getPointOfViewManager().setZoomDistance(progress);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
			}
		});
		mDistanceSeekBar.setMax((int) getManagers().getPointOfViewManager().getRmax());
		mDistanceText = (TextView) findViewById(R.id.DistanceText);

		Button button = (Button) findViewById(R.id.Reset);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ResetPOV();
			}
		});

		UpdateUI();

		getManagers().getPointOfViewManager().addObserver(PointOfViewPanel.this);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		getManagers().getPointOfViewManager().deleteObserver(PointOfViewPanel.this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// finish();
		return super.onTouchEvent(event);
	}

	private void ResetPOV()
	{
		getManagers().getPointOfViewManager().resetPOV();
	}

	@Override
	public void update(Observable observable, Object data)
	{
		UpdateUI();
	}

	private void UpdateUI()
	{
		float elevation = getManagers().getPointOfViewManager().getElevationAngle();
		mElevationSeekBar.setProgress((int) elevation + 90);// 90° offset to
		// center
		mElevationText.setText("Elevation=" + Integer.toString((int) elevation) + " °");

		float rotation = getManagers().getPointOfViewManager().getRotationAngle();
		mRotationSeekBar.setProgress((int) rotation + 180);// 180° offset to
		// center
		mRotationText.setText("Rotation=" + Integer.toString((int) rotation) + " °");

		float distance = getManagers().getPointOfViewManager().getZoomDistance();
		mDistanceSeekBar.setProgress((int) distance);
		mDistanceText.setText("Distance=" + Integer.toString((int) distance) + " m");
	}
}
