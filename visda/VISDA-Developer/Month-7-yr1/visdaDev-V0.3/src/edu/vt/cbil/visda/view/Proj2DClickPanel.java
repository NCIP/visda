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
 * Allow user to choose center pointers with mouse-click on projection plot
 *
 * @author Jiajing Wang
 * @version visda_v0.1
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
		  plotViewer.plotPanel.plotCanvas.ActionMode = plotViewer.plotPanel.plotCanvas.SELCENTER;
		  setBorder(BorderFactory.createTitledBorder(title));
		  add(plotViewer);
	   }
	   
	   public void mousePressed(MouseEvent e){} 
	   // dont use these so leave them empty 
	   
	   /**
	     * Get the coordinate of the pointer when it is selected with mouse click
	     */
	   public void mouseClicked(MouseEvent e){
		   int[] mouseCurent = new int[2];
		   mouseCurent[0] = e.getX();
		   mouseCurent[1] = e.getY();
		   
		   //if (clickedNum > totalNum-1) {
		   //	   JOptionPane.showMessageDialog(new JFrame(),
			//		    "No more clicked points are wanted.",
			//		    "Inane error",
			//		    JOptionPane.ERROR_MESSAGE);
		   //} else {
		   if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
			   double d[][] = plotViewer.plotPanel.plotCanvas.centerData;
			   plotViewer.plotPanel.addPlot("SCATTER", "Cluster Center", Color.BLACK, d);
			   
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
	   public void mouseEntered(MouseEvent e){} 
	   public void mouseExited(MouseEvent e){} 
	   public void mouseReleased(MouseEvent e){} 
	   
}
