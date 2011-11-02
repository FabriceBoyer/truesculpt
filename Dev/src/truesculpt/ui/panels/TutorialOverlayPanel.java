package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.TrueSculptApp;
import android.app.Activity;
import android.os.Bundle;

//news republic like tutorial with text, arrows and interest zone
//TODO XML to describe text position in percent and value, arrow positions, and cutout zone position and radius
//page suivante en clickant
public class TutorialOverlayPanel extends Activity
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
