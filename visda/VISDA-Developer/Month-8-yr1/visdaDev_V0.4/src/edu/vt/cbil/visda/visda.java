package edu.vt.cbil.visda;

import edu.vt.cbil.visda.MainFrame;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 * A simple class to launch the main window.
 */
public class visda {

    public static final String TITLE = "VISDA";
    public static final String VERSION = "0.1";
    
    public static void main( String[] args ) {
    	MainFrame mainFrm;
        try {
            System.setProperty("sun.java2d.translaccel", "true");
            System.setProperty("sun.java2d.ddforcevram", "true");
        } catch(Exception e) {
            // oh well ...
        }
        if(args.length == 0){
        	mainFrm = new MainFrame( TITLE, VERSION, true ); //run GUI version
        }
    }
}
