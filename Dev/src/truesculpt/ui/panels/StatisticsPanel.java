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

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy()
	{

		super.onDestroy();
	}

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}
}
