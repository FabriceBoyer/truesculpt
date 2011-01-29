package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class OptionsPanel extends PreferenceActivity
{

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getManagers().getUsageStatisticsManager().TrackPageView("/OptionsPanel");

		// must be in same package than manager ?
		addPreferencesFromResource(R.xml.options);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference)
	{
		// click value already set in manager
		if (preference.getKey().equalsIgnoreCase("PreventSleepMode"))
		{
			getManagers().getSleepPowerManager().restart();
		}
		if (preference.getKey().equalsIgnoreCase("UseSensorsToChangePOV"))
		{
			getManagers().getSensorsManager().restart();
		}
		if (preference.getKey().equalsIgnoreCase("GatherUsageData"))
		{
			getManagers().getUsageStatisticsManager().restart();
		}
		if (preference.getKey().equalsIgnoreCase("DisplayDebugInfos"))
		{
			getManagers().getPointOfViewManager().resetPOV();
		}
		if (preference.getKey().equalsIgnoreCase("FullScreenApplication"))
		{
			String msg = getString(R.string.you_need_to_restart_the_application_for_this_option_to_be_taken_into_account);
			getManagers().getUtilsManager().ShowToastMessage(msg);
		}

		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}

}
