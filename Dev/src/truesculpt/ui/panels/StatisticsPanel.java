package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.TrueSculptApp;
import android.app.Activity;
import android.os.Bundle;

//about mesh, cpu , fps,... not only debug data
public class StatisticsPanel extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}
}
