package truesculpt.utils.support;

/**
 * BitRegister is a boolean that can be made final AND still modified. Great for anonymous in-line inner class
 * declarations.
 *
 * @author Nazmul Idris
 * @version 1.0
 * @since Jun 1, 2007, 5:08:42 PM
 */
public class BitRegister {

private boolean value;

public BitRegister(boolean initValue) {
  value = initValue;
}

public synchronized void set() {
  value = true;
}

public synchronized boolean isSet() {
  return value;
}

public synchronized boolean isNotSet() {
  return !value;
}

public synchronized void clear() {
  value = false;
}

}//end class BitRegister
