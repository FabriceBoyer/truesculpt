package truesculpt.managers;

import java.util.Observable;

import truesculpt.main.Managers;
import truesculpt.main.TrueSculptApp;
import android.content.Context;


public abstract class BaseManager extends Observable
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
	public Context getbaseContext()
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
