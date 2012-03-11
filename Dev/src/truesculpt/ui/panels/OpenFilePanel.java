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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	OpenFileAdapter mAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getManagers().getUtilsManager().updateFullscreenWindowStatus(getWindow());

		setContentView(R.layout.openfile);

		mFileList.clear();
		mFileList = getManagers().getFileManager().getFileList();

		gridview = (GridView) findViewById(R.id.openfilegridview);
		mAdapter = new OpenFileAdapter(this, mFileList);
		gridview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				mSelectedElem = mFileList.get((int) id);
				OpenInternal();
			}
		});
		gridview.setAdapter(mAdapter);

		registerForContextMenu(gridview);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.open_item_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		mSelectedElem = mFileList.get((int) info.id);
		switch (item.getItemId())
		{
		case R.id.open:
			OpenInternal();
			return true;
		case R.id.delete:
			getManagers().getFileManager().deleteFile(mSelectedElem);
			mFileList.remove((int) info.id);
			mAdapter.notifyDataSetChanged();
			return true;
		case R.id.rename:
			getManagers().getUtilsManager().ShowToastMessage("Not implemented yet");
			return true;
		default:
			return super.onContextItemSelected(item);
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
