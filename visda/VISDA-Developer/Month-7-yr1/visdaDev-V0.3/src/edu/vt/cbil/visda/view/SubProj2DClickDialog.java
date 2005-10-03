package edu.vt.cbil.visda.view;

import java.awt.*; 
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

/**
 * Created with Eclipse
 * Date: July 27, 2005
 */

/**
 * Allow user to choose center pointers with mouse-click on Sub-level projections
 * @author Jiajing Wang
 * @version visda_v0.1
 */

public class SubProj2DClickDialog extends JDialog {
	   
	  /**
	   * the coordinate values of selected center pointers;
	   * row: total selected points;
	   * col: 2 colums for x and y coordinate;
	   */
	   public double cluster2DCenters[][];
	   
	   /**
		* the number of clicked pointers for each cluster
		*/
	   public int numPoints[];
	   
	   private int result = JOptionPane.CANCEL_OPTION;
	   
	   /**
		* the number of clusters
		*/
	   int numClusters;
	   
	   /**
		* the total number of clicked pointers for all the clusters
		*/
	   int totalNum;
	   
	   protected JLabel instructionsLabel;
	   protected JButton doneButton;
	   protected Proj2DClickPanel subPlots[];
	   
	   /**
	     * Create a SubProj2DClickDialog instance
	     * 
	     * @param frame		the frame containing the dialog
	     * @param level		the index of current level
	     * @param modal		the modal option for the dialog
	     * @param plots		the plot panel for each sub-cluster
	     * @param pointsNum	the required number of click pointers for each sub-cluster
	     */
	   public SubProj2DClickDialog(JFrame frame, int level, boolean modal, 
			   Plot2DViewer plots[], int pointsNum[]) {
		  super(frame, "Level "+level+" Cluster Centers Selection", modal);
		  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		  Container content = getContentPane();
		  numClusters = plots.length;
		  totalNum = numClusters;
		  numPoints = pointsNum;
		  cluster2DCenters = new double[numClusters][2];
		  		  
		  String labelText = "<html>Instruction: Left click to select the centers of clusters for each sub-window." +
			 				 "<P>" +
			 				 "When all finished, click the <b>DONE</b> button to continue.</html>";
		  instructionsLabel = new JLabel(labelText);
		  instructionsLabel.setForeground(java.awt.Color.magenta);
	      
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
		  int rowNum = numClusters/2;
		  int remain = numClusters%2;
		  if (remain > 0) {
			  rowNum += 1;
		  }	 
		  
		  JPanel plotArea = new JPanel((new GridLayout(rowNum, 2)));
		  for (int i=0; i<numClusters; i++) {
	    	  plotArea.add(subPlots[i]);
	      }
		  JScrollPane plotScrollPane = new JScrollPane(plotArea, 
				  ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		  plotScrollPane.setPreferredSize(new Dimension(800, 600));
		  
		  content.setLayout(new GridBagLayout());
		  GBA gba = new GBA();
		  gba.add(content, plotScrollPane, 0, 0, 3, 2, 1, 1, GBA.B, GBA.C, (new Insets(5,5,5,5)), 0, 0);
		  gba.add(content, instructionsLabel, 0, 2, 2, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
		  gba.add(content, buttonPanel, 2, 2, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 40, 0);
		  //content.add(plotArea, BorderLayout.NORTH);
		  //content.add(instructionsLabel, BorderLayout.CENTER);
		  //content.add(buttonPanel, BorderLayout.SOUTH);
		  
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
