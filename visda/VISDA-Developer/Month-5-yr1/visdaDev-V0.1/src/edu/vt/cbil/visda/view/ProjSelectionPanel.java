package edu.vt.cbil.visda.view;

import java.awt.*;
import javax.swing.*;


/**
 * Created with Eclipse
 * Author: Jiajing Wang
 * Date: July 18, 2005
 *
 */

/**
 * the class to generate two choice panel
 *
 */
public class ProjSelectionPanel extends JPanel 
{
	private ButtonGroup selectButtonGroup;
    private JRadioButton pcaButton;
    private JRadioButton pcappmButton;
    
	  public ProjSelectionPanel(String title) 
	  {
	    super(new GridLayout(1, 2));
	    setBackground(Color.white);
	    setForeground(Color.black);
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
	  
	  public boolean isPCASelected(){
	        return this.pcaButton.isSelected();
	    }
	  
	  public void setPCASelected(boolean value){
	        pcaButton.setSelected(value);
	    }
}
