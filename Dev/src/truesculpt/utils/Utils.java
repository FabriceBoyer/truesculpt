package truesculpt.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;

public class Utils
{
	public static void ColorIntToFloatVector(int color, float[] VColor)
	{
		VColor[0] = Color.red(color) / 255.0f;
		VColor[1] = Color.green(color) / 255.0f;
		VColor[2] = Color.blue(color) / 255.0f;
		VColor[3] = 1.0f;
	}

	public static void ShowURLInBrowser(Activity callingACtivity, String strURL)
	{
		if (callingACtivity != null)
		{
			Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strURL));
			callingACtivity.startActivity(myIntent);
		}
	}

	public static void StartMyActivity(Context callingContext, Class<?> cls, boolean bNewTask)
	{
		if (callingContext != null)
		{
			Intent myIntent = new Intent(callingContext, cls);
			if (bNewTask)
			{
				myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}

			callingContext.startActivity(myIntent);
		}
	}

}
