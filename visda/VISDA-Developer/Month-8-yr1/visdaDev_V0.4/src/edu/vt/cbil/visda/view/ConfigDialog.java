package edu.vt.cbil.visda.view;


import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import java.awt.Frame;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;


/**
 * The class is to configure main parameteres used in VISDA analysis
 * @author Huai Li, Jiajing Wang
 * @version visda_v0.4
 * 
 * Version Log:
 * 	visda_v0.1 (07/30/2005):	The file is created and basic function is implemented.
 * 	visda_v0.2 (08/30/2005):  	
 * 	visda_v0.3 (09/29/2005):  		
 *  visda_v0.4 (10/29/2005):  	Add MDL option.
 *
 */
public class ConfigDialog extends JDialog  {

	public int nLevels;
	public int nFeatures;
	public int clusterType;
	public String distCon, linkCon;
	public double zjkThresh;
	public String labelKey;
	public boolean todoMDL=false;
	
	public int rtnVal;
	
    JButton runButton, cancelButton;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JCheckBox geneCheckBox;
    private JButton jButton1;
    private JButton jButton2;
    private JPanel jPanel3;
    private JPanel clusterPanel;
    private JComboBox lineComboBox;
    private JLabel lineLabel;
    private JComboBox symComboBox;
    private JLabel symLabel;
    private JCheckBox phenoCheckBox;
    private JSpinner treeSpinner;
    private JLabel treejLabel;
    private JSpinner feaSelSpinner;
    private JLabel feaSeljLabel;
    private JSpinner zjkSpinner;
    private JLabel zjkjLabel;
    private JPanel treePanel;
    private JPanel buttonPanel;
    
    private JComboBox labelComboBox;
    private JLabel labeljLabel;
    
    private JPanel mdlPanel;
    private JCheckBox mdlCheckBox;
    
    // End of variables declaration//GEN-END:variables


    public ConfigDialog( Frame parent, String[] labelKeys ) {
        super( parent, "Define VISDA Parameters", true );
        initialize( labelKeys );
        setVisible( true );

    }

