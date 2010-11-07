package truesculpt.managers;

import android.app.Activity;
import android.content.SharedPreferences;

public class OptionsManager extends BaseManager {

	public static final String PREFS_NAME = "MyPrefsFile";
	SharedPreferences settings = null;

	private boolean mCheckUpdateAtStartup = true;

	/**
	 * @return the mbCheckUpdateAtStartup
	 */
	public boolean getCheckUpdateAtStartup() {
		return mCheckUpdateAtStartup;
	}

	/**
	 * @param mCheckUpdateAtStartup
	 *            the mCheckUpdateAtStartup to set
	 */
	public void setCheckUpdateAtStartup(boolean mCheckUpdateAtStartup) {
		this.mCheckUpdateAtStartup = mCheckUpdateAtStartup;		
	}

	public OptionsManager(Activity mBaseActivity) {
		super(mBaseActivity);

		//PreferenceManager.setDefaultValues(getBaseActivity(),

	}
		
	public void onCreate()
	{
		// Restore preferences
		settings = getBaseActivity().getSharedPreferences(PREFS_NAME, 0);
	
		mCheckUpdateAtStartup = settings.getBoolean("CheckUpdateAtStartup",
				true);
	}

	public void showOptions() {

		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("CheckUpdateAtStartup", mCheckUpdateAtStartup);

		// Commit the edits!
		editor.commit();
	}

}
