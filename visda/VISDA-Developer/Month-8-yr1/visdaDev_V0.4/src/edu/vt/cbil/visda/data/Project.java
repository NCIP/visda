package edu.vt.cbil.visda.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import edu.vt.cbil.visda.util.UserCancelException;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 */

/**
 * The culmination of raw and classification data concerned with the same
 * sets of genes and experiments.
 */
public class Project {
	
	/**
	 * Often the start of the project, <code>rawData</code> is the intensity
	 * or ratio data from a mircoarray experiment.
	 */
	private RawDataSet rawData;
	
	/**
	 * The time of creation
	 */
	private Date creationDate;
	
	/**
	 * Project's home directory
	 */
	private File projectHome;
	
	/**
	 * The parsed contents of <code>projectFile</code>
	 */
	private Properties properties;
	
	/**
	 * A single copy of the gene annotations in a pretty simple container
	 */
	private String[][] geneAnnotations;
	
	/**
	 * The file containing annotations for the classifiers
	 */
	private String classAnnotationFile;
	
	/**
	 * The classification annotations once loaded from the file.
	 */
	private String[][] classAnnotations;
	
	/**
	 * The list of <code>ProjectListeners</code> interested in <code>
	 * ProjectEvent</code>s
	 */
	private ArrayList projectListeners;
	
	/**
	 * True iff this object needs to be resaved
	 */
	private boolean isDirty;
	
	/**
	 * The low-end, log-scaled saturation threshold when visualizing log
	 * transformed raw data.
	 */
	private double lowerBounds;
	
	/**
	 * The high-end, log-scaled saturation threshold when visualizing log
	 * transformed raw data
	 */
	private double upperBounds;
	
	/**
	 * If true, this project should display raw data;  Otherwise false.
	 */
	private boolean displayRaw;
	
	
	/**
	 * the last saved location of the data/classifier split pane splitter.
	 */
	private int dividerLocation;

	/**
	 * Project names must be valid directory names.
	 * @param pName potential name of a project
	 * @return true iff the name is a valid file name
	 */
	public static boolean isValidProjectName( String pName ) {
		for (int i = 0; i < pName.length(); ++i) {
			switch( pName.charAt( i ) ) {
				case '\\': 
				case '/' :
				case ':' :
				case '*' : 
			    case '?' : 
			    case '"' :
			    case '<' :
			    case '>' :
			    case '|' : return false;
				default  : // do nothing
				
			}
			
		}
		// if we make it out of the loop pName is valid
		return true;
	}
	
	/**
	 * Construct a Project based on the properties defined in <code>DataManager</code>
	 * @see DataManager#getDefaultProjectProperties()
	 *
	 */
	public Project() {
		this( DataManager.getDefaultProjectProperties() );
		
	}
	
	/**
	 * Construct a Project based on the properties provided.
	 * @param props project properties
	 * @see DataManager#getDefaultProjectProperties()
	 */
	public Project( Properties props ) {
		setProperties( props );
		projectListeners = new ArrayList();
		isDirty = false;
		
	}
	
	/**
	 * Add a listener for this Project
	 * @param pl the interested party
	 */
	public void addProjectListener( ProjectListener pl ) {
		projectListeners.add( pl );
		
	}
	
	/**
	 * Alert all Project Listeners to a Project Event
	 * @param pe the event to distribute
	 */
	public void fireProjectListenerEvent( ProjectEvent pe ) {
		Iterator i = projectListeners.iterator();
		while (i.hasNext() ) {
			ProjectListener pl = (ProjectListener)i.next();
			
		}
		
	}

	/**
	 * Does our name equal the default project name?
	 * @return true iff the name of the project has not changed
	 * @see DataManager#DEFAULT_PROJECT_NAME
	 */
	public boolean isNewProject() {
		return ( getName().equals( DataManager.DEFAULT_PROJECT_NAME));
		
	}
	
	/**
	 * Answer the name of the project's file
	 * @return the name of the directory containing the project
	 */
    public String getName() {
    	return getProjectHome().getName();
    	
    }
    
    /**
     * Change the project's name ( and therefore File location )
     * @param name
     */
    public void setName( String name ) {
    	File parent = getProjectHome().getParentFile();
    	setProjectHome( new File( parent, name ) );
    	
    }
    
    /**
     * Cease sending this Project Listener Project Events
     * @param pl the disinterested party
     */
	public void removeProjectListener( ProjectListener pl ) {
		projectListeners.remove( pl );
		
	}
	
	/**
	 * Answers the question "would saving this project change anything
	 * on disc?"
	 * @return true iff a savable feature of the project has changed.
	 */
	public boolean isDirty() {
		return isDirty;
		
	}
	
	/**
	 * Force the project to require a save.
	 *
	 */
	public void makeDirty() {
		isDirty = true;
		
	}
	
	/**
	 * Force project to require a save or not
	 * @param bool true if project requires save, otherwise false
	 */
	public void setDirty( boolean bool ) {
		isDirty = bool;
		
	}
	
