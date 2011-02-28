package truesculpt.ui.panels;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.mesh.Mesh;
import truesculpt.utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SaveFilePanel extends Activity implements Runnable
{
	private EditText mEditNameText;
	private Button mSaveBtn;
	private Button mShareBtn;	
	private ProgressDialog waitDialog=null;
	
	private final int DIALOG_FILE_ALREADY_EXIST = 0;
	private final int DIALOG_WAIT = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.savefile);
		
		mEditNameText=(EditText)findViewById(R.id.name_edit);
		String name=getManagers().getMeshManager().getName();
		mEditNameText.setText(name);			
		
		mSaveBtn=(Button)findViewById(R.id.save_file);
		mSaveBtn.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				getManagers().getMeshManager().setName(mEditNameText.getText().toString());	
				String strObjFileName=getManagers().getUtilsManager().GetBaseFileName()+"Mesh.obj";					
				
				//check dir does not already exist
				File file=new File(strObjFileName);				
				if (!file.exists())
				{
					SaveInternal();
				}
				else
				{
					//Dialog box for already exist
					showDialog(DIALOG_FILE_ALREADY_EXIST);
				}
			}
		});
		
		mShareBtn=(Button)findViewById(R.id.share_file);
		mShareBtn.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
			    Thread thread = new Thread()
			    {
			    	@Override
			    	public void run()
			    	{
						String strSnapshotFileName = getManagers().getUtilsManager().CreateSnapshotFileName();
						
						getManagers().getToolsManager().TakeGLScreenshot(strSnapshotFileName);				
						
						//String msg = getString(R.string.snapshot_has_been_saved_to_) + strSnapshotFileName;
						//getManagers().getUtilsManager().ShowToastMessage(msg);
		
						// photo sound
						//MediaPlayer mp = MediaPlayer.create(SaveFilePanel.this, R.raw.photo_shutter);
						//mp.start();
						
						//getManagers().getUtilsManager().SetImageAsWallpaper(strSnapshotFileName);	
						
						//wait for async snapshot to be taken
						File snapshotFile=new File(strSnapshotFileName);
						while (!snapshotFile.exists())
						{
							try
							{
								Thread.sleep(500);
							} catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
						ArrayList<String> filePaths=new ArrayList<String>();
						filePaths.add(strSnapshotFileName);
						Utils.SendEmail(SaveFilePanel.this, "fabrice.boyer@gmail.com", "", "My sculpture", "Check it out it's really great", filePaths);
						
						String name=getManagers().getMeshManager().getName();
						getManagers().getUsageStatisticsManager().TrackEvent("Share", name, 1);
				    }
				};
				thread.start();
			}
		});	
	
	}
	
	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}
	
	public void SaveInternal()
	{		
		mSaveBtn.setEnabled(false);
		showDialog(DIALOG_WAIT);			
		
	    Thread thread = new Thread(this);
	    thread.start();
	
		String name=getManagers().getMeshManager().getName();
		getManagers().getUsageStatisticsManager().TrackEvent("SaveFile", name, 1);		
	}
	
	@Override
	protected Dialog onCreateDialog(int id)
	{
		Dialog dialog = null;
		switch (id)
		{
		case DIALOG_FILE_ALREADY_EXIST:
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.file_already_exist_wanna_override).setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
					SaveInternal();
				}
			}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
				}
			});
			dialog = builder.create();
			break;
		}
		case DIALOG_WAIT:
		{
			waitDialog=new ProgressDialog(this);
			waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			waitDialog.setMessage("Saving...");
			waitDialog.setCancelable(false);
			dialog=waitDialog;
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
		String strBaseFileName=getManagers().getUtilsManager().GetBaseFileName();
		String strObjFileName=strBaseFileName+"Mesh.obj";
		
		Mesh mesh=getManagers().getMeshManager().getMesh();
		if (mesh!=null && getManagers().getMeshManager().IsInitOver())
		{
			mesh.ExportToOBJ(strObjFileName);			
		}
		
		String strPictureFileName=strBaseFileName+"Image.png";
		getManagers().getToolsManager().TakeGLScreenshot(strPictureFileName);
		File snapshotFile=new File(strPictureFileName);
		while (!snapshotFile.exists())
		{
			try
			{
				Thread.sleep(500);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		getManagers().getUtilsManager().ShowToastMessage("Sculpture successfully exported to " + strBaseFileName);
		
	    handler.sendEmptyMessage(0);
	 }

	private Handler handler = new Handler()
	{
	    @Override
	    public void handleMessage(Message msg) 
	    {
	    	if (waitDialog!=null)
	    	{
		    	waitDialog.dismiss();
		    	waitDialog=null;
	    	}	    
	    	mSaveBtn.setEnabled(true);
	    	finish();
	    }
	};
	
	@Override
	protected void onPause()
	{
		getManagers().getMeshManager().setName(mEditNameText.getText().toString());
		super.onDestroy();
	}

}
