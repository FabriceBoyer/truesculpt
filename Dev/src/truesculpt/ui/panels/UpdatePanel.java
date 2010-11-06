package truesculpt.ui.panels;

import truesculpt.main.R;
import truesculpt.main.TrueSculpt;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class UpdatePanel extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);

		TrueSculpt parent = (TrueSculpt) getParent();// .getClass().cast(Class.forName("TrueSculpt"));
		String msg = parent.getManagers().getmUpdateManager().getUpdateStatus();
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

		// Launch associated web page
		// String lastestURL = strLatestVersion.replace(".", "_");
		// lastestURL =
		// "https://code.google.com/p/truesculpt/downloads/detail?name=TrueSculpt_"
		// + lastestURL + ".apk";

		String UpdateUrl = "http://code.google.com/p/truesculpt/downloads/list?can=3";// featured
																						// download
		Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(UpdateUrl));
		startActivity(myIntent);
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
