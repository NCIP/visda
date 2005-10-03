package edu.vt.cbil.visda.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.JDialog;

import org.jibble.epsgraphics.*;

import edu.vt.cbil.visda.data.DefaultSample;

import java.util.Vector;

/**
 * The class to view multiple 2D-projection plots at a window
 * 
 * @author Jiajing Wang
 * @version visda_v0.1
 */
 // History Log:
 //  09/14/2005:		add zoom function
 // 					add label display function
 //  09/26/2005:		plot can be saved as eps file
 //  09/27/2005:        the saved png image will have the exact size of the plot
public class MultiPlotsViewer extends ViewerAdapter implements PropertyChangeListener {
	
	private JPanel plotArea;
	private JPopupMenu popup; 
	
	private Plot2DViewer plots[];
	
	private JFileChooser fileChooser;
	private String title;
	
	// the original size of plot
	private int oldW;
	private int oldH;
	
	/**
     * the name of analysised samples
     */
	private ArrayList sampleNames;
	
	/** Creates a new instance of PCA2DViewer */
    public MultiPlotsViewer(String title, String plotNames[], Plot2DViewer multiPlots[],
    						ArrayList samples) {
    	setBackground(Color.white);
	    setForeground(Color.black);
	    //setLayout()
	    this.title = title;
	    
	    plots = new Plot2DViewer[multiPlots.length];
	    plots = multiPlots;
	    //	  add projection panel
	    int rowNum = multiPlots.length/2;
		int remain = multiPlots.length%2;
		if (remain > 0) {
			rowNum += 1;
		}	 
        plotArea = new JPanel(new GridLayout(rowNum, 2));
        
        for (int i=0; i<plots.length; i++) {
        	  //plots[i].removePlotToolBar();
			  String subTitle;
			  subTitle = plotNames[i];
			  plots[i].setBorder(BorderFactory.createTitledBorder(subTitle));
			  plotArea.add(plots[i]);
		}
    	add(plotArea, BorderLayout.LINE_START);
    	
    	sampleNames = samples;
    	
    	popup = new JPopupMenu();
        addMenuItems(popup);
        add(popup);
        
        // get the original size of plots for zoom function
        oldW = plots[0].plotPanel.plotCanvas.getSize().width;
        oldH = plots[0].plotPanel.plotCanvas.getSize().height;

    }
    
    
    /**
     * 
     */
    public void setSampleNames(ArrayList samples) {
    	sampleNames = samples;
    }
    
    /**
     * Adds the viewer specific menu items.
     */
    private void addMenuItems(JPopupMenu menu) {
        Listener listener = new Listener();
        JMenuItem menuItem;
        
        menuItem = new JMenuItem("Save projections..."); //, GUIFactory.getIcon("save16.gif"));
        menuItem.setEnabled(true);
        menuItem.setActionCommand("save_cmd");
        menuItem.addActionListener(listener);
        menu.add(menuItem);
        //menu.addSeparator();
        
        boolean denySaveSecurity = false;
    	fileChooser = new JFileChooser();
    	try {
            fileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".png");
                }

