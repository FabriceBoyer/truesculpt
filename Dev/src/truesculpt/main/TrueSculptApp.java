package truesculpt.main;

import android.app.Application;

public class TrueSculptApp extends Application {

	private ManagersManager mManagers = new ManagersManager();
		
	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {		
		super.onCreate();
		
		mManagers.Init(getBaseContext());
	}

	public ManagersManager getManagers() {
		return mManagers;
	}
}
