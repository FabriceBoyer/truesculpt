package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewFilePanel extends Activity
{
	private Button mNewBtn;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newfile);
		
		mNewBtn=(Button)findViewById(R.id.new_file);
		mNewBtn.setOnClickListener(new View.OnClickListener()
		{			
			@Override
			public void onClick(View v)
			{
				//TODO dialog for color and subdivion level and alert if new failed (already in init)
				getManagers().getMeshManager().NewMesh(5);	
				
				String name=getManagers().getMeshManager().getName();
				
				getManagers().getUsageStatisticsManager().TrackEvent("NewFile", "New", 1);
				
				finish();
			}
		});
	}
	
	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

}
