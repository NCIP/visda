package edu.vt.cbil.visda.view;

import java.awt.*; 
import java.awt.event.*;

import javax.swing.*;
import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;
import static org.math.array.LinearAlgebra.*;


/**
 * The class is used to let user choose center pointers with mouse-click for one cluster
 * @author Jiajing Wang
 * @version visda_v0.4
 * <p>
 * Version Log:
 * 	<br>visda_v0.1 (07/30/2005):	The file is created and basic function is implemented.
 * 	<br>visda_v0.4 (10/11/2005): 	Support sub-cluster specific clustering 		
 */

public class Proj2DClickDialog extends JDialog implements MouseListener{
	
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
	   public int totalNum;
	   
	   protected JLabel label;
	   protected JButton doneButton;
	   public Proj2DClickPanel plotArea;
	   
	   /**
	     * Create a Proj2DClickDialog instance
	     * 
	     * @param frame		the frame containing the dialog
	     * @param modal		the modal option for the dialog
	     * @param levelId	the index of current level
	     * @param plot		the plot for the top level clusters
	     */
	   public Proj2DClickDialog(JFrame frame, boolean modal, int levelId, Plot2DViewer plot) { 
		  super(frame, "Continue Partition on Level "+levelId+ " Clusters", modal);
		  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		  Container content = getContentPane();
		  totalNum = 0;

		  plotArea = new Proj2DClickPanel(plot.getPlotName(), plot, 0);
		  content.add(plotArea, BorderLayout.NORTH);
		   
		  JPanel instrPanel = new JPanel();
		  String labelText = "<html>Left click to select the centers of the subclusters." +
			 "<P>" +
			 "When finish, click the <b>DONE</b> button.</html>";
		  label = new JLabel(labelText);
		  label.setForeground(java.awt.Color.magenta);
		  instrPanel.add(label);
		  instrPanel.setBorder(BorderFactory.createTitledBorder("INSTRUCTION"));
		  content.add(instrPanel, BorderLayout.CENTER);
		  
		  
		  JPanel buttonPanel = new JPanel();
		  doneButton = new JButton("DONE");  
		  doneButton.setActionCommand("done-command");
		  doneButton.setSize(90,30);
		  doneButton.setPreferredSize(new Dimension(90,30));
		  doneButton.setFocusPainted(false);
		  buttonPanel.add(doneButton);
		  doneButton.addActionListener(new EventListener());
		  content.add(buttonPanel, BorderLayout.SOUTH);
		 
		  pack();
	      setVisible(true);
	   } 
	   
	   
	   /**
	     * Create a Proj2DClickDialog instance
	     * 
	     * @param frame		the frame containing the dialog
	     * @param modal		the modal option for the dialog
	     * @param levelId	the index of current level
	     * @param plot		the plot for the top level clusters
	     * @param numClusters	the number of clusters that should be selected
	     */
	   public Proj2DClickDialog(JFrame frame, boolean modal, int levelId, Plot2DViewer plot, int numClusters) {
		  super(frame, plot.getPlotName()+" Partition ("+numClusters+"-Center)", modal);
		  setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		  Container content = getContentPane();
		  totalNum = numClusters;

		  plotArea = new Proj2DClickPanel(plot.getPlotName(), plot, numClusters);
		  plotArea.plotViewer.plotPanel.plotCanvas.addMouseListener(this);		  
		  content.add(plotArea, BorderLayout.NORTH);
		  
		  JPanel instrPanel = new JPanel();
		  String labelText = "<html>Try again. <br>Please left click to select "+numClusters+" center points.</html>";
		  label = new JLabel(labelText);
		  label.setForeground(java.awt.Color.MAGENTA);
		  instrPanel.add(label);
		  instrPanel.setBorder(BorderFactory.createTitledBorder("INSTRUCTION"));
		  content.add(instrPanel, BorderLayout.CENTER);
		  
		  pack();
	      setVisible(true);
	   } 
	   
	   protected class EventListener implements ActionListener {
		   public void actionPerformed(ActionEvent event) {
	            String command = event.getActionCommand();
	            if (command.equals("done-command")) {
	                if (plotArea.clickedNum < 1) {
	                   JOptionPane.showMessageDialog(null, 
	                		   "Too few center pointers are selected", 
	                		   "Error", 
	                		   JOptionPane.ERROR_MESSAGE);
                       return;
	                } else {
	                	setVisible(false);
	                	dispose();
	                	result = JOptionPane.OK_OPTION;
	                	cluster2DCenters = copy(plotArea.cluster2DCenters);
	                	
	                	// restore the ActionMode
	                	plotArea.plotViewer.plotPanel.plotCanvas.setActionMode(-2);	
	                	//plotArea.plotViewer.plotPanel.plotCanvas.centers.clear();
	                }
	            }
		   }
	   }
	   
	   
	   public void mouseClicked(MouseEvent e) { 
		   if (plotArea.clickedNum == totalNum) {
			   result = JOptionPane.OK_OPTION;
           		cluster2DCenters = plotArea.cluster2DCenters;
           	
           		// restore the ActionMode
           		plotArea.plotViewer.plotPanel.plotCanvas.setActionMode(-2);
           		plotArea.plotViewer.plotPanel.plotCanvas.centers.clear();
           		setVisible(false);
           		dispose();
           		plotArea.plotViewer.plotPanel.plotCanvas.removeMouseListener(this);
		   }
	   }
	   public void mousePressed(MouseEvent e){}  
	   public void mouseEntered(MouseEvent e){} 
	   public void mouseExited(MouseEvent e){} 
	   public void mouseReleased(MouseEvent e){
		   
	   } 
	   
}
