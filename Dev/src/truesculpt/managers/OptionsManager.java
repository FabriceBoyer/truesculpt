package truesculpt.managers;

import truesculpt.utils.Utils;
import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class OptionsManager extends BaseManager {

	SharedPreferences settings = null;

	private boolean mCheckUpdateAtStartup = true;
	private boolean mViewTutorialAtStartup = true;
	private boolean mDisplaySplashScreenAtStartup = true;
	private boolean mGatherUsageData=true;
	

	public OptionsManager(Activity mBaseActivity) {
		super(mBaseActivity);

		//PreferenceManager.setDefaultValues(getBaseActivity(),

	}
		
	public void onCreate()
	{		
		// Restore preferences
		settings = PreferenceManager.getDefaultSharedPreferences(getBaseActivity());		//settings = getBaseActivity().getSharedPreferences(PREFS_NAME, 0);
	
		mCheckUpdateAtStartup = settings.getBoolean("CheckUpdateAtStartup", true);
		mViewTutorialAtStartup = settings.getBoolean("ViewTutorialAtStartup", true);
		mDisplaySplashScreenAtStartup = settings.getBoolean("DisplaySplashScreenAtStartup", true);
		mGatherUsageData = settings.getBoolean("GatherUsageData", true);
	}

	public void showOptionsPanel() {
		Utils.StartMyActivity(getBaseActivity(), truesculpt.ui.panels.OptionsPanel.class);
	}
	
	private void saveAllOptions()
	{
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("CheckUpdateAtStartup", mCheckUpdateAtStartup);
		editor.putBoolean("ViewTutorialAtStartup", mViewTutorialAtStartup);
		editor.putBoolean("DisplaySplashScreenAtStartup", mDisplaySplashScreenAtStartup);
		editor.putBoolean("GatherUsageData", mGatherUsageData);

		editor.commit();
	}
	

	public boolean getCheckUpdateAtStartup() {
		return mCheckUpdateAtStartup;
	}

	public void setCheckUpdateAtStartup(boolean mCheckUpdateAtStartup) {
		this.mCheckUpdateAtStartup = mCheckUpdateAtStartup;		
		saveAllOptions();
	}
	
	public boolean getViewTutorialAtStartup() {
		return mViewTutorialAtStartup;
	}

	public void setViewTutorialAtStartup(boolean mViewTutorialAtStartup) {
		this.mViewTutorialAtStartup = mViewTutorialAtStartup;
		saveAllOptions();
	}

	public void setDisplaySplashScreenAtStartup(
			boolean mDisplaySplashScreenAtStartup) {
		this.mDisplaySplashScreenAtStartup = mDisplaySplashScreenAtStartup;
		saveAllOptions();
	}

	public boolean getDisplaySplashScreenAtStartup() {
		return mDisplaySplashScreenAtStartup;
	}

	public void setGatherUsageData(boolean mGatherUsageData) {
		this.mGatherUsageData = mGatherUsageData;
	}

	public boolean getGatherUsageData() {
		return mGatherUsageData;
	}

}
