package edu.vt.cbil.visda.view;

import java.awt.*;

import javax.swing.*;

import org.math.plot.*;
import org.math.plot.canvas.*;


/**
 * Created with Eclipse
 * Author: Jiajing Wang
 * Date: July 18, 2005
 *
 */

/**
 * the class to create the frame for data projection
 *
 */
public class Proj2DPlotFrame extends JFrame {
	
	public Proj2DPlotFrame(String title, String typeName[], Plot2DPanel plots[]) {
		super(title);

		Container content = getContentPane();
		
		// add projection panel
        JPanel projArea = new JPanel(new GridLayout(1, 2));
        
        for (int i=0; i<plots.length; i++) {
        	  plots[i].removePlotToolBar();
			  String subTitle;
			  subTitle = typeName[i]+" Projection";
			  plots[i].setBorder(BorderFactory.createTitledBorder(subTitle));
			  projArea.add(plots[i]);
		}
        content.add(projArea, BorderLayout.NORTH);
        pack();
        setVisible(true);
    }
    
	
}