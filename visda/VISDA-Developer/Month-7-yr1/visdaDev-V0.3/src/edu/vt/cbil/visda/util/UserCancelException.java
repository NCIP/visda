package edu.vt.cbil.visda.util;
/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 */

/**
 * An exception to denote a user cancelled action.  When this exception is
 * caught, there is no need to alert the user, only to recover from what the 
 * application was trying to do.
 * 
 */
public class UserCancelException extends Exception {

	/**
	 * Constructor copied from superclass
	 */
	public UserCancelException() {
		super();
		
	}

	/**
	 * Constructor copied from superclass
	 * @param message
	 */
	public UserCancelException(String message) {
		super(message);
		
	}

	/**
	 * Constructor copied from superclass
	 * @param message
	 * @param cause
	 */
	public UserCancelException(String message, Throwable cause) {
		super(message, cause);
		
	}

	/**
	 * Constructor copied from superclass
	 * @param cause
	 */
	public UserCancelException(Throwable cause) {
		super(cause);
		
	}

}
