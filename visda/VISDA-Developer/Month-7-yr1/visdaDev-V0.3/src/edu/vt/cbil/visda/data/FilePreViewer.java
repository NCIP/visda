package edu.vt.cbil.visda.data;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.beans.*;
import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import edu.vt.cbil.visda.view.GBA;

/**
 * The class performs the data file pre-view for loading data
 * 
 * @author Jiajing Wang
 * @version visda_v0.1
 */
public class FilePreViewer extends JPanel implements PropertyChangeListener {

	public int firstDataLocation[];
	public String fieldNames[]; 
	File file = null;

	private StanfordFileLoaderPanel sflp;
	
	protected JButton cancelButton;
    protected JButton loadButton;
    protected JPanel buttonPanel;
    protected EventListener eventListener;
    
    public FilePreViewer(JFileChooser fc) {
        sflp = new StanfordFileLoaderPanel();
        fc.addPropertyChangeListener(this);
        setLayout(new BorderLayout(5,5));
		setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		add(sflp, BorderLayout.CENTER);
    }
	
    public void propertyChange(PropertyChangeEvent e) {
        boolean update = false;
        String prop = e.getPropertyName();

        //If the directory changed, don't show an image.
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
            file = null;
            update = false;

        //If a file became selected, find out which one.
        } else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
            file = (File) e.getNewValue();
            if (file == null) {
            	update = false;
            } else if (file.isFile() == true) {
            	update = true;
            }         	
        }

        //Update the preview accordingly.
        if (update) {
            //thumbnail = null;
            if (isShowing()) {
            	processStanfordFile(file);
                //loadImage();
                repaint();
            	sflp.setVisible(true);
            }
        }
    }
    
    private class EventListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            Object source = event.getSource();
            if (source == cancelButton) {
                //onCancel();
            } else if (source == loadButton) {
                //onLoad();
            }
        }
    }
    
	public boolean checkLoadEnable() {
        
        // Currently, the only requirement is that a cell has been highlighted
        
        int tableRow = sflp.getXRow() + 1; // Adjusted by 1 to account for the table header
        int tableColumn = sflp.getXColumn();
       
        if (tableColumn < 0) return false;
        
        TableModel model = sflp.getTable().getModel();
        String fieldSummary = "";
        fieldNames = new String[tableColumn];
        for (int i = 0; i < tableColumn; i++) {
            //  System.out.print(model.getColumnName(i) + (i + 1 == tableColumn ? "\n" : ", "));
            fieldSummary += model.getColumnName(i) + (i + 1 == tableColumn ? "" : ", ");            
            fieldNames[i] = model.getColumnName(i);
        }     
        sflp.setGeneFieldsText(fieldSummary);
        
        fieldSummary = "";
        for (int i=0; i<tableRow-1; i++) {
        	fieldSummary += model.getValueAt(i, sflp.getXColumn()-1) + (i + 1 == tableRow-1 ? "" : ", ");
        }
        sflp.setSampleFieldsText(fieldSummary);
        
        if (tableRow >= 1 && tableColumn >= 0) {
            //setLoadEnabled(true);
        	this.firstDataLocation = new int[2];
            this.firstDataLocation[0] = sflp.getXRow();
            this.firstDataLocation[1] = sflp.getXColumn();
            return true;           
        } else {
            //setLoadEnabled(false);
        	this.firstDataLocation = null;
            return false;          
        }
    }
    
	public void setLoadEnabled(boolean state) {
        loadButton.setEnabled(state);
    }
	
    public void processStanfordFile(File targetFile) {
        
        Vector columnHeaders = new Vector();
        Vector dataVector = new Vector();
        Vector rowVector = null;
        BufferedReader reader = null;
        String currentLine = null;
        
        //if (! validateFile(targetFile)) return;
        
        //sflp.setFileName(targetFile.getAbsolutePath());
        
        DefaultTableModel model = new DefaultTableModel() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        try {
            reader = new BufferedReader(new FileReader(targetFile), 1024 * 128);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        
        try {
            StringSplitter ss = new StringSplitter('\t');
            
            currentLine = reader.readLine();
            ss.init(currentLine);
            
            for (int i = 0; i < ss.countTokens()+1; i++) {
                columnHeaders.add(ss.nextToken());
            }
            
            model.setColumnIdentifiers(columnHeaders);
            int cnt = 0;
            while ((currentLine = reader.readLine()) != null && cnt < 100) {
                cnt++;
                ss.init(currentLine);
                rowVector = new Vector();
                for (int i = 0; i < ss.countTokens()+1; i++) {
                    try {
                        rowVector.add(ss.nextToken());
                    } catch (java.util.NoSuchElementException nsee) {
                        rowVector.add(" ");
                    }
                }
                
                dataVector.add(rowVector);
                model.addRow(rowVector);
            }
            
            reader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        sflp.setTableModel(model);
    }
    
	/*
	//
	 //		StanfordFileLoader - Internal Classes
	//
	 */	    
	private class StanfordFileLoaderPanel extends JPanel {
        JTable expressionTable;
        JLabel instructionsLabel;
        JScrollPane tableScrollPane;
        JPanel tablePanel;
        JPanel fileLoaderPanel;
        JTextField gfieldsTextField;
        JTextField sfieldsTextField;
        JPanel fieldsPanel;   
        
        private int xRow = -1;
        private int xColumn = -1;
        
        public StanfordFileLoaderPanel() {
            
            setLayout(new GridBagLayout());

            expressionTable = new JTable();
            expressionTable.setPreferredScrollableViewportSize(new Dimension(600, 200));
            expressionTable.setCellSelectionEnabled(true);
            expressionTable.setColumnSelectionAllowed(false);
            expressionTable.setRowSelectionAllowed(false);
            expressionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            expressionTable.getTableHeader().setReorderingAllowed(false);
            expressionTable.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent event) {
                    xRow = expressionTable.rowAtPoint(event.getPoint());
                    xColumn = expressionTable.columnAtPoint(event.getPoint());
                    checkLoadEnable();
                }
            });
            
            tableScrollPane = new JScrollPane(expressionTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
            
            instructionsLabel = new JLabel();
            instructionsLabel.setForeground(java.awt.Color.magenta);
            String instructions = "<html><b>Instruction</b>: <P>Click the upper-leftmost expression value. Click the <b>Load</b> button to finish.</html>";
            instructionsLabel.setText(instructions);
            
            tablePanel = new JPanel();
            tablePanel.setLayout(new BorderLayout());
            tablePanel.setBorder(BorderFactory.createTitledBorder("Expression Table"));
            tablePanel.add(tableScrollPane, BorderLayout.CENTER);
            tablePanel.add(instructionsLabel, BorderLayout.SOUTH);
            
            gfieldsTextField = new JTextField();
            gfieldsTextField.setEditable(false);
            gfieldsTextField.setForeground(Color.blue);
            gfieldsTextField.setFont(new Font("serif", Font.BOLD, 12));
            JPanel gfPanel = new JPanel();
            gfPanel.setLayout( new BorderLayout() );
            gfPanel.setBorder(BorderFactory.createTitledBorder("Gene Annotation Fields"));
            gfPanel.add(gfieldsTextField, BorderLayout.CENTER);
            
            sfieldsTextField = new JTextField();
            sfieldsTextField.setEditable(false);
            sfieldsTextField.setForeground(Color.blue);
            sfieldsTextField.setFont(new Font("serif", Font.BOLD, 12));
            JPanel sfPanel = new JPanel();
            sfPanel.setLayout( new BorderLayout() );
            sfPanel.setBorder(BorderFactory.createTitledBorder("Sample Annotation Fields"));
            sfPanel.add(sfieldsTextField, BorderLayout.CENTER);
            
            fieldsPanel = new JPanel();
            fieldsPanel.setLayout(new BorderLayout());
            fieldsPanel.add(gfPanel, BorderLayout.NORTH);
            fieldsPanel.add(sfPanel, BorderLayout.SOUTH);
           
            setLayout(new BorderLayout());
            add(tablePanel, BorderLayout.CENTER);
            add(fieldsPanel, BorderLayout.SOUTH);
            
        }
        
        //public void openDataPath() {
         //   fileTreePane.openDataPath();
        //}
        
        public JTable getTable() {
            return expressionTable;
        }
        
        public int getXColumn() {
            return xColumn;
        }
        
        public int getXRow() {
            return xRow;
        }
        
        
        public void setTableModel(TableModel model) {
            expressionTable.setModel(model);
            int numCols = expressionTable.getColumnCount();
            for(int i = 0; i < numCols; i++){
                expressionTable.getColumnModel().getColumn(i).setMinWidth(75);
            }
        }
        
        public void setGeneFieldsText(String fieldsText) {
            gfieldsTextField.setText(fieldsText);
        }
        
        public void setSampleFieldsText(String fieldsText) {
            sfieldsTextField.setText(fieldsText);
        }
        
        private class ListRenderer extends DefaultListCellRenderer {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                File file = (File) value;
                setText(file.getName());
                return this;
            }
        }  
        
    } 
	
}