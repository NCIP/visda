package edu.vt.cbil.visda.comp;

import org.math.plot.Plot2DPanel;
import edu.vt.cbil.visda.util.*;
import edu.vt.cbil.visda.view.*;
import javax.swing.*;
import static org.math.array.LinearAlgebra.*;

/**
 * Created with Eclipse
 * Date: July 26, 2005
 */

/**
 * The class performs clustering model selection and validation tasks for VISDA
 * @author Jiajing Wang
 * @version visda_v0.1
 */
public class CVMCore
{

	// input interface
	/**
     * the number of samples
     */
	int numSamples;
	
	/**
     * the number of features
     */
	int numFeatures;
	
	/**
     * the index of current level
     */
	int level;
	
	/**
     * the number of clusters at the upper level
     */
	//int numUpClusters;
	
	/**
     * the number of clusters at the current sub-level
     */
	int numSubClusters;
	
	// output interface
	/**
     * the initial Means of clusters in t-space; each row is for one cluster
     */
	double mu0[][];
	
	/**
     * the initial mixing proportions of all clusters
     */
	double w0[];
	
	/**
     * the initial posterior probabilities of all samples belonging to all clusters 
	 * at the current level; each column is for one cluster
     */
	double Zjk0[][];
	
	
	/**
     * Create a CVMCore instance
     * @param n		the number of samples
     * @param p		the number of genes									
     */
  	public CVMCore(int n, int p)
    {
  		numSamples = n;
  		numFeatures = p;
  		level = 1;
    }
  
  
  	/**
  	 * Model selection at Top level, including cluster initialization and MDL validation;
  	 * MDL will be developed in the next version.
  	 * @param plotPanel		the 2D projection panel, on which the cluster center pointers  
  	 * 						will be clicked
  	 * @param D				the matrix data
  	 * @param W				the top 2 components for projection
  	 */
  	public void veMselect(Plot2DViewer plotPanel, double[][] D, double[][] W)
    {
  		int K;
  		TopProj2DClickDialog centerCluster;
  		double X[][];  // x space data - 2D
  		double vv = 0.5;
  		double MU2[][];
  		//numUpClusters = 1;
        
        // Let user choose the center of clusters
        centerCluster = new TopProj2DClickDialog(new JFrame(), true, plotPanel, 1);
        
        numSubClusters = centerCluster.cluster2DCenters.length;
        K = numSubClusters;
        
        mu0 = new double[numSubClusters][numFeatures];
  		w0 = new double[numSubClusters];
  		Zjk0 = new double[numSamples][numSubClusters];
  		
        // Top level EM in x-space
        System.out.print("-----------------------------------------\n");
        System.out.print("| Top level EM in x-space(2D) starts ...\n"); 
        
        CFMCore cfmcore2D = new CFMCore(numSamples, 2, K);
        for (int i = 0; i < K; i++){
        	w0[i] = (double)1 / (double)K;
        }
        
        MU2 = new double[K][2];
        MU2 = centerCluster.cluster2DCenters;
        /*MU2[0][0] = -1.5619047619047617;
        MU2[0][1] = -0.8000000000000003;
        MU2[1][0] = 1.6380952380952385;
        MU2[1][1] = 0.5999999999999996;*/
        
        //System.out.println("Input mean0_x matrix:");
    	//Common.printMatrix(K, 2, MU2);
    	//System.out.println("Input w0 vector:");
  	  	//Common.printVector(K, w0);
  	  	//System.out.println("Input vv: " + vv);
  	  
  	  	X = new double[numSamples][2];
  	  	X = times(D, W);
  	  	//System.out.println("D:");
  	  	//Common.printMatrix(numSamples, numFeatures, D);
  	  	//System.out.println("X:");
  	  	//Common.printMatrix(numSamples, 2, X);
  	  	cfmcore2D.Data = X;
    	cfmcore2D.Mean0_x = MU2;
    	cfmcore2D.W0 = w0;
    	cfmcore2D.vv = vv;
    	cfmcore2D.errorThresh = 0.01;
        // EM
        cfmcore2D.veSubNew();
        
        w0 = copy(cfmcore2D.W0_t);
        Zjk0 = copy(cfmcore2D.Zjk);
        // transfer the cluster center to t-space
        mu0 = transpose(times(W, transpose(MU2)));
        
        //numUpClusters = numSubClusters;
    }
  
  	
  	/**
  	 * Model selection at Sub level, including cluster initialization and MDL validation
  	 * MDL will be developed in the next version.
  	 * @param plotPanels	the 2D projection panels for all sub-clusters; on each of them,
  	 * 						the cluster center pointers will be selected
  	 * @param D				the matrix data
  	 * @param WW			an array of the top 2 components; each sub-cluster has its own
  	 * 						top 2 components
  	 * @param w_up			the mixing proportions at the upper level
  	 * @param MU_up			the mean of clusters at the upper level
  	 * @param Zjk_up		the posterior probabilities of all samples belonging to all clusters 
  	 * 						at the upper level; each column is for one cluster
  	 */
  	public void veMselect2(Plot2DViewer plotPanels[], double[][] D, double[][][] WW,
  			               double w_up[], double MU_up[][], double[][] Zjk_up)
  	{
  		int numUpClusters = w_up.length;
  		int subPointNum[] = new int[numUpClusters];
  		SubProj2DClickDialog centerSubCluster;
  		double X[][];  // x space data - 2D
  		double vv = 0.5;
  		double MU2[][]; 	
  		int curSubClusters = 0;
  		//double w0_up[];
  		//double MU_up[][];
  		
        // choose 1 center pointer for each sub-cluster
  		for (int i=0; i<numUpClusters; i++) {
  			subPointNum[i] = 1;
  		}
  		
        // Let user choose the center of each cluster in each window
        centerSubCluster = new SubProj2DClickDialog(new JFrame(), level, true,
        		                                    plotPanels, subPointNum);
        for (int i=0; i<numUpClusters; i++) {
        	subPointNum[i] = centerSubCluster.numPoints[i];
  		}
        numSubClusters = centerSubCluster.cluster2DCenters.length;
        
        //MU_up = copy(mu0);
        mu0 = new double[numSubClusters][numFeatures];
        //w0_up = copy(w0);
  		w0 = new double[numSubClusters];
  		Zjk0 = new double[numSamples][numSubClusters];
        
        for (int i=0; i<numUpClusters; i++) {
        	// Sub level EM in x-space
        	System.out.print("-----------------------------------------\n");
        	System.out.print("| Sublevel_"+level+"_SubCluster_"+i+" EM in x-space(2D) starts ...\n");
            
            int K = subPointNum[i];
            
            CFMCore cfmcore2DSub = new CFMCore(numSamples, 2, K);
            double w0_Init[] = new double[K];
            for (int j = 0; j < K; j++) {
            	w0_Init[j] = (double)1 / (double)K;
            }
            
            MU2 = new double[K][2];
            
            for (int j = 0; j < K; j++) {
            	MU2[j][0] = centerSubCluster.cluster2DCenters[curSubClusters+j][0];
            	MU2[j][1] = centerSubCluster.cluster2DCenters[curSubClusters+j][1];
            }
            /*if (i==0) {
            	MU2[0][0] = 0.4490;
            	MU2[0][1] = -0.0207;
            } else if (i==1){  
            	MU2[0][0] = -2.1853;
            	MU2[0][1] = -0.5115;
            	MU2[1][0] = 4.1689;
            	MU2[1][1] = -0.4244;
            } else if (i==2){
            	MU2[0][0] = -0.1005;
            	MU2[0][1] = -0.0521;
            }*/
            //System.out.println("Input mean0_x matrix:");
        	//Common.printMatrix(K, 2, MU2);
        	//System.out.println("Input w0 vector:");
      	  	//Common.printVector(K, w0_Init);
      	  	//System.out.println("Input vv: " + vv);
      	  
      	  	X = new double[numSamples][2];
      	  	double D_cen[][] = copy(D);
      	    Common.centerData(numSamples, numFeatures, D_cen, MU_up[i]);
      	  	X = times(D_cen, WW[i]);
      	  	cfmcore2DSub.Data = X;
        	cfmcore2DSub.Mean0_x = MU2;
        	cfmcore2DSub.W0 = w0_Init;
        	cfmcore2DSub.vv = vv;
        	cfmcore2DSub.errorThresh = 0.01;
        	cfmcore2DSub.Zjk_up = getColumnCopy(Zjk_up, i);
            // x-space EM
            cfmcore2DSub.veSubNew();
            
            double mu0_subi[][] = transpose(times(WW[i], transpose(MU2)));
            double eye_t[][];
            eye_t = one(K, 1, 1.0);
            double MU_i[][] = new double[1][numFeatures];
            MU_i[0] = MU_up[i];
            double MU_add[][] = times(eye_t, MU_i);
            
            for (int j = 0; j < K; j++) {
            	w0[curSubClusters+j] = cfmcore2DSub.W0_t[j] * w_up[i];
            	
            	for (int t = 0; t < numFeatures ; t++) {
            		mu0[curSubClusters+j][t] = mu0_subi[j][t] + MU_add[j][t];
            	}
            	
            	for (int u = 0; u < numSamples ; u++) {
            		Zjk0[u][curSubClusters+j] = cfmcore2DSub.Zjk[u][j];
            	}
            }
            curSubClusters += K;
        }	
        
        //numUpClusters = numSubClusters;
  	}
  	
  	/**
     * This method prints the initial parameters of selected model
     */
	public void print()
	{
		//		 Print the result
    	System.out.print("------------------- Current Level " + level + " ----------------\n");
    	
    	System.out.println("mu0:");
        Common.printMatrix(numSubClusters, numFeatures, mu0); 
        
        System.out.println("w0:");
        Common.printVector(numSubClusters, w0);
        
        System.out.println("Zjk:");
        Common.printMatrix(numSamples, numSubClusters, Zjk0);
    	
	}
	
}