    private void initialize(String[] labelKeys) {
        Container contentPane = getContentPane();
        contentPane.setLayout( new GridBagLayout() );
        GBA gba = new GBA();
        addWindowListener( new SimpleWindowAdapter() );
        
        treePanel = new JPanel();
        treejLabel = new JLabel();
        treeSpinner = new JSpinner( new SpinnerNumberModel( 3, 1, 30, 1));

        feaSeljLabel = new JLabel();
        feaSelSpinner = new JSpinner( new SpinnerNumberModel( 40, 1, 100, 5));
        
        zjkjLabel = new JLabel();
        zjkSpinner = new JSpinner( new SpinnerNumberModel( 10, 0, 100, 10));
        
        labeljLabel = new JLabel();
        labelComboBox = new JComboBox();
        
        clusterPanel = new JPanel();
        phenoCheckBox = new JCheckBox();
        geneCheckBox = new JCheckBox();
        symComboBox = new JComboBox();
        symLabel = new JLabel();
        lineComboBox = new JComboBox();
        lineLabel = new JLabel();
        jPanel3 = new JPanel();
        jButton1 = new JButton();
        jButton2 = new JButton();

        treePanel.setLayout(new GridLayout(4, 2, 0, 10));
        
        treejLabel.setText("Tree Level ");
        treePanel.add(treejLabel);
        
        treeSpinner.setName("treeSpinner");
        treePanel.add(treeSpinner);
        
        feaSeljLabel.setText("Number of Selected Features");
        treePanel.add(feaSeljLabel);

        feaSelSpinner.setName("feaSelSpinner");
        treePanel.add(feaSelSpinner);     
        
        labeljLabel.setText("Label ");
        treePanel.add(labeljLabel);
        
        labelComboBox.setModel(new javax.swing.DefaultComboBoxModel(labelKeys));
        labelComboBox.setName("labelComboBox");
        treePanel.add(labelComboBox);
        
        zjkjLabel.setText("<html>Threshold of Posterior" +
		          "<p>"+   		          
				  "Probability for Display (%)</html>");
        treePanel.add(zjkjLabel);

        zjkSpinner.setName("zjkSpinner");
        treePanel.add(zjkSpinner);

        clusterPanel.setLayout(new GridLayout(3, 2, 0, 10));

        clusterPanel.setBorder(new javax.swing.border.TitledBorder(""));
        phenoCheckBox.setText("Phenotype Clustering");
        phenoCheckBox.setName("phenoCheckBox");
        phenoCheckBox.setSelected(true);
        phenoCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	phenoCheckBoxActionPerformed(evt);
            }
        });
        clusterPanel.add(phenoCheckBox);

        geneCheckBox.setText("Gene Clustering");
        geneCheckBox.setName("geneCheckBox");
        geneCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                geneCheckBoxActionPerformed(evt);
            }
        });
        clusterPanel.add(geneCheckBox);
        
        symLabel.setText(" Distance Metric");
        clusterPanel.add(symLabel);
        
        symComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Euclidean Distance", "Pearson Correlation", "Manhattan Distance", "Mutual Information"}));
        symComboBox.setName("symComboBox");
        clusterPanel.add(symComboBox);

        lineLabel.setText(" Linkage Method");
        clusterPanel.add(lineLabel);

        lineComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Average linkage", "Complete linkage", "Single linkage" }));
        lineComboBox.setName("lineComboBox");
        clusterPanel.add(lineComboBox);
       
        // for MDL
        mdlPanel = new JPanel();
        mdlCheckBox = new JCheckBox();
        mdlCheckBox.setText("MDL Validation");
        mdlCheckBox.setName("mdlCheckBox");
        mdlCheckBox.setSelected(true);
        mdlCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	mdlCheckBoxActionPerformed(evt);
            }
        });
        mdlPanel.add(mdlCheckBox);
        
        buttonPanel = new JPanel();
        JButton configButton = new JButton( new ConfigureAction() );
        JButton cancelButton = new JButton( new CancelAction() );
        buttonPanel.add(configButton);
        buttonPanel.add(cancelButton);
        
        gba.add(contentPane, treePanel, 0, 0, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
        gba.add(contentPane, clusterPanel, 0, 1, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
        gba.add(contentPane, mdlPanel, 0, 2, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
        gba.add(contentPane, buttonPanel, 0, 3, 1, 1, 1, 0, GBA.H, GBA.C, (new Insets(5,5,5,5)), 0, 0);
        pack();
    }

    private void phenoCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	geneCheckBox.setSelected(false);
    }
    
    private void geneCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    	phenoCheckBox.setSelected(false);
    }

    private void mdlCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    
    /**
     * Yet another WindowAdapter to dispose() of the dialog.
     */
    public class SimpleWindowAdapter extends WindowAdapter {

        public void windowClosed( WindowEvent we ) {
            //dispose();

        }

    }

    /**
     * a class to encapsulate the task of <i>preparing</i> to run a network.
     * Our parent will be in charge of running the network <code>this</code>
     * will make ready.
     */
    private class ConfigureAction extends AbstractAction {

        public ConfigureAction() {
            super( "Configure" );
            putValue( AbstractAction.NAME, "Configure" );
            putValue( AbstractAction.ACCELERATOR_KEY,
                      KeyStroke.getKeyStroke( KeyEvent.VK_C, ActionEvent.CTRL_MASK, false ) );

        }

        public void actionPerformed( ActionEvent ae ) {
        	
            setVisible( false );
            //dispose();
            
            nLevels = ((SpinnerNumberModel)treeSpinner.getModel()).getNumber().intValue();
            nFeatures = ((SpinnerNumberModel)feaSelSpinner.getModel()).getNumber().intValue();
            int zjkPercentage = ((SpinnerNumberModel)zjkSpinner.getModel()).getNumber().intValue();
            zjkThresh = (double) zjkPercentage / (double) 100;
            labelKey = (String) labelComboBox.getSelectedItem();
            if (phenoCheckBox.isSelected()) {
            	clusterType = 0;
            } else if (geneCheckBox.isSelected()){
            	clusterType = 1;           	
            }
            distCon = (String) symComboBox.getSelectedItem();
            linkCon = (String) lineComboBox.getSelectedItem(); 
            todoMDL = mdlCheckBox.isSelected();
            rtnVal = 1;
        }

    }

    /**
     * a class to encapsulate the task of cancelling a new network
     */
    private class CancelAction extends AbstractAction {

        public CancelAction() {
            super( "Cancel" );
            putValue( AbstractAction.NAME, "Cancel" );
            putValue( AbstractAction.ACCELERATOR_KEY,
                      KeyStroke.getKeyStroke( KeyEvent.VK_A, ActionEvent.CTRL_MASK, false ) );

        }

        public void actionPerformed( ActionEvent ae ) {
            setVisible( false );
            rtnVal = 0;
        }

    }


}
