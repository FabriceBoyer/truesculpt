package com.TrueSculpt.UI;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class TutorialWizard extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		 WebView webview = new WebView(this);
		 setContentView(webview);
		 
		 webview.loadUrl("http://slashdot.org/");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
