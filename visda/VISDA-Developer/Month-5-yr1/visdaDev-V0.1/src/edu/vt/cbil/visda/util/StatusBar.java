package edu.vt.cbil.visda.util;

import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 */

/**
 * JLabel subclass to display messages to the user.
 */
public class StatusBar extends JLabel {

	/**
	 * Create a no-frills, bevelled status area for text.
	 *
	 */
    public StatusBar() {
        setBorder( new BevelBorder( BevelBorder.LOWERED ) );
        display( " " );

    }

    /**
     * Immediately replace the current message with this new message.
     * @param message the new status text for display.
     */
    public void display( String message ) {
        setText( " " + message );

    }

}
