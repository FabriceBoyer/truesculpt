package truesculpt.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
		ColorIntToFloatVector(color, VColor);
		res = "(" + Integer.toString((int) (VColor[0] * 255)) + "," + Integer.toString((int) (VColor[1] * 255)) + "," + Integer.toString((int) (VColor[2] * 255)) + ")";
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

	public static void parseIntTriple(String face, int[] res)
	{
		int ix = face.indexOf("/");
		if (ix == -1)
		{
			res[0] = Integer.parseInt(face) - 1;
		}
		else
		{
			int ix2 = face.indexOf("/", ix + 1);
			if (ix2 == -1)
			{
				res[0] = Integer.parseInt(face.substring(0, ix)) - 1;
				res[1] = Integer.parseInt(face.substring(ix + 1)) - 1;
			}
			else
			{
				res[0] = parseInt(face.substring(0, ix)) - 1;
				res[1] = parseInt(face.substring(ix + 1, ix2)) - 1;
				res[2] = parseInt(face.substring(ix2 + 1)) - 1;
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

	public static void SendEmail(Context context, String emailTo, String emailCC, String subject, String emailText, List<String> filePaths)
	{
		// need to "send multiple" to get more than one attachment
		final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { emailTo });
		emailIntent.putExtra(android.content.Intent.EXTRA_CC, new String[] { emailCC });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailText);
		emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// has to be an ArrayList
		ArrayList<Uri> uris = new ArrayList<Uri>();
		// convert from paths to Android friendly Parcelable Uri's
		for (String file : filePaths)
		{
			File fileIn = new File(file);
			Uri u = Uri.fromFile(fileIn);
			uris.add(u);
		}
		emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

		Intent intent = Intent.createChooser(emailIntent, "Share your sculpture");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
}
