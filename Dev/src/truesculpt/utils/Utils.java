package truesculpt.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Utils {

	public static void StartMyActivity(Activity callingActivity,
			Class<?> cls) {
		if (callingActivity != null) {
			boolean bSuccess = true;
			String msg = "";

			// Intent startIntent = new Intent(callingACtivity,
			// ChildActivity.class);
			// getLocalActivityManager().startActivity("and.mypackage.ChildActivity",
			// startIntent);

			Intent myIntent = new Intent(callingActivity,cls);			
			try {
				callingActivity.startActivity(myIntent);
			} catch (Exception e) {
				msg = e.getMessage();
				bSuccess = false;
			}

			if (!bSuccess) {
				Toast.makeText(callingActivity, msg, Toast.LENGTH_LONG);
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
