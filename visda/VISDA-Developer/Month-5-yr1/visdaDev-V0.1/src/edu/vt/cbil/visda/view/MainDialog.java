package edu.vt.cbil.visda.view;


import edu.vt.cbil.visda.util.*;

import javax.swing.*;
import java.awt.*;

import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.util.Properties;

public class MainDialog extends JDialog {
  
    PlotGraph graph;
    Action resetAction;
    Action runAction;
    Action closeAction;
  
    double[][] data;
    private Properties properties;
    public static final Dimension DEFAULT_SIZE = new Dimension( 400, 400 );
    public static final String GENEIDSET_LOCATION_KEY = "geneIDSet.location";
    
    public MainDialog( Frame parent, double[][] datain) {
        super( parent, "Main Dialog", true );
        properties = new Properties();
        data = datain;
        initialize();
        setVisible( true );       
    }

    private void initialize() {
        createActions();
        createFileMenu();
        addWindowListener( new SimpleWindowAdapter() );
        Container contentPane = getContentPane();
        contentPane.setLayout( new BorderLayout() );
        
        graph = new PlotGraph(data);
        graph.setSize( DEFAULT_SIZE );
        graph.setGraphTitle("VISDA test");
        graph.setXaxisLegend("PC1");  
        graph.setYaxisLegend("PC2");    
        graph.setLine(0); //Plot scatter plot
        graph.setForeground(Color.blue);
        
        contentPane.add( graph, BorderLayout.CENTER );
        JPanel southButtonPanel = createMainButtonPanel();
        contentPane.add( southButtonPanel, BorderLayout.SOUTH );
        pack();
    }

    private void createActions() {
        resetAction = new ResetAction();
        runAction = new RunAction();
        closeAction = new CloseAction();
    }

    private void createFileMenu() {
        JMenuBar menuBar = new JMenuBar();
        // file menu
        JMenu fileMenu = new JMenu( "File" );     
        menuBar.add( fileMenu );
        setJMenuBar( menuBar );
    }

    private JPanel createMainButtonPanel() {
        JPanel mainButtonPanel = new JPanel();
        JButton resetButton = new JButton( resetAction );
        mainButtonPanel.add( resetButton );
        JButton runButton = new JButton( runAction );
        mainButtonPanel.add( runButton );
        JButton closeButton = new JButton( closeAction );
        mainButtonPanel.add( closeButton );
        return mainButtonPanel;
    }
  
 

 

 

    private class ResetAction extends AbstractAction {

        public ResetAction() {
            super( "Reset" );
            putValue( AbstractAction.NAME, "Reset" );
            putValue( AbstractAction.ACCELERATOR_KEY,
                      KeyStroke.getKeyStroke( KeyEvent.VK_R, ActionEvent.CTRL_MASK, false ) );

        }

        public void actionPerformed( ActionEvent ae ) {
            graph.repaint();
        }

    }

    private class RunAction extends AbstractAction {

        public RunAction() {
            super( "Run" );
            putValue( AbstractAction.ACCELERATOR_KEY,
                      KeyStroke.getKeyStroke( KeyEvent.VK_A, ActionEvent.CTRL_MASK, false ) );

        }

        public void actionPerformed( ActionEvent ae ) {
            // run the thang
            final SwingWorker worker = new SwingWorker() {
               public Object construct() {
                   return new Integer(1);

               }
               public void finished() {

               }

            };
            worker.setPriority( Thread.MIN_PRIORITY );
            worker.start();
            setVisible( false );

        }

    }

    private class CloseAction extends AbstractAction {

        public CloseAction() {
            super("Close");

        }

        public void actionPerformed( ActionEvent ae ) {
            setVisible( false );
            
        }

    }
  
    private class SimpleWindowAdapter extends WindowAdapter {
        public void windowClosed( WindowEvent we ) {
            MainDialog.this.dispose();
        }

    }

}