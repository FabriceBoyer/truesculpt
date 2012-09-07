package truesculpt.managers;

import java.util.ArrayList;

import truesculpt.parser.WebEntry;
import truesculpt.parser.WebLibraryParser;
import android.content.Context;
import android.os.Handler;

public class WebManager extends BaseManager 
{
	private Handler mHandler = new Handler();
	ArrayList<WebEntry> mEntries=null;
	
	public ArrayList<WebEntry> getWebEntries() 
	{
		synchronized (this) 
		{
			return mEntries;
		}
	}

	public WebManager(Context baseContext) 
	{
		super(baseContext);		
	}
	
	Runnable mGetWebLibraryTask = new Runnable()
	{
		@Override
		public void run()
		{
			synchronized (this) 
			{
				mEntries=WebLibraryParser.getWebLibrary();
			}
			
		}	
	};

	@Override
	public void onCreate() 
	{		
		mHandler.post(mGetWebLibraryTask);
	}

	@Override
	public void onDestroy() 
	{		

	}
	
	public static String GetBaseWebLibraryAdress()
	{
		return "http://truesculpt-hrd.appspot.com/";
	}

}
