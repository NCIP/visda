package edu.vt.cbil.visda.data;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.swing.JOptionPane;

import edu.vt.cbil.visda.util.Platform;
import edu.vt.cbil.visda.util.UserCancelException;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 */

/**
 * <code>DataManager</code> is more or less the model of the application,
 * holding references to all data that should be available to all parts
 * of the application.
 */
public class DataManager {
    
    /**
     * the location of the project's classification dataset
     */
    public static final String CLASSIFICATION_LOCATION_KEY = "classification.location";
    
    /**
     * the location of the classification/dataTable splitter bar when last used.
     */
    public static final String CLASSIFICATION_SPLITTER_LOCATION = "classification.splitter";
    
    /**
     * Our current singlton.
     */
    private static DataManager current;
    
    /**
     * the location of the project's dataset
     */
    public static final String DATASET_LOCATION_KEY = "dataSet.location";
	
	/**
	 * the application's default typeset
	 */
	public static final Font DEFAULT_FONT = new Font( "arial", Font.PLAIN, 10 );
	
	/**
	 * The location of the default ExPattern home in the user's home dir.
	 */
	public static final String DEFAULT_HOME = Platform.getUserHomeDirectory()+ "/nnbio";

	/**
	 * the location of the directory containing projects
	 */
    public static final String DEFAULT_PROJECT_HOME = DEFAULT_HOME+ "/Projects";

    /**
     * the default name of new projects
     */
	public static final String DEFAULT_PROJECT_NAME = "New_Project";
	
	/**
	 * the default suffix of an ExPattern project file
	 */
	public static final String DEFAULT_PROJECT_SUFFIX = ".exp";
	
    /**
     * the location of the default systemProperties file
     */
    public static final String DEFAULT_PROPERTIES_LOCATION = 
    	DEFAULT_HOME + "/nnbio.properties";
	
	/**
	 * the system's file line separator.  probably "\n" or "\n\r"
	 */
	public final static String eol = System.getProperty("line.separator");

    /**
     * the height of the main window
     */
    public static final String EXPATTERN_HEIGHT_KEY = "nnbio.height";

    /**
     * the width of the main window
     */
    public static final String EXPATTERN_WIDTH_KEY = "nnbio.width";

    /**
     * position of the window from the left of the screen
     */
    public static final String EXPATTERN_X_KEY = "nnbio.x";

    /**
     * position of the window from the top of the screen
     */
    public static final String EXPATTERN_Y_KEY = "nnbio.y";
    
    /**
     * the location of the user's GOMiner jar.  
     * GOMiner = http://discover.nci.nih.gov/gominer/
     */
    public static final String GOMINER_LOCATION_KEY = "gominer.location";
    
    /**
     * The creation date of the project in milliseconds
     */
	public static final String PROJECT_DATE_KEY = "project.creation.date";
	
	/**
	 * the file path to the project folder
	 */
	public static final String PROJECT_HOME_KEY = "project.home";

	/**
	 * The log scaled float value of upper color saturation in raw data
	 */
	public static final String PROJECT_UPPERBOUNDS_KEY = "project.upper.bounds";
	
	/**
	 * the flag to view raw data or quantized data.
	 */
	public static final String PROJECT_VIEW_RAW_KEY = "project.view.raw";	
	
	/**
	 * The log scaled float value of lower color saturation in raw data
	 */
	public static final String PROJECT_LOWERBOUNDS_KEY = "project.lower.bounds";
	
	/**
     * the Hexidecimal RGB value for down regulated, quantized values
     */
	public static final String QUANTIZED_DOWN_KEY = "quantized.down";
	
	/**
     * the Hexidecimal RGB value for evenly regulated, quantized values
     */
	public static final String QUANTIZED_EVEN_KEY =	"quantized.even";
    
    /**
     * the Hexidecimal RGB value for up regulated, quantized values
     */
	public static final String QUANTIZED_UP_KEY	=	"quantized.up";
    
	/**
	 * The location of the project's raw data
	 */
    public static final String RAW_LOCATION_KEY = "rawData.location";

	/**
	 * A list of Strings synonymous with 'Symbol'
	 */
	private static ArrayList symbolSynonyms;

    /**
     * The systemProperties for the application
     */
    private static Properties systemProperties;
    
