package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class NewFilePanel extends Activity
{
	private Button mNewBtn;
	private Spinner mSubdivion_level_spinner;
	
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
				getManagers().getMeshManager().NewMesh(mSubdivion_level_spinner.getSelectedItemPosition());	
				
				getManagers().getUsageStatisticsManager().TrackEvent("NewFile", "New", 1);
				
				finish();
			}
		});
		
	    mSubdivion_level_spinner = (Spinner) findViewById(R.id.Subdivion_level_spinner);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	            this, R.array.subdivision_levels, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    mSubdivion_level_spinner.setAdapter(adapter);
	    mSubdivion_level_spinner.setSelection(5);

	}
	
	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

}
