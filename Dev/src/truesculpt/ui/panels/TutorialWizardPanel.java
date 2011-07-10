package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import truesculpt.ui.adapters.JavaScriptInterface;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class TutorialWizardPanel extends Activity
{

	private class MyWebViewClient extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			view.loadUrl(url);
			return true;
		}
	}

	private final int DIALOG_SEE_WIZARD_AGAIN_ID = 0;
	private WebView mWebView = null;

	//TODO use javascript interface to have a button inside , avoid showing everytime
	private void ExitConfirmation()
	{
		if (getSeeAgainOption() == true)
		{
			showDialog(DIALOG_SEE_WIZARD_AGAIN_ID);
		} else
		{
			finish();
		}
	}

	public Managers getManagers()
	{
		return ((TrueSculptApp) getApplicationContext()).getManagers();
	}

	public boolean getSeeAgainOption()
	{
		return getManagers().getOptionsManager().getViewTutorialAtStartup();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		getManagers().getUsageStatisticsManager().TrackPageView("/TutorialWizardPanel");

		setContentView(R.layout.tutorialwizard);

		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setWebViewClient(new MyWebViewClient());
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
	
		String strUrl = "file:///android_asset/tutorial0.html";
		mWebView.loadUrl(strUrl);
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		Dialog dialog = null;
		switch (id)
		{
		case DIALOG_SEE_WIZARD_AGAIN_ID:
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.this_tutorial_is_over_do_you_want_to_see_it_again_next_time_).setCancelable(false).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
					setSeeAgainOption(true);
					finish();
				}
			}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
					setSeeAgainOption(false);
					finish();
				}
			});
			dialog = builder.create();
			break;
		}
		default:
			dialog = null;
		}
		return dialog;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// Handle the back button
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			ExitConfirmation();
			return true;
		} 
		else
		{
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	public void setSeeAgainOption(boolean bSeeAgain)
	{
		getManagers().getOptionsManager().setViewTutorialAtStartup(bSeeAgain);
	}

}
