package edu.vt.cbil.visda.view;

import java.awt.*; 
import java.awt.event.*;

import javax.swing.*;
import org.math.plot.Plot2DPanel;
import static org.math.array.DoubleArray.*;

/**
 * Created with Eclipse
 * Date: July 19, 2005
 */

/**
 * The class is used to let user choose center pointers with mouse-click on 2D projection plot
 *
 * @author Jiajing Wang
 * @version visda_v0.4
 * 
 * Version Log:
 * 	visda_v0.1 (07/30/2005):	The file is created and basic function is implemented.
 *  visda_v0.2 (08/30/2005):
 *  visda_v0.3 (09/30/2005):
 * 	visda_v0.4 (10/29/2005): 	Implement MDL model selection; 
 *
 */
public class Proj2DClickPanel extends JPanel implements MouseListener{
	
	  /**
	   * the coordinate values of selected center pointers
	   */
	   public double cluster2DCenters[][]; 
	   
	   /**
		* the number of clicked pointers
		*/
	   public int clickedNum = 0;
	   
	   /**
		* the required number of clicked pointers
		*/
	   private int totalNum;
	   
	   /**
		* the panel for projection as well as center selection
		*/
	   protected Plot2DViewer plotViewer;
	   
	   
	   /**
	     * Create a Proj2DClickPanel instance
	     * 
	     * @param title		the name for the sub-window
	     * @param plotViewer	the plot panel for 2D projection as well as center selection
	     * @param numPoints	the required number of click pointers
	     */
	   public Proj2DClickPanel(String title, Plot2DViewer plotViewer, int numPoints) {
		  totalNum = numPoints;
		  //cluster2DCenters = new double[numPoints][2];
		  
		  this.plotViewer = plotViewer;
	      
		  plotViewer.plotPanel.plotCanvas.addMouseListener(this);
		  // change the ActionMode to be center selection
		  plotViewer.plotPanel.plotCanvas.setActionMode(plotViewer.plotPanel.plotCanvas.SELCENTER);
		  setBorder(BorderFactory.createTitledBorder(title));
		  add(plotViewer);
	   }
	   
	   public void mousePressed(MouseEvent e){} 
	   // dont use these so leave them empty 
	   
	   /**
	     * Get the coordinate of the pointer when it is selected with mouse click
	     */
	   public void mouseClicked(MouseEvent e){
		   
	   } 
	   public void mouseEntered(MouseEvent e){} 
	   public void mouseExited(MouseEvent e){} 
	   public void mouseReleased(MouseEvent e){
		   int[] mouseCurent = new int[2];
		   mouseCurent[0] = e.getX();
		   mouseCurent[1] = e.getY();
		   
		   if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
			   double d[][] = plotViewer.plotPanel.plotCanvas.centerData;
			   //plotViewer.plotPanel.addPlot("SCATTER", "Cluster Center", Color.BLACK, d);
			   
			   // increase the center data storage memory
			   if (clickedNum == 0) {
				   cluster2DCenters = new double[1][2];
				   cluster2DCenters[0] = d[0];
				   clickedNum++;
			   } else {
				   double bak[][] = new double[clickedNum][2];
				   bak = copy(cluster2DCenters);	
				   clickedNum++;
				   cluster2DCenters = new double[clickedNum][2];
				   for (int i=0; i<clickedNum-1; i++) {
					   cluster2DCenters[i] = copy(bak[i]);
				   }
				   cluster2DCenters[clickedNum-1] = d[0];
			   }
			   
		   }
		   
	   } 
	   
}
