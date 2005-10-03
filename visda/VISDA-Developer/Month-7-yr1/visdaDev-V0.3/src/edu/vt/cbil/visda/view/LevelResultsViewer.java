package edu.vt.cbil.visda.view;

import static org.math.array.LinearAlgebra.*;
import static org.math.array.StatisticSample.*;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.math.plot.Plot2DPanel;
import edu.vt.cbil.visda.util.Common;
import edu.vt.cbil.visda.comp.*;

/**
 * Created with Eclipse
 * Date: Aug 21, 2005
 */

/**
 * The class to add the results of one certain level into the view tree
 * 
 * @author Jiajing Wang
 * @version visda_v0.1
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
        
        addProjNode(node, levelId, expData, displaySetting);
        
        addVisualizeNode(node, time, levelId, expData, displaySetting);
        
        addTableNode(node, levelId, expData);

	}
	
	
	/**
     * Adds projection node into a result tree root.
     */
	private void addProjNode(DefaultMutableTreeNode node, int levelId, ExperimentData expData,
			DisplayParam displaySetting) {
		DefaultMutableTreeNode projectionNode;
		
		int clustersNum = expData.getSubLevelAttribute(levelId).subClustersNum;
		WW = new double[clustersNum][expData.numFeatures][2];
		plotPanelsSub = new Plot2DViewer[clustersNum];
		
		selectedProjType = new String[clustersNum];
		projectionNode = new DefaultMutableTreeNode("Each Sub-Cluster Projections");
		for (int i=0; i<clustersNum; i++) {
			addPerClusterProjNode(projectionNode, levelId, i+1, expData,
					displaySetting);
		}
		
		node.add(projectionNode);
		expData.setSubLevelVisaulize(levelId, plotPanelsSub, WW);
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
		
		String projTypes[] = {"PCA Projection", "PCAPPM Projection"};
		
        //Plot2DPanel projPanels[] = new Plot2DPanel[2]; 
        
        Plot2DViewer projsView[] = new Plot2DViewer[2];
        projsView[0] = new Plot2DViewer(data, clusterAttribute.top2Comp[0], 
        		clusterAttribute.mu, labels, clusterAttribute.Zjk, displaySetting, expData);
        
        projsView[1] = new Plot2DViewer(data, clusterAttribute.top2Comp[1], 
        		clusterAttribute.mu, labels, clusterAttribute.Zjk, displaySetting, expData);
        
        String title = "Level_"+levelId+"_Cluster_"+clusterId;
        
        // Let user choose one of the projections
		// ....
        ProjSelectionDialog selDialog = new ProjSelectionDialog(new JFrame(), true, 
        		title, projTypes, projsView);
        if(selDialog.showModal() == JOptionPane.CANCEL_OPTION)
        	System.out.print("cancel to choose a projection\n");
        
        if (selDialog.isPCA == true) {
		    WW[clusterId-1] = clusterAttribute.top2Comp[0];
		    selectedProjType[clusterId-1] = "PCA";
		} else {
			WW[clusterId-1] = clusterAttribute.top2Comp[1];
			selectedProjType[clusterId-1] = "PCAPPM";
		}
		selectedPlot = new Plot2DViewer(data, WW[clusterId-1], 
        		clusterAttribute.mu, labels, clusterAttribute.Zjk, displaySetting, expData);
		
		plotPanelsSub[clusterId-1] = selectedPlot;
		
		MultiPlotsViewer multiProjsView = new MultiPlotsViewer(title+"_Projection", projTypes, 
												projsView, expData.sampleNames);
		perClusterProjNode = new DefaultMutableTreeNode(new LeafInfo(title, multiProjsView,  
        		multiProjsView.getJPopupMenu(), multiProjsView.getPlots()));
        node.add(perClusterProjNode);
	}
	
	/**
     * Adds Visualization node into a result tree root.
     */
    private void addVisualizeNode(DefaultMutableTreeNode node, long time, 
    		int levelId, ExperimentData expData, DisplayParam displaySetting) {
    	int N = expData.numSamples;
		int P = expData.numFeatures;
		double[][] data = expData.data;
		int[] labels = expData.getLabels();
    	DefaultMutableTreeNode visualizeNode;
    	
    	// get the current level information
		PerLevelVisualize levelVisualize = new PerLevelVisualize();
		levelVisualize = expData.getSubLevelVisualize(levelId);
		
		PerLevelAttribute levelAttribute = new PerLevelAttribute();
		levelAttribute = expData.getSubLevelAttribute(levelId);
		
		Plot2DViewer levelPlots[] = new Plot2DViewer[levelAttribute.subClustersNum]; 
		for (int i=0; i<levelAttribute.subClustersNum; i++) {
			levelPlots[i] = new Plot2DViewer(data, levelVisualize.WW[i], levelAttribute.attributeOfSubClusters[i].mu, 
					labels, levelAttribute.attributeOfSubClusters[i].Zjk, displaySetting, expData);
		}
		
		String plotNames[];	
    	plotNames = new String[levelVisualize.subLevelPlots.length];
    	for (int i=0; i<plotNames.length; i++) {
    		plotNames[i] = "Level_"+levelId+"_Cluster_"+(i+1);
    	}
    	
    	String title;
    	title = "Level_"+levelId+"_Visualization";
    	MultiPlotsViewer levelVisualizeViewer = new MultiPlotsViewer(title, plotNames, 
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
		
    	JTable table = createTable(levelAttribute.subClustersNum, expData.sampleNames.size(), 
    			expData.sampleNames, expData.getLabels(), levelAttribute.Zjk);
    	  	
    	String title = "Level_"+levelId+"_PosteriorProbabilities";
    	TableViewer tableViewer = new TableViewer(table, title);
    	tableNode = new DefaultMutableTreeNode(new LeafInfo(title, tableViewer,  
    			tableViewer.getJPopupMenu()));
        node.add(tableNode);
    }
    
    private JTable createTable(int clustersNum, int samplesNum, ArrayList sampleNames,
    						 int[] labels, double[][] Zjk) {
    	String[] columnNames = new String[2+clustersNum];
    	
    	for (int i=0; i<columnNames.length; i++) {
    		if (i==0) {
    			columnNames[i] = "Sample Name";
    		} else if (i==1) {
    			columnNames[i] = "Label";
    		} else if (i>1) {
    			columnNames[i] = "Cluster_" + (i-1);
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