package edu.vt.cbil.visda;

import edu.vt.cbil.visda.data.*;
import edu.vt.cbil.visda.util.*;
import edu.vt.cbil.visda.comp.*;
import edu.vt.cbil.visda.view.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;

import java.io.*;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Created with Eclipse
 * @author Huai Li, Jiajing Wang
 * @version visda_v0.4
 * <p>
 * Version Log:
 * 	<br>visda_v0.1 (07/30/2005):	The file is created and basic function is implemented.
 * 	<br>visda_v0.2 (08/30/2005):  	Add view tree.
 * 	<br>visda_v0.3 (09/29/2005):  	Add history log.	
 *  <br>visda_v0.4 (10/29/2005):  	Implement MDL function.
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

    JMenuBar menu;
    JMenu fileMenu;
    JMenu analysisMenu;
    JMenu configMenu;
    JMenu labelConfigMenu;
    
    Action configAction;
    Action labelConfigAction;
    Action viewAction;
    Action openRawAction;
    Action openCaArrayAction;
    Action analysisDataAction;
    Action exitAction;
    
    //Action visualizeAction;
    Action subClusterAction;

    /**
     * The file chooser shared by all the inner class actions.
     */
    FilePreViewer fileViewer;
    JFileChooser chooser;
    DataManager dataManager;
    static MainFrame current;
    private Properties properties;
    public static final String GENEDATASET_LOCATION_KEY = "geneDataSet.location";
    
    ExperimentData experimentData;
    int numGenes;
	int numSamples;
	double[][] data;
	int [] label;
    int numLevels;
    int numSelectedFeatures;
	int clusterType;
	String distCon, linkCon;
	String labelKey;
	
	/**
	 * The main View Panel
	 */
	private MainViewer viewPanel;
	
	int levelCnt;
	
	// for display
	DisplayParam displaySetting;
	double thre_Zjk;
	int vl1 = 3;
	int vl2 = 1;
	int vl3 = 0;
	int flag_tc = 0;
	double[] R;
	int blobSize = 2;
	
    FloatDataSet floatData;
    
    
    /**
     * Create a new instance with the given title and version
     * @param aTitle the name of the application and <code>this</code> window
     * @param aVersion e.g. "2.0"
     */
    public MainFrame( String aTitle, String aVersion, boolean visibleFlag ) {
        super( aTitle );
        title = aTitle;
        version = aVersion;
        //chooser = new JFileChooser();
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
        configAction = new ConfigAction();
        //labelConfigAction = new LabelConfigAction();
        viewAction = new ViewAction();
        openRawAction = new OpenRawAction();
        openCaArrayAction = new OpenCaArrayAction();
        analysisDataAction = new AnalysisDataAction();   
        exitAction = new ExitAction();
        subClusterAction = new SubClusterAction();
        //visualizeAction = new VisualizeAction();
    }

    /**
     * Creates the file menu for the application.
     */
    private void createFileMenu() {
        JPopupMenu.setDefaultLightWeightPopupEnabled( false );
        
        fileMenu = new JMenu( "File" );

        JMenuItem openRawItem = new JMenuItem( openRawAction );
        fileMenu.add( openRawItem );
        
        JMenuItem openCaArrayItem = new JMenuItem( openCaArrayAction );
        fileMenu.add( openCaArrayItem );
        
        JMenuItem viewItem = new JMenuItem( viewAction );
        fileMenu.add( viewItem );
           
        JMenuItem exitItem = new JMenuItem( exitAction );
        fileMenu.add( exitItem );

        menu.add( fileMenu );

    }

    // added by JJW
    /**
     * Creates the configuration menu for the application.
     */
    private void createConfigMenu() {
        JPopupMenu.setDefaultLightWeightPopupEnabled( false );
        
        configMenu = new JMenu( "Config" );

        JMenuItem configItem = new JMenuItem( configAction );
        configMenu.add( configItem );

        menu.add( configMenu );

    }
    

    /**
     * Creates the analysis menu for the application.
     */
    private void createAnalysisMenu() {
        JPopupMenu.setDefaultLightWeightPopupEnabled( false );
        //JMenuBar menu = new JMenuBar();
        analysisMenu = new JMenu( "Analysis" );

        JMenuItem analysisItem = new JMenuItem( analysisDataAction );
        analysisMenu.add( analysisItem );
        
        //JMenuItem visualizeItem = new JMenuItem( visualizeAction );
        //analysisMenu.add( visualizeItem );
        //analysisMenu.addSeparator();
        
        JMenuItem clusterItem = new JMenuItem( subClusterAction );
        analysisMenu.add( clusterItem );
        
        menu.add( analysisMenu );
        //setJMenuBar( menu );

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
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize( screenSize.width*4/5, screenSize.height*4/5 );
        dataManager = DataManager.getInstance();
        SimpleWindowAdapter eventListener = new SimpleWindowAdapter();
        addWindowListener(eventListener);
        createActions();
        menu = new JMenuBar();
        createFileMenu();
        createConfigMenu();
        createAnalysisMenu();
        setJMenuBar( menu );
        viewAction.setEnabled( false );
        //openCaArrayAction.setEnabled( false );
        configMenu.setEnabled( false );
        configAction.setEnabled( false );
        analysisMenu.setEnabled( false );
        analysisDataAction.setEnabled( false );
        subClusterAction.setEnabled( false );
        Container contentPane = getContentPane();
        contentPane.setLayout( new BorderLayout() );
        statusBar = new StatusBar();
        contentPane.add( statusBar, BorderLayout.SOUTH );
        
        // added by jjw
        viewPanel = new MainViewer(this);
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

    private boolean configDone = false;

    /**
     * a class to encapsulate the task of configuring the main parameters.
     */
    private class ConfigAction extends AbstractAction {

        public ConfigAction() {
        	super( "Config VISDA Parameters" );
            putValue( AbstractAction.NAME, "Configure Parameters" );
            putValue( AbstractAction.ACCELERATOR_KEY,
                      KeyStroke.getKeyStroke( KeyEvent.VK_N, ActionEvent.CTRL_MASK, false ) );

        }

        public void actionPerformed( ActionEvent ae ) {
        	ArrayList labelKeys = (ArrayList) floatData.getLabelKeyNames();
        	String slabelKeys[] = new String[labelKeys.size()];
        	for (int i=0; i<slabelKeys.length; i++) {
        		slabelKeys[i] = (String) labelKeys.get(i);
        	}
        	ConfigDialog dialog = new ConfigDialog( MainFrame.this, slabelKeys );
        	if (dialog.rtnVal == 1) {
        		numLevels = dialog.nLevels;
        		numSelectedFeatures = dialog.nFeatures;
        		clusterType = dialog.clusterType;
        		distCon = dialog.distCon;
        		linkCon = dialog.linkCon;
        		thre_Zjk = dialog.zjkThresh;
        		labelKey = dialog.labelKey;
        		
        		// get the raw data
        		experimentData = new ExperimentData(floatData);
        		experimentData.setMDLFlag(dialog.todoMDL);
    		
        		for(int i=0; i<experimentData.numRawSamples; i++) {
            		((DefaultSample) floatData.getDataColumns().get(i)).setSampleKey(labelKey);
            		((DefaultSample) experimentData.rawSampleNames.get(i)).setSampleKey(labelKey);
            	}
        		
        		if (numSelectedFeatures <= experimentData.numRawFeatures) {           		
        			// perform feature selection
        			DataPreProcessor dataPreProcessor = new DataPreProcessor(experimentData, clusterType, numSelectedFeatures);       		
        			// add data view
        			DataPreViewer dataView = new DataPreViewer(experimentData);
        			FeaturePreViewer featureView = new FeaturePreViewer(experimentData);
        			//viewPanel.addData(dataView.node);
        			//viewPanel.addData(featureView.node);   
        			if (!configDone) {  // if config has not been started
        				viewPanel.addAnalysisNode();
        				viewPanel.addDataView(dataView.node);
        				viewPanel.addDataView(featureView.node);
        				experimentData.initAnalysisResult(numLevels);
        				viewPanel.addHistory("Configuration begins. Label '"+labelKey+"' is selected; "+
        						numSelectedFeatures+" Features are selected");
        				configDone = true;
        			} else {
        				viewPanel.removeFstDataViewNode();
        				viewPanel.removeFstDataViewNode();
        				viewPanel.addDataView(dataView.node);
        				viewPanel.addDataView(featureView.node);
        				experimentData.initAnalysisResult(numLevels);
        				viewPanel.addHistory("Reconfiguration is performed. Label '"+labelKey+"' is selected; "+
        						numSelectedFeatures+" Features are selected");
        			}
        			
        			analysisMenu.setEnabled( true );
            		analysisDataAction.setEnabled( true );
            		subClusterAction.setEnabled( false );
            		
        		} else {
        			// If the selected feature number exceeds the number of total features
        			DataManager.warning("The selected feature number exceeded the total number of features  " + 
    					experimentData.numRawFeatures + ". \n Please choose a smaller one.\n" );
        		}       		
            
        	} else if (dialog.rtnVal == 0) {
        		
        	}
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
            putValue( AbstractAction.NAME, "Start Top-level Analysis" );
            putValue( AbstractAction.ACCELERATOR_KEY,
                      KeyStroke.getKeyStroke( KeyEvent.VK_Z, ActionEvent.CTRL_MASK, false ) ); 
    	}
   	 		
    	/**
    	 * Create a quantized version of the current raw dataset and
    	 * make it the new or current quantized dataset in the system.
    	 */
    	public void actionPerformed(ActionEvent e) {
    		
    		viewPanel.addHistory("Configuration is done. VISDA analysis begins...");
    		viewPanel.addClusterTopNode();
    		viewPanel.addLevelTopNode();
    		
    		R = new double[experimentData.numSamples];
    		for (int i=0; i<experimentData.numSamples; i++) {
    	  		  R[i] = 1.0;
    	  	  }
    		displaySetting = new DisplayParam(thre_Zjk, vl1, vl2, vl3, flag_tc, R, blobSize) ;
    		
    		levelCnt = 1;
    		
    		VisualizeAnalysis visualAnalysis = new VisualizeAnalysis(levelCnt, viewPanel, 
    				experimentData, displaySetting);   		
    				
    		// Once the analysis starts, 
    		// if reconfiguration performs, a new analysis node will be added. 
    		configDone = false;
    		analysisDataAction.setEnabled( false );
    		subClusterAction.setEnabled( true );
    		
    		
    		
    		MainFrame.showBusy();
    		MainFrame.showReady();	
    		
    	}
    }
    
    
    
    /**
     * 
     * @author Jiajing
     *
     */
    public class SubClusterAction extends AbstractAction {
    	
    	/**
    	 * Required empty constructor for dynamically loaded class
    	 *
    	 */
    	public SubClusterAction() {
    		super();
            putValue( AbstractAction.NAME, "Start Deeper-level Analysis" );
            putValue( AbstractAction.ACCELERATOR_KEY,
                      KeyStroke.getKeyStroke( KeyEvent.VK_S, ActionEvent.CTRL_MASK, false ) );    
    	}
   	 		
    	/**
    	 * Create a quantized version of the current raw dataset and
    	 * make it the new or current quantized dataset in the system.
    	 */
    	public void actionPerformed(ActionEvent e) {
    		
    		//boolean endFlags[] = experimentData.getSubLevelAttribute(levelCnt).getAllClusterEndFlags();
    		//boolean allFinished = true;
    		//for (int i=0; i<endFlags.length; i++) {
    			//if (endFlags[i] == false) {
    			//	allFinished = false;
    			//	break;
    			//}
    		//}
    		//if (allFinished == false) {	  			
    			ClusteringAnalysis clusteringAnalysis = new ClusteringAnalysis(experimentData, 
    				levelCnt);
    		
    			if (clusteringAnalysis.returnVal == 1) { //normal operation
    				levelCnt += 1;
    		
    				VisualizeAnalysis visualAnalysis = new VisualizeAnalysis(levelCnt, viewPanel, 
    						experimentData, displaySetting); 		
    			
    			} else {
    				
    			}
    		//}
    		
    		
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
            chooser = new JFileChooser();
            chooser.setDialogTitle( "Open a Tab-Delimited DataSet" );
            chooser.setCurrentDirectory( new File( pathName ).getParentFile() );           
            chooser.setFileFilter(new DataFileFilter());
            
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension newSize = new Dimension(screenSize.width*2/3, screenSize.height*2/3);
            chooser.setPreferredSize(newSize); // set preferred size
            Dimension chooserSize = chooser.getPreferredSize();
            fileViewer = new FilePreViewer(chooser);
            fileViewer.setPreferredSize(new Dimension(chooserSize.width*2/3, chooserSize.height));
            chooser.setAccessory(fileViewer);
            if ( chooser.showDialog( MainFrame.this, "Load Data" )
            		== JFileChooser.APPROVE_OPTION               ) {
                try {
                    MainFrame.showBusy();
                    selectedFile = chooser.getSelectedFile();
                    setLastOpenedPath( selectedFile.getAbsolutePath() );
                    //dataManager.setCurrentRawDataSet( selectedFile );
                    // changed by JJW
                    dataManager.setCurrentRawDataSet( selectedFile, fileViewer.firstDataLocation );
                    
                    floatData =	(FloatDataSet)dataManager.getCurrentRawDataSet();
                    	
                    // add a data root node into the view tree
                    viewPanel.addDataNode(floatData.getDatasetName());
                    RawDataViewer rawDataView = new RawDataViewer(floatData);
                    viewPanel.addRawDataView(new DefaultMutableTreeNode(new LeafInfo("MainView", rawDataView, rawDataView.getJPopupMenu())));
                    
                    // Once a new data is loaded, configuration should start and a new node will add. 
            		configDone = false;   
            		
            		configMenu.setEnabled( true );
                    configAction.setEnabled( true );
                    analysisMenu.setEnabled( false );
            		analysisDataAction.setEnabled( false );
            		subClusterAction.setEnabled( false );
            		
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
 	
    /**
     * A class to encapsulte the task of opening a raw dataSet
     */
    private class OpenCaArrayAction extends AbstractAction {
    	
        public OpenCaArrayAction() {
            super( "Open a CaArray DataSet" );
            putValue( AbstractAction.NAME, "Open a CaArray DataSet" );
            putValue( AbstractAction.ACCELERATOR_KEY,
                       KeyStroke.getKeyStroke( KeyEvent.VK_C, ActionEvent.CTRL_MASK, false ) );
        }

        /**
         * Always enabled
         *
         */
        public void enableOrDisable() {
        	setEnabled( false );
        }
        
        /**
         * Show user the file chooser, attempt to open the raw dataset and 
         * if successful display it in a new window.
         * 
         */
        public void actionPerformed( ActionEvent ae ) {
        	
        }
        
    }
    
}
