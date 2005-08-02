package edu.vt.cbil.visda.view;

import java.awt.*; 
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

/**
 * Created with Eclipse
 * Author: Jiajing Wang
 * Date: July 27, 2005
 *
 */

/**
 * the class to choose center pointers with mouse-click on Sub-level projections
 *
 */

public class SubProj2DClickDialog extends JDialog {
	
	   //row: total selected points
	   //col: 2 colums for x and y coordinate
	   public double cluster2DCenters[][];
	   public int numPoints[];
	   private int result = JOptionPane.CANCEL_OPTION;
	   int numClusters;
	   int totalNum;
	     
	   protected JLabel label;
	   protected JButton doneButton;
	   protected Proj2DClickPanel subPlots[];
	   
	   public SubProj2DClickDialog(JFrame frame, int level, boolean modal, 
			                       Plot2DPanel plots[], int pointsNum[]) {
		  super(frame, "Level "+level+" Visualization", modal);
		  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		  Container content = getContentPane();
		  numClusters = plots.length;
		  totalNum = numClusters;
		  numPoints = pointsNum;
		  cluster2DCenters = new double[numClusters][2];
		  
		  String labelText = "<html>Please left click to select the centers of clusters for each sub-window." +
			 				 "<P>" +
			 				 "When all finished, click the 'Done' button.";
		  label = new JLabel(labelText);
	      
	      JPanel buttonPanel = new JPanel();
	      doneButton = new JButton("DONE");  
	      doneButton.setActionCommand("done-command");
	      doneButton.setSize(90,30);
	      doneButton.setPreferredSize(new Dimension(90,30));
	      doneButton.setFocusPainted(false);
	      buttonPanel.add(doneButton);
	      doneButton.addActionListener(new EventListener());
	      
	      subPlots = new Proj2DClickPanel[numClusters];
		  for (int i=0; i<numClusters; i++) {
			  String subTitle;
			  subTitle = "Level_"+level+"_SubCluster_"+(i+1)+" Plot";
			  subPlots[i] = new Proj2DClickPanel(subTitle, plots[i], numPoints[i]);
		  }
		  
		  // layout
		  int rowNum = numClusters/3;
		  int remain = numClusters%3;
		  if (remain > 0) {
			  rowNum += 1;
		  }	 
		  JPanel plotArea = new JPanel(new GridLayout(rowNum, 3));
		  for (int i=0; i<numClusters; i++) {
	    	  plotArea.add(subPlots[i]);
	      }

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
	            	boolean clickEnough = true;
	            	for (int i=0; i<numClusters; i++) {
	            		if (subPlots[i].clickedNum < numPoints[i]-1) {
	            			JOptionPane.showMessageDialog(null, 
	            				   "Too few center pointers are selected in SubWindow "+i, 
	 	                		   "Error", 
	 	                		   JOptionPane.ERROR_MESSAGE);
	            			clickEnough = false;
	            			return;
	            		}
	            	}
	            	if (clickEnough) {
	                	setVisible(false);
	                	dispose();
	                	result = JOptionPane.OK_OPTION;
	                	totalNum = 0;
	                	for(int i=0; i<numClusters; i++) {
	                		numPoints[i] = subPlots[i].cluster2DCenters.length;
	                		totalNum += numPoints[i];
	                	}
	                	cluster2DCenters = new double[totalNum][2];
	                	int u = 0;
	                	for (int i=0; i<numClusters; i++) {
	                		for (int j=0; j<numPoints[i]; j++) {
	                			cluster2DCenters[u+j] = subPlots[i].cluster2DCenters[j];                			
	                		}
	                		u += numPoints[i];
	                	}
	                }
	            }
		   }
	   }
	   
}
