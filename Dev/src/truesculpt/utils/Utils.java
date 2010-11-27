package truesculpt.utils;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.widget.Toast;

public class Utils {

	//returns R, theta, phi in degrees
	public static float[] CartToPol(float[] vec)
	{
		float[] res= new float[3];
	
		float R = MatrixUtils.magnitude(vec);
		float theta = (float) ( Math.asin(vec[0]/R) );
		float phi = (float) ( Math.acos(vec[1]/R) );		
		
		res[0]=R;
		res[1]=theta;
		res[2]=phi;
		
		return res;
	}
	
	//in degrees, return x,y,z in vector
	public static float[] PolToCart( float R, float theta, float phi)
	{
		float[] res= new float[3];
	
		float x = (float) (R * Math.cos(Math.toRadians(theta) * Math.cos(Math.toRadians(phi))));
		float y = (float) (R * Math.sin(Math.toRadians(theta)) * Math.cos(Math.toRadians(phi)));
		float z = (float) (R * Math.sin(Math.toRadians(phi)));
		
		res[0]=x;
		res[1]=y;
		res[2]=z;
		
		return res;
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
	
	public static void StartMyActivity(Context callingContext,
			Class<?> cls, boolean bNewTask) {
		if (callingContext != null) {
			boolean bSuccess = true;
			String msg = "";

			// Intent startIntent = new Intent(callingACtivity,
			// ChildActivity.class);
			// getLocalActivityManager().startActivity("and.mypackage.ChildActivity",
			// startIntent);

			Intent myIntent = new Intent(callingContext,cls);		
			if (bNewTask) { myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);}
			try {
				callingContext.startActivity(myIntent);
			} catch (Exception e) {
				msg = e.getMessage();
				bSuccess = false;
			}

			if (!bSuccess) {
				Toast.makeText(callingContext, msg, Toast.LENGTH_LONG);
			}
		}
	}
	
	public static void ColorIntToFloatVector(int color, float[] VColor)
	{    	
    	VColor[0]=(float)Color.red(color)/255.0f;
    	VColor[1]=(float)Color.green(color)/255.0f;
    	VColor[2]=(float)Color.blue(color)/255.0f;
    	VColor[3]=1.0f;
	}
	
	

}
