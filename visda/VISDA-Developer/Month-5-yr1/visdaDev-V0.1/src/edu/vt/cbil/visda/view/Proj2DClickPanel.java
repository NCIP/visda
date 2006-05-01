package edu.vt.cbil.visda.view;

import java.awt.*; 
import java.awt.event.*;
import javax.swing.*;
import org.math.plot.Plot2DPanel;
import static org.math.array.DoubleArray.*;

/**
 * Created with Eclipse
 * Author: Jiajing Wang
 * Date: July 19, 2005
 *
 */

/**
 * the class to choose center pointer with mouse-click on projection plot
 *
 */

public class Proj2DClickPanel extends JPanel implements MouseListener{
	
	   public double cluster2DCenters[][]; 
	   int clickedNum = 0;
	   int totalNum;
	   
	   //protected JLabel label;
	   protected Plot2DPanel plotPanel;
	   
	   public Proj2DClickPanel(String title, Plot2DPanel Panel, int numPoints) {
		  totalNum = numPoints;
		  //cluster2DCenters = new double[numPoints][2];
		  
	      plotPanel = Panel;
	      plotPanel.removePlotToolBar();
		  plotPanel.plotCanvas.addMouseListener(this);
		  setBorder(BorderFactory.createTitledBorder(title));
		  add(plotPanel);
	   }
	   
	   public void mousePressed(MouseEvent e){} 
	   // dont use these so leave them empty 
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
			   double d[][] = plotPanel.plotCanvas.centerData;
			   plotPanel.addPlot("SCATTER", "Cluster Center", Color.BLACK, d);
			   
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
		   //}
	   } 
	   public void mouseEntered(MouseEvent e){} 
	   public void mouseExited(MouseEvent e){} 
	   public void mouseReleased(MouseEvent e){} 
	   
}
