package edu.vt.cbil.visda.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import edu.vt.cbil.visda.comp.*;
import edu.vt.cbil.visda.data.DefaultSample;


/**
 * Created with Eclipse
 * Date: August 29, 2005
 */

/**
 * The class is used to generate the dialog for sample annotation
 * 
 * @author Jiajing Wang
 * @version visda_v0.3
 * <p>
 * Version Log:
 * 	<br>visda_v0.2 (08/30/2005):  The file is created
 * 	<br>visda_v0.3 (09/29/2005):  Add Zjk annotation
 */
public class AnnotationDialog extends JDialog {
	
	protected JPanel sampleAnotPanel;
	protected JPanel featureAnotPanel;
	protected JPanel zjkAnotPanel;
	protected JPanel buttonPanel;

	protected JLabel sampleAnotLabel;
	protected JLabel featureAnotLabel;
	protected JLabel zjkAnotLabel;
	protected JButton closeButton;
	
	
	public AnnotationDialog(JFrame frame, boolean modal,
			  int sampleIndex, ExperimentData expData, double Zjk) {
	    super(frame, "Data Information", modal);
	    setPreferredSize(new Dimension(200, 300));
	    
	    // Plot Information
	    DefaultSample curSample = (DefaultSample) expData.sampleNames.get(sampleIndex);
	    String SampleName = "";
		SampleName += curSample;
		
	    //WindowUtilities.setNativeLookAndFeel();
	    //addWindowListener(new ExitListener());
	    //setBackground(Color.white);
	    //setForeground(Color.black);
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getSize().width)/2, (screenSize.height - getSize().height)/2);
        //setPreferredSize(new Dimension(200, 200));
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    Container content = getContentPane();
	    
	    sampleAnotPanel = new JPanel();
	    sampleAnotLabel = new JLabel();
	    sampleAnotLabel.setForeground(java.awt.Color.blue);
        String sampleAnots = "<html>Index:  "+(sampleIndex+1)+"<P>" +
        					 	   "Name:   "+SampleName+"<P>";
        for (int i=0; i<expData.labelKeys.size(); i++) {
        	String key = (String) expData.labelKeys.get(i);
        	String label = (String) curSample.getSampleLabels().get(key);
        	sampleAnots += key+": "+label+"<P>";
        }
        sampleAnots += "</html>";
        					 	   
        sampleAnotLabel.setText(sampleAnots);
        sampleAnotPanel.setBorder(BorderFactory.createTitledBorder("Sample Annotation"));  
        sampleAnotPanel.add(sampleAnotLabel);
        
        featureAnotPanel = new JPanel();
	    featureAnotLabel = new JLabel();
	    featureAnotLabel.setForeground(java.awt.Color.blue);
        String featureAnots = "<html></html>";
        featureAnotLabel.setText(featureAnots);
        featureAnotPanel.setBorder(BorderFactory.createTitledBorder("Feature Annotation"));
        featureAnotPanel.add(featureAnotLabel);
        
        zjkAnotPanel = new JPanel();
        zjkAnotLabel = new JLabel();
        zjkAnotLabel.setForeground(java.awt.Color.blue);
        String zjkAnots = Double.toString(Zjk);
        zjkAnotLabel.setText(zjkAnots);
        zjkAnotPanel.setBorder(BorderFactory.createTitledBorder("Posterior Probability Annotation"));
        zjkAnotPanel.add(zjkAnotLabel);
        
	    //	  button panel
	    buttonPanel = new JPanel();
	    closeButton = new JButton("CLOSE");  
        closeButton.setActionCommand("close-command");
        closeButton.setSize(80,30);
        closeButton.setPreferredSize(new Dimension(80,30));
        closeButton.setFocusPainted(false);	  
        buttonPanel.add(closeButton);
        
        setActionListeners(new EventListener());
        
        content.setLayout(new GridBagLayout());
        GBA gba = new GBA();
        gba.add(content, sampleAnotPanel, 0, 0, 2, 1, 1, 1, GBA.B, GBA.C, (new Insets(5,5,5,5)), 0, 0);
        gba.add(content, featureAnotPanel, 0, 1, 2, 1, 1, 1, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
        gba.add(content, zjkAnotPanel, 0, 2, 2, 1, 1, 1, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
        gba.add(content, buttonPanel, 1, 3, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
	    pack();
	    setVisible(true);
        
	}
	
	protected void setActionListeners(ActionListener listener){
        closeButton.addActionListener(listener);
        
    }
	
	protected class EventListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String command = event.getActionCommand();
            if (command.equals("close-command")) {
                dispose();
            } 
        }
    }
	
}
