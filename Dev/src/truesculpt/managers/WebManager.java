package truesculpt.managers;

import java.util.ArrayList;

import truesculpt.parser.WebLibraryParser;
import android.content.Context;
import android.os.Handler;

public class WebManager extends BaseManager 
{
	private Handler mHandler = new Handler();
	WebLibraryParser mWebParser=new WebLibraryParser();
	ArrayList mEntries=null;
	
	public ArrayList getWebEntries() 
	{
		return mEntries;
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
			mEntries=WebLibraryParser.getWebLibrary();
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

}
