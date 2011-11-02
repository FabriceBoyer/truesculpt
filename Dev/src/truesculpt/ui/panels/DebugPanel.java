package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class DebugPanel extends Activity
{
	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug);

		final Button button = (Button) findViewById(R.id.show_sensors);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Utils.StartMyActivity(DebugPanel.this, truesculpt.ui.panels.DebugSensorsPanel.class, false);
			}
		});

		// TODO update on timer
		TextView lastFrameText = (TextView) findViewById(R.id.lastFrameDurationText);
		long lLastRenderDuration = getManagers().getRendererManager().getMainRenderer().getLastFrameDurationMs();
		String msg = "Last frame duration = " + Long.toString(lLastRenderDuration) + " ms\n";
		msg += "Equivalent FPS is " + Integer.toString((int) (1.0f / lLastRenderDuration * 1000.0f)) + " images/s\n\n";
		long lLastPickDuration = getManagers().getMeshManager().getLastPickDurationMs();
		msg += "Last picking duration = " + Long.toString(lLastPickDuration) + " ms\n";
		long lLastSculptDuration = getManagers().getMeshManager().getLastSculptDurationMs();
		msg += "Last sculpting duration = " + Long.toString(lLastSculptDuration) + " ms\n";
		lastFrameText.setText(msg);

		TextView meshStatText = (TextView) findViewById(R.id.MeshStatsText);
		int nVertex = getManagers().getMeshManager().getVertexCount();
		int nFaces = getManagers().getMeshManager().getFacesCount();
		msg = "Number of vertices = " + Integer.toString(nVertex) + "\n";
		msg += "Number of faces = " + Integer.toString(nFaces) + "\n";
		meshStatText.setText(msg);

		SeekBar mDebugSeekBar = (SeekBar) findViewById(R.id.debugBar);
		mDebugSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				// getManagers().
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
		mDebugSeekBar.setMax(0);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
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
}
