package truesculpt.ui.panels;

import truesculpt.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class TutorialWizardPanel extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tutorialwizard);

		WebView webview = new WebView(this);
		setContentView(webview);

		webview.loadUrl("http://slashdot.org/");
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
