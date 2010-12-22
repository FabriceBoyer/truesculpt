package truesculpt.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//is a local cache really necessary ? direct call to manager better?
public class OptionsManager extends BaseManager
{

	SharedPreferences settings = null;

	public OptionsManager(Context baseContext)
	{
		super(baseContext);

		// Restore preferences
		settings = PreferenceManager.getDefaultSharedPreferences(getbaseContext());

	}

	public boolean getCheckUpdateAtStartup()
	{
		return settings.getBoolean("CheckUpdateAtStartup", true);
	}

	public boolean getDisplayDebugInfos()
	{
		return settings.getBoolean("DisplayDebugInfos", true);
	}

	public boolean getDisplaySplashScreenAtStartup()
	{
		return settings.getBoolean("DisplaySplashScreenAtStartup", true);
	}

	public boolean getGatherUsageData()
	{
		return settings.getBoolean("GatherUsageData", true);
	}

	public boolean getLoadLastUsedFileAtStartup()
	{
		return settings.getBoolean("LoadLastUsedFileAtStartup", true);
	}

	public boolean getUseSensorsToChangePOV()
	{
		return settings.getBoolean("UseSensorsToChangePOV", true);
	}

	public boolean getViewTutorialAtStartup()
	{
		return settings.getBoolean("ViewTutorialAtStartup", true);
	}
	
	public void setViewTutorialAtStartup(boolean mViewTutorialAtStartup)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("ViewTutorialAtStartup", mViewTutorialAtStartup);
		editor.commit();
	}
	
	public boolean getPreventSleepMode()
	{
		return settings.getBoolean("PreventSleepMode", true);
	}

	@Override
	public void onCreate()
	{
		//Nothing done here because other managers use this class at create time
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub

	}

	public long getLastSoftwareUpdateCheckDate()
	{
		if (settings.contains("LastSoftwareUpdateCheckDate") == false) // init default values
		{
			updateLastSoftwareUpdateCheckDate();
		}
		return settings.getLong("LastSoftwareUpdateCheckDate", System.currentTimeMillis());
	}
	
	public void updateLastSoftwareUpdateCheckDate()
	{
		long today = System.currentTimeMillis();

		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("LastSoftwareUpdateCheckDate", today);
		editor.commit();
	}
}
