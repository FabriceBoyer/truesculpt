package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WebFilePanel extends Activity
{
	private Button mOpenFromWebBtn;
	private Button mPublishToWebBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webfile);			
		
		mOpenFromWebBtn=(Button)findViewById(R.id.open_from_web);
		mOpenFromWebBtn.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{				
				getManagers().getUsageStatisticsManager().TrackEvent("OpenFromWeb", "", 1);				
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
	
	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

}
