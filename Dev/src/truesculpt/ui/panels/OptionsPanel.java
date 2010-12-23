package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;

public class OptionsPanel extends PreferenceActivity
{

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		getManagers().getUsageStatisticsManager().TrackPageView("/OptionsPanel");

		// must be in same package than manager ?
		addPreferencesFromResource(R.xml.options);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.preference.PreferenceActivity#onDestroy()
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();

	}

	@Override
	protected void onStop()
	{
		super.onStop();

	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)
	{
		//click value already set in manager		
		if( preference.getKey().equalsIgnoreCase("PreventSleepMode"))
		{		
			getManagers().getSleepPowerManager().restart();
		}
		if( preference.getKey().equalsIgnoreCase("UseSensorsToChangePOV"))
		{		
			getManagers().getSensorsManager().restart();
		}
		if( preference.getKey().equalsIgnoreCase("GatherUsageData"))
		{		
			getManagers().getUsageStatisticsManager().restart();
		}
		if( preference.getKey().equalsIgnoreCase("DisplayDebugInfos"))
		{		
			getManagers().getPointOfViewManager().resetPOV();
		}	
		if( preference.getKey().equalsIgnoreCase("FullScreenApplication"))
		{		
			String msg = "You need to restart the application for this option to be taken into account";
			Toast.makeText(this, msg, Toast.LENGTH_LONG);
		}	
		
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

}
