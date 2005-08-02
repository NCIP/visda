package edu.vt.cbil.visda.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Created with Eclipse
 * Author: Jiajing Wang
 * Date: July 19, 2005
 *
 */

/**
 * the class to generate the dialog for projection type selection
 *
 */
public class ProjSelectionDialog extends JDialog {
	
	  private int result = JOptionPane.CANCEL_OPTION;
	  public boolean isPCA= true;
	  
	  private ProjSelectionPanel choosePanel;
	  protected JButton okButton;
	  protected JButton cancelButton;
	  protected JButton resetButton;

	  public ProjSelectionDialog(Frame frame, boolean modal, int x, int y) {
	    super(frame, "Choose a Data Projection Type", modal);
	    //WindowUtilities.setNativeLookAndFeel();
	    //addWindowListener(new ExitListener());
	    setLocation(x, y);
	    setBackground(Color.white);
	    setForeground(Color.black);
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    Container content = getContentPane();
	    
	    // choose panel
        choosePanel = new ProjSelectionPanel("Projection Types");
	    
	    JPanel buttonPanel = new JPanel();
	    //setLayout(new GridBagLayout());
        //GridBagConstraints gridBagConstraints1;
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
        content.add(choosePanel, BorderLayout.NORTH);
        content.add(buttonPanel, BorderLayout.SOUTH);
	    pack();
	    setVisible(true);
	  }

	  protected void setActionListeners(ActionListener listener){
	        okButton.addActionListener(listener);
	        cancelButton.addActionListener(listener);
	        resetButton.addActionListener(listener);
	    }
	  
	  public boolean isPCASelected(){
	        return choosePanel.isPCASelected();
	    }
	    
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
	  
	  public static void main(String [] args){
		  	ProjSelectionDialog dialog = new ProjSelectionDialog(new JFrame(), true, 0, 0);
	        int result = dialog.showModal();
	        System.out.println("result = "+result);
	        System.exit(0);
	    }

	}