	/**
	 * Attempt to initialize this project from the current properties.
	 *
	 */
	private void initializeFromProperties() {
		if ( properties != null ) {
			// The follow properties are required.  fire an exception for any problem
			try {
				Long millis = Long.valueOf((String)properties.get( DataManager.PROJECT_DATE_KEY ));
				creationDate = new Date( millis.longValue() );
				String projectPath = (String)properties.get( DataManager.PROJECT_HOME_KEY );
				projectHome = new File( projectPath );
				Boolean bool = Boolean.valueOf( (String)properties.get( DataManager.PROJECT_VIEW_RAW_KEY ));
				displayRaw = bool.booleanValue();			
			} catch (Exception x) {
				throw new IllegalArgumentException( 
						"Project properties must have valid entries for " + 
						"DataManager.PROJECT_DATE_KEY, DataManager.PROJECT_HOME_KEY, " +
						"DataManager.PROJECT_VIEW_RAW_KEY, DataManager.QUANTIZED_UP_KEY, " +
						"DataManager.QUANTIZED_EVEN_KEY, and DataManager.QUANTIZED_DOWN_KEY");
				
			}
			// these properties are not required
			DataManager dataManager = DataManager.getInstance();		
			String rPath = properties.getProperty( DataManager.RAW_LOCATION_KEY );
			if ( rPath != null ) {
				try {
					setRawData( new File( rPath ), null);
					
				} catch( java.io.IOException iox ) {
					DataManager.warning( "The raw dataset " + rPath + 
							"\ncould not be opened." );
				} catch( UserCancelException ucx ) {
					// user already knows
				}
				String upperBounds = properties.getProperty( DataManager.PROJECT_UPPERBOUNDS_KEY );
				String lowerBounds = properties.getProperty( DataManager.PROJECT_LOWERBOUNDS_KEY );
				double u, l;
				try {
					if ( upperBounds == null )
						u = (double)(Math.log(rawData.getHighest())/Math.log(2));
					else 
						u = Double.parseDouble(upperBounds);
					
					if ( lowerBounds == null )
						l = (double)(Math.log(rawData.getLowest())/Math.log(2));
					else
						l = Double.parseDouble(lowerBounds);
					
					setUpperBounds( u );
					setLowerBounds( l );
					
				} catch( Exception x ) {
					throw new IllegalArgumentException( 
							"Illegal value for either upper or lower bounds in project properties file. + " +
							"Try reselecting the raw data file through Project Properties." );
					
				}
				
			}				
			setDividerLocation( -1 );
			String location = properties.getProperty( DataManager.CLASSIFICATION_SPLITTER_LOCATION );
			if ( location != null ) {
				int loc = Integer.parseInt( location );
				setDividerLocation( loc );
				
			}
			
		}
		
	}
	
	/**
	 * Returns the directory contains the project's files
	 * @return Returns the project's home directory.
	 */
	public File getProjectHome() {
		return projectHome;
	}
	
	/**
	 * Sets the directory that will contain the project's files
	 * @param projectHome directory to house the project.
	 */
	public void setProjectHome(File projectHome) {
		this.projectHome = projectHome;
		isDirty = true;
	}
	
	/**
	 * Returns the projects properties.
	 * @return Returns the properties.
	 */
	public Properties getProperties() {
		return properties;
	}
	
	/**
	 * Sets the project's properties.
	 * @param properties The properties to set.
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
		initializeFromProperties();
		
	}
	
    /**
     * Parse the given file and make it the new raw data set AND reset the upper and
	 * lower bounds to the dataset's scaled max and min, respectively.
     * @param file a raw dataset
     * @throws IOException all problems passed back through this exception
     * @throws UserCancelException thrown if the user cancels parsing when a 
     * foreseeable error is caught.
     * 
     */
	/*private void setRawData( File file ) throws IOException, UserCancelException {
		DataSetParser parser = new DataSetParser( new FloatDataSet() );
		setRawData( (RawDataSet)parser.parseFile( file ) );		
	
	}
	
	private void setRawData( File file, String fieldNames[] ) throws IOException, UserCancelException {
		DataSetParser parser = new DataSetParser( new FloatDataSet() );
		setRawData( (RawDataSet)parser.parseFile( file, fieldNames ) );		
	
	}*/
	private void setRawData( File file, int firstDataLocation[] ) throws IOException, UserCancelException {
		DataSetParser parser = new DataSetParser( new FloatDataSet() );
		setRawData( (RawDataSet)parser.parseFile( file, firstDataLocation ) );		
	
	}
	
	/**
	 * Returns the raw dataSet.
	 * @return the raw dataSet.
	 */
	
	public RawDataSet getRawData() {
		return rawData;
	}
	
