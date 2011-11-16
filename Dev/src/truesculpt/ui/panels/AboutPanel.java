package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

		getManagers().getUtilsManager().updateFullscreenWindowStatus(getWindow());

		getManagers().getUsageStatisticsManager().TrackPageView("/AboutPanel");

		setContentView(R.layout.about);

		final String strWebSiteURL = "http://code.google.com/p/truesculpt";
		TextView aboutText = (TextView) findViewById(R.id.about_text);
		String aboutMsg = getString(R.string.about_text) + "\n" + getString(R.string.Website) + " : " + "\n" + strWebSiteURL;
		aboutText.setText(aboutMsg);
		Linkify.addLinks(aboutText, Linkify.ALL);

		// final Button buttonDonate = (Button) findViewById(R.id.donateBtn);
		// buttonDonate.setOnClickListener(new View.OnClickListener()
		// {
		// @Override
		// public void onClick(View v)
		// {
		// Utils.ShowURLInBrowser(AboutPanel.this, strWebSiteURL);
		// }
		// });

		// final Button buttonTutorial = (Button) findViewById(R.id.tutorials);
		// buttonTutorial.setOnClickListener(new View.OnClickListener()
		// {
		// @Override
		// public void onClick(View v)
		// {
		// finish();
		// Utils.StartMyActivity(AboutPanel.this, truesculpt.ui.panels.TutorialOverlayPanel.class, false);
		// }
		// });

		final Button buttonHelp = (Button) findViewById(R.id.help);
		buttonHelp.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Utils.StartMyActivity(AboutPanel.this, truesculpt.ui.panels.TutorialWizardPanel.class, false);
			}
		});

		TextView currVersionText = (TextView) findViewById(R.id.current_version);
		String strCurrVersion = getManagers().getUpdateManager().getCurrentVersion();
		String msg = getString(R.string.current_version_is_) + " " + strCurrVersion + " \n";
		currVersionText.setText(msg);
		Linkify.addLinks(currVersionText, Linkify.ALL);

		TextView licenseText = (TextView) findViewById(R.id.licence);
		Linkify.addLinks(licenseText, Linkify.ALL);
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu)
	// {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.about, menu);
	// return true;
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item)
	// {
	// switch (item.getItemId())
	// {
	// case R.id.show_debug_panel:
	// {
	// Utils.StartMyActivity(this, truesculpt.ui.panels.DebugPanel.class, false);
	// return true;
	// }
	// case R.id.show_check_version_panel:
	// {
	// Utils.StartMyActivity(this, truesculpt.ui.panels.UpdatePanel.class, false);
	// return true;
	// }
	//
	// default:
	// return super.onOptionsItemSelected(item);
	// }
	// }
	//
	// @Override
	// public boolean onTouchEvent(MotionEvent event)
	// {
	// // finish();
	// return super.onTouchEvent(event);
	// }

}
