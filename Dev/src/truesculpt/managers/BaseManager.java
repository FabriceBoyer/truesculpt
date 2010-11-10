package truesculpt.managers;

import truesculpt.main.ManagersManager;
import truesculpt.main.TrueSculptApp;
import truesculpt.ui.panels.RendererMainPanel;
import android.app.Activity;
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

	public ManagersManager getManagers() {	
		return ((TrueSculptApp)getbaseContext().getApplicationContext()).getManagers();
	}
}
