package truesculpt.ui.panels;

import java.util.ArrayList;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.managers.FileManager;
import truesculpt.ui.adapters.OpenFileAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class OpenFilePanel extends Activity implements Runnable
{
	private ProgressDialog waitDialog = null;
	private GridView gridview = null;
	private FileManager.FileElem mSelectedElem = null;
	private ArrayList<FileManager.FileElem> mFileList = new ArrayList<FileManager.FileElem>();
	private final int DIALOG_WAIT = 1;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getManagers().getUtilsManager().updateFullscreenWindowStatus(getWindow());

		setContentView(R.layout.openfile);

		mFileList.clear();
		mFileList = getManagers().getFileManager().getFileList();

		gridview = (GridView) findViewById(R.id.openfilegridview);
		gridview.setAdapter(new OpenFileAdapter(this, mFileList));
		gridview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				mSelectedElem = mFileList.get((int) id);
				OpenInternal();
			}
		});
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
			waitDialog.setMessage("Opening...");
			waitDialog.setCancelable(false);
			dialog = waitDialog;
			break;
		}
		default:
			dialog = null;
		}
		return dialog;
	}

	@Override
	public void run()
	{
		if (mSelectedElem != null)
		{
			try
			{
				if (getManagers().getMeshManager().IsInitOver())
				{
					getManagers().getMeshManager().ImportFromOBJ(mSelectedElem.objfilename);
					getManagers().getMeshManager().setName(mSelectedElem.name);
					mSelectedElem = null;
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		handler.sendEmptyMessage(0);
	}

	private void OpenInternal()
	{
		showDialog(DIALOG_WAIT);

		Thread thread = new Thread(OpenFilePanel.this);
		thread.start();

		String name = getManagers().getMeshManager().getName();
		getManagers().getUsageStatisticsManager().TrackEvent("OpenFile", name, 1);
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
