package edu.vt.cbil.visda.view;

import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 * The interface of a class, which can be displayed
 * in the framework scroll pane.
 *
 * @version 1.0
 * @author Aleksey D.Rezantsev
 */
public interface IViewer {
    
    public static int UPPER_LEFT_CORNER = 0;
    public static int UPPER_RIGHT_CORNER = 1;
    public static int LOWER_LEFT_CORNER = 2;
    
    /**
     * Returns a component to be inserted into scroll pane view port.
     */
    public JComponent getContentComponent();
    
    /**
     * Returns a component to be inserted into scroll pane header.
     */
    public JComponent getHeaderComponent();
    
    /**
     * Returns a component to be inserted into the scroll pane row header
     */
    public JComponent getRowHeaderComponent();  

    /**
     * Returns the corner component corresponding to the indicated corner,
     * posibly null
     */
    public JComponent getCornerComponent(int cornerIndex);
    
    
    /**
     * Invoked by the framework when this viewer is selected.
     */
    //public void onSelected(IFramework framework);
    
    /**
     * Invoked by the framework when data is changed,
     * if this viewer is selected.
     * @see IData
     */
    //public void onDataChanged(IData data);
    
    /**
     * Invoked by the framework when display menu is changed,
     * if this viewer is selected.
     * @see IDisplayMenu
     */
    //public void onMenuChanged(IDisplayMenu menu);
    
    /**
     * Invoked by the framework when this viewer was deselected.
     */
    public void onDeselected();
    
    /**
     * Invoked when the framework is going to be closed.
     */
    public void onClosed();
    
    /**
     * Invoked by the framework to save or to print viewer image.
     */
    public BufferedImage getImage();
    
    /**
     * Returns the viewer's clusters or null
     */
    public int[][] getClusters();
    
    
    /**
     *  Returns the viewer's experiment or null
     */
    //public Experiment getExperiment();
    
    /**
     * Returns int value indicating viewer type
     * Cluster.GENE_CLUSTER, Cluster.EXPERIMENT_CLUSTER, or -1 for both or unspecified
     */
    public int getViewerType();
}
