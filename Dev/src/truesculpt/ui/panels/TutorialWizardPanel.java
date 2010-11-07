package truesculpt.ui.panels;

import truesculpt.main.R;
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
		showDialog(DIALOG_SEE_WIZARD_AGAIN_ID);		
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
				builder.setMessage(R.string.this_tutorial_is_over_dow_you_want_to_see_it_again_next_time_)
				       .setCancelable(false)
				       .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				              
				        	   finish();
				           }
				       })
				       .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   
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

	
	
	private void RefreshView()
	{
		if (mStepCurrentIndex>=(mStepsCount-1)) 
		{
			nextBtn.setText(R.string.finishBtn);			
		}
		else
		{
			nextBtn.setText(R.string.nextBtn);
		}
		
		String url="http://www.google.fr/"+Integer.toString(mStepCurrentIndex);
		//mWebView
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