    /**
     * Copy the contents of <code>from</code> into <code>to</code>
     * @param from source file
     * @param to destination file
     * @throws IOException All problems are passed along through this exception
     */
    public static void copy( File from, File to ) throws IOException {
    	System.out.print( "copying " + from + " to project directory.");
    	FileInputStream fin = new FileInputStream( from );
    	FileOutputStream fout = new FileOutputStream( to );
    	byte[] buffer = new byte[4096];
    	int bytesRead;
    	while (( bytesRead = fin.read(buffer)) != -1 ) {
    		fout.write( buffer, 0, bytesRead );
    		
    	}
    	fin.close();
    	fout.close();
    	    	
    }

    /**
     * Create a properties object that can be used to initialize a new project.
     * @return a valid Properties instance for a new project.
     */
    public static Properties getDefaultProjectProperties() {
    	Properties props = new Properties();
    	props.put( PROJECT_DATE_KEY, String.valueOf( new Date().getTime() ) );
    	props.put( PROJECT_HOME_KEY, DEFAULT_PROJECT_HOME + "/" + DEFAULT_PROJECT_NAME );
    	props.put( PROJECT_VIEW_RAW_KEY, "false" );
    	return props;
    	
    }

    /**
     * the only way to get an instance of this singleton
     *
     * @return the only data manager
     */
    public static DataManager getInstance() {
        if ( current == null )
            current = new DataManager();

        return current;

    }
   
    /**
     * Answer the properties of the current project
     * @return the current project's properties
     */
    public static Properties getProjectProperties() {
    	if ( getInstance().getCurrentProject() == null )
    		return new Properties();
    	
    	return getInstance().currentProject.getProperties();
    	
    }

    /**
     * Return a list of synonyms for "Symbol" and "HUGO" annotation names.
     * @return symbolSynonyms
     */
    public static ArrayList getSymbolSynonyms() {
    	return symbolSynonyms;
    	
    }
    
    /**
     * Answer the system systemProperties
     * @return the systemProperties for this application.
     */
    public static Properties getSystemProperties() {
        return systemProperties;

    }

    /**
     * Display a warning dialogue to the user with the specified message.
     * @param warning the message displayed for the user
     */
   public static void warning( String warning ) {
       JOptionPane.showMessageDialog( null,
                                      warning,
                                      "Warning",
                                      JOptionPane.ERROR_MESSAGE );

   }
   
   /**
    * Ask the user a "Yes to all" or "No" question.
    * @param title the message at the top of the dialog
    * @param question the query posed to the user
    * @return JOptionPane.YES_OPTION || JOptionPane.NO_OPTION  
    */
   public static int yesToAll( String title, String question ) {
		Object[] options = { "Yes to all", "No" };
		int n = JOptionPane.showOptionDialog( null, // frame
			question,
			title,
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,     //don't use a custom Icon
			options,  //the titles of buttons
			options[0]); //default button title
		
		return n;

   }
    	
    /**
     * The project currently holding on to all the data and preferences
     */
    private Project currentProject;

	/**
	 * Get off my lawn!
	 */
    private DataManager() {
        init();

    }
    
    /**
     * If there is no current project, create one for the data that is being opened.
     *
     */
    private void checkForCurrentProject() {
    	if ( currentProject == null ) { 
    		currentProject = new Project();
    		
    	}
    	
    }
    
    /**
     * Save expattern properties and the current project.
     */
    public void cleanUp() {
    	 saveProperties();
    	 if ( currentProject != null && currentProject.isDirty() ) {
    	 	
	    	 int retVal = JOptionPane.showConfirmDialog(null, 
	    	 		"Save current project?", 
					"Save Current Project?", 
					JOptionPane.YES_NO_OPTION);
	    	 //You can put save project action here!
	    	 
    	 }
	    	 
    }
    
    
	/**
	 * Returns the current project.
	 * @return Returns the currentProject.
	 */
	public Project getCurrentProject() {
		return currentProject;
	}
	/**
	 * Returns the current raw dataset or null if one has not been opened.
	 * @return Returns the currentRawDataSet.
	 */
	public RawDataSet getCurrentRawDataSet() {
		if ( currentProject == null )
			return null;
		else 
			return currentProject.getRawData();
		
	}

	/**
	 * Returns true if the user has selected to display raw, false otherwise.
	 * @return Returns the displayRaw.
	 */
	public boolean getDisplayRaw() {
		return currentProject.getDisplayRaw();
		
	}
	
	/**
	 * Getter for the current project.
	 * @return the int location of the splitter bar when last saved
	 */
	public int getDividerLocation() {
		return currentProject.getDividerLocation();
		
	}

	/**
	 * initialize!
	 *
	 */
    private void init() {
        readProperties(); //Huai debug 1-04-05
        
        // similair data is repeated in WebSearch and it really, really bugs me.
		symbolSynonyms = new ArrayList();
		symbolSynonyms.add( "hugo" );
		symbolSynonyms.add( "hugo id" );
		symbolSynonyms.add( "hugo symbol" );
		symbolSynonyms.add( "symbol" );
		symbolSynonyms.add( "gene symbol" );
    }

