package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import android.os.Bundle;
import android.preference.PreferenceActivity;

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

}
