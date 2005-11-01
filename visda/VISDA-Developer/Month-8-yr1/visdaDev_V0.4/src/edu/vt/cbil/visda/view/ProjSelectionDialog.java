package edu.vt.cbil.visda.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


/**
 * The class is used to generate the dialog for projection type selection
 * 
 * @author Jiajing Wang
 * @version visda_v0.1
 * 
 * Version Log:
 * 	visda_v0.1 (07/30/2005):  The file is created.
 * 
 */
public class ProjSelectionDialog extends JDialog {
	
	  private int result = JOptionPane.CANCEL_OPTION;
	  
	  /**
	   * True - PCA is selected;
	   * False - PCA-PPM is selected
	   */
	  public boolean isPCA= true;
	  
	  /**
	   * 1 - PCA is selected;
	   * 2 - PCA-PPM is selected;
	   * 3 - DCA is selected
	   */
	  private int selectedProjType;
	  
	  /**
	   * True - No more sub-clusters will be discovered under current cluster
	   * False - More sub-clusters will be discovered under current cluster
	   */
	  public boolean isEndPoint = false;
	  
	  private JPanel plotsPanel;
	  private JLabel instructionsLabel;
	  private ProjSelectionPanel choosePanel;
	  
	  protected JButton okButton;
	  protected JButton cancelButton;
	  protected JButton continueButton;
	  
	 /**
	  * Create a ProjSelectionDialog instance
	  * 
	  * @param frame		the frame containing the dialog
	  * @param modal		the modal option for the dialog
	  * @param title		the dialog title
	  * @param plots		the projection plots
	  */
	  public ProjSelectionDialog(JFrame frame, boolean modal,
			  String title, Plot2DViewer plots[]) {
	    super(frame, title+" is generated", modal);
	    //WindowUtilities.setNativeLookAndFeel();
	    //addWindowListener(new ExitListener());
	    //setBackground(Color.white);
	    //setForeground(Color.black);
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    Container content = getContentPane();
	    
	    // plot panel
	    plotsPanel = new JPanel(new GridLayout(1, 2));
        for (int i=0; i<plots.length; i++) {
			  String subTitle = plots[i].getPlotName();
			  plots[i].setBorder(BorderFactory.createTitledBorder(subTitle));
			  plotsPanel.add(plots[i]);
		}
        
        JPanel instructPanel = new JPanel();
        instructionsLabel = new JLabel();
        instructionsLabel.setForeground(java.awt.Color.MAGENTA);
        String instructions = "<html>Please choose one of the projections for this cluster. " +
        		                 "<P>Then click <b>OK</b> button.</html>";
        instructionsLabel.setText(instructions);
        instructPanel.add(instructionsLabel);
        instructPanel.setBorder(BorderFactory.createTitledBorder("INSTRUCTION"));
        
	    // choose panel
        choosePanel = new ProjSelectionPanel("Projection Types");
	    
        // button panel
	    JPanel buttonPanel = new JPanel();
	    okButton = new JButton("OK");  
        okButton.setActionCommand("ok-command");
        okButton.setSize(90,30);
        okButton.setPreferredSize(new Dimension(90,30));
        okButton.setFocusPainted(false);	    
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel-command");
        cancelButton.setSize(90,30);
        cancelButton.setPreferredSize(new Dimension(90,30));
        cancelButton.setFocusPainted(false);      
        //continueButton= new JButton("Continue");
        //continueButton.setActionCommand("continue-command");
        //continueButton.setSize(90,30);
        //continueButton.setPreferredSize(new Dimension(90,30));
        //continueButton.setFocusPainted(false);     
        //buttonPanel.add(continueButton);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
       
        
        setActionListeners(new EventListener());
        
        content.setLayout(new GridBagLayout());
        GBA gba = new GBA();
        gba.add(content, plotsPanel, 0, 0, 2, 2, 1, 1, GBA.B, GBA.C, (new Insets(5,5,5,5)), 0, 0);
        gba.add(content, choosePanel, 0, 2, 2, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
        gba.add(content, instructPanel, 0, 3, 1, 2, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
        gba.add(content, buttonPanel, 1, 4, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
	    pack();
	    setVisible(true);
	  }

	  protected void setActionListeners(ActionListener listener){
	        okButton.addActionListener(listener);
	        cancelButton.addActionListener(listener);
	        //continueButton.addActionListener(listener);
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
	   * the method indicates whether which projection is selected
	   * 
	   * @return 	1 - PCA is selected
	   * 			2 - PCAPPM is selected
	   * 			3 - DCA is selected
	   */
	  public int getSelectedProjType(){
	        return choosePanel.getSelectedType();
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
	                result = JOptionPane.YES_OPTION;
	                //isPCA = isPCASelected();
	                isEndPoint = true;
	                dispose();
	            } else if (command.equals("cancel-command")){
	                result = JOptionPane.CANCEL_OPTION;
	                isEndPoint = false;
	                dispose();
	            //} else if (command.equals("continue-command")){
	            //	result = JOptionPane.NO_OPTION;
	                //isPCA = isPCASelected();
	            //    isEndPoint = false;
	            //    dispose();
	                //resetControls();
	                //isPCA = true;
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
