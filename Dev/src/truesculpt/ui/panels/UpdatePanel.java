package truesculpt.ui.panels;

import truesculpt.main.R;
import truesculpt.main.TrueSculpt;
import truesculpt.utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UpdatePanel extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);

		TrueSculpt parent = (TrueSculpt) getParent();// .getClass().cast(Class.forName("TrueSculpt"));
		String msg ="";
		if (parent!=null)
		{
			parent.getManagers().getmUpdateManager().getUpdateStatus();			
		}

		final TextView text = (Button) findViewById(R.id.UpdateStatusText);
		text.setText(msg);	//Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

		// Launch associated web page
		// String lastestURL = strLatestVersion.replace(".", "_");
		// lastestURL =
		// "https://code.google.com/p/truesculpt/downloads/detail?name=TrueSculpt_"
		// + lastestURL + ".apk";

		//featured download
		String strUpdateUrl = "http://code.google.com/p/truesculpt/downloads/list?can=3";
		Utils.ShowURLInBrowser(this,strUpdateUrl);
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
