package truesculpt.ui.panels;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.mesh.Mesh;
import truesculpt.ui.adapters.OpenFileAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class OpenFilePanel extends Activity implements Runnable
{
	private ProgressDialog waitDialog=null;
	private GridView gridview=null;
	private FileElem mSelectedElem=null;
	private ArrayList<FileElem> mFileList= new ArrayList<FileElem>();
	private final int DIALOG_WAIT = 1;
		
	public class FileElem
	{
		public String name;
		public String objfilename;
		public String imagefilename;
		public Bitmap bmp=null;
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.openfile);
		
		mFileList.clear();
		String strRootDir=getManagers().getUtilsManager().GetRootDirectory();
		File rootDir=new File(strRootDir);
		File[] listFiles=rootDir.listFiles();	
		for(File file : listFiles)
		{
			boolean bIsDir=file.isDirectory();			
			if (bIsDir)
			{
				String strName=file.getName();
				String strObjName=file.getAbsolutePath()+"/"+"Mesh.obj";
				String strImageName=file.getAbsolutePath()+"/"+"Image.png";
				File ObjName=new File(strObjName);
				File ImageName=new File(strImageName);
				if (ObjName.exists() && ImageName.exists())
				{
					FileElem elem= new FileElem();
					elem.objfilename=strObjName;
					elem.name=strName;
					elem.imagefilename=strImageName;
					mFileList.add(elem);
				}				
			}
		}
		gridview = (GridView) findViewById(R.id.openfilegridview);
	    gridview.setAdapter(new OpenFileAdapter(this, mFileList));
	    gridview.setOnItemClickListener(new OnItemClickListener() 
	    {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	        {
	        	mSelectedElem=mFileList.get((int) id);
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
		if (mSelectedElem!=null)
		{
			try
			{
				Mesh mesh=getManagers().getMeshManager().getMesh();
				if (mesh!=null && getManagers().getMeshManager().IsInitOver())
				{
					mesh.ImportFromOBJ(mSelectedElem.objfilename);		
					getManagers().getMeshManager().setName(mSelectedElem.name);
					mSelectedElem=null;
				}				
			} 
			catch (IOException e)
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
	    	}	
	    	finish();
	    }
	};
	
	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

}
