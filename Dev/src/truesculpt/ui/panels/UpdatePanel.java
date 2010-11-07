package truesculpt.ui.panels;

import truesculpt.main.R;
import truesculpt.main.TrueSculpt;
import truesculpt.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UpdatePanel extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);
		
		final Button button = (Button) findViewById(R.id.Ok_btn);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();				
			}
		});

		String strCurrVersion = TrueSculpt.getManagers().getmUpdateManager().getCurrentVersion();
		String strLatestVersion = TrueSculpt.getManagers().getmUpdateManager().getLatestVersion();
		
		boolean bIsUpdateNeeded= TrueSculpt.getManagers().getmUpdateManager().getIsUpdateNeeded(strCurrVersion,strLatestVersion);			

		String msg = getString(R.string.current_version_is_) + " " + strCurrVersion + " \n" 
		+ getString(R.string.latest_version_is_) + " "	+ strLatestVersion + " \n";
		
		//if (bIsBeta) {
		//	msg += getString(R.string.this_version_is_a_beta_);
		//} else {
		//	msg += getString(R.string.this_version_is_not_a_beta_);
		//}
		
		if (bIsUpdateNeeded) {
			msg += getString(R.string.an_update_is_needed_);
		} else {
			msg += getString(R.string.no_update_is_needed_);
		}

		final TextView text = (TextView) findViewById(R.id.UpdateStatusText);
		text.setText(msg);

		if (bIsUpdateNeeded)
		{
			// Launch associated web page
			 String strLastestURL = strLatestVersion.replace(".", "_");
			 strLastestURL = "https://code.google.com/p/truesculpt/downloads/detail?name=TrueSculpt_"
			 + strLastestURL + ".apk";
	
			//featured download
			//String strUpdateUrl = "http://code.google.com/p/truesculpt/downloads/list?can=3";
			 
			Utils.ShowURLInBrowser(this,strLastestURL);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
