package truesculpt.managers;

import android.content.Context;

//open and save a file, autosave?
public class FileManager extends BaseManager
{

	private String mLastUsedFile = "";// MRU

	public FileManager(Context baseContext)
	{
		super(baseContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub

	}

}
