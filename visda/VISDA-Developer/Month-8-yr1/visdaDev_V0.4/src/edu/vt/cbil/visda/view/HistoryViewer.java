package edu.vt.cbil.visda.view;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import java.security.AccessControlException;

import java.text.DateFormat;

import java.util.Date;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;


/**
 * The class to view history log
 * 
 * @author Jiajing Wang
 */
public class HistoryViewer extends ViewerAdapter {
	/** Contains the text contents of the viewer.
     */    
    private JTextArea content;
    /** Context menu.
     */    
    private JPopupMenu menu;
    
    JFileChooser chooser;
    
    /** Creates a new instance of HistoryViewer */
    public HistoryViewer() {
    	// initialize content
    	content = new JTextArea();
        content.setEditable(false);
        content.setMargin(new Insets(10,10,10,10)); 
        content.setBackground(Color.white);
        content.setForeground(Color.DARK_GRAY);
        content.setFont(new Font("Arial", Font.BOLD, 12));
        
        // initialize popupMenu
        menu = new JPopupMenu();
        JMenuItem saveItem = new JMenuItem("Save History to File"); //, GUIFactory.getIcon("save16.gif"));
        saveItem.setActionCommand("save");
        saveItem.addActionListener(new HistoryListener());
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
            chooser.setSelectedFile(new File("history.txt"));
        } catch (AccessControlException ace) {
            denySaveSecurity = true;
        }
        
        addHistory("Open VISDA Viewer");
      
        content.addMouseListener(new HistoryListener());
    }
    
    
    /** Adds a history entry
     * @param msg Messge to add to history.
     */    
    public void addHistory(String msg) {
        content.append(getDateStamp()+msg+"\n\n");
        
    }

    /** Returns the current date/time stamp in <CODE>String</CODE>
     * format.
     */    
    private String getDateStamp() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat format = DateFormat.getDateTimeInstance();
        format.setTimeZone(TimeZone.getDefault());
        return "["+format.format(date) + "]      ";
    }
    
    /** Returns the viewers content.
     * @return	JComponent
     */    
    public JComponent getContentComponent() {
        return content;
    }
        
    /** Writes the HistoryViewer to an <CODE>ObjectOutputStream</CODE>
     * @param oos ObjectOutputStream
     * @throws IOException
     */    
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject(content.getText());
    }
    
    /** Reads the HistoryViewer to an <CODE>ObjectOuputStream</CODE>
     *
     * @param ois ObjectInputStream
     * @throws IOException
     * @throws ClassNotFoundException
     */    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        String text = (String)ois.readObject();
        // initialize content
    	content = new JTextArea();
        content.setEditable(false);
        content.setMargin(new Insets(10,10,10,10));
        content.setBackground(new Color(252,255,168));  
        
        // initialize popupMenu
        menu = new JPopupMenu();
        JMenuItem saveItem = new JMenuItem("Save History to File"); //, GUIFactory.getIcon("save16.gif"));
        saveItem.setActionCommand("save");
        saveItem.addActionListener(new HistoryListener());
        menu.add(saveItem);  
        
        content.append(text);
        addHistory("Load Analysis From File");
        content.addMouseListener(new HistoryListener());
    }
    
   
    /** HistoryListener is responsible for listening to mouse
     * and menu events
     */    
    public class HistoryListener extends MouseAdapter implements ActionListener {
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
     * Saves the history.
     */
    private void onSave() {

        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	saveHistoryFile();   
        } else {
        	
        }
    	
    }
    
    /** Saves the history to file.
     */    
    private void saveHistoryFile() {
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
        	JOptionPane.showMessageDialog(content, "An error occurred while saving history. \nMessage: "+msg, "Error Saving History", JOptionPane.WARNING_MESSAGE);
        }
                   
    }
    
    /**
     * Returns the viewer popup menu.
     */
    public JPopupMenu getJPopupMenu() {
        return menu;
    }
}
