package truesculpt.utils;

import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Utils {

	//returns R, theta, phi in degrees
	public static Vector<Float> CartToPol(float x, float y, float z)
	{
		Vector<Float> res= new Vector<Float>(3);
	
		Vector<Float> vec= new Vector<Float>(3);
		vec.add(x);
		vec.add(y);
		vec.add(z);
		float R = Length(vec);
		float theta = (float) ( Math.asin(x/R) );
		float phi = (float) ( Math.acos(y/R) );		
		
		res.add(R);
		res.add(theta);
		res.add(phi);
		
		return res;
	}
	
	public static float DegToRad(float deg) {
		return deg * (float)Math.PI / 180.0f;
		//return (float) Math.toRadians(deg);
	}
	
	public static float Distance(Vector<Float> pt1, Vector<Float> pt2)
	{		
		float dRes=0.0f;
		
		Vector<Float> diff=Substract(pt1,pt2);
		dRes=Length(diff);
		
		return dRes;		
	}
	
	public static Vector<Float> DivideBy(Vector<Float> pt, float value) 
	{
		Vector<Float> res=new Vector<Float>(pt);
		int n=pt.size();
		
		if( value!=0.0f)
		{
			for (int i = 0; i < n; i++) {
				float val=pt.get(i);				
				
				res.set(i,val/value);
			}			
		}
		return res;	
	}
	
	public static float Dot(Vector<Float> pt1, Vector<Float> pt2)
	{
		float dRes=0.0f;
		int n=pt1.size();
		if (n==pt2.size())
		{
			for (int i = 0; i < n; i++) {
				float val1=pt1.get(i);
				float val2=pt2.get(i);
				
				dRes+=val2*val1;
			}			
		}
		return dRes;		
	}
	
	public static float Length(Vector<Float> pt)
	{
		float dRes=0.0f;
		
		dRes=(float)Math.sqrt(Dot(pt,pt));
		
		return dRes;
	}
	
	public static Vector<Float> Normalize(Vector<Float> pt)
	{
		float dLength=Length(pt);
		
		Vector<Float> res=DivideBy(pt,dLength);
		return res;
	}
	
	//in degrees, return x,y,z in vector
	public static Vector<Float> PolToCart( float R, float theta, float phi)
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
			Class<?> cls) {
		if (callingContext != null) {
			boolean bSuccess = true;
			String msg = "";

			// Intent startIntent = new Intent(callingACtivity,
			// ChildActivity.class);
			// getLocalActivityManager().startActivity("and.mypackage.ChildActivity",
			// startIntent);

			Intent myIntent = new Intent(callingContext,cls);			
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
	
	
	public static Vector<Float> Substract(Vector<Float> pt1, Vector<Float> pt2) 
	{
		Vector<Float> res=new Vector<Float>(pt2);
		int n=pt1.size();
		if (n==pt2.size())
		{
			for (int i = 0; i < n; i++) {
				float val1=pt1.get(i);
				float val2=pt2.get(i);
				
				res.set(i, val1-val2);
			}			
		}
		return res;	
	}
}
