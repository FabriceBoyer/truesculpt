package truesculpt.utils;

import truesculpt.main.TrueSculptApp;
import truesculpt.ui.dialogs.HSLColorPickerDialog;
import truesculpt.ui.dialogs.HSLColorPickerDialog.OnAmbilWarnaListener;
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
	
	public static String ColorIntToString(int color)
	{
		String res;
		float[] VColor = new float[4];
		ColorIntToFloatVector(color,VColor);
		res="("+
		Integer.toString((int)(VColor[0]*255))+","+
		Integer.toString((int)(VColor[1]*255))+","+
		Integer.toString((int)(VColor[2]*255))+		
		")";
		return res;
	}
	
	protected static int parseInt(String val)
	{
		if (val.length() == 0)
		{
			return -1;
		}
		return Integer.parseInt(val);
	}

	public static int[] parseIntTriple(String face)
	{
		int ix = face.indexOf("/");
		if (ix == -1)
		{
			return new int[] { Integer.parseInt(face) - 1 };
		} else
		{
			int ix2 = face.indexOf("/", ix + 1);
			if (ix2 == -1)
			{
				return new int[] { Integer.parseInt(face.substring(0, ix)) - 1, Integer.parseInt(face.substring(ix + 1)) - 1 };
			} else
			{
				return new int[] { parseInt(face.substring(0, ix)) - 1, parseInt(face.substring(ix + 1, ix2)) - 1, parseInt(face.substring(ix2 + 1)) - 1 };
			}
		}
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
	
	public static void ShowHSLColorPickerDialog(Context context)
	{
		// initialColor is the initially-selected color to be shown in the rectangle on the left of the arrow.
		// for example, 0xff000000 is black, 0xff0000ff is blue. Please be aware of the initial 0xff which is the alpha.
		HSLColorPickerDialog dialog = new HSLColorPickerDialog(context, ((TrueSculptApp)(context.getApplicationContext())).getManagers().getToolsManager().getColor(), new OnAmbilWarnaListener()
		{
			@Override
			public void onCancel(HSLColorPickerDialog dialog)
			{
				// cancel was selected by the user
			}

			@Override
			public void onOk(HSLColorPickerDialog dialog, int color)
			{
				((TrueSculptApp)(dialog.getContext().getApplicationContext())).getManagers().getToolsManager().setColor(color);
			}
		});

		dialog.show();
	}

}
