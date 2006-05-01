package edu.vt.cbil.visda.view;

import java.awt.*;
import javax.swing.*;


/**
 * Created with Eclipse
 * Date: July 18, 2005
 */

/**
 * the class to generate two choice panel
 * @author Jiajing Wang
 * @version visda_v0.1
 */
public class ProjSelectionPanel extends JPanel 
{
	private ButtonGroup selectButtonGroup;
    private JRadioButton pcaButton;
    private JRadioButton pcappmButton;
    
     /**
      * Creates a ProjSelectionPanel instance
      * @param title the name of the panel
      */
	  public ProjSelectionPanel(String title) 
	  {
	    super(new GridLayout(1, 2));
	    //setBackground(Color.white);
	    //setForeground(Color.black);
	    setBorder(BorderFactory.createTitledBorder(title));
	    selectButtonGroup = new ButtonGroup();
	    pcaButton = new JRadioButton();
	    pcappmButton = new JRadioButton();

	    pcaButton.setSelected(true);
	    pcaButton.setText("PCA");
	    pcaButton.setFocusPainted(false);
	    selectButtonGroup.add(pcaButton);
	    add(pcaButton);
	    pcappmButton.setText("PCA-PPM");
	    pcappmButton.setFocusPainted(false);
	    selectButtonGroup.add(pcappmButton);
	    add(pcappmButton);
	  }
	  
	  /**
	   * the method indicates whether PCA projection is selected
	   * 
	   * @return 	TRUE - PCA is selected
	   * 			FALSE - PCAPPM is selected
	   */
	  public boolean isPCASelected(){
	        return this.pcaButton.isSelected();
	    }
	  
	  /**
	   * the method to select PCA projection
	   * 
	   * @param 	value:	TRUE - PCA is selected
	   * 					FALSE - PCAPPM is selected
	   */
	  public void setPCASelected(boolean value){
	        pcaButton.setSelected(value);
	    }
}
