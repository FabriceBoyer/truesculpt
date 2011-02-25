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
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class OpenFilePanel extends Activity implements Runnable
{
	private Button mOpenBtn;
	private ProgressDialog waitDialog=null;
	private GridView gridview=null;
	
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
		
		gridview = (GridView) findViewById(R.id.openfilegridview);
	    gridview.setAdapter(new ImageAdapter(this));

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            Toast.makeText(OpenFilePanel.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });

	}
	
	public class ImageAdapter extends BaseAdapter {
	    private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return mThumbIds.length;
	    }

	    public Object getItem(int position) {
	        return null;
	    }

	    public long getItemId(int position) {
	        return 0;
	    }

	    // create a new ImageView for each item referenced by the Adapter
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(8, 8, 8, 8);
	        } else {
	            imageView = (ImageView) convertView;
	        }

	        imageView.setImageResource(mThumbIds[position]);
	        return imageView;
	    }

	    // references to our images
	    private Integer[] mThumbIds = {
	            R.drawable.brush, R.drawable.colorpicker,
	            R.drawable.copy, R.drawable.no_symmetry,
	            R.drawable.disc, R.drawable.smooth,
	            R.drawable.equalizer, R.drawable.draw,
	            R.drawable.clock, R.drawable.wizard
	    };
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
