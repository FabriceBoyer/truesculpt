package truesculpt.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Utils {

	public static void StartMyActivity(Activity callingACtivity,
			Class<?> cls) {
		if (callingACtivity != null) {
			boolean bSuccess = true;
			String msg = "";

			// Intent startIntent = new Intent(callingACtivity,
			// ChildActivity.class);
			// getLocalActivityManager().startActivity("and.mypackage.ChildActivity",
			// startIntent);

			Intent myIntent = new Intent(callingACtivity,cls);
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
	
	
	public static void ShowURLInBrowser(Activity callingACtivity,
			String strURL)
	{
		if (callingACtivity != null) 
		{
			Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strURL));
			callingACtivity.startActivity(myIntent);
		}
	}
}
