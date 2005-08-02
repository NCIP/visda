package edu.vt.cbil.visda.comp;

import org.math.plot.Plot2DPanel;

import edu.vt.cbil.visda.util.*;
import edu.vt.cbil.visda.view.*;
import javax.swing.*;
import static org.math.array.LinearAlgebra.*;

/**
 * Created with Eclipse
 * Author: Jiajing Wang
 * Date: July 26, 2005
 *
 */

/**
 * the class to perform clustering model selection and validation tasks for VISDA
 *
 */
public class CVMCore
{

	// input interface
	int numSamples;
	int numFeatures;
	int level;
	int numUpClusters;
	int numSubClusters;
	
	// output interface
	double MU_t[][];
	double w0[];
	double Zjk_t[][];
	//int vk0[];
	//double w2[];
	
  	public CVMCore(int n, int p)
    {
  		numSamples = n;
  		numFeatures = p;
  		level = 1;
    }
  
  
  	/**
  	 * Model selection at top level,
  	 * including cluster initialization and MDL validation
  	 */
  	// MDL will be developed in the next version
  	public void veMselect(Plot2DPanel plotPanel, double[][] D, double[][] W)
  	// W - top 2 components
    {
  		int K;
  		TopProj2DClickDialog centerCluster;
  		double X[][];  // x space data - 2D
  		double vv = 0.5;
  		double MU2[][];
  		
  		numUpClusters = 1;
        
        // Let user choose the center of clusters
        centerCluster = new TopProj2DClickDialog(new JFrame(), true, plotPanel, 1);
        numSubClusters = centerCluster.cluster2DCenters.length;
        K = numSubClusters;
        
        MU_t = new double[numSubClusters][numFeatures];
  		w0 = new double[numSubClusters];
  		Zjk_t = new double[numSamples][numSubClusters];
  		
        // Top level EM in x-space
        System.out.print("-----------------------------------------\n");
        System.out.print("| Top level EM in x-space(2D) starts ...\n"); 
        
        CFMCore cfmcore2D = new CFMCore(numSamples, 2, K);
        for (int i = 0; i < K; i++){
        	w0[i] = (double)1 / (double)K;
        }
        
        MU2 = new double[K][2];
        MU2 = centerCluster.cluster2DCenters;
        /*MU2[0][0] = -5.7812;
        MU2[0][1] = 0.3100;
        MU2[1][0] = 2.9753;
        MU2[1][1] = 1.6157;
        MU2[2][0] = 2.3224;
        MU2[2][1] = -7.7936;*/
        
        System.out.println("Input mean0_x matrix:");
    	Common.printMatrix(K, 2, MU2);
    	System.out.println("Input w0 vector:");
  	  	Common.printVector(K, w0);
  	  	System.out.println("Input vv: " + vv);
  	  
  	  	X = new double[numSamples][2];
  	  	X = times(D, W);
  	  	cfmcore2D.Data = X;
    	cfmcore2D.Mean0_x = MU2;
    	cfmcore2D.W0 = w0;
    	cfmcore2D.vv = vv;
    	cfmcore2D.errorThresh = 0.01;
        // EM
        cfmcore2D.veSubNew();
        
        w0 = copy(cfmcore2D.W0_t);
        Zjk_t = copy(cfmcore2D.Zjk);
        // transfer the cluster center to t-space
        MU_t = transpose(times(W, transpose(MU2)));
        
        numUpClusters = numSubClusters;
    }
  
  	
  	/**
  	 * Model selection at sub level,
  	 * including cluster initialization and MDL validation
  	 */
  	// MDL will be developed in the next version
  	public void veMselect2(Plot2DPanel plotPanels[], double[][] D, double[][][] WW,
  			               double[][] ZjkUp)
  	{
  		int subPointNum[] = new int[numUpClusters];
  		SubProj2DClickDialog centerSubCluster;
  		double X[][];  // x space data - 2D
  		double vv = 0.5;
  		double MU2[][]; 	
  		int curSubClusters = 0;
  		double w0_up[];
  		double MU_up[][];
  		
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
        
        MU_up = copy(MU_t);
        MU_t = new double[numSubClusters][numFeatures];
        w0_up = copy(w0);
  		w0 = new double[numSubClusters];
  		Zjk_t = new double[numSamples][numSubClusters];
        
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
            System.out.println("Input mean0_x matrix:");
        	Common.printMatrix(K, 2, MU2);
        	System.out.println("Input w0 vector:");
      	  	Common.printVector(K, w0_Init);
      	  	System.out.println("Input vv: " + vv);
      	  
      	  	X = new double[numSamples][2];
      	  	double D_cen[][] = copy(D);
      	    Common.centerData(numSamples, numFeatures, D_cen, MU_up[i]);
      	  	X = times(D_cen, WW[i]);
      	  	cfmcore2DSub.Data = X;
        	cfmcore2DSub.Mean0_x = MU2;
        	cfmcore2DSub.W0 = w0_Init;
        	cfmcore2DSub.vv = vv;
        	cfmcore2DSub.errorThresh = 0.01;
        	cfmcore2DSub.Zjk_up = getColumnCopy(ZjkUp, i);
            // x-space EM
            cfmcore2DSub.veSubNew();
            
            double MU_t_subi[][] = transpose(times(WW[i], transpose(MU2)));
            double eye_t[][];
            eye_t = one(K, 1, 1.0);
            double MU_i[][] = new double[1][numFeatures];
            MU_i[0] = MU_up[i];
            double MU_add[][] = times(eye_t, MU_i);
            
            for (int j = 0; j < K; j++) {
            	w0[curSubClusters+j] = cfmcore2DSub.W0_t[j] * w0_up[i];
            	
            	for (int t = 0; t < numFeatures ; t++) {
            		MU_t[curSubClusters+j][t] = MU_t_subi[j][t] + MU_add[j][t];
            	}
            	
            	for (int u = 0; u < numSamples ; u++) {
            		Zjk_t[u][curSubClusters+j] = cfmcore2DSub.Zjk[u][j];
            	}
            }
            curSubClusters += K;
        }	
        
        numUpClusters = numSubClusters;
  	}
  	
	public void print()
	{
		//		 Print the result
    	System.out.print("------------------- Current Level " + level + " ----------------\n");
    	
    	System.out.println("MU_t:");
        Common.printMatrix(numSubClusters, numFeatures, MU_t); 
        
        System.out.println("w0:");
        Common.printVector(numSubClusters, w0);
        
        System.out.println("Zjk:");
        Common.printMatrix(numSamples, numSubClusters, Zjk_t);
    	
	}
	
}
