package truesculpt.ui.panels;

import truesculpt.main.ManagersManager;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class OptionsPanel extends PreferenceActivity {

	/* (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//must be in same package than manager ?
		addPreferencesFromResource(R.xml.options);
	}

	/* (non-Javadoc)
	 * @see android.preference.PreferenceActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {		
		super.onDestroy();
		
	}
	
	public ManagersManager getManagers() {	
		return ((TrueSculptApp)getApplicationContext()).getManagers();
	}

}
