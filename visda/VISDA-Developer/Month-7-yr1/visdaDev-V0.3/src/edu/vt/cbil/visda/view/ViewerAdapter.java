package edu.vt.cbil.visda.view;

import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JPanel;


/**
 * The class to view history log
 * 
 * @author Jiajing Wang
 */
public class ViewerAdapter extends JPanel implements IViewer {
	
	public BufferedImage getImage() {return null;}

    //public void onSelected(IFramework framework) {}

    //public void onDataChanged(IData data) {}

    //public void onMenuChanged(IDisplayMenu menu) {}

    public void onDeselected() {}

    public void onClosed() {}

    public JComponent getContentComponent() {return null;}

    public JComponent getHeaderComponent() {return null;}

    public JComponent getRowHeaderComponent() {return null;}

    public JComponent getCornerComponent(int cornerIndex) {
        return null;
    }

    public int[][] getClusters() {
        return null;
    }
    
    //public Experiment getExperiment() {
    //    return null;
    //}
    
    /** Returns int value indicating viewer type
     * Cluster.GENE_CLUSTER, Cluster.EXPERIMENT_CLUSTER, or -1 for both or unspecified
     */
    public int getViewerType() {
        return -1;
    }    
}