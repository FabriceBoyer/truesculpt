package truesculpt.utils.support;

import java.util.ArrayList;

/**
 * <B>TheStringTokenizer</B> is a replacement for java.util.TheStringTokenizer (because it sucks!).
 * Please note that this class uses the support.Tokenizer class. If that class is changed then this
 * will break!
 * <p/>
 * <p/>
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since 2/26/2000,4:09pm
 */
public class TheStringTokenizer {

/**
 * This class simply takes an input String or StringBuffer and tokenizes the input based on a
 * delimiter String.
 * <p/>
 * <B>Usage:</B>
 * <p/>
 * <pre>
 *   String input = "20,LOR;ispq.com;4900<T>30,LOR;ispq.com;4900<T>40,LOR;ispq.com;490";
 *   String delim = "<T>";
 * <p/>
 *   Tokenizer t = new Tokenizer();
 *   t.set( input , delim );
 * <p/>
 *   //this is for input with tokens
 *   System.out.println( "***valid input with tokens***" );
 *   System.out.println( "input:"+input );
 *   Tokenizer t = new Tokenizer( input , delim );
 *   System.out.println( "hasTokens():"+t.hasTokens() );
 * <p/>
 *   String[] strRay = t.getTokens();
 *   for( int i=0; i<strRay.length; i++) {
 *     System.out.println( "token:"+strRay[i] );
 *   }
 *   System.out.println( t.getRemainderOfInput() );
 * </pre>
 * </pre>
 * <p/>
 * <blockquote>
 * <p/>
 * <B>Output:</B><P> <pre> ***valid input with tokens input:20,LOR;ispq.com;4900<T>30,LOR;ispq.com;4900<T>40,LOR;ispq.com;490
 * hasTokens():true token:20,LOR;ispq.com;4900 <B><--token1</B> token:30,LOR;ispq.com;4900
 * <b><--token2</B> 40,LOR;ispq.com;490 <b><--remainder</B> </pre> </pre> </blockquote>
 * <p/>
 * <p/>
 * <b>Note: Definition of a token</b><p> This class is different from the StringTokenizer class.
 * Unlike the StringTokenizer class (which just looks for chars in the String to break up the String
 * into tokens), the Tokenizer class looks for delimiters to break up words( and it there MUST be
 * trailing delimiters to get the last token, or else it will be the remainder).<p> For example:
 * <tt>t.set( "LOR;ispq.com;4900" , ";" );</tt> will yield {"LOR","ispq.com"} when getTokens() is
 * called. Whereas StringTokenizer would return {"LOR", "ispq.com","4900"}
 * <p/>
 * <b>Note: dependencies</b><p> TheStringTokenizer class depends on this class, so be sure to check
 * this class if Tokenzier is modified.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since 2/18/2000,12:08pm
 */
public class Tokenizer {

  protected String delim = null;
  //data
  protected String input = null;
  protected String remainder = null;


  //methods
  public Tokenizer() {
  }


  /**
   * This method returns an empty StringBuffer if there is no remainder in this Tokenizer object.
   * Otherwise, it simply returns a StringBuffer with the data in it.
   * <p/>
   * This behavior is different as it does not return null if there is no remainder.
   * <p/>
   * The remainder is defined as data left in the buffer trailing the last delim string.
   * <p/>
   *
   * @return StringBuffer containing remainder text (is never null)
   */
  public StringBuffer getRemainderOfInput() {
    return
        (remainder != null) ? new StringBuffer(remainder) : new StringBuffer();
  }


  /**
   * Returns all the tokens in the current message buffer, that are delimited by the String. The
   * tokens are returned as an array of Strings.
   * <p/>
   * If the input parameter(s) are invalid, null is returned.
   * <p/>
   * If there are no tokens, null is returned.
   *
   * @return String array of tokens (if they exist), otherwise return null.
   */
  public String[] getTokens() {

    //invalid input checking
    if (input == null || delim == null) {
      return null;
    }

    //if there are no tokens, return null
    if (input.indexOf(delim) == -1) {
      return null;
    }

    //there is at least one token, which means all the code below is
    //valid
    ArrayList<String> list = new ArrayList<String>();

    int markStart = 0;
    int markEnd = 0;

    while (true) {
      markEnd = input.indexOf(delim, markStart);

      if (markEnd == -1) {
        break;
      }

      list.add(input.substring(markStart, markEnd));

      markStart = markEnd + delim.length();

    }

    //deal with the remainder
    if (markStart != input.length()) {
      remainder = input.substring(markStart, input.length());
    }
    else {
      remainder = null;
    }

    //convert list to String[]
    String[] strRay = new String[list.size()];
    for (int i = 0; i < list.size(); i++) {
      strRay[i] = list.get(i);
    }

    return strRay;
  }


