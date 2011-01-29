package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class AboutPanel extends Activity
{

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getManagers().getUsageStatisticsManager().TrackPageView("/AboutPanel");

		setContentView(R.layout.about);

		final Button button = (Button) findViewById(R.id.Ok_btn);
		button.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.about, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.show_debug_panel:
		{
			Utils.StartMyActivity(this, truesculpt.ui.debug.DebugPanel.class, false);
			return true;
		}
		case R.id.show_check_version_panel:
		{
			Utils.StartMyActivity(this, truesculpt.ui.panels.UpdatePanel.class, false);
			return true;
		}
		case R.id.show_tutorial_wizard_panel:
		{
			Utils.StartMyActivity(this, truesculpt.ui.panels.TutorialWizardPanel.class, false);
			return true;
		}

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		finish();
		return super.onTouchEvent(event);
	}

}
