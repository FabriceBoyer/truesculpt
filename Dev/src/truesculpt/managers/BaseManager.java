package truesculpt.managers;

import truesculpt.main.Managers;
import truesculpt.main.TrueSculptApp;
import android.content.Context;

public abstract class BaseManager
{

	private Context mbaseContext;

	public BaseManager(Context baseContext)
	{
		super();
		this.mbaseContext = baseContext;
	}

	/**
	 * @return the mbaseContext
	 */
	protected Context getbaseContext()
	{
		return mbaseContext;
	}

	protected Managers getManagers()
	{
		return ((TrueSculptApp) getbaseContext().getApplicationContext()).getManagers();
	}

	public abstract void onCreate();

	public abstract void onDestroy();

}