    /**
     * Access the application's property file and populate <code>systemProperties</code>
     * with it's contents.
     *
     */
    private void readProperties() {
    	
    	File home = new File( DEFAULT_HOME );
    	if ( !home.exists() ) {
    		home.mkdir();
    		
    	}
    	File props = new File( DEFAULT_PROPERTIES_LOCATION );
    	try{
    		props.createNewFile();
    		
    	} catch( java.io.IOException iox) {
    		warning( DEFAULT_PROPERTIES_LOCATION + "\" could not be created."  );
    	}
    	systemProperties = new Properties();
    	try {
    		if ( props.isFile() && props.canRead() ) {
    			systemProperties.load( new FileInputStream( props ) );
    			
    		} else {
    			warning( DEFAULT_PROPERTIES_LOCATION + "\" could not be read." );
    			
    		}
    		
    	} catch( java.io.FileNotFoundException fnfx) {
    		warning( DEFAULT_PROPERTIES_LOCATION + "\" could not be found." );
    		
    	} catch( java.io.IOException iox) {
    		warning( DEFAULT_PROPERTIES_LOCATION + "\" could not be read." );
    		
    	}

    }

    /**
     * Remember the current value of all key/value pairs in the Properties
     * instance.
     */
    public void saveProperties() {

        File props = new File( DEFAULT_PROPERTIES_LOCATION );
        try {
            if ( ! props.exists() ) {
                props.createNewFile();

            }
        } catch( java.io.IOException iox) {
            warning( "Could not create default system properties file \"" + DEFAULT_PROPERTIES_LOCATION + "\"." );
            return;

        }
        try {
            FileOutputStream out = new FileOutputStream( props );
            systemProperties.store( out, "nnbio settings" );

        } catch( java.io.FileNotFoundException fnfx) {
            warning( "Could not find or create default system properties file \"" + DEFAULT_PROPERTIES_LOCATION + "\"." );

        } catch( java.io.IOException iox) {
            warning( "Could not create default system properties file \"" + DEFAULT_PROPERTIES_LOCATION + "\"." );

        }

    }
 
	/**
	 * Set the current project and enabled actions as necessary
	 * @param currentProject The currentProject to set.
	 */
	public void setCurrentProject(Project currentProject) {
		this.currentProject = currentProject;
		
	}
    
    /**
     * Parse the given file and make it the new raw data set of the 
     * current project.
     * @param file a raw dataset
     * @throws IOException all problems passed back through this exception
     * @throws UserCancelException thrown if the user cancels parsing when a 
     * foreseeable error is caught.
     * 
     */
	/*public void setCurrentRawDataSet( File file ) throws IOException, UserCancelException {
	    RawDataSet dataSet = new FloatDataSet();
	    DataSetParser parser = new DataSetParser( dataSet );
	    parser.parseFile( file );
	    setCurrentRawDataSet( dataSet );

    }
    public void setCurrentRawDataSet( File file, String fieldNames[] ) throws IOException, UserCancelException {
	    RawDataSet dataSet = new FloatDataSet();
	    DataSetParser parser = new DataSetParser( dataSet );
	    parser.parseFile( file, fieldNames);
	    setCurrentRawDataSet( dataSet );

    }*/
	public void setCurrentRawDataSet( File file, int firstDataLocation[] ) throws IOException, UserCancelException {
	    RawDataSet dataSet = new FloatDataSet();
	    DataSetParser parser = new DataSetParser( dataSet );
	    parser.parseFile( file, firstDataLocation);
	    setCurrentRawDataSet( dataSet );

    }
	
	/**
	 * Make the dataset the current project's current raw dataset
	 * @param currentRawDataSet The currentRawDataSet to set.
	 */
	public void setCurrentRawDataSet(RawDataSet currentRawDataSet) {
		checkForCurrentProject();
		currentProject.setRawData( currentRawDataSet );
		
	}
	
	/**
	 * Set the project to display raw data value over the quantized 
	 * data in the table.
	 * @param displayRaw true if the project should display raw
	 */
	public void setDisplayRaw(boolean displayRaw) {
		currentProject.setDisplayRaw( displayRaw );
		
	}
	
	/**
	 * Setter for the current project.
	 * @param loc the int location of the splitter bar
	 */
	public void setDividerLocation( int loc ) {
		currentProject.setDividerLocation( loc );
		
	}
}
