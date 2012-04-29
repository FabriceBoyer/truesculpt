package truesculpt.ui.panels;

import java.util.ArrayList;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.managers.FileManager;
import truesculpt.managers.FileManager.FileElem;
import truesculpt.parser.WebEntry;
import truesculpt.ui.adapters.OpenFileAdapter;
import truesculpt.ui.adapters.StreamingCoverFlowAdapter;
import truesculpt.ui.views.CoverFlow;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

public class OpenFileFlowPanel extends Activity implements Runnable
{
	private ProgressDialog waitDialog = null;
	private TextView mNameView = null;
	private TextView mDescriptionView = null;
	private CoverFlow mCoverFlow = null;
	private StreamingCoverFlowAdapter mAdapter = null;
	private Button mOpenBtn = null;
	private Button mRenameBtn = null;
	private Button mDeleteBtn = null;
	
	private FileManager.FileElem mSelectedElem = null;
	private int mSelectedElemID = -1;
	private ArrayList<FileManager.FileElem> mFileList = new ArrayList<FileManager.FileElem>();
	private final int DIALOG_WAIT = 1;
	private final int DIALOG_RENAME = 2;

	//TODO place in standalone class, nothing to do here
	private EditText mInput = null;;
	private int mRenameID = -1;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getManagers().getUsageStatisticsManager().TrackPageView("/OpenFileFlowPanel");
		getManagers().getUtilsManager().updateFullscreenWindowStatus(getWindow());

		setContentView(R.layout.openfileflow);

		mFileList.clear();
		mFileList = getManagers().getFileManager().getFileList();

		mNameView = (TextView) findViewById(R.id.name);
		mDescriptionView = (TextView) findViewById(R.id.description);
		mDescriptionView.setMovementMethod(new ScrollingMovementMethod());
		mCoverFlow = (CoverFlow) findViewById(R.id.coverflow);
		
		mOpenBtn = (Button) findViewById(R.id.open);
		mOpenBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				OpenInternal();			
			}
		});
		
		mRenameBtn = (Button) findViewById(R.id.rename);
		mRenameBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				RenameInternal();			
			}
		});
			
		
		mDeleteBtn = (Button) findViewById(R.id.delete);
		mDeleteBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				DeleteInternal();			
			}
		});			

		mAdapter = new StreamingCoverFlowAdapter(this);
		UpdateEntries();
		
		mCoverFlow.setAdapter(mAdapter);

		mCoverFlow.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
			{				
				OpenInternal();		
			}
		});
		mCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id)
			{		
				mSelectedElemID=(int)id;
				mSelectedElem = mFileList.get(mSelectedElemID);				
				mNameView.setText(mSelectedElem.name);
				mDescriptionView.setText("");				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				mSelectedElem=null;
				mSelectedElemID=-1;
				mNameView.setText("");
				mDescriptionView.setText("");
			}
		});
		
		registerForContextMenu(mCoverFlow);
	}

	private void UpdateEntries()
	{
		ArrayList<String> stringEntries= new ArrayList<String>();
		for(FileElem entry : mFileList)
		{
			stringEntries.add("file://"+entry.imagefilename);
		}
		mAdapter.setEntries(stringEntries);	
		mAdapter.notifyDataSetChanged();
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
		mSelectedElemID=(int) info.id;
		mSelectedElem = mFileList.get(mSelectedElemID);		
		switch (item.getItemId())
		{
		case R.id.open:
			OpenInternal();
			return true;
		case R.id.delete:
			DeleteInternal();
			return true;
		case R.id.rename:
			RenameInternal();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private void RenameInternal() {
		if (mSelectedElemID>=0)
		{
			mRenameID = mSelectedElemID;
			if (mInput != null)
			{
				mInput.setText("");
			}
			showDialog(DIALOG_RENAME);// no bundle params in V7
		}
	}

	private void DeleteInternal() {
		if (mSelectedElemID>=0 && mSelectedElem!=null)
		{
			getManagers().getFileManager().deleteFile(mSelectedElem);
			mFileList.remove(mSelectedElemID);
			UpdateEntries();			
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
		case DIALOG_RENAME:
		{
			mInput = new EditText(this);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Enter new name").setCancelable(false).setPositiveButton(R.string.ok_btn, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
					String renameNewName = mInput.getText().toString();
					if (renameNewName != "")
					{
						FileElem elem = getManagers().getFileManager().renameFile(mSelectedElem, renameNewName);
						if (elem != null)
						{
							mFileList.set(mRenameID, elem);// replace old elem
							mAdapter.notifyDataSetChanged();
							getManagers().getUtilsManager().ShowToastMessage("Renaming to " + renameNewName);
						}
						else
						{
							getManagers().getUtilsManager().ShowToastMessage("A sculpture named " + renameNewName + " already exists\nPlease choose another name");
						}
					}
				}
			}).setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int id)
				{

				}
			}).setView(mInput);
			dialog = builder.create();
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

		Thread thread = new Thread(OpenFileFlowPanel.this);
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
