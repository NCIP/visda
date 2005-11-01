package edu.vt.cbil.visda.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;

import edu.vt.cbil.visda.data.FloatDataSet;import edu.vt.cbil.visda.view.HistoryViewer.HistoryListener;
;

/**
 * The class to view Raw DataSet
 * 
 * @author Jiajing Wang
 */
public class RawDataViewer extends ViewerAdapter {
	/** Contains the text contents of the viewer.
     */    
    private JTextArea content;
    /** Context menu.
     */    
    private JPopupMenu menu;
    
    JFileChooser chooser;
    
    /** Creates a new instance of HistoryViewer */
    public RawDataViewer(FloatDataSet floatData) {
    	// initialize content
    	content = new JTextArea();
        content.setEditable(false);
        content.setMargin(new Insets(10,10,10,10)); 
        content.setBackground(Color.white);
        content.setForeground(Color.DARK_GRAY);
        content.setFont(new Font("Arial", Font.BOLD, 12));
        
        // initialize popupMenu
        menu = new JPopupMenu();
        JMenuItem saveItem = new JMenuItem("Save to File"); //, GUIFactory.getIcon("save16.gif"));
        saveItem.setActionCommand("save");
        saveItem.addActionListener(new Listener());
        menu.add(saveItem);  
        
        chooser = new JFileChooser();
        boolean denySaveSecurity = false;
    	try {
            chooser.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".txt");
                }

                public String getDescription() {
                    return "TXT file";
                }
            });
            chooser.setSelectedFile(new File("rawDataSummary.txt"));
        } catch (AccessControlException ace) {
            denySaveSecurity = true;
        }
        
        addContent(floatData);
        
        content.addMouseListener(new Listener());
    }
   
    private void addContent(FloatDataSet floatData) {
    	content.append("File Name:\t\t"+floatData.getDatasetName()+"\n\n");
    	
    	content.append("Sample Number:\t"+((ArrayList)floatData.getAllSamples()).size()+"\n\n");
    	
    	content.append("Feature Number:\t"+((ArrayList)floatData.getAllGenes()).size()+"\n\n");
    	
    	content.append("Sample Discriptions:\t");
    	int size1 = ((ArrayList) floatData.getLabelKeyNames()).size();
    	for (int i=0; i<size1-1; i++) {
    		content.append(((ArrayList) floatData.getLabelKeyNames()).get(i)+",  ");
    	}
    	content.append(((ArrayList) floatData.getLabelKeyNames()).get(size1-1)+"\n\n");
    	
    	content.append("Feature Discriptions:\t");
    	int size2 = ((ArrayList) floatData.getAttributeNames()).size();
    	for (int i=0; i<size2-1; i++) {
    		content.append(((ArrayList) floatData.getAttributeNames()).get(i)+",  ");
    	}
    	content.append(((ArrayList) floatData.getAttributeNames()).get(size2-1)+"\n\n");
    	
    }
   
    /** Returns the viewers content.
     * @return	JComponent
     */    
    public JComponent getContentComponent() {
        return content;
    }
    
    /** Listener is responsible for listening to mouse
     * and menu events
     */    
    public class Listener extends MouseAdapter implements ActionListener {
        /** Responds to press events.
         */        
        public void mousePressed(MouseEvent me) {
            if(me.isPopupTrigger()) {
                menu.show(content, me.getX(), me.getY());
            }
        }
        
        /** Responds to mouse released events.
         */        
        public void mouseReleased(MouseEvent me) {
            if(me.isPopupTrigger()) {
                menu.show(content, me.getX(), me.getY());
            }
        }
        /** Responds to menu events.
         */        
        public void actionPerformed(ActionEvent ae) {
            if(ae.getActionCommand().equals("save")){
            	onSave();
            }
        }
          
    }
    
    /**
     * Saves the view.
     */
    private void onSave() {

        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	saveFile();   
        } else {
        	
        }
    	
    }
    
    /** Saves to file.
     */    
    private void saveFile() {
        String sep = System.getProperty("file.separator");
        
        try {
        	File file = chooser.getSelectedFile();
        	PrintWriter bfr = new PrintWriter(new FileWriter(file));

        	StringTokenizer stok = new StringTokenizer(content.getText(), "\n\n");
                
        	while(stok.hasMoreTokens()){
        		bfr.println(stok.nextToken());
            }
                
        	bfr.flush();
        	bfr.close();
     
        } catch (IOException ioe) {
        	String msg = ioe.getMessage();
        	JOptionPane.showMessageDialog(content, "An error occurred while saving data attributes. \nMessage: "+msg, "Error Saving data attributes", JOptionPane.WARNING_MESSAGE);
        }
                   
    }
    
    /**
     * Returns the viewer popup menu.
     */
    public JPopupMenu getJPopupMenu() {
        return menu;
    }
    
}
