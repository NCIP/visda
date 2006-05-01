package edu.vt.cbil.visda.view;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

import java.security.AccessControlException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;
import javax.swing.JTable;

/**
 * The class to view data with table
 * 
 * @author Jiajing Wang
 * @version visda_v0.1
 */
public class TableViewer extends JPanel implements IViewer {
	
	private JPopupMenu popup; 
	
	private JTable table;
	
	private String tableTitle;
	
	JFileChooser tableFileChooser;
	
	/** Creates a new instance of PCA2DViewer */
    public TableViewer(JTable table, String title) {
    	setBackground(Color.white);
	    setForeground(Color.black);
    	this.table = table;
    	this.tableTitle = title;
    	
    	GBA gba = new GBA();
    	JPanel tableArea = new JPanel();
    	tableArea.setLayout(new GridBagLayout());
    	gba.add(tableArea, table.getTableHeader(), 0, 0, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(0,0,0,0)), 0, 0);
    	gba.add(tableArea, table, 0, 1, 1, 1, 1, 1, GBA.B, GBA.C, (new Insets(0,0,0,0)), 0, 0);
    	tableArea.setBorder(BorderFactory.createTitledBorder(title));
    	
    	setLayout(new GridBagLayout());
    	gba.add(this, tableArea, 0, 0, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(0,0,0,0)), 0, 0);
    	
    	popup = new JPopupMenu();
        addMenuItems(popup);
        add(popup);    	
    }
    
    /**
     * Adds the viewer specific menu items.
     */
    private void addMenuItems(JPopupMenu menu) {
        Listener listener = new Listener();
        JMenuItem menuItem;
        
        menuItem = new JMenuItem("Save Table..."); //, GUIFactory.getIcon("save16.gif"));
        menuItem.setEnabled(true);
        menuItem.setActionCommand("save_cmd");
        menuItem.addActionListener(listener);
        menu.add(menuItem);
        //menu.addSeparator();
        
        boolean denySaveSecurity = false;
    	tableFileChooser = new JFileChooser();
    	try {
            tableFileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".txt");
                }

                public String getDescription() {
                    return "TXT file";
                }
            });
            tableFileChooser.setSelectedFile(new File(tableTitle+".txt"));
        } catch (AccessControlException ace) {
            denySaveSecurity = true;
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
            //} else if (command.equals("select_cmd")) {
            //	onSelect();
            }
        }        
    }    
    
    /**
     * Saves selected genes.
     */
    private void onSave() {

        int returnVal = tableFileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	saveTableFile();   
        } else {
        	
        }
    	
    }
    
    void saveTableFile() {
    	java.io.File file = tableFileChooser.getSelectedFile();
    	try
		{
			FileWriter fwriter = new FileWriter(file);
			BufferedWriter bwriter = new BufferedWriter(fwriter);
			
			for(int j = 0; j < table.getColumnCount(); j++) {
				String xml = "";
				xml += table.getColumnName(j);
				bwriter.write(xml);
				bwriter.write('\t');
			}
			bwriter.newLine();	
				
			for(int i = 0; i < table.getRowCount(); i++) {
				//xml+= "<row number = '" + i + "'>\n";
				for(int j = 0; j < table.getColumnCount(); j++) {
					String xml = "";
					xml += table.getValueAt(i,j);
					bwriter.write(xml);
					bwriter.write('\t');
				}
				//xml += "\n";
				bwriter.newLine();
			}	

			//fwriter.write(xml);
			//fwriter.close();
			
			bwriter.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
    }
    
    /**
     * Returns the viewer plot.
     */
    public JTable getTable() {
        return table;
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
    
    /** Returns the corner component corresponding to the indicated corner,
     * posibly null
     */
    public JComponent getCornerComponent(int cornerIndex) {
        return null;
    }
    
    
    /** Returns a component to be inserted into scroll pane header.
     */
    public JComponent getHeaderComponent() {
        return null;
    }
    
    /** Invoked by the framework to save or to print viewer image.
     */
    public BufferedImage getImage() {
        return null;
    }
    
    /** Returns a component to be inserted into the scroll pane row header
     */
    public JComponent getRowHeaderComponent() {
        return null;
    }
    
    /** Invoked when the framework is going to be closed.
     */
    public void onClosed() {
    }
    
    /** Invoked by the framework when data is changed,
     * if this viewer is selected.
     * @see IData
     */
    //public void onDataChanged(IData data) {
    //    setData(data);        
    //}
    
    /** Invoked by the framework when this viewer was deselected.
     */
    public void onDeselected() {
    }
    
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
    
    /** Returns int value indicating viewer type
     * Cluster.GENE_CLUSTER, Cluster.EXPERIMENT_CLUSTER, or -1 for both or unspecified
     */
    public int getViewerType() {
        return -1;
    }
    
    /** Returns the viewer's clusters or null
     */
    public int[][] getClusters() {
        return null;
    }
}
