package truesculpt.utils;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Utils {

	public static float DegToRad(float deg) {
		return (float) (deg * Math.PI / 180.0f);
	}
	
	//in degrees, return x,y,z in vector
	public static Vector<Float> PolToCart(float theta, float phi, float psi, float R)
	{
		Vector<Float> res= new Vector<Float>(3);
	
		float x = (float) (R * Math.cos(DegToRad(theta)) * Math.cos(DegToRad(phi)));
		float y = (float) (R * Math.sin(DegToRad(theta)) * Math.cos(DegToRad(phi)));
		float z = (float) (R * Math.sin(DegToRad(phi)));
		
		res.add(x);
		res.add(y);
		res.add(z);
		
		return res;
	}
	
	public static float Distance(Vector<Float> pt1, Vector<Float> pt2)
	{
		float dRes=0.0f;
		int n=pt1.size();
		if (n==pt2.size())
		{
			for (int i = 0; i < n; i++) {
				float val1=pt1.get(i);
				float val2=pt2.get(i);
				
				dRes+=Math.pow((val2-val1),2);
			}			
		}
		return dRes;		
	}
	
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
