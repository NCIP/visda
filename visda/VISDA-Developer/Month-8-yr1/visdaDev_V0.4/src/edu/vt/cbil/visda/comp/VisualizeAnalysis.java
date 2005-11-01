package edu.vt.cbil.visda.comp;

import edu.vt.cbil.visda.comp.ExperimentData;
import edu.vt.cbil.visda.view.DisplayParam;
import edu.vt.cbil.visda.view.MainViewer;
import edu.vt.cbil.visda.view.LevelResultsViewer;
import edu.vt.cbil.visda.view.ClusterResultsViewer;


/**
 * The class performs the 2D projection process for all the clusters under the current level.
 * The data is projected onto the top 2 principal components resulting from PCA/PCAPPM/DCA. 
 * The 2D projection will be plotted with posterior probability of samples. 
 * And the user can choose one of the projection types.
 * The clustering results at this level can be viewed under the 'Analysis Results' tree.
 * 
 * @author Jiajing Wang
 * @version visda_v0.4
 * <p>
 * Version Log:
 * 	<br>visda_v0.2 (08/30/2005):	The file is created and basic function is implemented.
 * 	<br>visda_v0.4 (10/29/2005): 	
 */
public class VisualizeAnalysis {

	//int levelID;
	
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
  		  	
  		  	subLevelAttribute.setProjCompAttribute(j+1, drmcoreSub[j].top2PCVectors, 
  		  			drmcoreSub[j].top2PCPPMVectors);
  		  	
		}
					
		//LevelResultsViewer levelView = new LevelResultsViewer(levelId, expData, displaySetting);
		//for (int i=0; i<subClustersNum; i++) {
		//	String projType = levelView.selectedProjType[i];
		//	viewPanel.addHistory(projType+" is selected for Level "+levelId+" Cluster "+(i+1));
		//}
		//viewPanel.addAnalysisResult(levelView.node);
		//viewPanel.addLevelResult(levelView.node);
		
		ClusterResultsViewer clusterView = new ClusterResultsViewer(viewPanel.getClusterTopNode(),
																	levelId, expData, displaySetting);
		LevelResultsViewer levelView = new LevelResultsViewer(levelId, expData, displaySetting);
		viewPanel.addLevelResult(levelView.node);
		viewPanel.addHistory("Level "+levelId+" clusters visualization is generated");	
		  
	}
}
