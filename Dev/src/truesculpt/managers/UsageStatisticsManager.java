package truesculpt.managers;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.content.Context;

public class UsageStatisticsManager extends BaseManager
{
	
	private GoogleAnalyticsTracker tracker=null;

	public UsageStatisticsManager(Context baseContext)
	{
		super(baseContext);
		// TODO Auto-generated constructor stub
	}

	long startTime = 0;
	
	@Override
	public void onCreate()
	{
		startTime = System.currentTimeMillis();
		
		restart();
		
		TrackEvent("AppSession", "Count",0); 
	}

	@Override
	public void onDestroy()
	{
		long stopTime = System.currentTimeMillis();
		int duration=(int) (stopTime-startTime);
		
		TrackEvent("AppSession", "Duration",duration);        
		
		stop();		
	}
	
	private void start()
	{
		if (getManagers().getOptionsManager().getGatherUsageData() == true)
		{
			tracker = GoogleAnalyticsTracker.getInstance();
			tracker.start("UA-18915484-3", 300 , getbaseContext());		
		}
	}
	
	private void stop()
	{
		if (tracker!=null)
		{
			tracker.dispatch();
			tracker.stop();
			tracker=null;
		}
	}
	
	public void restart()
	{
		stop();
		start();
	}

	public void TrackEvent(String action, String label, int value)
	{
		if (tracker!=null)
		{
			tracker.trackEvent("Truesculpt",action,label,value);
		}
	}
	
	public void TrackPageView(String pageName)
	{
		if (tracker!=null)
		{
			tracker.trackPageView(pageName);
		}
	}
	
}