  /**
   * Determines whether there is any data left over in the message buffer after all the tokens have
   * been extracted from it.
   *
   * @return true if there is data left over, after tokens are removed, false otherwise.
   */
  public boolean hasRemainder() {
    return
        (remainder == null) ? false : true;
  }


  /**
   * Returns true if there tokens exist in the message buffer.
   *
   * @return true if there are any tokens (delimted by delim), else returns false
   */
  public boolean hasTokens() {
    if (input.indexOf(delim) == -1) {
      return false;
    }
    else {
      return true;
    }
  }


  /**
   * This method sets up the Tokenizer to process the given message buffer and delim (delimiter)
   * string. The internal state of the Tokenizer object is reset and setup to work with all the
   * accessor methods available to this class.
   *
   * @param input message buffer that contains the untokenized data received from the socket
   * @param delim delimiter string used to delimit the tokens in the message buffer.
   *
   * @return returns a reference to this object (just as a convenience) to invoke other methods on
   *         it.
   */
  public Tokenizer set(String input, String delim) {
    this.input = input;
    this.delim = delim;
    remainder = null;   //reset remainder data to initial state
    return this;
  }


  /**
   * same as set( String , String ), only it converts the StringBuffer to a String before calling
   * the other set()
   */
  public Tokenizer set(StringBuffer sb, String delim) {
    return set(new String(sb), delim);
  }

/**
 * This is a self test method that puts the Tokenizer class
 * through its paces to determine that it works as advertised.
 *
 * @param args
 */
/*
public static void main( String[] args ){
  Tokenizer t = new Tokenizer();

  String noTokenInput = "20,LOR;ispq.com;4900";
  String input = "20,LOR;ispq.com;4900<T>30,LOR;ispq.com;4900<T>40,LOR;ispq.com;490";
  String inputWithNoRemainder = "20,LOR;ispq.com;4900<T>30,LOR;ispq.com;4900<T>";
  String delim = "<T>";


  //this is for input with tokens
  System.out.println( "***valid input with tokens***" );
  System.out.println( "input:"+input );
  t.set( input , delim );
  System.out.println( "hasTokens():"+t.hasTokens() );

  String[] strRay = t.getTokens();
  for( int i=0; i<strRay.length; i++) {
    System.out.println( "token:"+strRay[i] );
  }
  System.out.println( "t.hasRemainder():"+t.hasRemainder() );
  System.out.println( "t.getReminderOfInput():"+t.getRemainderOfInput() );


  //this is for input with tokens (but no remainder)
  System.out.println( "\n\n***valid input with tokens and no remainder***" );
  System.out.println( "input:"+inputWithNoRemainder );
  t.set( inputWithNoRemainder , delim );
  System.out.println( "hasTokens():"+t.hasTokens() );

  strRay = t.getTokens();
  for( int i=0; i<strRay.length; i++) {
    System.out.println( "token:"+strRay[i] );
  }
  System.out.println( "t.hasRemainder():"+t.hasRemainder() );
  System.out.println( "t.getReminderOfInput():"+t.getRemainderOfInput() );


  //this is for input without tokens
  System.out.println( "\n\n***invalid input with no tokens***" );
  t.set( noTokenInput , delim );
  System.out.println( "input:"+noTokenInput );
  System.out.println( "hasTokens:"+t.hasTokens() );
  if( t.getTokens() == null ) {
    System.out.println( "input does not contain any tokens" );
  }
  System.out.println( "t.hasRemainder():"+t.hasRemainder() );
  System.out.println( "t.getReminderOfInput():"+t.getRemainderOfInput() );


}
*/

}//end Tokenizer
protected String delim;

//

//
// Data Members
//
protected String s;

// Methods
//
public TheStringTokenizer(String s, String delim) {
  this.s = s;
  this.delim = delim;
}

//
// Inner Class : Tokenizer
// this class was taken from ch.plugin.core.channel and copied
// here in order to limit dependencies.
//


public String[] getTokens() {
  Tokenizer t = new Tokenizer();
  t.set(s, delim);

  String[] strRay = t.getTokens();

  if (t.hasRemainder()) {

    String[] strRay2 = new String[strRay.length + 1];

    System.arraycopy(strRay, 0, strRay2, 0, strRay.length);

    strRay2[strRay2.length - 1] = new String(t.getRemainderOfInput());

    return strRay2;

  }//endif (t.hasRemainder())

  return strRay;

}


}//end of TheStringTokenizer class

