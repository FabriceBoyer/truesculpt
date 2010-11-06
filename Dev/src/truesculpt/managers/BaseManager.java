package truesculpt.managers;

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

}
