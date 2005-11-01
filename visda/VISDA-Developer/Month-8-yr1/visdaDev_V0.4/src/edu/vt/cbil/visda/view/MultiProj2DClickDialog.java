package edu.vt.cbil.visda.view;

import java.awt.*; 
import java.awt.event.*;
import javax.swing.*;
import javax.swing.JFrame;

import static org.math.array.LinearAlgebra.*;

import org.math.plot.Plot2DPanel;


/**
 * The class is used to let user choose center pointers with mouse-click for multiple
 *   clusters on the same level
 * @author Jiajing Wang
 * @version visda_v0.4
 * <p>
 * Version Log:
 * 	<br>visda_v0.1 (07/30/2005):	The file is created and basic function is implemented.
 * 	<br>visda_v0.4 (10/11/2005): 	Support sub-cluster specific clustering 		
 */

public class MultiProj2DClickDialog extends JDialog {
	   
	  /**
	   * the coordinate values of selected center pointers;
	   * row: total selected points;
	   * col: 2 colums for x and y coordinate;
	   */
	   public double cluster2DCenters[][];
	   
	   /**
		* the centers for all the up-level clusters
		*/
	   private double centers_uplevel[][];
	   
	   /**
		* the total number of clicked pointers for all the clusters
		*/
	   private int totalNum;
	   
	   /**
		* the number of clicked pointers for each cluster that has more sub-clusters
		*/
	   public int numPoints[];
	   
	   /**
		* The number of clicked pointers for all the up-level clusters
		* If one cluster has no more sub-clusters, its corresponding point is ZERO. 
		*/
	   public int numAllPoints[];
	   
	   private int result = JOptionPane.CANCEL_OPTION;
	   
	   /**
		* the number of up-level clusters that will be sub-clustered
		*/
	   private int numClusters;
	   
	   /**
		* the number of all the up-lelvel clusters
		*/
	   private int numAllClusters;
	   
	   
	   protected JLabel instructionsLabel;
	   protected JButton doneButton;
	   protected Proj2DClickPanel subPlots[];
	   
