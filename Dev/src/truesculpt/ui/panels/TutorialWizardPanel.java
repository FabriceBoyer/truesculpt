package truesculpt.ui.panels;

import truesculpt.main.R;
import truesculpt.main.TrueSculpt;
import truesculpt.utils.ResourceUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug.IntToString;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.SeekBar;

public class TutorialWizardPanel extends Activity {

	private class MyWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        view.loadUrl(url);
	        return true;
	    }	 
	}
	
	private WebView mWebView=null;
	private Button prevBtn;
	private Button nextBtn;
	private Button finishBtn;
	
	private int mStepsCount=5;
	private int mStepCurrentIndex=0;
	
	private final int DIALOG_SEE_WIZARD_AGAIN_ID=0;
	
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
				           public void onClick(DialogInterface dialog, int id) {	
				        	   setSeeAgainOption(true);
				        	   finish();
				           }
				       })
				       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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

	public void setSeeAgainOption(boolean bSeeAgain)
	{
		TrueSculpt.getManagers().getmOptionsManager().setViewTutorialAtStartup(bSeeAgain);		
	}
	
	public boolean getSeeAgainOption()
	{
		return TrueSculpt.getManagers().getmOptionsManager().getViewTutorialAtStartup();
	}
	
	private void RefreshView()
	{		
		  final String mimetype = "text/html";
		  final String encoding = "UTF-8";
		  String htmldata = "<html><body>boo</body></html>";

		  {
		    String data = ResourceUtils.loadResToString(R.raw.tutorial1, this);
		    if (data != null) htmldata = data;
		  }

		  mWebView.loadData(htmldata,
		               mimetype,
		               encoding);


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

}
