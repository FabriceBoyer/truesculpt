package truesculpt.managers;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.content.Context;

public class UsageStatisticsManager extends BaseManager
{
	
	private GoogleAnalyticsTracker tracker;

	public UsageStatisticsManager(Context baseContext)
	{
		super(baseContext);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate()
	{
		if (getManagers().getOptionsManager().getGatherUsageData() == true)
		{
			tracker = GoogleAnalyticsTracker.getInstance();
			tracker.start("UA-18915484-3", 20 , getbaseContext()); 		
		}
	}

	@Override
	public void onDestroy()
	{
		tracker.stop();
	}


	public GoogleAnalyticsTracker getTracker()
	{
		return tracker;
	}
	
}
