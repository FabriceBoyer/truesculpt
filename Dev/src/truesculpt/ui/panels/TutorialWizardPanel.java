package truesculpt.ui.panels;

import truesculpt.main.Managers;
import truesculpt.main.R;
import truesculpt.main.TrueSculptApp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class TutorialWizardPanel extends Activity {

	private class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }	 
	}
	
	private final int DIALOG_SEE_WIZARD_AGAIN_ID=0;
	private Button finishBtn;
	private int mStepCurrentIndex=0;
	private int mStepsCount=4;
	
	private WebView mWebView=null;
	private Button nextBtn;
	
	private Button prevBtn;
	
	private void ExitConfirmation()
	{		
		if (getSeeAgainOption()==true)
		{
			showDialog(DIALOG_SEE_WIZARD_AGAIN_ID);
		}
		else
		{
			finish();
		}
	}
	
	public Managers getManagers() {	
		return ((TrueSculptApp)getApplicationContext()).getManagers();
	}
	
	public boolean getSeeAgainOption()
	{
		return getManagers().getmOptionsManager().getViewTutorialAtStartup();
	}
	
	private void GoToNextStep()
	{
		mStepCurrentIndex++;

		if (mStepCurrentIndex>=mStepsCount) 
		{
			mStepCurrentIndex=mStepsCount;
			
			ExitConfirmation();
		}
		
		RefreshView();
	}
	
	private void GoToPreviousStep()
	{
		mStepCurrentIndex--;
		if (mStepCurrentIndex<0) 
		{
			mStepCurrentIndex=0;
		}
		
		RefreshView();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tutorialwizard);

		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setWebViewClient(new MyWebViewClient());
		
		prevBtn= (Button) findViewById(R.id.prevBtn);
		prevBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GoToPreviousStep();
			}
		});
		
		nextBtn= (Button) findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GoToNextStep();
			}
		});
		
		finishBtn= (Button) findViewById(R.id.finishBtn);
		finishBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ExitConfirmation();
			}
		});
		
		RefreshView();	
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog=null;
		switch(id)
		{
			case DIALOG_SEE_WIZARD_AGAIN_ID:
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.this_tutorial_is_over_do_you_want_to_see_it_again_next_time_)
				       .setCancelable(false)
				       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				           @Override
						public void onClick(DialogInterface dialog, int id) {	
				        	   setSeeAgainOption(true);
				        	   finish();
				           }
				       })
				       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				           @Override
						public void onClick(DialogInterface dialog, int id) {
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
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void RefreshView()
	{		
		if (mStepCurrentIndex==(mStepsCount-1))
		{
			nextBtn.setEnabled(false);
		}
		else
		{
			nextBtn.setEnabled(true);
		}
		
		String strUrl="file:///android_asset/tutorial"+Integer.toString(mStepCurrentIndex)+".html";
		mWebView.loadUrl(strUrl);
	}
	
	public void setSeeAgainOption(boolean bSeeAgain)
	{
		getManagers().getmOptionsManager().setViewTutorialAtStartup(bSeeAgain);		
	}

}
