package truesculpt.managers;

import truesculpt.main.Managers;
import truesculpt.main.TrueSculptApp;
import android.content.Context;

public class BaseManager {

	private Context mbaseContext;

	public BaseManager(Context baseContext) {
		super();
		this.mbaseContext = baseContext;
	}

	/**
	 * @return the mbaseContext
	 */
	public Context getbaseContext() {
		return mbaseContext;
	}

	public Managers getManagers() {	
		return ((TrueSculptApp)getbaseContext().getApplicationContext()).getManagers();
	}
	
	
	public void onCreate()
	{
		
	}
	
	public void onDestroy()
	{
		
	}
}
