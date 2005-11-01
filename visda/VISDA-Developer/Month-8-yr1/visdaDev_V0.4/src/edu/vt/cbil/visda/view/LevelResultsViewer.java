package edu.vt.cbil.visda.view;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import edu.vt.cbil.visda.comp.*;

/**
 * Created with Eclipse
 * Date: Aug 21, 2005
 */

/**
 * The class to add the results of one certain level into the view tree
 * 
 * @author Jiajing Wang
 * @version visda_v0.4
 * 
 * Version Log:
 *  visda_v0.2 (08/30/2005):	The file is created and basic function is implemented.
 *  visda_v0.3 (09/30/2005):
 * 	visda_v0.4 (10/29/2005):  	Support MDL and subcluster specific process;
 * 
 */
public class LevelResultsViewer {
	
	public DefaultMutableTreeNode node;
	
	public String[] selectedProjType;
	
	Plot2DViewer plotPanelsSub[];
	double WW[][][];
	
	public LevelResultsViewer(int levelId, ExperimentData expData,
			DisplayParam displaySetting) {
    	
        long start = System.currentTimeMillis();       
        String title = "Level_"+levelId;
        node = new DefaultMutableTreeNode(title);       
        //logger.append("Creating the result viewers");
        long time = System.currentTimeMillis() - start;     
        
        //addProjNode(node, levelId, expData, displaySetting);
        
        addVisualizeNode(node, time, levelId, expData);
        
        addTableNode(node, levelId, expData);

	}
	
	
	/**
     * Adds projection node into a result tree root.
     */
	private void addProjNode(DefaultMutableTreeNode node, int levelId, ExperimentData expData,
			DisplayParam displaySetting) {
		DefaultMutableTreeNode projectionNode;
		
		int clustersNum = expData.getSubLevelAttribute(levelId).subClustersNum;
		boolean endFlags[] = expData.getSubLevelAttribute(levelId).getAllClusterEndFlags();
		WW = new double[clustersNum][expData.numFeatures][2];
		plotPanelsSub = new Plot2DViewer[clustersNum];
		
		selectedProjType = new String[clustersNum];
		projectionNode = new DefaultMutableTreeNode("Each Sub-Cluster Projections");
		for (int i=0; i<clustersNum; i++) {
			//if (endFlags[i] == false) {
				addPerClusterProjNode(projectionNode, levelId, i+1, expData,
						displaySetting);
			//} 
		}
		
		node.add(projectionNode);
	}
	
	// Note: clusterId is from 1
	private void addPerClusterProjNode(DefaultMutableTreeNode node, 
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
        
        String title = "Level_"+levelId+"_Cluster_"+clusterId;
        
        boolean endFlag = expData.getSubLevelAttribute(levelId).getClusterEndFlag(clusterId);
        int selType = 1;
        if (endFlag == false) {
        	// Let user choose one of the projections
        	// ....
        	ProjSelectionDialog selDialog = new ProjSelectionDialog(new JFrame(), true, 
        		title, projsView);
        	if(selDialog.showModal() == JOptionPane.CANCEL_OPTION) {
        		System.out.print("cancel to choose a projection\n");
        	} else {
        		selType = selDialog.getSelectedProjType();
        		if (selType == 1) { // PCA
        			WW[clusterId-1] = clusterAttribute.top2Comp[0];
        			selectedProjType[clusterId-1] = "PCA";
        		} else if (selType == 2) { // PCAPPM
        			WW[clusterId-1] = clusterAttribute.top2Comp[1];
        			selectedProjType[clusterId-1] = "PCAPPM";
        		} else if (selType == 3) { // DCA
        			System.out.print("DCA is not implemented yet.\n");
        		}
        	}
        	selectedPlot = new Plot2DViewer(projsView[selType-1]);
        	expData.getSubLevelAttribute(levelId).setClusterProjtype(clusterId, selType);
        	if(selDialog.showModal() == JOptionPane.YES_OPTION) {  // the sub clustering is end
            	expData.getSubLevelAttribute(levelId).setClusterEndFlag(clusterId);  
            	double[] center = selectedPlot.getCenterDot();
                expData.getSubLevelAttribute(levelId).set1Cluster2DCenter(clusterId, center[0], center[1]);
            }
        	
        	MultiPlotsViewer multiProjsView = new MultiPlotsViewer(title+"_Projection",
					projsView, expData.sampleNames);
        	perClusterProjNode = new DefaultMutableTreeNode(new LeafInfo(title, multiProjsView,  
        			multiProjsView.getJPopupMenu(), multiProjsView.getPlots()));
        	node.add(perClusterProjNode); 

        } else {
        	selType = expData.getSubLevelAttribute(levelId).getClusterProjtype(clusterId);
        	//WW[clusterId-1] = clusterAttribute.top2Comp[selType-1]; 
        	selectedPlot = new Plot2DViewer(projsView[selType-1]);
        	//selectedPlot = new Plot2DViewer(data, WW[clusterId-1], 
            //		clusterAttribute.mu, labels, clusterAttribute.Zjk, displaySetting, expData);
        	//double[] center = selectedPlot.getCenterDot();
        	//expData.getSubLevelAttribute(levelId).set1Cluster2DCenter(clusterId, center[0], center[1]);
        }
		
		plotPanelsSub[clusterId-1] = selectedPlot; 
		expData.getSubLevelAttribute(levelId).getSubClusterAttribute(clusterId).setProjPlot(selectedPlot);
        
	}
	
