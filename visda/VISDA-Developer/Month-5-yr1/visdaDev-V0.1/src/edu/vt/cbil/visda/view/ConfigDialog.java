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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA
 * User: jwhitmore
 * Date: Oct 6, 2003
 * Time: 12:55:28 PM
 *
 * Copyright 2003 TGen, Inc.  All Rights Reserved.
 */
public class ConfigDialog extends JDialog  {

	public int nLevels;
	public int nFeatures;
	public int clusterType;
	public String distCon, linkCon;
	
    JButton runButton, cancelButton;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JCheckBox geneCheckBox;
    private JPanel geneClusterPanel;
    private JComboBox geneLineComboBox;
    private JLabel geneLineLabel;
    private JComboBox geneSymComboBox;
    private JLabel geneSymLabel;
    private JButton jButton1;
    private JButton jButton2;
    private JPanel jPanel3;
    private JPanel phClusterPanel;
    private JComboBox phLineComboBox;
    private JLabel phLineLabel;
    private JComboBox phSymComboBox;
    private JLabel phSymLabel;
    private JCheckBox phenoCheckBox;
    private JSpinner treeSpinner;
    private JLabel treejLabel;
    private JSpinner feaSelSpinner;
    private JLabel feaSeljLabel;
    private JPanel treePanel;
    // End of variables declaration//GEN-END:variables


    public ConfigDialog( Frame parent ) {
        super( parent, "Define VISDA Parameters", true );
        initialize();
        setVisible( true );

    }

    private void initialize() {
        Container contentPane = getContentPane();
        contentPane.setLayout( new GridBagLayout() );
        addWindowListener( new SimpleWindowAdapter() );
        
        treePanel = new JPanel();
        treejLabel = new JLabel();
        treeSpinner = new JSpinner( new SpinnerNumberModel( 3, 1, 5, 1));

        feaSeljLabel = new JLabel();
        feaSelSpinner = new JSpinner( new SpinnerNumberModel( 40, 10, 60, 5));
        
        phClusterPanel = new JPanel();
        phenoCheckBox = new JCheckBox();
        phSymComboBox = new JComboBox();
        phSymLabel = new JLabel();
        phLineComboBox = new JComboBox();
        phLineLabel = new JLabel();
        jPanel3 = new JPanel();
        jButton1 = new JButton();
        jButton2 = new JButton();
        geneClusterPanel = new JPanel();
        geneCheckBox = new JCheckBox();
        geneSymComboBox = new JComboBox();
        geneSymLabel = new JLabel();
        geneLineComboBox = new JComboBox();
        geneLineLabel = new JLabel();

        treePanel.setLayout(new GridLayout(2, 2));
        
        treejLabel.setText("Tree Level ");
        treePanel.add(treejLabel);
        
        treeSpinner.setName("treeSpinner");
        treePanel.add(treeSpinner);
        
        feaSeljLabel.setText("Number of Selected Feature");
        treePanel.add(feaSeljLabel);

        feaSelSpinner.setName("feaSelSpinner");
        treePanel.add(feaSelSpinner);
        contentPane.add(treePanel);
        
        phClusterPanel.setLayout(new GridLayout(0, 2));

        phClusterPanel.setBorder(new javax.swing.border.TitledBorder(""));
        phenoCheckBox.setText("Phenotype Clustering");
        phenoCheckBox.setName("phenoCheckBox");
        phClusterPanel.add(phenoCheckBox);
        phClusterPanel.add(new JLabel());

        phSymLabel.setText(" Distance Metric");
        phClusterPanel.add(phSymLabel);
        
        phSymComboBox.setForeground(new java.awt.Color(51, 0, 102));
        phSymComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Euclidean Distance", "Pearson Correlation", "Manhattan Distance", "Mutual Information"}));
        phSymComboBox.setBorder(new javax.swing.border.EtchedBorder());
        phSymComboBox.setName("phSymComboBox");
        phClusterPanel.add(phSymComboBox);

        phLineLabel.setText(" Linkage Method");
        phClusterPanel.add(phLineLabel);

        phLineComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Average linkage", "Complete linkage", "Single linkage" }));
        phLineComboBox.setName("phLineComboBox");
        phClusterPanel.add(phLineComboBox);

 
        contentPane.add(phClusterPanel);

       
        geneClusterPanel.setLayout(new GridLayout(0, 2));

        geneClusterPanel.setBorder(new javax.swing.border.TitledBorder(""));
        geneCheckBox.setLabel("Gene Clustering");
        geneCheckBox.setName("geneCheckBox");
        geneCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                geneCheckBoxActionPerformed(evt);
            }
        });

        geneClusterPanel.add(geneCheckBox);
        geneClusterPanel.add(new JLabel());
        
        geneSymLabel.setText(" Distance Metric");
        geneClusterPanel.add(geneSymLabel);
        
        geneSymComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Euclidean Distance", "Pearson Correlation", "Manhattan Distance", "Mutual Information" }));
        geneSymComboBox.setName("phSymComboBox");
        geneClusterPanel.add(geneSymComboBox);
   

        geneLineLabel.setText("Linkage Method");
        geneClusterPanel.add(geneLineLabel);
        
        geneLineComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Average linkage", "Complete linkage", "Single linkage"}));
        geneLineComboBox.setName("phLineComboBox");
        geneClusterPanel.add(geneLineComboBox);

        contentPane.add(geneClusterPanel);

        contentPane.add( new JButton( new ConfigureAction() ));
        contentPane.add( new JButton( new CancelAction() ));
        pack();
    }

 
    
    private void geneCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
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
            if (phenoCheckBox.isSelected()) {
            	clusterType = 0;
            	distCon = (String) phSymComboBox.getSelectedItem();
                linkCon = (String) phLineComboBox.getSelectedItem();
            } else if (geneCheckBox.isSelected()){
            	clusterType = 1;
            	distCon = (String) geneSymComboBox.getSelectedItem();
                linkCon = (String) geneLineComboBox.getSelectedItem();
            }
             
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
        }

    }


}
