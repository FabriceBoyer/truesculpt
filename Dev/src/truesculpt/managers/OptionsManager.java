package truesculpt.managers;

import truesculpt.utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;

//is a local cache really necessary ? direct call to manager better?
public class OptionsManager extends BaseManager {

	SharedPreferences settings = null;

	public OptionsManager(Context baseContext) {
		super(baseContext);

		// Restore preferences
		settings = PreferenceManager.getDefaultSharedPreferences(getbaseContext()); 

	}

	public boolean getCheckUpdateAtStartup() {
		return settings.getBoolean("CheckUpdateAtStartup", true);
	}

	public boolean getDisplaySplashScreenAtStartup() {
		return settings.getBoolean("DisplaySplashScreenAtStartup", true);
	}

	public boolean getGatherUsageData() {
		return settings.getBoolean("GatherUsageData", true);
	}

	public boolean getLoadLastUsedFileAtStartup() {
		return settings.getBoolean("LoadLastUsedFileAtStartup", true);
	}

	public boolean getViewTutorialAtStartup() {
		return settings.getBoolean("ViewTutorialAtStartup", true);
	}

	public boolean getUseSensorsToChangePOV() {
		return settings.getBoolean("UseSensorsToChangePOV", true);
	}
	
	public void setLoadLastUsedFileAtStartup(boolean mLoadLastUsedFileAtStartup) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("LoaLastdUsedFileAtStartup", mLoadLastUsedFileAtStartup);
		editor.commit();
	}

	public void setCheckUpdateAtStartup(boolean mCheckUpdateAtStartup) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("CheckUpdateAtStartup", mCheckUpdateAtStartup);
		editor.commit();
	}

	public void setDisplaySplashScreenAtStartup(boolean mDisplaySplashScreenAtStartup) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("DisplaySplashScreenAtStartup", mDisplaySplashScreenAtStartup);
		editor.commit();
	}

	public void setGatherUsageData(boolean mGatherUsageData) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("GatherUsageData", mGatherUsageData);
		editor.commit();
	}
		
	public void setViewTutorialAtStartup(boolean mViewTutorialAtStartup) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("ViewTutorialAtStartup", mViewTutorialAtStartup);
		editor.commit();
	}
	
	public void setUseSensorsToChangePOV(boolean mUseSensorsToChangePOV) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("UseSensorsToChangePOV", mUseSensorsToChangePOV);
		editor.commit();
		
		getManagers().getSensorsManager().restart();
	}
	
	public void updateLastSoftwareUpdateCheckDate()
	{
		long today= System.currentTimeMillis();
		
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("LastSoftwareUpdateCheckDate", today);
		editor.commit();		
	}
	
	public long getLastSoftwareUpdateCheckDate() {
		return settings.getLong("LastSoftwareUpdateCheckDate", System.currentTimeMillis());
	}

	public void showOptionsPanel(Activity callingActivity) {
		Utils.StartMyActivity(callingActivity, truesculpt.ui.panels.OptionsPanel.class);
		
		getManagers().getSensorsManager().restart();//to ensure updated, should be better to implement listener on activity
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}

}