	/**
     * Adds Visualization node into a result tree root.
     */
    private void addVisualizeNode(DefaultMutableTreeNode node, long time, 
    		int levelId, ExperimentData expData) {
    	DefaultMutableTreeNode visualizeNode;
    	
    	PerLevelAttribute levelAttribute = 
			expData.getSubLevelAttribute(levelId);
    	
    	int clusterNum = levelAttribute.subClustersNum;
    	Plot2DViewer levelPlots[] = new Plot2DViewer[clusterNum]; 
    	for (int i=0; i<clusterNum; i++) {
    		Plot2DViewer p = levelAttribute.getSubClusterAttribute(i+1).getProjPlot();
			levelPlots[i] = new Plot2DViewer(p);
		}
    	
    	String title;
    	title = "Level "+levelId+" Visualization";
    	
    	MultiPlotsViewer levelVisualizeViewer = new MultiPlotsViewer(title, 
    			levelPlots, expData.sampleNames);
    	visualizeNode = new DefaultMutableTreeNode(new LeafInfo(title, levelVisualizeViewer,  
    			levelVisualizeViewer.getJPopupMenu(), levelVisualizeViewer.getPlots()));
        node.add(visualizeNode);
    }
    
    /**
     * Adds Posterior probabilities node into a result tree root.
     */
    private void addTableNode(DefaultMutableTreeNode node, int levelId, ExperimentData expData) {
    	
    	DefaultMutableTreeNode tableNode;
    	  	
    	PerLevelAttribute levelAttribute = new PerLevelAttribute();
		levelAttribute = expData.getSubLevelAttribute(levelId);
		
		String clusterNames[] = levelAttribute.gettAllClusterTreeloc();
    	
    	JTable table = createTable(levelAttribute.subClustersNum, expData.sampleNames.size(), 
    			clusterNames, expData.sampleNames, expData.getLabels(), levelAttribute.Zjk);
    	  	
    	String title = "Level_"+levelId+"_PosteriorProbabilities";
    	TableViewer tableViewer = new TableViewer(table, title);
    	tableNode = new DefaultMutableTreeNode(new LeafInfo(title, tableViewer,  
    			tableViewer.getJPopupMenu()));
        node.add(tableNode);
    }
    
    private JTable createTable(int clustersNum, int samplesNum, String[] clusterNames,
    						   ArrayList sampleNames, int[] labels, double[][] Zjk) {
    	String[] columnNames = new String[2+clustersNum];
    	
    	for (int i=0; i<columnNames.length; i++) {
    		if (i==0) {
    			columnNames[i] = "Sample Name";
    		} else if (i==1) {
    			columnNames[i] = "Label";
    		} else if (i>1) {
    			columnNames[i] = "Cluster " + clusterNames[i-2];
    		}
    	}
    	
    	Object[][] rowData = new Object[samplesNum][columnNames.length];
    	for (int i=0; i<samplesNum; i++) {
    		for (int j=0; j<columnNames.length; j++) {
    			if (j==0) {
    				rowData[i][j] = sampleNames.get(i);
        		} else if (j==1) {
        			rowData[i][j] = labels[i];
        		} else if (j>1) {
        			rowData[i][j] = Zjk[i][j-2];
        		}
    		}
    	}
    		
    	JTable table = new JTable(rowData, columnNames);
    	table.setGridColor(Color.DARK_GRAY);
    	return table;
    }
}
