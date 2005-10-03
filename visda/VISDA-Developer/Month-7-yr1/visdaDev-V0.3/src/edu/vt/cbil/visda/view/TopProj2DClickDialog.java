package edu.vt.cbil.visda.view;

import java.awt.*; 
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;

/**
 * Created with Eclipse
 * Date: July 19, 2005
 */

/**
 * Allow user to choose center pointers with mouse-click on Top-level projections
 * @author Jiajing Wang
 * @version visda_v0.1
 */

public class TopProj2DClickDialog extends JDialog {
	
	   /**
	   * the coordinate values of selected center pointers;
	   * row: total selected points;
	   * col: 2 colums for x and y coordinate;
	   */
	   public double cluster2DCenters[][]; 
	   
       private int result = JOptionPane.CANCEL_OPTION;
       
       /**
		* the minimum total number of clicked pointers
		*/
	   int totalNum;
	   
	   protected JLabel label;
	   protected JButton doneButton;
	   public Proj2DClickPanel plotArea;
	   
	   /**
	     * Create a TopProj2DClickDialog instance
	     * 
	     * @param frame		the top-level visualization frame containing the dialog
	     * @param modal		the modal option for the dialog
	     * @param Panel		the plot panel for the top level clusters
	     * @param numClusters	the minimum number of clusters
	     */
	   public TopProj2DClickDialog(JFrame frame, boolean modal, Plot2DViewer Panel, int numClusters) {
		  super(frame, "Top Level Cluster Centers Selection", modal);
		  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		  Container content = getContentPane();
		  totalNum = numClusters;
		  
		  String labelText = "<html>Instruction: Left click to select the centers of clusters." +
			  				 "<P>" +
			  				 "When finish, click the <b>DONE</b> button.</html>";
		  label = new JLabel(labelText);
		  label.setForeground(java.awt.Color.magenta);
		  
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