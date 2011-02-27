package truesculpt.managers;

import java.util.Observable;

import truesculpt.main.Managers;
import truesculpt.main.TrueSculptApp;
import android.content.Context;
import android.os.Handler;

public abstract class BaseManager extends Observable
{
	private Context mbaseContext;

	public BaseManager(Context baseContext)
	{
		super();
		this.mbaseContext = baseContext;
	}

	public Handler mHandler = new Handler();
	public Runnable mNotifyTask = new Runnable()
	{
		@Override
		public void run()
		{
			notifyObservers(this);
		}
	};

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

	public void NotifyListeners()
	{
		setChanged();
		mHandler.post(mNotifyTask);
	}

}
