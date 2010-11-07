package truesculpt.utils;

import android.app.*;
import android.content.*;
import android.util.*;

import java.io.*;

/**
 * ResourceUtils makes it easier to access compiled resources.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Jun 24, 2008, 6:45:15 PM
 */
public class ResourceUtils {

public static String loadResToString(int resId, Activity ctx) {

  try {
    InputStream is = ctx.getResources().openRawResource(resId);

    byte[] buffer = new byte[4096];
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    while (true) {
      int read = is.read(buffer);

      if (read == -1) {
        break;
      }

      baos.write(buffer, 0, read);
    }

    baos.close();
    is.close();

    String data = baos.toString();

    Log.i(Global.TAG, "ResourceUtils loaded resource to string:" + data);

    return data;
  }
  catch (Exception e) {
    Log.e(Global.TAG, "ResourceUtils failed to load resource to string", e);
    return null;
  }

}

public static int getResourceIdForDrawable(Context _context, String resPackage, String resName) {
  return _context.getResources().getIdentifier(
      resName,
      "drawable",
      resPackage
  );
}

}//end class ResourceUtils
