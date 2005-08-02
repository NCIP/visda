package edu.vt.cbil.visda;

import edu.vt.cbil.visda.data.DataManager;
import edu.vt.cbil.visda.data.DefaultSample;
import edu.vt.cbil.visda.data.FloatDataSet;
import edu.vt.cbil.visda.util.*;
import edu.vt.cbil.visda.comp.*;
import edu.vt.cbil.visda.view.*;

import javax.swing.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.io.*;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Created with Eclipse
 * Author: Huai Li
 * Date: July 14, 2005
 *
 */
public class MainFrame extends JFrame {

    /**
     * the title of <code>this</code> Frame
     */
    String title;

    /**
     * the version number of tihs application
     */
    String version;

    /**
     * The application's area to communicate progress or current status.
     */
    StatusBar statusBar;

    /**
     * the model of our application.
     */
    File selectedFile = null;

    Action newAction;
    Action viewAction;
    Action openRawAction;
    Action analysisDataAction;
    Action exitAction;

    /**
     * The file chooser shared by all the inner class actions.
     */
    JFileChooser chooser;
    DataManager dataManager;
    static MainFrame current;
    private Properties properties;
    public static final String GENEDATASET_LOCATION_KEY = "geneDataSet.location";
    
    int numLevels;
    int numSelectedFeatures;
	int clusterType;
	String distCon, linkCon;
	
    /**
     * Create a new instance with the given title and version
     * @param aTitle the name of the application and <code>this</code> window
     * @param aVersion e.g. "2.0"
     */
    public MainFrame( String aTitle, String aVersion, boolean visibleFlag ) {
        super( aTitle );
        title = aTitle;
        version = aVersion;
        chooser = new JFileChooser();
        properties = new Properties();
        initialize();
        setVisible( visibleFlag );
        current = this;
    }

    /**
     * Take care of any loose ends before closing.
     */
    private void cleanUp() {

    }

    private void createActions() {
        newAction = new NewAction();
        viewAction = new ViewAction();
        openRawAction = new OpenRawAction();
        analysisDataAction = new AnalysisDataAction();   
        exitAction = new ExitAction();
    }

    /**
     * Creates the file menu for the application.
     */
    private void createFileMenu() {
        JPopupMenu.setDefaultLightWeightPopupEnabled( false );
        JMenuBar menu = new JMenuBar();
        JMenu fileMenu = new JMenu( "File" );

        JMenuItem newItem = new JMenuItem( newAction );
        fileMenu.add( newItem );

        JMenuItem viewItem = new JMenuItem( viewAction );
        fileMenu.add( viewItem );
        
        JMenuItem openRawItem = new JMenuItem( openRawAction );
        fileMenu.add( openRawItem );
        
        JMenuItem analysisItem = new JMenuItem( analysisDataAction );
        fileMenu.add( analysisItem );
           
        JMenuItem exitItem = new JMenuItem( exitAction );
        fileMenu.add( exitItem );

        menu.add( fileMenu );
        setJMenuBar( menu );

    }

  

