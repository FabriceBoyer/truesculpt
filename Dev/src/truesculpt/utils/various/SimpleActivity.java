package truesculpt.utils.various;

import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;

/**
 * SimpleActivity is a subclass of Activity that makes it trivial to create a sub-Activity, and handle it's results (ok or cancel). No need to deal with requestCodes, since this class handles creating correlationIds automatically.
 * 
 * @author Nazmul Idris
 * @version 1.0
 * @since Jul 3, 2008, 12:08:46 PM
 */
public class SimpleActivity extends Activity
{

	/**
	 * ResultCallbackIF is a simple interface that you have to implement to handle results - ok or cancel from a sub-Activity.
	 * 
	 * @author Nazmul Idris
	 * @version 1.0
	 * @since Jul 3, 2008, 12:11:31 PM
	 */
	public static interface ResultCallbackIF
	{

		public void resultCancel(Intent data);

		public void resultOk(Intent data);

	}// end interface ResultCallbackIF

	/** holds the map of callbacks */
	protected HashMap<Integer, ResultCallbackIF> _callbackMap = new HashMap<Integer, ResultCallbackIF>();

	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	// fade out animation support when activity is finished
	// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	protected View rootView;

	/**
	 * use this method to launch the sub-Activity, and provide a functor to handle the result - ok or cancel
	 */
	public void launchSubActivity(Class subActivityClass, ResultCallbackIF callback)
	{

		Intent i = new Intent(this, subActivityClass);

		Random rand = new Random();
		int correlationId = rand.nextInt();

		_callbackMap.put(correlationId, callback);

		startActivityForResult(i, correlationId);

	}

	/**
	 * this is the underlying implementation of the onActivityResult method that handles auto generation of correlationIds and adding/removing callback functors to handle the result
	 */
	@Override
	protected void onActivityResult(int correlationId, int resultCode, Intent data)
	{

		try
		{
			ResultCallbackIF callback = _callbackMap.get(correlationId);

			switch (resultCode)
			{
			case Activity.RESULT_CANCELED:
				Log.i(Global.TAG3, this.getClass().getSimpleName() + " - Activity returned result [CANCEL]:" + data.toString());
				callback.resultCancel(data);
				_callbackMap.remove(correlationId);
				break;
			case Activity.RESULT_OK:
				Log.i(Global.TAG3, this.getClass().getSimpleName() + " - Activity returned result [OK]:" + data.toString());
				callback.resultOk(data);
				_callbackMap.remove(correlationId);
				break;
			default:
				Log.e(Global.TAG3, "Couldn't find callback handler for correlationId");
			}
		} catch (Exception e)
		{
			Log.e(Global.TAG3, "Problem processing result from sub-activity", e);
		}

	}

	@Override
	public boolean onKeyDown(int i, KeyEvent event)
	{

		// only intercept back button press
		if (i == KeyEvent.KEYCODE_BACK)
		{
			if (rootView != null)
			{
				runFadeOutAnimAndFinish(i, event);
				return true; // consume this keyevent
			} else
			{
				super.onKeyDown(i, event);
				return true;
			}
		}

		return false; // propagate this keyevent
	}

	/**
	 * simply runs a fadeout anim on the {@link #rootView}, and then call the super class's implementation of the back button press eventhandler.
	 */
	public void runFadeOutAnimAndFinish(final int i, final KeyEvent event)
	{

		if (rootView == null)
		{
			throw new IllegalArgumentException("rootView can not be null!");
		}

		// run an animation to fade this out...
		Animation animation = AnimUtils.runFadeOutAnimationOn(this, rootView);

		animation.setAnimationListener(new Animation.AnimationListener()
		{
			@Override
			public void onAnimationEnd(Animation animation)
			{

				if (i == 0 && event == null)
				{
					finish();
				} else
				{
					SimpleActivity.super.onKeyDown(i, event);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}

			@Override
			public void onAnimationStart(Animation animation)
			{
			}
		});

	}

	/**
	 * simply calls the {@link #runFadeOutAnimAndFinish(int, KeyEvent)} with some params that will cause it not to call the super class's implpementation of KEYCODE_BACK pressed
	 */
	public void runFadeOutAnimationAndFinish()
	{
		if (rootView == null)
		{
			throw new IllegalArgumentException("rootView can not be null!");
		}
		runFadeOutAnimAndFinish(0, null);
	}

	public void setRootViewToRunFinishAnimOn(View root)
	{
		rootView = root;
	}

}// end class SimpleActivity
