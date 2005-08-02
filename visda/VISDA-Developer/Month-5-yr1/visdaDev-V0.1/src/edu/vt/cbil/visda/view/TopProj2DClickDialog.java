package edu.vt.cbil.visda.view;

import java.awt.*; 
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;

/**
 * Created with Eclipse
 * Author: Jiajing Wang
 * Date: July 19, 2005
 *
 */

/**
 * the class to choose center pointers with mouse-click on Top-level projections
 *
 */

public class TopProj2DClickDialog extends JDialog {
	
	   public double cluster2DCenters[][]; 
       private int result = JOptionPane.CANCEL_OPTION;
	   int totalNum;
	   
	   protected JLabel label;
	   protected JButton doneButton;
	   protected Proj2DClickPanel plotArea;
	   
	   public TopProj2DClickDialog(JFrame frame, boolean modal, Plot2DPanel Panel, int numClusters) {
		  super(frame, "Top Level Visualization", modal);
		  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		  Container content = getContentPane();
		  totalNum = numClusters;
		  
		  String labelText = "<html>Please left click to select the centers of clusters." +
			  				 "<P>" +
			  				 "When finish, click the 'Done' button.";
		  label = new JLabel(labelText);
		  
	      JPanel buttonPanel = new JPanel();
	      doneButton = new JButton("DONE");  
	      doneButton.setActionCommand("done-command");
	      doneButton.setSize(90,30);
	      doneButton.setPreferredSize(new Dimension(90,30));
	      doneButton.setFocusPainted(false);
	      buttonPanel.add(doneButton);
	      doneButton.addActionListener(new EventListener());
	      
	      plotArea = new Proj2DClickPanel("Top level Plot", Panel, numClusters);
	      
		  content.add(plotArea, BorderLayout.NORTH);
		  content.add(label, BorderLayout.CENTER);
		  content.add(buttonPanel, BorderLayout.SOUTH);
		  
		  pack();
	      setVisible(true);
	   } 
	   
	   protected class EventListener implements ActionListener {
		   public void actionPerformed(ActionEvent event) {
	            String command = event.getActionCommand();
	            if (command.equals("done-command")) {
	                if (plotArea.clickedNum < totalNum-1) {
	                   JOptionPane.showMessageDialog(null, 
	                		   "Too few center pointers are selected", 
	                		   "Error", 
	                		   JOptionPane.ERROR_MESSAGE);
                       return;
	                } else {
	                	setVisible(false);
	                	dispose();
	                	result = JOptionPane.OK_OPTION;
	                	cluster2DCenters = plotArea.cluster2DCenters;
	                }
	            }
		   }
	   }
	   
}