    /**
     * Change cursor to the hour-glass
     */
    public static void showBusy() {
        current.setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );

    }

    /**
     * Change cursor to the default
     */
    public static void showReady() {
        current.setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ));

    }
     
      
    /**
     * Complete ( possibly lengthy ) start up tasks.
     */
    private void initialize() {
        setSize( 400, 400 );
        dataManager = DataManager.getInstance();
        addWindowListener( new SimpleWindowAdapter() );
        createActions();
        createFileMenu();
        viewAction.setEnabled( false );
        analysisDataAction.setEnabled( false );
        Container contentPane = getContentPane();
        contentPane.setLayout( new BorderLayout() );
        statusBar = new StatusBar();
        contentPane.add( statusBar, BorderLayout.SOUTH );
    }

    /**
     * An adapter to end the program when the user closes the window.
     */
    public class SimpleWindowAdapter extends WindowAdapter {

        public void windowClosing( WindowEvent we ) {
            cleanUp();
            System.exit( 0 );

        }

    }

    /**
     * a class to encapsulate the task of closing the application.
     */
    private class ExitAction extends AbstractAction {

        public ExitAction() {
            super( "Exit the application" );
            putValue( AbstractAction.NAME, "Exit" );
            putValue( AbstractAction.ACCELERATOR_KEY,
                      KeyStroke.getKeyStroke( KeyEvent.VK_E, ActionEvent.CTRL_MASK, false ) );

        }

        public void actionPerformed( ActionEvent ae ) {
            cleanUp();
            System.exit( 0 );

        }

    }


    /**
     * a class to encapsulate the task of closing the application.
     */
    private class NewAction extends AbstractAction {

        public NewAction() {
        	super( "Config VISDA Parameters" );
            putValue( AbstractAction.NAME, "Config VISDA Parameters" );
            putValue( AbstractAction.ACCELERATOR_KEY,
                      KeyStroke.getKeyStroke( KeyEvent.VK_N, ActionEvent.CTRL_MASK, false ) );

        }

        public void actionPerformed( ActionEvent ae ) {
        	ConfigDialog dialog = new ConfigDialog( MainFrame.this );
        	numLevels = dialog.nLevels;
        	numSelectedFeatures = dialog.nFeatures;
        	clusterType = dialog.clusterType;
        	distCon = dialog.distCon;
        	linkCon = dialog.linkCon;
        }

    }
 
    private class ViewAction extends AbstractAction {

        public ViewAction() {
            super( "View" );
            putValue( AbstractAction.ACCELERATOR_KEY,
                      KeyStroke.getKeyStroke( KeyEvent.VK_I, ActionEvent.CTRL_MASK, false ) );

        }

        public void actionPerformed( ActionEvent ae ) {
        	//        put your action here!
        }
    }

    public class AnalysisDataAction extends AbstractAction {
	
    	/**
    	 * Required empty constructor for dynamically loaded class
    	 *
    	 */
    	public AnalysisDataAction() {
    		super();
            putValue( AbstractAction.NAME, "Perform VISDA Analysis" );
            putValue( AbstractAction.ACCELERATOR_KEY,
                      KeyStroke.getKeyStroke( KeyEvent.VK_Z, ActionEvent.CTRL_MASK, false ) );    
    	}
   	 		
    	/**
    	 * Create a quantized version of the current raw dataset and
    	 * make it the new or current quantized dataset in the system.
    	 */
    	public void actionPerformed(ActionEvent e) {
    		FloatDataSet floatData = 
    			(FloatDataSet)dataManager.getCurrentRawDataSet();
    		
    		// get your data and start the analysis here
    		ArrayList samples = (ArrayList) floatData.getAllSamples();
    		ArrayList genes = (ArrayList) floatData.getAllGenes();
    		ArrayList attributes = (ArrayList) floatData.getAttributeNames();
    		int numGenes = genes.size();
    		int numSamples = samples.size();
    		double[][] data = new double[numGenes][numSamples];
    		
    		for (int i = 0; i < numGenes; i++) {
    			for (int j = 0; j < numSamples; j++) {
    				data[i][j] = (double) floatData.getDouble(i, j); 
    			}
    		}
    		
    		//example to get class label
    		int [] label = new int[numSamples];
    		for (int j = 0; j < numSamples; j++) {
				String labeldata  = (((DefaultSample) samples.get(j)).getLabel());
				Integer integerObject = new Integer(labeldata);
				label[j] = integerObject.intValue();
			}
    		
    		//-----------------------------------
    		// the following added by jjw
    		//----------------------------------- 		
    		double[][] dataSelected = new double[numSamples][numSelectedFeatures];
    		
    		// feature selection
    		FeaturePreSelector preSel = new FeaturePreSelector(1, data, numGenes, numSamples, numSelectedFeatures, label);
    		preSel.snrSupervised();
    		//preSel.print();
    		dataSelected = preSel.selectedData;
    		
    		// data projection and EM
    		VeRun run = new VeRun(dataSelected, label, numSamples, numSelectedFeatures, numLevels, 0.0, 3, 1, 0);
    		run.veExe();
    		
    		MainFrame.showBusy();
    		MainFrame.showReady();	
    		
    	}
    }
    
    /**
     * A class to encapsulte the task of opening a raw dataSet
     */
    private class OpenRawAction extends AbstractAction {
    	
        public OpenRawAction() {
            super( "Open a Tab-Delimited DataSet" );
            putValue( AbstractAction.NAME, "Open a Tab-Delimited DataSet" );
            putValue( AbstractAction.ACCELERATOR_KEY,
                       KeyStroke.getKeyStroke( KeyEvent.VK_R, ActionEvent.CTRL_MASK, false ) );
        }

        /**
         * Always enabled
         *
         */
        public void enableOrDisable() {
        	setEnabled( true );
        	
        }
        
        /**
         * Show user the file chooser, attempt to open the raw dataset and 
         * if successful display it in a new window.
         * 
         */
        public void actionPerformed( ActionEvent ae ) {
            String pathName = getLastOpenedPath();
            if ( pathName == null || pathName.length() == 0 ) {
                pathName = Platform.getUserHomeDirectory();

            }
            chooser.setDialogTitle( "Open a Tab-Delimited DataSet" );
            chooser.setCurrentDirectory( new File( pathName ).getParentFile() );
            if ( chooser.showOpenDialog( MainFrame.this ) ==
                    JFileChooser.APPROVE_OPTION               ) {
                try {
                    MainFrame.showBusy();
                    selectedFile = chooser.getSelectedFile();
                    setLastOpenedPath( selectedFile.getAbsolutePath() );
                    dataManager.setCurrentRawDataSet( selectedFile );
                    analysisDataAction.setEnabled( true );
                    
                } catch ( java.io.IOException iox ) {
                    DataManager.warning( selectedFile.getName() + " had this error:\n" +
                    					 iox.getMessage() );

                } catch ( UserCancelException ucx ) {
                	// thrown by dataManager.setCurrentDataSet( File file )
                	// user knows, fall through
                	
                } catch ( Exception x ) {
                	String msg = x.getMessage();
                	if ( msg == null )
                		msg = new String();
                	
                	x.printStackTrace();
                	DataManager.warning( "There was an unexpected exception while " + 
                			"opening " + selectedFile.getName() + "\n" + msg );
                	
                }	finally {
                	MainFrame.showReady();
                	
                }

            }

        }
        
        /**
         * Access data manager's DATASET_LOCATION_KEY
         * @return value of data manager's DATASET_LOCATION_KEY
         */
        private String getLastOpenedPath() {
        	if ( properties != null ) {
        		return (String)properties.getProperty(GENEDATASET_LOCATION_KEY) ;
        	}
        	return null;
        }

        /**
         * Set data manager's DATASET_LOCATION_KEY
         * @param path file path that the DataManager should save.
         * 
         */
        private void setLastOpenedPath( String path ) {
           properties.setProperty(GENEDATASET_LOCATION_KEY, path);
        }

    }
 	
}
