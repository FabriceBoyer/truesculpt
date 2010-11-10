package truesculpt.main;

import android.app.Application;

public class TrueSculptApp extends Application {

	private Managers mManagers = new Managers();
		
	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {		
		super.onCreate();
		
		mManagers.Init(getBaseContext());
	}

	public Managers getManagers() {
		return mManagers;
	}
}