	/**
	 * Make <code>data</code> the current raw dataset AND reset the upper and
	 * lower bounds to the dataset's scaled max and min, respectively.
	 * @param data	a raw dataset.
	 */
	public void setRawData(RawDataSet data) {
		rawData = data;
		upperBounds = (double)(Math.log(rawData.getHighest())/Math.log(2));
		lowerBounds = (double)(Math.log(rawData.getLowest())/Math.log(2));
		isDirty = true;
		
	}
	
	
	/**
	 * Returns the lower bounds of saturation for log transformed raw data.
	 * @return the lower bounds.
	 */
	public double getLowerBounds() {
		return lowerBounds;
	}
	/**
	 * Sets the lower bounds of saturation for log transformed raw data.
	 * @param lowerBounds The lower bounds to set.
	 */
	public void setLowerBounds(double lowerBounds) {
		this.lowerBounds = lowerBounds;
		isDirty = true;
	}
	/**
	 * Returns the upper bounds of saturation for log transformed raw data.
	 * @return Returns the upper Bounds.
	 */
	public double getUpperBounds() {
		return upperBounds;
	}
	/**
	 * Sets the upper bounds of saturation for log transformed raw data.
	 * @param upperBounds The upper bounds to set.
	 */
	public void setUpperBounds(double upperBounds) {
		this.upperBounds = upperBounds;
		isDirty = true;
	}
	/**
	 * Returns true if raw data should be displayed.
	 * @return Returns true to display raw data, otherwise false.
	 */
	public boolean getDisplayRaw() {
		return displayRaw;
	}
	/**
	 * Sets the value that determines if raw data will be displayed.
	 * @param viewRaw	 true to display raw data, otherwise false.
	 */
	public void setDisplayRaw(boolean viewRaw) {
		this.displayRaw = viewRaw;
		isDirty = true;
	}

	
	/**
	 * If the dataSet does not live in the <code>projectHome</code>
	 * directory, then copy it to the <code>projectHome</code> dir
	 * and let the dataSet know it moved.
	 * 
	 * @param dataSet any dataset
	 */
	public void copyDataSetIfNecessary( DataSet dataSet ) {
		if ( dataSet != null ) {
			File dataFile = dataSet.getFile();
			if(  dataFile.getParentFile().compareTo( projectHome )!= 0 ) {
				// raw file needs to be copied
				File newDataFile = new File( projectHome, dataFile.getName() );
				try {
					DataManager.copy( dataSet.getFile(), newDataFile );
					
				} catch ( IOException iox ) {
					iox.printStackTrace();
					
				}
				dataSet.setFile( newDataFile );	
			}	
		}
	}
	
	/**
	 * Save the project if it is dirty and copy data files if necessary.
	 * <ul>
	 * <li>create the projectHome directory if it does not exist.</li>
	 * <li>save the properties every project must have</li>
	 * <li>save the properties that only some projects will have</li>
	 * <li>copy datasets to the project directory as necessary</li></ul>
	 *
	 */
	public void save() {
		if ( isDirty ) {
			// create project directory if necessary
			if ( !projectHome.exists() )
				projectHome.mkdirs();
			
			// set all properties and save file
			// creation date should already be present	
			properties.setProperty( DataManager.PROJECT_HOME_KEY, projectHome.getAbsolutePath() );
			properties.setProperty( DataManager.PROJECT_VIEW_RAW_KEY, String.valueOf( displayRaw ));
			if ( rawData != null ) {
				copyDataSetIfNecessary( rawData );
				properties.setProperty( DataManager.RAW_LOCATION_KEY, 
						rawData.getFile().getAbsolutePath() );
				properties.setProperty( DataManager.PROJECT_UPPERBOUNDS_KEY, 
						String.valueOf( getUpperBounds() ) );
				properties.setProperty( DataManager.PROJECT_LOWERBOUNDS_KEY, 
						String.valueOf( getLowerBounds() ) );
			
			}
			if ( dividerLocation > 0 )
				properties.setProperty( DataManager.CLASSIFICATION_SPLITTER_LOCATION, String.valueOf(dividerLocation) );
			
			// also save ... 
			// classification annotations
			
			saveProperties();
			
		}
		isDirty = false;
		
	}
	
    /**
     * Remember the current value of all key/value pairs in the Properties
     * instance.
     */
    public void saveProperties() {

        File props = new File( projectHome, "project.exp" );
        try {
            if ( ! props.exists() ) {
                props.createNewFile();

            }
        } catch( java.io.IOException iox) {
            DataManager.warning( "Could not create the properties file \"" + props + "\"." );
            return;

        }
        try {
            FileOutputStream out = new FileOutputStream( props );
            properties.store( out, projectHome.getName() + " settings" );

        } catch( java.io.FileNotFoundException fnfx) {
            DataManager.warning( "Could not find or create the properties file \"" + props + "\"." );

        } catch( java.io.IOException iox) {
            DataManager.warning( "Could not create the properties file \"" + props + "\"." );

        }

    }

	/**
	 * Returns the dividers last saved location.
	 * @return Returns the dividerLocation.
	 */
	public int getDividerLocation() {
		return dividerLocation;
	}
	
	/**
	 * Sets the dividers new location.
	 * @param loc	The dividerLocation to set.
	 */
	public void setDividerLocation(int loc) {
		dividerLocation = loc;
		isDirty = true;
		
	}
	
}
