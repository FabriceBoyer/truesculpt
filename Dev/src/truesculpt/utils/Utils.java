package truesculpt.utils;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class Utils {

	public static void StartMyActivity(Activity callingACtivity,
			String strClassName) {
		if (callingACtivity != null) {
			boolean bSuccess = true;
			String msg = "";

			// Intent startIntent = new Intent(callingACtivity,
			// ChildActivity.class);
			// getLocalActivityManager().startActivity("and.mypackage.ChildActivity",
			// startIntent);

			Intent myIntent = new Intent();
			myIntent.setClassName(callingACtivity, strClassName);
			try {
				callingACtivity.startActivity(myIntent);
			} catch (Exception e) {
				msg = e.getMessage();
				bSuccess = false;
			}

			if (!bSuccess) {
				Toast.makeText(callingACtivity, msg, Toast.LENGTH_LONG);
			}
		}
	}
}
