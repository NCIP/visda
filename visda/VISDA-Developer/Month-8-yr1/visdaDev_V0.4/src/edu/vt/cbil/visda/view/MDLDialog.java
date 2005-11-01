package edu.vt.cbil.visda.view;

import java.awt.*; 
import java.awt.event.*;

import javax.swing.*;
import javax.swing.JFrame;


/**
 * The class is used to let user choose the optimum number of sub-clusters for one class
 * @author Jiajing Wang
 * @version visda_v0.4
 * 
 * Version Log:
 * 	visda_v0.4 (10/30/2005):  The file is created and basic view tree is implemented.
 * 
 */

public class MDLDialog extends JDialog {
	
	public int selectedK;
	
	protected JLabel label;
	protected JButton doneButton;
	protected ButtonGroup selectButtonGroup;
	protected JRadioButton[] kButtons;
	
	private int result = JOptionPane.CANCEL_OPTION;
    
	
	public MDLDialog(JFrame frame, boolean modal, String title, double[] mdls, int[] ks) {
		  super(frame, title+" MDL Selection", modal);
		  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		  Container content = getContentPane();
		  
		  MDLPanel mdlPanel = new MDLPanel(mdls, ks);
		   
		  double minMdl = mdls[0];
		  int minK = ks[0];
		  for(int i=1; i<mdls.length; i++) {
			  if (mdls[i] < minMdl) {
				  minMdl = mdls[i];
				  minK = ks[i];
			  }
		  }
		  
		  JPanel instrPanel = new JPanel();
		  String labelText = "<html>Please select the best number of centers." +
			 "<P>The best number from MDL validataion is "+minK+".</html>";
		  label = new JLabel(labelText);
		  label.setForeground(java.awt.Color.magenta);  
		  instrPanel.add(label);
		  instrPanel.setBorder(BorderFactory.createTitledBorder("INSTRUCTION"));
		  
		  JPanel selectPanel = new JPanel(new GridLayout(1, ks.length));
		  selectButtonGroup = new ButtonGroup();	
		  kButtons = new JRadioButton[ks.length];
		  for (int i=0; i<ks.length; i++) {
			  kButtons[i] = new JRadioButton();
			  if (ks[i] == minK) {
				  kButtons[i].setSelected(true);
			  } else {
				  kButtons[i].setSelected(false);
			  }
			  kButtons[i].setText(""+ks[i]+" Centers");
			  kButtons[i].setFocusPainted(false);
			  selectButtonGroup.add(kButtons[i]);
			  selectPanel.add(kButtons[i]);
		  }
		    
		    
		  JPanel buttonPanel = new JPanel();	  
		  doneButton = new JButton("Continue");  
		  doneButton.setActionCommand("done-command");
		  doneButton.setSize(90,30);
		  doneButton.setPreferredSize(new Dimension(90,30));
		  doneButton.setFocusPainted(false);
		  buttonPanel.add(doneButton);
		  doneButton.addActionListener(new EventListener());
		  
		  content.setLayout(new GridBagLayout());
		  GBA gba = new GBA();
		  gba.add(content, mdlPanel, 0, 0, 1, 1, 1, 1, GBA.B, GBA.C, (new Insets(5,5,5,5)), 0, 0);
		  gba.add(content, selectPanel, 0, 1, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
		  gba.add(content, instrPanel, 0, 2, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
		  gba.add(content, buttonPanel, 0, 3, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 40, 0);
		 
		  pack();
	      setVisible(true);
	   } 
	
	protected class EventListener implements ActionListener {
		   public void actionPerformed(ActionEvent event) {
	            String command = event.getActionCommand();
	            if (command.equals("done-command")) {
	            	result = JOptionPane.OK_OPTION;
	            	for (int i=0; i<kButtons.length; i++) {
	            		if (kButtons[i].isSelected()) {
	            			String strK[] = (kButtons[i].getText()).split(" ");
	            			selectedK = Integer.parseInt(strK[0]);
	            		}
	            	}
                	setVisible(false);
	                dispose();
	            }
		   }
	   }
}

