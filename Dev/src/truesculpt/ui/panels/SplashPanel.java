package truesculpt.ui.panels;

import truesculpt.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

//TODO : callback notification to dismiss only when the rest of the app is ready

public class SplashPanel extends Activity {

	// time in milliseconds
	private static final long SPLASHTIME = 2000;

	private ImageView mSplash;

	// handler for splash screen
	private Handler mHandler = new Handler();

	private Runnable mCloseActivityTask = new Runnable() {
		public void run() {
			finish();
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		mSplash = new ImageView(this);
		setContentView(mSplash);

		mSplash.setImageResource(R.drawable.flash);

		mHandler.postDelayed(mCloseActivityTask, SPLASHTIME);
	}
}
