package edu.vt.cbil.visda.comp;

import org.math.plot.Plot2DPanel;
import edu.vt.cbil.visda.comp.ExperimentData;
import edu.vt.cbil.visda.view.DisplayParam;
import edu.vt.cbil.visda.view.MainViewer;
import edu.vt.cbil.visda.view.LevelResultsViewer;;

/**
 * Created with Eclipse
 * Date: Aug 21, 2005
 */

/**
 * The class performs the 2D projection process for all the sub-clusters at the certain level.
 * The data is projected onto the top 2 principal components resulting from PCA/PCAPPM. 
 * The 2D projection will be plotted with posterior probability of samples. 
 * And the user can choose one of the projection types.
 * The clustering results at this level can be viewed under the 'Analysis Results' tree.
 * 
 * @author Jiajing Wang
 * @version visda_v0.2
 */
public class VisualizeAnalysis {

	//int levelID;
	Plot2DPanel plotPanelsSub[];
	double WW[][][];
	
	public VisualizeAnalysis(int levelId, MainViewer viewPanel, ExperimentData expData, 
			DisplayParam displaySetting) {
		
		int N = expData.numSamples;
		int P = expData.numFeatures;
		double[][] data = expData.data;
		int[] labels = expData.getLabels();
		double Zjk[]= new double[N];
		double mu[] = new double[P];
		double cov[][] = new double[P][P];
	  	
	  	// get the sub level information
	  	PerLevelAttribute subLevelAttribute = new PerLevelAttribute();
		subLevelAttribute = expData.getSubLevelAttribute(levelId);
		int subClustersNum = subLevelAttribute.subClustersNum;
		
		// Plot multiple sublevel projections
		WW = new double[subClustersNum][P][2];
		plotPanelsSub = new Plot2DPanel[subClustersNum];
		DRMCore[] drmcoreSub = new DRMCore[subClustersNum];
		
		for (int j = 0; j<subClustersNum; j++) {
			mu = subLevelAttribute.attributeOfSubClusters[j].mu;
			Zjk = subLevelAttribute.attributeOfSubClusters[j].Zjk;
			cov = subLevelAttribute.attributeOfSubClusters[j].covMat;
			//centerData(N, P, data, mu);
			  
			drmcoreSub[j] = new DRMCore(N, P, levelId, j+1);
			drmcoreSub[j].data = data;
			drmcoreSub[j].covCluster = cov;
			drmcoreSub[j].meanCluster = mu;
			drmcoreSub[j].Zjk = Zjk;
  		  	drmcoreSub[j].veProjection();
  		  	//drmcoreSub[j].print();
  		  	
  		  	subLevelAttribute.setProjCompAttribute(j, drmcoreSub[j].top2PCVectors, 
  		  			drmcoreSub[j].top2PCPPMVectors);
  		  	
		}
					
		LevelResultsViewer levelView = new LevelResultsViewer(levelId, expData, displaySetting);
		for (int i=0; i<subClustersNum; i++) {
			String projType = levelView.selectedProjType[i];
			viewPanel.addHistory(projType+" is selected for Level "+levelId+" Cluster "+(i+1));
		}
		viewPanel.addAnalysisResult(levelView.node);
		viewPanel.addHistory("Level "+levelId+" clusters visualization is generated");
		
		//expData.setSubLevelVisaulize(levelId, plotPanelsSub, WW);		
		  
	}
}