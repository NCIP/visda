package edu.vt.cbil.visda.view;

import java.awt.Color;
import javax.swing.JPanel;
import org.math.plot.Plot2DPanel;


/**
 * The class is used to plot the MDL values
 * 
 * @author Jiajing Wang
 * @version visda_v0.4
 * 
 * Version Log:
 * 	visda_v0.4 (10/30/2005):  The file is created and basic view tree is implemented.
 * 
 */
public class MDLPanel extends JPanel {
	
	public MDLPanel(double[] mdls, int[] ks) {
		
		Plot2DPanel mdlPanel = new Plot2DPanel();
		mdlPanel.plotCanvas.setAxeSliceNum(0, 2);
		int size = mdls.length;
		double[][] d = new double[size+1][2];
		d[0][0] = ks[0];
		d[0][1] = mdls[0];
		for (int i=0; i<size; i++) {
			d[i+1][0] = ks[i];
			d[i+1][1] = mdls[i]; 
		}
		mdlPanel.addPlot("LINE", "MDL", Color.BLACK, d);
		for (int i=0; i<size; i++) {
			d = new double[1][2];
			d[0][0] = ks[i];
			d[0][1] = mdls[i]; 
			mdlPanel.addPlot("SCATTER", "MDL"+i, Color.RED, d);
		}
		mdlPanel.plotCanvas.setAxeLabel(0, "Numbers");
		mdlPanel.plotCanvas.setAxeLabel(1, "MDL");		
		mdlPanel.removePlotToolBar();
		add(mdlPanel);
	}
	
}
