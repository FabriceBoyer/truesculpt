package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;

//TODO : callback notification to dismiss only when the rest of the app is ready

public class SplashPanel extends Activity
{

	// time in milliseconds
	private static final long SPLASHTIME = 5000;

	private final Runnable mCloseActivityTask = new Runnable()
	{
		@Override
		public void run()
		{
			finish();
		}
	};

	// handler for splash screen
	private final Handler mHandler = new Handler();

	private ImageView mSplash;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);

		getManagers().getUtilsManager().updateFullscreenWindowStatus(getWindow());

		mSplash = new ImageView(this);
		setContentView(mSplash);

		mSplash.setImageResource(R.drawable.splash);

		mHandler.postDelayed(mCloseActivityTask, SPLASHTIME);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		finish();
		return super.onTouchEvent(event);
	}

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

}
