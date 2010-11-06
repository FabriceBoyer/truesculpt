package truesculpt.managers;

import truesculpt.main.TrueSculpt;
import android.app.Activity;

public class BaseManager {

	private Activity mBaseActivity;

	public BaseManager(Activity baseActivity) {
		super();
		this.mBaseActivity = baseActivity;
	}

	/**
	 * @return the mBaseActivity
	 */
	public Activity getBaseActivity() {
		return mBaseActivity;
	}

	public ManagersManager getManagers() {
		TrueSculpt parent = (TrueSculpt) mBaseActivity;
		return parent.getManagers();
	}
}
