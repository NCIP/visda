package edu.vt.cbil.visda.comp;

import edu.vt.cbil.visda.view.Plot2DViewer;

import edu.vt.cbil.visda.comp.ExperimentData;

/**
 * Created with Eclipse
 * Date: Aug 21, 2005
 */

/**
 * to perform lower level sub-clustering
 * 
 * @author Jiajing Wang
 * @version visda_v0.2
 */
public class ClusteringAnalysis {
	
	public CFMCore cfmcoreSub;
	
	public ClusteringAnalysis(ExperimentData expData, int upperLevelID) {
		
		int N = expData.numSamples;
		int P = expData.numFeatures;
		double[][] data = expData.data;
		int subK;
		double vv = 0.5;
		
		// get the upper level visualization information
	  	PerLevelVisualize upLevelVisualize = new PerLevelVisualize();
		upLevelVisualize = expData.getSubLevelVisualize(upperLevelID);
		Plot2DViewer upLevelPlots[] = new Plot2DViewer[upLevelVisualize.subLevelPlots.length];
		upLevelPlots = upLevelVisualize.subLevelPlots;
		
		// get the upper level posterior probabilities information
		PerLevelAttribute upLevelAttribute = new PerLevelAttribute();
		upLevelAttribute = expData.getSubLevelAttribute(upperLevelID);
		
		// Sub-Clusters initialization on Up-level plot
		CVMCore cvmcore = new CVMCore(N, P);
		cvmcore.level = upperLevelID;
		if (upperLevelID==1) {
			cvmcore.veMselect(upLevelPlots[0], data, upLevelVisualize.WW[0]);
		} else if (upperLevelID > 1) {
			cvmcore.veMselect2(upLevelPlots, data, upLevelVisualize.WW, 
					upLevelAttribute.w, upLevelAttribute.mu, upLevelAttribute.Zjk);
		}
		//cvmcore.print();
		  
		subK = cvmcore.numSubClusters;
		  
		// EM (model formation)
		if (upperLevelID == 1) {
			System.out.print("-----------------------------------------\n");
			System.out.print("| Top level EM in t-space starts ...\n");
		} else {
			System.out.print("-----------------------------------------\n");
			System.out.print("| Level_"+upperLevelID+" EM in t-space starts ...\n");
		}
		
		cfmcoreSub = new CFMCore(N, P, subK);
		//cfmcoreSub.uc = vk0;
		cfmcoreSub.Data = data;
		cfmcoreSub.Mean0_x = cvmcore.mu0;
		cfmcoreSub.W0 = cvmcore.w0;
		cfmcoreSub.vv = vv;
		cfmcoreSub.errorThresh = 0.01;
		cfmcoreSub.veSubNew();
		
		System.out.print("-----------------------------------------\n");
		System.out.print("| Level "+(upperLevelID+1)+" mixture model is generated.\n\n");
		
		expData.setSubLevelAttribute(upperLevelID + 1, cfmcoreSub.W0_t, cfmcoreSub.Mean1_x, 
				cfmcoreSub.Cov_mat, cfmcoreSub.Zjk);
		
	}
}