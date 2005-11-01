package edu.vt.cbil.visda.data;

import java.io.File;
import javax.swing.filechooser.*;

/**
 * The class performs the data file filter
 * 
 * @author Jiajing Wang
 * @version visda_v0.1
 */
public class DataFileFilter extends FileFilter {

    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals("txt") || (extension.equals("dat"))) {
            	return true;
            } else {
                return false;
            }
        }

        return false;
    }

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    
    //The description of this filter
    public String getDescription() {
        return "Tab Delimited Sample Files";
    }
}
