package edu.vt.cbil.visda.comp;

import javax.swing.JOptionPane;

import edu.vt.cbil.visda.comp.ExperimentData;

/**
 * The class performs the lower level sub-clustering
 * 
 * @author Jiajing Wang
 * @version visda_v0.4
 * <p>
 * Version Log:
 * 	<br>visda_v0.2 (08/30/2005):	The file is created and basic function is implemented.
 * 	<br>visda_v0.4 (10/11/2005): 	Support sub-cluster specific clustering.
 * 									Add protection when EM results are not real number
 *  
 */
public class ClusteringAnalysis {
	
	public CFMCore cfmcoreSub;
	
	public int returnVal = 0;
	
	private int upperLevelID;
	
	public ClusteringAnalysis(ExperimentData expData, int upperLevelID) {
		
		this.upperLevelID = upperLevelID;
		int N = expData.numSamples;
		int P = expData.numFeatures;
		double[][] data = expData.data;
		int upK, subK;
		double vv = 0.5;
		boolean doMDL = expData.getMDLFlag();
		
		// get the upper level attribute
		PerLevelAttribute upLevelAttribute = new PerLevelAttribute();
		upLevelAttribute = expData.getSubLevelAttribute(upperLevelID);
		upK = upLevelAttribute.subClustersNum;
		
		// Sub-Clusters initialization on Up-level plot
		CVMCore cvmcore = new CVMCore(N, P, doMDL);
		cvmcore.level = upperLevelID;
		if (upperLevelID==1) {
			cvmcore.veMselect(data, upLevelAttribute);
		} else if (upperLevelID > 1) {
			int result = cvmcore.veMselect2(data, upLevelAttribute);
			int badClusterId = cvmcore.rntClusterId;
			String badClusterName = upLevelAttribute.getSubClusterAttribute(badClusterId+1).getClusterTreeLoc();
			if (result == 0) {
				exceptionHandle("Fail to partition Cluster "+badClusterName+".\n");
				returnVal = 0; 
				return;
			}
		}
		//cvmcore.print();
		  
		subK = cvmcore.numSubClusters;
		if (subK == upK) { // no more clusters are found
			JOptionPane.showMessageDialog(null, 
         		   "No new level is generated since the clusters are same as previous level", 
         		   "Warning", 
         		   JOptionPane.INFORMATION_MESSAGE);
			returnVal = 0;
			return;
		}
		  
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
		
		if (cfmcoreSub.isValid == true) {
					
			System.out.print("-----------------------------------------\n");
			System.out.print("| Level "+(upperLevelID+1)+" Mixture Model is generated.\n\n");
				
			//	set the current level attribute
			expData.setSubLevelAttribute(upperLevelID + 1, cfmcoreSub.W0_t, cfmcoreSub.Mean1_x, 
				cfmcoreSub.Cov_mat, cfmcoreSub.Zjk);
		
			int[] numChildForUpClusters = cvmcore.numChildForUpClusters;
			//boolean endFlags[] = new boolean[subK]; 
			int projTypes[] = new int[subK];
			int u = 0;
			int upProjtypes[] = new int[upK];
			upProjtypes = expData.getSubLevelAttribute(upperLevelID).getAllClusterProjtypes();
			String clusterTreeLoc[] = new String[subK];
			for (int i=0; i<upK; i++) {
				expData.getSubLevelAttribute(upperLevelID).getSubClusterAttribute(i+1).setNumSubClusters(numChildForUpClusters[i]);
				String upClusterTreeLoc = expData.getSubLevelAttribute(upperLevelID).getSubClusterAttribute(i+1).getClusterTreeLoc();
				if (numChildForUpClusters[i] == 1) {
					//endFlags[u] = true;	
					projTypes[u] = upProjtypes[i];
					clusterTreeLoc[u] = upClusterTreeLoc; //upClusterTreeLoc+"-1";
					u++;
				} else if (numChildForUpClusters[i] > 1) {
					for (int j=0; j<numChildForUpClusters[i]; j++) {
						//endFlags[u+j] = false;	
						projTypes[u+j] = upProjtypes[i];
						clusterTreeLoc[u+j] = upClusterTreeLoc+"-"+(j+1);
					}
					u += numChildForUpClusters[i];
				}
			}
			
			//expData.getSubLevelAttribute(upperLevelID+1).setAllClusterEndFlags(endFlags);
			expData.getSubLevelAttribute(upperLevelID+1).setCluster2DCenters(cvmcore.centers);
			expData.getSubLevelAttribute(upperLevelID+1).setAllClusterProjtypes(projTypes);
			expData.getSubLevelAttribute(upperLevelID+1).setAllClusterTreeloc(clusterTreeLoc);
		
			returnVal = 1;
		
		} else {
			
			exceptionHandle("Fail to generate the Mixture Model on Level "+(upperLevelID+1)+".\n");
			returnVal = 0; 
			return;
		}
	}
	
	public void exceptionHandle(String message) {
		System.out.print("-----------------------------------------\n");
		System.out.print("| Fail to generate the Mixture Model on Level "+(upperLevelID+1)+"!!!\n");
		Object[] options = {"OK"};
		int n = JOptionPane.showOptionDialog(null,
				message+
				"The reason might be clusters cannot be further partitioned \n"+
				"     or you selected the bad center points.\n",
				"Warning",
				JOptionPane.OK_OPTION,
				JOptionPane.WARNING_MESSAGE,
				null,     //don't use a custom Icon
				options,  //the titles of buttons
				options[0]); //default button title	
	}
}
