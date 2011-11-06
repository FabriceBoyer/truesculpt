package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class NewFilePanel extends Activity implements Runnable
{
	private Button mNewBtn;
	private Spinner mSubdivion_level_spinner;
	private Spinner mInitial_shape_spinner;
	private ProgressDialog waitDialog = null;

	private final int DIALOG_WAIT = 1;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getManagers().getUtilsManager().updateFullscreenWindowStatus(getWindow());

		setContentView(R.layout.newfile);

		mNewBtn = (Button) findViewById(R.id.new_file);
		mNewBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				NewInternal();
			}
		});

		// TODO not implemented yet
		{
			mInitial_shape_spinner = (Spinner) findViewById(R.id.initial_shape);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.initial_shape, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mInitial_shape_spinner.setAdapter(adapter);
			mInitial_shape_spinner.setSelection(0);
		}

		{
			mSubdivion_level_spinner = (Spinner) findViewById(R.id.Subdivion_level_spinner);
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.subdivision_levels, android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mSubdivion_level_spinner.setAdapter(adapter);
			mSubdivion_level_spinner.setSelection(5);
		}

	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		Dialog dialog = null;
		switch (id)
		{
		case DIALOG_WAIT:
		{
			waitDialog = new ProgressDialog(this);
			waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			waitDialog.setMessage("Creating...");
			waitDialog.setCancelable(false);
			dialog = waitDialog;
			break;
		}
		default:
			dialog = null;
		}
		return dialog;
	}

	private void NewInternal()
	{
		showDialog(DIALOG_WAIT);

		Thread thread = new Thread(NewFilePanel.this);
		thread.start();

		getManagers().getUsageStatisticsManager().TrackEvent("NewFile", "New", 1);
	}

	@Override
	public void run()
	{
		if (getManagers().getMeshManager().IsInitOver())
		{
			getManagers().getMeshManager().NewMeshBlocking(mSubdivion_level_spinner.getSelectedItemPosition());
		}

		handler.sendEmptyMessage(0);
	}

	private final Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			if (waitDialog != null)
			{
				waitDialog.dismiss();
				waitDialog = null;
			}
			finish();
		}
	};

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

}
