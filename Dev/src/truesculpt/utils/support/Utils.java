package truesculpt.utils.support;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;

/**
 * <B>Utils</B> is a static function library to do mundane and repetitive, yet important functions
 * that are used everywhere in the system.
 * <p/>
 * <p/>
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since 2/26/2000,3:08pm
 */
public class Utils {

public static String arrayToString(Object[] args) {
  StringBuilder sb = new StringBuilder();

  sb.append("{number of entries=").append(args.length).append("}\n");

  int i = 0;

  for (Object obj : args) {
    if (obj != null) {
      sb.append("[").append(++i).append("] ").append(obj.toString()).append("\n");
    }
    else {
      sb.append("[").append(++i).append("] ").append("null").append("\n");
    }
  }

  return sb.toString();
}

//-------------------------------------------------------------------------------------------------------------------
// Methods
//-------------------------------------------------------------------------------------------------------------------

public static final String buildUri(String uri, HashMap<String, String> map)
    throws IllegalArgumentException {

  StringBuilder sb = new StringBuilder();

  sb.append(uri);

  int i = 0;

  if (map != null && !map.isEmpty()) {
    for (String key : map.keySet()) {
      if (i == 0) {
        sb.append("?");
      }
      else {
        sb.append("&");
      }

      sb.append(key);
      sb.append("=");
      sb.append(map.get(key));

      i++;
    }
  }

  return sb.toString();
}


public static String concatenateFilePath(String path1, String path2) {
  StringBuilder sb = new StringBuilder();
  sb.append(path1);
  sb.append(System.getProperty("file.separator"));
  sb.append(path2);
  return sb.toString();
}

/**
 * copies the object and returns it
 *
 * @param copythis this is the object to copy
 *
 * @return this is null if the object can't be copied for some reason
 */
@SuppressWarnings({"unchecked"})
public static <Type extends Serializable> Type copyObject(Type copythis) {
  Type retval = null;

  try {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(copythis);

    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
    retval = (Type) ois.readObject();

    baos.close();
    oos.close();
    ois.close();
  }
  catch (Exception e) {
    return null;
  }

  return retval;
}

/** this method reads a file and returns a ByteBuffer of the contents of this file. */
public static ByteBuffer fileToByteBuffer(String fileName) {
  //System.out.println( "reading icon file" );
  try {
    InputStream is = new FileInputStream(fileName);
    byte[] buffer = new byte[4096];

    ByteBuffer bb = new ByteBuffer();

    while (true) {
      int read = is.read(buffer);
      if (read == -1) break;
      bb.append(buffer, 0, read);
    }

    //System.out.println( "completed reading icon file" );

    return bb;

  }
  catch (Exception e) {
    System.out.println(e);
    return null;
  }
}

/** this method reads a file and returns it as a ByteBuffer, but it uses a Map for a fileCache */
public static ByteBuffer fileToByteBuffer(String fileName, Map<String, ByteBuffer> fileCache) {
  ByteBuffer bb;

  try {
    //try to get the file from the cache
    Object o = fileCache.get(fileName);
    if (o != null) {
      bb = (ByteBuffer) o;
      //System.out.println( "got "+fileName+" from cache." );
      return bb;
    }

  }
  catch (Exception e) {
    System.out.println(e);
  }

  //couldnt get file from the fileCache, so read it fresh and then
  //put the file in the cache
  //System.out.println( "loading "+fileName+" from hdd." );
  bb = new ByteBuffer(fileToBytes(fileName));
  fileCache.put(fileName, bb);
  return bb;
}

/** this method reads a file and returns a byte[] of the contents of this file. */
public static byte[] fileToBytes(String fileName) {
  ByteBuffer retVal = fileToByteBuffer(fileName);
  return retVal == null
         ? null
         : retVal.getBytes();
}


/** this method reads a file and returns it as a byte array, but it uses a Map for a fileCache */
public static byte[] fileToBytes(String fileName, Map<String, ByteBuffer> fileCache) {
  ByteBuffer retVal = fileToByteBuffer(fileName, fileCache);
  return retVal == null
         ? null
         : retVal.getBytes();
}

public static String getCallerClassName() {
  try {
    return Thread.currentThread().getStackTrace()[1].getClassName();
  }
  catch (Exception e) {
    return "n/a";
  }
}

public static String getCallerMethodName() {
  try {
    return Thread.currentThread().getStackTrace()[1].getMethodName();
  }
  catch (Exception e) {
    return "n/a";
  }

}

public static String getClassNameFromStackTrace(int depth) {
  try {
    return Thread.currentThread().getStackTrace()[depth].getClassName();
  }
  catch (Exception e) {
    return "n/a";
  }
}

public static String getDebugHeaderString(String msg) {

  StringBuilder lines = new StringBuilder();
  for (int i = 0; i < msg.length() + 6; i++) {
    lines.append("-");
  }

  StringBuilder sb = new StringBuilder();
  sb.
      append(lines).append("\n").
      append(":: ").append(msg).append(" ::").append("\n").
      append(lines);

  return sb.toString();
}

public static Object[] getObjectRay(Object... args) {
  return args;
}

/**
 * this method does away with a NullPointerException that might get thrown (incorrectly semantically
 * speaking) when the following call is made: <tt>(String)Map.get("nonexistentkey");</tt>. The
 * NullPointerException gets thrown because you can't typecast NULL. So the code simply checks to
 * see if get(key) returns a null; and if it does, merely returns null; else returns the String
 * representation of the object.s
 */
public static String getString(Map m, Object key) {
  return m.get(key) == null
         ? null
         : m.get(key).toString();
}

/** this method returns the current time in */
public static String getTime() {
  Date d = new Date();
  SimpleDateFormat sdf = new SimpleDateFormat(
      "yyyy_MM_dd.hh_mm_ss_a.zzz");
  return sdf.format(d);
}

/**
 * this is just a convenience method to tokenize a string using the services of TheStringTokenizer
 * class.
 */
public static String[] getTokens(String s, String delim) {
  TheStringTokenizer st = new TheStringTokenizer(s, delim);
  return st.getTokens();
}

/**
 * checks to see if lhs and rhs objects are the same or not
 *
 * @param lhs this might be null
 * @param rhs this might be null
 */
public static boolean isDifferent(Object lhs, Object rhs) {

  // they are both null, not different...
  if (lhs == null && rhs == null) return false;

  try {
    // if they are equal, they are not different...
    if (lhs.equals(rhs)) {
      return false;
    }
    // if they are inequal, they are different...
    else {
      return true;
    }
  }
  catch (Exception e) {
    // this means they are different in some way...
    return true;
  }

}

public static boolean isNullOrEmpty(String string) {
  if (string == null || string.length() == 0) {
    return true;
  }
  else {
    return false;
  }

}


public static <T> boolean isObjectInArray(T item, T[] list) {

  for (T listItem : list) {
    if (item.equals(listItem)) return true;
  }
  return false;
}

public static <T> boolean isObjectInList(T item, List<T> list) {

  for (T listItem : list) {
    if (item.equals(listItem)) return true;
  }
  return false;
}

public static boolean isStringInList(String item, List<String> list) {

  for (String listItem : list) {
    if (item.equals(listItem)) return true;
  }
  return false;
}

/** Turns a list of LogRecord objects into a string for debug purposes */
public static String listOfLogRecordToString(List<LogRecord> list) {
  StringBuilder sb = new StringBuilder();

  for (LogRecord rec : list) {
    sb.append("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n").
        append("LEVEL:").append(rec.getLevel()).append(", Name:").append(rec.getLoggerName()).append("\n").
        append("MSG:  ").append(rec.getMessage()).append("\n").
        append("EX:   ").append(rec.getThrown()).append("\n");
  }

  return sb.toString();
}


public static String listToString(List list) {
  StringBuilder sb = new StringBuilder();

  sb.append("{number of entries=").append(list.size()).append("}\n");

  int i = 0;

  for (Object obj : list) {
    sb.append("[").append(++i).append("] ").append(obj.toString());
    if (i != list.size()) sb.append("\n");
  }

  return sb.toString();
}


public static String mapToString(Map map) {
  StringBuilder sb = new StringBuilder();

  sb.append("{number of entries=").append(map.size()).append("}\n");

  int i = 0;

  String separator = "\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n";

  for (Object key : map.keySet()) {
    if (i == 0 && !map.isEmpty()) sb.append(separator);
    sb.append("\t[").append(++i).append("] ").
        append(key.toString()).append("=\n\t\t ").
        append(map.get(key).toString()).append(separator);
  }

  return sb.toString();
}


public static void printDebugHeaderToConsole(String msg) {
  System.out.println(getDebugHeaderString(msg));
}

public static ArrayList<String> toArrayListFromObjectRay(Object[] objRay) {
  if (objRay == null) {
    return new ArrayList<String>();
  }
  else {
    ArrayList<String> list = new ArrayList<String>();
    for (Object o : objRay) {
      list.add(o.toString());
    }
    return list;
  }
}

public static void writeToFile(byte[] msg, String file) {
  try {
    FileOutputStream fos = new FileOutputStream(file);
    fos.write(msg);
    fos.close();
  }
  catch (Exception e) {
    System.out.println(e);
  }
}

public static void writeToFile(ByteBuffer msg, String file) {
  try {
    FileOutputStream fos = new FileOutputStream(file);
    fos.write(msg.getBytes());
    fos.close();
  }
  catch (Exception e) {
    System.out.println(e);
  }
}

public static void writeToFile(String msg, String file) {
  try {
    FileOutputStream fos = new FileOutputStream(file);
    fos.write(msg.getBytes());
    fos.close();
  }
  catch (Exception e) {
    System.out.println(e);
  }
}

/** no constructor */
private Utils() {}

}//end of Utils class

