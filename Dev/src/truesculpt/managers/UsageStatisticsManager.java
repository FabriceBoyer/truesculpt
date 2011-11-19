package truesculpt.managers;

import android.content.Context;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class UsageStatisticsManager extends BaseManager
{

	private GoogleAnalyticsTracker tracker = null;

	long startTime = 0;

	public UsageStatisticsManager(Context baseContext)
	{
		super(baseContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate()
	{
		startTime = System.currentTimeMillis();

		restart();

		TrackEvent("AppSession", "Count", 0);
	}

	@Override
	public void onDestroy()
	{
		long stopTime = System.currentTimeMillis();
		int duration = (int) (stopTime - startTime);

		TrackEvent("AppSession", "Duration", duration);

		stop();
	}

	public void restart()
	{
		stop();
		start();
	}

	private void start()
	{
		if (getManagers().getOptionsManager().getGatherUsageData() == true)
		{
			tracker = GoogleAnalyticsTracker.getInstance();
			tracker.startNewSession("UA-18915484-3", 20, getbaseContext());
		}
	}

	private void stop()
	{
		if (tracker != null)
		{
			tracker.dispatch();
			tracker.stopSession();
			tracker = null;
		}
	}

	public void TrackEvent(String action, String label, int value)
	{
		if (tracker != null)
		{
			tracker.trackEvent("Truesculpt", action, label, value);
		}
	}

	public void TrackPageView(String pageName)
	{
		if (tracker != null)
		{
			tracker.trackPageView(pageName);
		}
	}

}
