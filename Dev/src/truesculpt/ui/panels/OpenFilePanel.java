package truesculpt.ui.panels;

import java.io.File;
import java.io.IOException;
import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.mesh.Mesh;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OpenFilePanel extends Activity
{
	private Button mOpenBtn;
	private Button mOpenFromWebBtn;
	
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
				String name=getManagers().getMeshManager().getName();
				getManagers().getUsageStatisticsManager().TrackEvent("OpenFile", name, 1);
				
				finish();
			}
		});
	
	}
	
	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

}
