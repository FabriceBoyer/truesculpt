package truesculpt.utils.support;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * ZipObject is a utility class for compressing and decompressing any object.
 * The idea is to compress object parameters before passing them to remote
 * objects. The reason for doing this is improving network transmission speeds.
 * With most customers using modems, compressing any data transmission even
 * by 50% is a great help. Since most transmissions are textual, the compression
 * should be even better on those.
 * <p/>
 * This class uses Serialization, ObjectIn/OutputStreams, and
 * ByteArrayIn/OutputStreams.
 *
 * @author Nazmul Bin Idris
 * @version 1.0
 *          <p/>
 *          Creation Date : 9/3/1999 Creation Time : 8:00pm
 */
public class ZipObject<T extends Serializable> implements Serializable {

static final long serialVersionUID = 5572357487744011409L;

/**
 * Given a byte[] which contains the output of {@link #toBytes()}, allows the original uncompressed
 * object to be reconstituted... using {@link #getObject()}.
 */
public static ZipObject fromBytes(byte[] bytes) throws IllegalArgumentException {

  ZipObject zobj = new ZipObject();
  zobj.buffer = bytes;

  return zobj;
}

/**
 * Given an InputStream which contains the output of {@link #toBytes()}, allows the original
 * uncompressed object to be reconstituted... using {@link #getObject()}.
 */
public static ZipObject fromInputStream(InputStream is) throws IOException, IllegalArgumentException {

  ByteBuffer bb = new ByteBuffer(is);

  return fromBytes(bb.getBytes());
}

/**
 * SELF TEST METHOD <br>
 *
 * @param args command line args
 */
@SuppressWarnings("unchecked")
public static void main(String[] args) {

  //create a large object, a vector of hashtables
  Vector<Hashtable<Object, String>> v = new Vector<Hashtable<Object, String>>();
  for (int j = 0; j < 100; j++) {
    Hashtable<Object, String> ht = new Hashtable<Object, String>();
    for (int i = 0; i < 10; i++) {
      ht.put(i, "whatever");
    }
    v.addElement(ht);
  }

  try {

    //write uncompressed obj to disk (check size)
    File tempFile1 = File.createTempFile("uncompressed", "ser");
    File tempFile2 = File.createTempFile("compressed", "ser");
    tempFile1.deleteOnExit();
    tempFile2.deleteOnExit();

    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tempFile1));
    oos.writeObject(v);
    oos.flush();
    oos.close();

    //create a ZipObject and save it to disk
    ZipObject vZip = new ZipObject(v);
    oos = new ObjectOutputStream(new FileOutputStream(tempFile2));
    oos.writeObject(vZip);
    oos.flush();
    oos.close();

    System.out.println("Uncompressed file size=" + tempFile1.length());
    System.out.println("Compressed file size=" + tempFile2.length());

    //create a zip object, unzip it and display to screen
    Vector<String> v2 = new Vector<String>();
    v2.addElement("one");
    v2.addElement("two");
    v2.addElement("three");
    v2.addElement("four");

    ZipObject<Vector<String>> v2Zip = new ZipObject<Vector<String>>(v2);

    Vector unzipV = v2Zip.getObject();

    System.out.println(Utils.listToString(unzipV));
  }
  catch (Exception e) {
    System.out.println(e);
  }

}

/** Internal byteArray buffer for storing the compressed Serializable obj */
protected byte[] buffer;

/** no default constructor accessible publicly... */
private ZipObject() {}

/**
 * Constructor for a ZipObject given an uncompressed object.
 *
 * @param obj Uncompressed Serializable object which has to be compressed
 *
 * @return compressed object encapsulated in a ZipObject
 *
 * @throws ZipObjectException this is thrown if the object couldnt be compressed for whatever
 *                            reason
 */
public ZipObject(T obj) throws ZipObjectException {
  try {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    GZIPOutputStream gzos = new GZIPOutputStream(baos);
    ObjectOutputStream oos = new ObjectOutputStream(gzos);

    oos.writeObject(obj);

    oos.flush();
    gzos.finish();  //flush

    oos.close();
    gzos.close();   //close

    buffer = baos.toByteArray();//save data

    baos.close();               //close
  }
  catch (Exception e) {
    System.out.println(e);
    e.printStackTrace();
    throw new ZipObjectException("could not zip given object into a ZipObject");
  }
}

/**
 * this method extracts the compress object (from this ZipObject) and returns it. <br>
 *
 * @return returns the uncompressed object contained in this ZipObject
 *
 * @throws ZipObjectException this is thrown if the object could not be uncompressed for whatever
 *                            reason
 */
@SuppressWarnings("unchecked")
public final T getObject() throws ZipObjectException {
  try {
    ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
    GZIPInputStream gzis = new GZIPInputStream(bais);
    ObjectInputStream ois = new ObjectInputStream(gzis);

    Object obj = ois.readObject();

    bais.close();
    gzis.close();
    ois.close(); //close

    return (T) obj;
  }
  catch (Exception e) {
    //System.out.println(e);
    //e.printStackTrace();
    throw new ZipObjectException("could not unzip ZipObject to get object", e);
  }

}

public int getSize() {
  return (buffer == null)
      ? 0
      : buffer.length;
}

public String getSizeString() {
  return Integer.toString(getSize());
}

/**
 * simply returns the internal byte array used to store the ZipObject data in
 *
 * @return array of bytes used by ZipObject to store compressed data
 */
public byte[] toBytes() throws ZipObjectException {
  if (buffer == null) {
    throw new ZipObjectException("the internal byte array is null");
  }
  return buffer;
}

/** returns an InputStream that can read from the buffer of the ZipObject */
public InputStream toInputStream() throws ZipObjectException {
  if (buffer == null) {
    throw new ZipObjectException("the internal byte array is null");
  }

  return new ByteArrayInputStream(buffer);
}

/** returns a string representation of the ZipObject */
@Override
public String toString() {
  if (buffer != null) {
    return buffer.length / 1000f + " KB";
  }
  else {
    return "0 KB";
  }
}


}//end class