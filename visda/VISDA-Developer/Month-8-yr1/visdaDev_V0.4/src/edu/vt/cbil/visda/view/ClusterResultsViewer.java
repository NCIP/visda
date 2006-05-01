package edu.vt.cbil.visda.view;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import edu.vt.cbil.visda.comp.*;

/**
 * The class to add the results of one certain level into the view tree
 * 
 * @author Jiajing Wang
 * @version visda_v0.4
 * <p>
 * Version Log:
 *  <br>visda_v0.4 (10/30/2005):	The file is created and basic function is implemented.
 * 
 */
public class ClusterResultsViewer {
	
	public String[] selectedProjType;
	
	Plot2DViewer plotPanelsSub[];
	double WW[][][];
	
	public ClusterResultsViewer(DefaultMutableTreeNode node, int levelId, ExperimentData expData,
			DisplayParam displaySetting) {
		
		if (levelId == 1) {
			addSubclusterNode(node, 1, 1, expData, displaySetting);	
		} else {
			int upClustersTotalNum = expData.getSubLevelAttribute(levelId-1).subClustersNum;
			int clusterId = 0;
			for (int i=0; i<upClustersTotalNum; i++) {
				int upClusterNum = expData.getSubLevelAttribute(levelId-1).getSubClusterAttribute(i+1).getNumSubClusters();
				if (upClusterNum > 1) {
					for (int k=0; k<upClusterNum; k++) {
						clusterId++;
						String clusterTreeLoc = expData.getSubLevelAttribute(levelId).getSubClusterAttribute(clusterId).getClusterTreeLoc();
						String[] ss = clusterTreeLoc.split("-");
						DefaultMutableTreeNode parentNode = node;
						for (int j=0; j<ss.length-1; j++) {
							int id = (int) Integer.parseInt(ss[j]);
							parentNode = (DefaultMutableTreeNode) parentNode.getChildAt(id-1);
						}
						addSubclusterNode(parentNode, levelId, clusterId, expData, displaySetting);				
					}
				} else if (upClusterNum == 1) {
					clusterId++;
					updateCluster(levelId, clusterId, expData, displaySetting);
				}
			}
		}
	}
	
	//	 Note: clusterId is from 1
	private void addSubclusterNode(DefaultMutableTreeNode node, 
			int levelId, int clusterId, ExperimentData expData, DisplayParam displaySetting) {
		int N = expData.numSamples;
		int P = expData.numFeatures;
		double[][] data = expData.data;
		int[] labels = expData.getLabels();
		Plot2DViewer selectedPlot;
		DefaultMutableTreeNode perClusterProjNode;
		
		PerLevelPerClusterAttribute clusterAttribute = 
			expData.getSubLevelAttribute(levelId).getSubClusterAttribute(clusterId);
		
        Plot2DViewer projsView[] = new Plot2DViewer[2];
        projsView[0] = new Plot2DViewer(data, clusterAttribute.top2Comp[0], 
        		clusterAttribute.mu, labels, clusterAttribute.Zjk, displaySetting, expData);
        projsView[0].setPlotName("PCA Projection");
        //projsView[0].addCenterDot();
        
        projsView[1] = new Plot2DViewer(data, clusterAttribute.top2Comp[1], 
        		clusterAttribute.mu, labels, clusterAttribute.Zjk, displaySetting, expData);
        projsView[1].setPlotName("PCAPPM Projection");
        //projsView[1].addCenterDot();
        
        
        //String title = "Level_"+levelId+"_Cluster_"+clusterId;
        String title = "Cluster "+clusterAttribute.getClusterTreeLoc();
        //boolean endFlag = expData.getSubLevelAttribute(levelId).getClusterEndFlag(clusterId);
        int selType = 1;
        
        // Let user choose one of the projections
        // ....
        ProjSelectionDialog selDialog = new ProjSelectionDialog(new JFrame(), true, 
        														title, projsView);
        if(selDialog.showModal() == JOptionPane.CANCEL_OPTION) {
        	System.out.print("cancel to choose a projection\n");
        } else {
        	selType = selDialog.getSelectedProjType();
        }
        
        selectedPlot = new Plot2DViewer(projsView[selType-1]);
        selectedPlot.setPlotName(title);
        expData.getSubLevelAttribute(levelId).setClusterProjtype(clusterId, selType);
        //if(selDialog.showModal() == JOptionPane.YES_OPTION) {  // the sub clustering is end
        //	expData.getSubLevelAttribute(levelId).setClusterEndFlag(clusterId);  
        	//double[] center = selectedPlot.getCenterDot();
        	//expData.getSubLevelAttribute(levelId).set1Cluster2DCenter(clusterId, center[0], center[1]);
        //}
        	
        MultiPlotsViewer multiProjsView = new MultiPlotsViewer(title, 
        													   projsView, expData.sampleNames);
        perClusterProjNode = new DefaultMutableTreeNode(new LeafInfo(title, multiProjsView,  
        												multiProjsView.getJPopupMenu(), multiProjsView.getPlots()));
        node.add(perClusterProjNode); 
		
		expData.getSubLevelAttribute(levelId).getSubClusterAttribute(clusterId).setProjPlot(selectedPlot);
        
	}
	
	
	//	 Note: clusterId is from 1
	private void updateCluster(
			int levelId, int clusterId, ExperimentData expData, DisplayParam displaySetting) {
		double[][] data = expData.data;
		int[] labels = expData.getLabels();
		Plot2DViewer selectedPlot;
		
		PerLevelPerClusterAttribute clusterAttribute = 
			expData.getSubLevelAttribute(levelId).getSubClusterAttribute(clusterId);
		
        Plot2DViewer projsView[] = new Plot2DViewer[2];
        projsView[0] = new Plot2DViewer(data, clusterAttribute.top2Comp[0], 
        		clusterAttribute.mu, labels, clusterAttribute.Zjk, displaySetting, expData);
        projsView[0].setPlotName("PCA Projection");
        //projsView[0].addCenterDot();
        
        projsView[1] = new Plot2DViewer(data, clusterAttribute.top2Comp[1], 
        		clusterAttribute.mu, labels, clusterAttribute.Zjk, displaySetting, expData);
        projsView[1].setPlotName("PCAPPM Projection");
        //projsView[1].addCenterDot();
        
        int selType = 1;
        selType = expData.getSubLevelAttribute(levelId).getClusterProjtype(clusterId);
        selectedPlot = new Plot2DViewer(projsView[selType-1]); 
        String name = "Cluster "+clusterAttribute.getClusterTreeLoc();
        selectedPlot.setPlotName(name);
		expData.getSubLevelAttribute(levelId).getSubClusterAttribute(clusterId).setProjPlot(selectedPlot);
        
	}
	
}