	   /**
	     * Create a MultiProj2DClickDialog instance
	     * 
	     * @param frame		the frame containing the dialog
	     * @param level		the index of current level
	     * @param modal		the modal option for the dialog
	     * @param plots		the plot panel for each sub-cluster
	     * @param pointsNum	the required number of click pointers for each sub-cluster
	     */
	   public MultiProj2DClickDialog(JFrame frame, int level, boolean modal, 
			   Plot2DViewer plots[], int pointsNum[]) {
		  super(frame, "Continue Partition on Level "+level+ " Clusters", modal);
		  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		  Container content = getContentPane();
		  numClusters = plots.length;
		  totalNum = numClusters;
		  numPoints = pointsNum;
		  cluster2DCenters = new double[numClusters][2];
		  		  
		  String labelText = "<html>INSTRUCTION: Left click to select the centers of clusters for each sub-window." +
			 				 "<P>" +
			 				 "When all finished, click the <b>DONE</b> button to continue.</html>";
		  instructionsLabel = new JLabel(labelText);
		  instructionsLabel.setForeground(java.awt.Color.black);
	      
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
			  String subTitle = plots[i].getPlotName();
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
		  
		  pack();
	      setVisible(true);
	   }
	   
	   
	   /**
	     * Create a MultiProj2DClickDialog instance
	     * 
	     * @param frame		the frame containing the dialog
	     * @param level		the index of current level
	     * @param modal		the modal option for the dialog
	     * @param plots		the plot panel for each sub-cluster
	     * @param pointsNum	the required number of click pointers for each sub-cluster
	     * @param centers_up	the centers of the upper cluster
	     */
	   public MultiProj2DClickDialog(JFrame frame, int level, boolean modal, 
			   Plot2DViewer plots[], int pointsNum[], double[][] centers_up) {
		  super(frame, "Continue Partition on Level "+level+ " Clusters", modal);
		  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		  Container content = getContentPane();
		  numClusters = plots.length;
		  	  
		  centers_uplevel = new double[numClusters][2];
		  centers_uplevel = copy(centers_up);
		  totalNum = numClusters;
		  cluster2DCenters = new double[numClusters][2];
		  		  
		  JPanel instrPanel = new JPanel();
		  String labelText = "<html>If one cluster can be further partitioned, please left click on its " +
		  					   "<br>sub-window to select its centers. Otherwise, just skip its sub-window."+
		  					   "<br>When all finish, click the <b>DONE</b> button to continue.</html>";
		  instructionsLabel = new JLabel(labelText);
		  instructionsLabel.setForeground(java.awt.Color.MAGENTA);
		  instrPanel.add(instructionsLabel);
		  instrPanel.setBorder(BorderFactory.createTitledBorder("INSTRUCTION"));
	      
	      JPanel buttonPanel = new JPanel();
	      doneButton = new JButton("DONE");  
	      doneButton.setActionCommand("done-command");
	      doneButton.setSize(90,30);
	      doneButton.setPreferredSize(new Dimension(90,30));
	      doneButton.setFocusPainted(false);
	      buttonPanel.add(doneButton);
	      doneButton.addActionListener(new EventListener());
	            
	      subPlots = new Proj2DClickPanel[numClusters];
	      int count2=0;
		  for (int i=0; i<numClusters; i++) {
				  String subTitle = plots[i].getPlotName();
				  subPlots[count2] = new Proj2DClickPanel(subTitle, plots[i], pointsNum[count2]);
				  count2++;
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
		  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		  plotScrollPane.setPreferredSize(new Dimension(screenSize.width*2/3, screenSize.height*2/3));
		  
		  content.setLayout(new GridBagLayout());
		  GBA gba = new GBA();
		  gba.add(content, plotScrollPane, 0, 0, 3, 3, 1, 1, GBA.B, GBA.C, (new Insets(5,5,5,5)), 0, 0);
		  gba.add(content, instrPanel, 0, 3, 2, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
		  gba.add(content, buttonPanel, 2, 3, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 40, 0);
		  
		  pack();
	      setVisible(true);
	   } 
	   
	   protected class EventListener implements ActionListener {
		   public void actionPerformed(ActionEvent event) {
	            String command = event.getActionCommand();
	            if (command.equals("done-command")) {
	            	/*boolean clickEnough = true;
	            	for (int i=0; i<numClusters; i++) {
	            		if (subPlots[i].clickedNum < pointsNum[i]-1) {
	            			JOptionPane.showMessageDialog(null, 
	            				   "Too few center pointers are selected in SubWindow "+i, 
	 	                		   "Error", 
	 	                		   JOptionPane.ERROR_MESSAGE);
	            			clickEnough = false;
	            			return;
	            		}
	            	}*/
	            	//if (clickEnough) {
	                	setVisible(false);
	                	dispose();
	                	result = JOptionPane.OK_OPTION;
	                	totalNum = 0;
	                	numPoints = new int[numClusters];
	                	for(int i=0; i<numClusters; i++) {
	                		if ((subPlots[i].cluster2DCenters == null) ||
	                		    (subPlots[i].cluster2DCenters.length==1)) {
	                			numPoints[i] = 1;
	                		} else {
	                			numPoints[i] = subPlots[i].cluster2DCenters.length;
	                		}
	                		totalNum += numPoints[i];
	                	}
	                	//totalNum += numClusters - numClusters;
	                	cluster2DCenters = new double[totalNum][2];
	                	int u = 0;
	                	int count3 = 0;
	                	for (int i=0; i<numClusters; i++) {
	                		if (numPoints[i]==1) {
	                			cluster2DCenters[u] = subPlots[i].plotViewer.getCenterDot();
	                			u += 1;
	                		} else {
	                			for (int j=0; j<numPoints[i]; j++) {
	                				cluster2DCenters[u+j] = subPlots[i].cluster2DCenters[j];                			
	                			}
	                			u += numPoints[i];
	                			//count3++;
	                		}
	                	}
	                	
	                	// restore the ActionMode
	                	for(int i=0; i<numClusters; i++) {
	                		subPlots[i].plotViewer.plotPanel.plotCanvas.setActionMode(-2);
	                		subPlots[i].plotViewer.plotPanel.plotCanvas.centers.clear();
	                	}
	                //}
	            }
		   }
	   }
	   
}
