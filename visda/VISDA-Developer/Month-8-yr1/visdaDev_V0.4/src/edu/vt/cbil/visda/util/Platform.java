package edu.vt.cbil.visda.util;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 */

/**
 * Answer questions about the platform that Java is running on.  A necessary evil.
 *
 */
public class Platform {
    private static String WIN_ID = "Windows";
    private static String MAC_ID = "Mac OS";
    private static String OS_KEY = "os.name";
    private static String HOME_DIR_KEY = "user.home";

    /**
     * Answer the question, "am I running on Window?"
     * @return true iff we are running on Windows, otherwise false
     */
    public static boolean isWindows() {
        String platform = System.getProperty( OS_KEY );
        return ( platform != null && platform.startsWith( WIN_ID ) );

    }

    /**
     * Answer the question, "am I running on a Mac?"
     * @return true, iff we are running on Windows, otherwise false
     */
    public static boolean isMac() {
        String platform = System.getProperty( OS_KEY );
        return ( platform != null && platform.startsWith( MAC_ID ) );

    }

    /**
     * Answer the user home directory if possible, else the current dir.
     * @return the string value of the user's home directory or "."
     */
    public static String getUserHomeDirectory() {
        return System.getProperty( HOME_DIR_KEY, "." );

    }
}