                public String getDescription() {
                    return "Portable Network Graphic file";
                }
            });
            fileChooser.addChoosableFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".eps");
                }

                public String getDescription() {
                    return "Encapsulated Postscript file";
                }
            });
            fileChooser.addPropertyChangeListener(this);
            fileChooser.setSelectedFile(new File(title+".eps"));
        } catch (AccessControlException ace) {
            denySaveSecurity = true;
        }
        
        menuItem = new JMenuItem("Zoom..."); //, GUIFactory.getIcon("save16.gif"));
        menuItem.setEnabled(true);
        menuItem.setActionCommand("zoom_cmd");
        menuItem.addActionListener(listener);
        menu.add(menuItem);
        
        //if (!this.geneViewer) {
        menu.addSeparator();
        //add label display menu
        JMenu labelDisplayMunu = new JMenu( "Display Labels" );
        JMenuItem subMenuItem;
        Vector keys =  ((DefaultSample) sampleNames.get(0)).getSampleKeys();
        String[] displayKeys = new String[keys.size()+2];
        keys.copyInto(displayKeys);
        displayKeys[keys.size()] = "Default Sample Name";
        displayKeys[keys.size()+1] = "None";
        for (int i=0; i<displayKeys.length; i++) {
        	subMenuItem = new JMenuItem(new LabelDisplayAction(displayKeys[i]));                 	
        	labelDisplayMunu.add(subMenuItem);
        }
        labelDisplayMunu.setEnabled(true);
        menu.add(labelDisplayMunu);          
        //}        
        
        /*menuItem = new JCheckBoxMenuItem("Larger point size");
        menuItem.setEnabled(true);
        menuItem.setActionCommand(SHOW_LARGER_POINTS_CMD);
        menuItem.addActionListener(listener);
        menu.add(menuItem);           
        
        menuItem = new JCheckBoxMenuItem("Show tick marks and labels");
        menuItem.setEnabled(true);
        menuItem.setSelected(true);
        menuItem.setActionCommand(SHOW_TICK_LABELS_CMD);
        menuItem.addActionListener(listener);
        menu.add(menuItem);*/        
        
    }  
    
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        // Try to update filename after changing the filefilter.
        // But it doesn't work...
        if (JFileChooser.FILE_FILTER_CHANGED_PROPERTY.equals(prop)) {
        	String disp = fileChooser.getFileFilter().getDescription();
        	if (disp.equals("Portable Network Graphic file")) {
        		fileChooser.setSelectedFile(new File(title+".png"));
        	} else if (disp.equals("Encapsulated Postscript file")) {
        		fileChooser.setSelectedFile(new File(title+".eps"));
        	}
        }
       
    }
    
    /**
     * The listener to listen to menu items events.
     */
    private class Listener extends MouseAdapter implements ActionListener {
    	
        public void actionPerformed(ActionEvent event) {
            String command = event.getActionCommand();
            if (command.equals("save_cmd")) {
                onSave();
            } else if (command.equals("zoom_cmd")) {            
            	ZoomDialog zoomDialog = new ZoomDialog(new Frame());
            	onZoom(zoomDialog.scale);
            }
        }      
        
        
    }    
    
    private int prevScale = 100;
    
    /**
     * A class to encapsulte the task of opening a raw dataSet
     */
    private class ZoomDialog extends JDialog {
    	
    	private JSpinner zoomSpinner;
    	public int scale;
    	//private int prevScale;
    	
    	public ZoomDialog( Frame parent ) {
            super( parent, "Zoom Plot Dialog", true );
            Container contentPane = getContentPane();
            contentPane.setLayout(new GridLayout(2, 2));
            //addWindowListener( new WindowAdapter() );

        	JPanel zoomPanel = new JPanel();
            JLabel zoomjLabel = new JLabel("Zoom to Percent : ");
            zoomSpinner = new JSpinner( new SpinnerNumberModel( 100, 50, 300, 25));
            zoomPanel.add(zoomjLabel);
            zoomSpinner.setName("zoomSpinner");
            zoomPanel.add(zoomSpinner);
            contentPane.add(zoomPanel);
            
            JPanel buttonPanel = new JPanel();
            JButton okButton = new JButton( new OkAction() );
            JButton cancelButton = new JButton( new CancelAction() );
            buttonPanel.add(okButton);
            buttonPanel.add(cancelButton);
            contentPane.add(buttonPanel);
            
            pack();
            setVisible( true );
        }
    	
    	private class OkAction extends AbstractAction {
            public OkAction() {
                super( "OK" );
            }
            public void actionPerformed( ActionEvent ae ) {          	
                setVisible( false );
                //dispose();             
                scale = ((SpinnerNumberModel)zoomSpinner.getModel()).getNumber().intValue(); 
                prevScale = scale;
            }
        }
    	
    	private class CancelAction extends AbstractAction {
            public CancelAction() {
                super( "Cancel" );
            }
            public void actionPerformed( ActionEvent ae ) {
                setVisible( false );
                scale = prevScale;
            }
        }
    }
    
    
    /**
     * Saves selected genes.
     */
    private void onSave() {

        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	saveGraphicFile();   
        } else {
        	
        }
    	
    }
    
    private void saveGraphicFile() {
    	java.io.File file = fileChooser.getSelectedFile();
    	/*FileFilter curFilter = fileChooser.getFileFilter();
    	char[] nameArray = file.getName().toCharArray();
    	boolean hasExtension = false;
    	for (int i=0; i<nameArray.length; i++) {
    		if (nameArray[i] == '.') {
    			hasExtension = true;
    		}
    	}
    	if (!hasExtension) {
    		if (curFilter.getDescription().equals("Portable Network Graphic file")) {
    			file = new File(file.getName()+".png");
    		} else if (curFilter.getDescription().equals("Encapsulated Postscript file")) {
    			file = new File(file.getName()+".eps");
    		}
    	}*/
        try {      	
        	if (file.getName().endsWith(".png")) {
        		int x = plotArea.getX();
        		int y = plotArea.getY();
        		plotArea.setBounds(0, 0, plotArea.getWidth(), plotArea.getHeight());
        		Image image = createImage(plotArea.getWidth(), plotArea.getHeight());
            	paint(image.getGraphics());
            	image = new ImageIcon(image).getImage();

        		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        		Graphics g = bufferedImage.createGraphics();
        		g.drawImage(image, 0, 0, Color.WHITE, null);
        		g.dispose();
        		plotArea.setBounds(x, y, plotArea.getWidth(), plotArea.getHeight());
        		try {
            		ImageIO.write((RenderedImage) bufferedImage, "PNG", file);
                } catch (IllegalArgumentException ex) {
                }
        	} else if (file.getName().endsWith(".eps")) {
        		FileOutputStream outputStream = new FileOutputStream(file);
        		EpsGraphics2D g = new EpsGraphics2D("Example", outputStream, 0, 0, plotArea.getWidth(), plotArea.getHeight());
        		plotArea.print(g);
        		g.flush();
        		g.close();
        	}
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null, "Save failed : " + e.getMessage(), 
            		"Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Zoom plot.
     */
    private void onZoom(int percent) {
    	double scale = percent / 100.0;   	
    	int newW = (int) (scale * (double)oldW);
    	int newH = (int) (scale * (double)oldH);
    	Dimension newD = new Dimension(newW, newH);
    	for(int i=0; i<plots.length; i++) {
    		plots[i].plotPanel.plotCanvas.setPreferredSize(newD);
    		plots[i].plotPanel.setPreferredSize(newD);
    		plots[i].revalidate();	
	    }
    }
      
    
    /**
     * a class to encapsulate the task of displaying the sample labels.
     */
    private class LabelDisplayAction extends AbstractAction {
    	String labelKey;
    	
        public LabelDisplayAction(String keyName) {
        	super( "by "+keyName );
            putValue( AbstractAction.NAME, "by "+keyName );
            labelKey = keyName;
        }

        public void actionPerformed( ActionEvent ae ) {

        	for(int i=0; i<plots.length; i++) {   
        		String newName[] = new String[plots[i].plotPanel.plotCanvas.plots.size()];
        		for(int j=0; j<plots[i].plotPanel.plotCanvas.plots.size(); j++) {      			
        			String id = plots[i].plotPanel.plotCanvas.plots.get(j).getName();
        			Integer integerObject = new Integer(id);
        			int realIndex = integerObject.intValue()-1;
        			if (labelKey.equals("None")) {
        				newName[j] = "";
        			} else if (labelKey.equals("Default Sample Name")) {
        				newName[j] = ((DefaultSample) sampleNames.get(realIndex)).getId();
        			} else {
        				newName[j] = ((DefaultSample) sampleNames.get(realIndex)).getSampleLabel(labelKey);
        			}
        		}	
        		plots[i].plotPanel.plotCanvas.setAnnotation(newName);
        		plots[i].plotPanel.plotCanvas.ActionMode = plots[i].plotPanel.plotCanvas.ANNOTATION;
        	}
        }
    }
    
    
    /**
     * Returns the viewer plot.
     */
    public Plot2DViewer[] getPlots() {
        return plots;
    }
    
    /**
     * Returns the viewer popup menu.
     */
    public JPopupMenu getJPopupMenu() {
        return popup;
    }
    
    
    /** Returns a component to be inserted into scroll pane view port.
     */
    public JComponent getContentComponent() {
        return this;
    }
    
    
    /** Invoked by the framework when data is changed,
     * if this viewer is selected.
     * @see IData
     */
    //public void onDataChanged(IData data) {
    //    setData(data);        
    //}
    
    
    /** Invoked by the framework when display menu is changed,
     * if this viewer is selected.
     * @see IDisplayMenu
     */
    //public void onMenuChanged(IDisplayMenu menu) {
    //}
    
    /** Invoked by the framework when this viewer is selected.
     */
    /*public void onSelected(IFramework framework) {
        this.framework = framework;
        this.frame = framework.getFrame();
        setData(framework.getData());   
        
        //In case it is viewed after serialization
        if(popup == null){
            popup = createJPopupMenu(); 
            DefaultMutableTreeNode node = framework.getCurrentNode();
            if(node != null){
                if(node.getUserObject() instanceof LeafInfo){
                    LeafInfo leafInfo = (LeafInfo) node.getUserObject();
                    leafInfo.setPopupMenu(this.popup);
                }
            }
        }         
    }*/
    
    
}