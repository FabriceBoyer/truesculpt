package truesculpt.ui.panels;

import java.io.File;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SaveFilePanel extends Activity
{
	private EditText mEditNameText;
	private Button mSaveBtn;
	private Button mShareBtn;
	private Button mPublishToWebBtn;
	
	private final int DIALOG_FILE_ALREADY_EXIST = 0;
	
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
				getManagers().getToolsManager().TakeGLScreenshot();	
				
				String name=getManagers().getMeshManager().getName();
				getManagers().getUsageStatisticsManager().TrackEvent("Share", name, 1);
			}
		});

		
		mPublishToWebBtn=(Button)findViewById(R.id.publish_to_web);
		mPublishToWebBtn.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				String name=getManagers().getMeshManager().getName();
				getManagers().getUsageStatisticsManager().TrackEvent("PublishToWeb", name, 1);				
			}
		});
	
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
					finish();
				}
			}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
					finish();
				}
			});
			dialog = builder.create();
			break;
		}
		default:
			dialog = null;
		}
		return dialog;
	}
	
	private void SaveInternal()
	{
		String strBaseFileName=getManagers().getUtilsManager().GetBaseFileName();
		String strObjFileName=strBaseFileName+"Mesh.obj";
		getManagers().getMeshManager().getMesh().ExportToOBJ(strObjFileName);				
		
		String strPictureFileName=getManagers().getUtilsManager().GetBaseFileName()+"Image.png";
		//TODO snapshot for nice open
		
		String name=getManagers().getMeshManager().getName();
		getManagers().getUsageStatisticsManager().TrackEvent("SaveFile", name, 1);
		
		finish();
	}
	
	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}
	
	@Override
	protected void onPause()
	{
		getManagers().getMeshManager().setName(mEditNameText.getText().toString());
		super.onDestroy();
	}

}
