package edu.vt.cbil.visda.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


/**
 * Created with Eclipse
 * Date: July 19, 2005
 */

/**
 * the class to generate the dialog for projection type selection
 * 
 * @author Jiajing Wang
 * @version visda_v0.1
 */
public class ProjSelectionDialog extends JDialog {
	
	  private int result = JOptionPane.CANCEL_OPTION;
	  
	  /**
	   * True - PCA is selected;
	   * False - PCA-PPM is selected
	   */
	  public boolean isPCA= true;
	  
	  private JPanel plotsPanel;
	  private JLabel instructionsLabel;
	  private ProjSelectionPanel choosePanel;
	  
	  protected JButton okButton;
	  protected JButton cancelButton;
	  protected JButton resetButton;

	 /**
	  * Create a ProjSelectionDialog instance
	  * 
	  * @param frame		the frame containing the dialog
	  * @param modal		the modal option for the dialog
	  * @param title		the dialog title
	  * @param typeName		the projection type names
	  * @param plots		the projection plots
	  */
	  public ProjSelectionDialog(JFrame frame, boolean modal,
			  String title, String typeName[], Plot2DViewer plots[]) {
	    super(frame, title, modal);
	    //WindowUtilities.setNativeLookAndFeel();
	    //addWindowListener(new ExitListener());
	    //setBackground(Color.white);
	    //setForeground(Color.black);
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    Container content = getContentPane();
	    
	    // plot panel
	    plotsPanel = new JPanel(new GridLayout(1, 2));
        for (int i=0; i<plots.length; i++) {
        	  //plots[i].removePlotToolBar();
			  String subTitle;
			  subTitle = typeName[i]+" Projection";
			  plots[i].setBorder(BorderFactory.createTitledBorder(subTitle));
			  plotsPanel.add(plots[i]);
		}
        
        instructionsLabel = new JLabel();
        instructionsLabel.setForeground(java.awt.Color.red);
        String instructions = "<html>Choose one of the data projection types. " +
        		"Then click <b>OK</b> button to continue.</html>";
        instructionsLabel.setText(instructions);
        
	    // choose panel
        choosePanel = new ProjSelectionPanel("Projection Types");
	    
        // button panel
	    JPanel buttonPanel = new JPanel();
	    okButton = new JButton("OK");  
        okButton.setActionCommand("ok-command");
        okButton.setSize(60,30);
        okButton.setPreferredSize(new Dimension(60,30));
        okButton.setFocusPainted(false);	    
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel-command");
        cancelButton.setSize(90,30);
        cancelButton.setPreferredSize(new Dimension(90,30));
        cancelButton.setFocusPainted(false);      
        resetButton= new JButton("Reset");
        resetButton.setActionCommand("reset-command");
        resetButton.setSize(90,30);
        resetButton.setPreferredSize(new Dimension(90,30));
        resetButton.setFocusPainted(false);       
        buttonPanel.add(resetButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(okButton);
        
        setActionListeners(new EventListener());
        
        content.setLayout(new GridBagLayout());
        GBA gba = new GBA();
        gba.add(content, plotsPanel, 0, 0, 2, 2, 1, 1, GBA.B, GBA.C, (new Insets(5,5,5,5)), 0, 0);
        gba.add(content, instructionsLabel, 0, 2, 2, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
        gba.add(content, choosePanel, 0, 3, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
        gba.add(content, buttonPanel, 1, 3, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
	    pack();
	    setVisible(true);
	  }

	  protected void setActionListeners(ActionListener listener){
	        okButton.addActionListener(listener);
	        cancelButton.addActionListener(listener);
	        resetButton.addActionListener(listener);
	    }
	  
	  
	 /**
	   * the method indicates whether PCA projection is selected
	   * 
	   * @return 	TRUE - PCA is selected
	   * 			FALSE - PCAPPM is selected
	   */
	  public boolean isPCASelected(){
	        return choosePanel.isPCASelected();
	    }
	    
	  /**
	   * the method resets the projection selection to be PCA
	   */
	  public void resetControls(){
	        choosePanel.setPCASelected(true);
	    }
	    
	  protected class EventListener implements ActionListener {
	        public void actionPerformed(ActionEvent event) {
	            String command = event.getActionCommand();
	            if (command.equals("ok-command")) {
	                result = JOptionPane.OK_OPTION;
	                isPCA = isPCASelected();
	                dispose();
	            } else if (command.equals("cancel-command")){
	                result = JOptionPane.CANCEL_OPTION;
	                dispose();
	            } else if (command.equals("reset-command")){
	                resetControls();
	                isPCA = true;
	            }
	        }
	    }
	  
	  public int showModal(){
	        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	        setLocation((screenSize.width - getSize().width)/2, (screenSize.height - getSize().height)/2);
	        //show();
	        return result;
	    }
	 

	}