package truesculpt.ui.panels;

import java.io.File;
import java.io.IOException;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.mesh.Mesh;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

public class OpenFilePanel extends Activity implements Runnable
{
	private Button mOpenBtn;
	private ProgressDialog waitDialog=null;
	private final int DIALOG_WAIT = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.openfile);

		mOpenBtn=(Button)findViewById(R.id.open_file);
		mOpenBtn.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
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
			waitDialog=new ProgressDialog(this);
			waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			waitDialog.setMessage("Opening...");
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
		String strRootDir=getManagers().getUtilsManager().GetRootDirectory();
		File rootDir=new File(strRootDir);
		File[] listFiles=rootDir.listFiles();
		for(File file : listFiles)
		{
			//TODO selector and open
			//temp test with MyTrueSculpture
			boolean bIsDir=file.isDirectory();
			String strName=file.getName();
			if (bIsDir && (strName.compareToIgnoreCase("MyTrueSculpture")==0))
			{
				String strObjFileName=strRootDir+"/"+file.getName()+"/"+"Mesh.obj";	
				try
				{
					//TODO check for init over
					Mesh mesh=getManagers().getMeshManager().getMesh();
					if (mesh!=null && getManagers().getMeshManager().IsInitOver())
					{
						mesh.ImportFromOBJ(strObjFileName);						
					}
					
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}
		
		handler.sendEmptyMessage(0);
	}
	
	private void OpenInternal()
	{
		showDialog(DIALOG_WAIT);
		
	    Thread thread = new Thread(OpenFilePanel.this);
	    thread.start();		
	    
		String name=getManagers().getMeshManager().getName();
		getManagers().getUsageStatisticsManager().TrackEvent("OpenFile", name, 1);		
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
		    	Mesh mesh=getManagers().getMeshManager().getMesh();
				if (mesh!=null && getManagers().getMeshManager().IsInitOver())
				{
					mesh.ComputeBoundingSphereRadius();
				}				
	    	}	
	    	finish();
	    }
	};
	
	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

}
