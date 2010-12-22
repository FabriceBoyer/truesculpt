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

	public long getLastSoftwareUpdateCheckDate()
	{
		if (settings.contains("LastSoftwareUpdateCheckDate") == false) // init default values
		{
			updateLastSoftwareUpdateCheckDate();
		}
		return settings.getLong("LastSoftwareUpdateCheckDate", System.currentTimeMillis());
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

	public void setCheckUpdateAtStartup(boolean mCheckUpdateAtStartup)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("CheckUpdateAtStartup", mCheckUpdateAtStartup);
		editor.commit();
	}

	public void setDisplayDebugInfos(boolean mDisplayDebugInfos)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("DisplayDebugInfos", mDisplayDebugInfos);
		editor.commit();
	}

	public void setDisplaySplashScreenAtStartup(boolean mDisplaySplashScreenAtStartup)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("DisplaySplashScreenAtStartup", mDisplaySplashScreenAtStartup);
		editor.commit();
	}

	public void setGatherUsageData(boolean mGatherUsageData)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("GatherUsageData", mGatherUsageData);
		editor.commit();
		
		getManagers().getUsageStatisticsManager().restart();
	}

	public void setLoadLastUsedFileAtStartup(boolean mLoadLastUsedFileAtStartup)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("LoadLastUsedFileAtStartup", mLoadLastUsedFileAtStartup);
		editor.commit();		
	}

	public void setUseSensorsToChangePOV(boolean mUseSensorsToChangePOV)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("UseSensorsToChangePOV", mUseSensorsToChangePOV);
		editor.commit();

		getManagers().getSensorsManager().restart();
	}

	public void setViewTutorialAtStartup(boolean mViewTutorialAtStartup)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("ViewTutorialAtStartup", mViewTutorialAtStartup);
		editor.commit();
	}

	public void updateLastSoftwareUpdateCheckDate()
	{
		long today = System.currentTimeMillis();

		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("LastSoftwareUpdateCheckDate", today);
		editor.commit();
	}
	
	public void setPreventSleepMode(boolean mPreventSleepMode)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("PreventSleepMode", mPreventSleepMode);
		editor.commit();
		
		getManagers().getSleepPowerManager().restart();
	}	

}
