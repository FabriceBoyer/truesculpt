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
		return settings.getBoolean("CheckUpdateAtStartup", false);
	}

	public boolean getDisplayDebugInfos()
	{
		return settings.getBoolean("DisplayDebugInfos", false);
	}

	public boolean getDisplaySplashScreenAtStartup()
	{
		return settings.getBoolean("DisplaySplashScreenAtStartup", false);
	}

	public boolean getFullScreenApplication()
	{
		return settings.getBoolean("FullScreenApplication", false);
	}

	public boolean getGatherUsageData()
	{
		return settings.getBoolean("GatherUsageData", true);
	}

	public long getLastSoftwareUpdateCheckDate()
	{
		if (settings.contains("LastSoftwareUpdateCheckDate") == false) // init
																		// default
																		// values
		{
			updateLastSoftwareUpdateCheckDate();
		}
		return settings.getLong("LastSoftwareUpdateCheckDate", System.currentTimeMillis());
	}

	public boolean getLoadLastUsedFileAtStartup()
	{
		return settings.getBoolean("LoadLastUsedFileAtStartup", true);
	}

	public String getLastUsedFile()
	{
		return settings.getString("LastUsedFile", "");
	}

	public boolean getPreventSleepMode()
	{
		return settings.getBoolean("PreventSleepMode", true);
	}

	public boolean getUseSensorsToChangePOV()
	{
		return settings.getBoolean("UseSensorsToChangePOV", false);
	}

	public boolean getViewTutorialAtStartup()
	{
		return settings.getBoolean("ViewTutorialAtStartup", false);
	}

	@Override
	public void onCreate()
	{
		// Nothing done here because other managers use this class at create
		// time
	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub

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

	public void setLastUsedFile(String lastUsedFile)
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("LastUsedFile", lastUsedFile);
		editor.commit();
	}
}
