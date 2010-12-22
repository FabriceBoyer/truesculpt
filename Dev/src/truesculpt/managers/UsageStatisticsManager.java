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

	long startTime = System.currentTimeMillis();
	
	@Override
	public void onCreate()
	{
		if (getManagers().getOptionsManager().getGatherUsageData() == true)
		{
			tracker = GoogleAnalyticsTracker.getInstance();
			tracker.start("UA-18915484-3", 300 , getbaseContext());		
			
			startTime = System.currentTimeMillis();
			
			TrackEvent("AppSession", "StartCount",0); 
		}
	}

	@Override
	public void onDestroy()
	{
		long stopTime = System.currentTimeMillis();
		int duration=(int) (stopTime-startTime);
		
		TrackEvent("AppSession", "Duration",duration);        
		
		tracker.stop();
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
