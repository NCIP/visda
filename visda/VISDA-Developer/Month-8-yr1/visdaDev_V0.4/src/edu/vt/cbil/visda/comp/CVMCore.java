package edu.vt.cbil.visda.comp;

import edu.vt.cbil.visda.util.*;
import edu.vt.cbil.visda.view.*;
import javax.swing.*;
import static org.math.array.LinearAlgebra.*;
import java.util.*;


/**
 * The class performs clustering model selection and validation tasks for VISDA
 * @author Jiajing Wang
 * @version visda_v0.4
 * <p>
 * Version Log:
 * 	<br>visda_v0.1 (07/30/2005):	The file is created and basic function is implemented.
 * 	<br>visda_v0.4 (10/29/2005): 	Implement sub-cluster specific clustering; 
 * 								    Add MDL calculation, and implement the model selection process;
 * 
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
	
	/**
     * the number of cluster children for each up-level cluster
     */
	int numChildForUpClusters[];
	   
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
	 * the coordinate values of selected center pointers;
	 * row: total selected points;
	 * col: 2 colums for x and y coordinate;
	*/
	public double centers[][];
	   	
	/**
	 * whether to perform MDL
	 * True: to do MDL;
	 * False: not to do MDL;
	*/
	boolean toDoMDL;
	
	
	int rntClusterId;
	
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
     * Create a CVMCore instance
     * @param n		the number of samples
     * @param p		the number of genes		
     * @param doMDL		whether to do MDL					
     */
  	public CVMCore(int n, int p, boolean doMDL)
    {
  		numSamples = n;
  		numFeatures = p;
  		level = 1;
  		toDoMDL = doMDL;
    }
  	
  	
  	/**
  	 * Model selection at Top level, including cluster initialization and MDL validation
  	 * @param D				the matrix data
  	 * @param topLevelAttribute	the top level clustering attribute
  	 */
  	public void veMselect(double[][] D, PerLevelAttribute topLevelAttribute)
    {
  		Plot2DViewer plotPanel = topLevelAttribute.getAllClusterProjPlots()[0];
  		double[][] W = topLevelAttribute.getAllClusterProjComps()[0];
  		int K;
  		Proj2DClickDialog dialog0;
  		double X[][];  // x space data - 2D
  		double vv = 0.5;
  		double MU2[][];
  		//numUpClusters = 1;
        double[][] MU2_0;
        Plot2DViewer p;
        
        // Let user choose the center of clusters
        p = new Plot2DViewer(plotPanel);
        dialog0 = new Proj2DClickDialog(new JFrame(), true, 1, p);     
        MU2_0 = copy(dialog0.cluster2DCenters);
        //MU2_0 = new double[2][2];
        //MU2_0[0][0] = -1.5619047619047617;
        //MU2_0[0][1] = -0.8000000000000003;
        //MU2_0[1][0] = 1.6380952380952385;
        //MU2_0[1][1] = 0.5999999999999996;
      			
        int midK = dialog0.cluster2DCenters.length;
        int smallK = midK;
        int largeK = midK; 
        double[] mdls = new double[1];
        int[] ks = new int[1];
        int j=0;
        OneClusterAttributes[] attributes = new OneClusterAttributes[1];
        
        if (toDoMDL) {
        	smallK = midK - 1;
        	largeK = midK + 1;
        	if (smallK < 1) {
        		mdls = new double[2];
    			ks = new int[2];
    			attributes = new OneClusterAttributes[2];
    		} else {
    			mdls = new double[3];
    			ks = new int[3];
    			attributes = new OneClusterAttributes[3];
        	}
        }
        
        // Top level EM in x-space
        System.out.print("-----------------------------------------\n");
        System.out.print("| Top level EM in x-space(2D) starts ...\n"); 
        
        
        
        for (K=smallK; K<largeK+1; K++) {
        	if (K < 1) {
        		continue;
        	}
    	  
        	MU2 = new double[K][2];
        	if (K == 1) {
        		MU2[0] = plotPanel.getCenterDot();
        	} else if (K != midK) {
        		p = new Plot2DViewer(plotPanel);
        		Proj2DClickDialog dialog1 = new Proj2DClickDialog(new JFrame(), true, 1, p, K);  
        		MU2 = dialog1.cluster2DCenters;
        	} else {
        		MU2 = MU2_0;
        	}

        	double[] w0_tmp = new double[K];
  	   
        	CFMCore cfmcore2D = new CFMCore(numSamples, 2, K);
        	for (int i = 0; i < K; i++){
        		w0_tmp[i] = (double)1 / (double)K;
        	}
        
        	
  	  		X = new double[numSamples][2];
  	  		X = times(D, W);
  	  		cfmcore2D.Data = X;
  	  		cfmcore2D.Mean0_x = MU2;
  	  		cfmcore2D.W0 = w0_tmp;
  	  		cfmcore2D.vv = vv;
  	  		cfmcore2D.errorThresh = 0.01;
  	  		// initialize mdl to be positive so that the MDL calculation will be performed.
  	  		cfmcore2D.mdl = 1.0;   
  	  		// EM
  	  		cfmcore2D.veSubNew();
  	  		System.out.println("MDL = "+cfmcore2D.mdl);
  	  		mdls[j] = cfmcore2D.mdl;
  	  		ks[j] = K;	   		
  	  		attributes[j] = new OneClusterAttributes(0, K, 
  	  			cfmcore2D.W0_t, transpose(times(W, transpose(MU2))), cfmcore2D.Zjk, MU2);
  	  		j++;
        }
        
        int finalK;
        finalK = midK;
        if (toDoMDL) {
        	String name = "Cluster "+topLevelAttribute.gettAllClusterTreeloc()[0];
        	MDLDialog mdlDialog = new MDLDialog(new JFrame(), true, name, mdls, ks);
        	finalK = mdlDialog.selectedK;
        }
         
        numChildForUpClusters = new int[1];
        numChildForUpClusters[0] = finalK;
        numSubClusters = finalK;
        
        for (int i=0; i<ks.length; i++) {
        	if (finalK == attributes[i].numSubClusters) {
  	  			w0 = copy(attributes[i].w0);
  	  			Zjk0 = copy(attributes[i].zjk0);
  	  			mu0 = copy(attributes[i].mu0);
  	  			centers = copy(attributes[i].centers);	
        	}
        }
        
        //numUpClusters = numSubClusters;
    }
  	
  	
  	/**
  	 * Model selection at Sub level, including cluster initialization and MDL validation
  	 * @param D				the matrix data
  	 * @param upLevelAttribute  the upper level clustering attribute
  	 */
  	public int veMselect2(double[][] D, PerLevelAttribute upLevelAttribute)
  	{
  		double w_up[] = upLevelAttribute.w;
  		double MU_up[][] = upLevelAttribute.mu;
  		double Zjk_up[][] = upLevelAttribute.Zjk;
  		//boolean endFlag_up[] = upLevelAttribute.getAllClusterEndFlags();
  		double centers_up[][] = upLevelAttribute.getCluster2DCenters();
  		Plot2DViewer plotPanels[] = upLevelAttribute.getAllClusterProjPlots();
  		double[][][] WW = upLevelAttribute.getAllClusterProjComps();
  		String[] upClustersSymbol = upLevelAttribute.gettAllClusterTreeloc();
  		
  		int numUpClusters = w_up.length;
  		int subPointNum[] = new int[numUpClusters];
  		MultiProj2DClickDialog dialog0;
  		double X[][];  // x space data - 2D
  		double vv = 0.5;
  		double MU2[][]; 	
  		int curSubClusters = 0;
  		
        // By default, choose 1 center pointer for each sub-cluster
  		// If the sub-cluster has been purely partitioned (endFlag is true), it is not 
  		//    nessecary to go through the center selection for this cluster. 
  		for (int i=0; i<numUpClusters; i++) {
  			//if (endFlag_up[i]) {
  			//	subPointNum[i] = 0;
  			//} else {
  				subPointNum[i] = 1;
  			//}
  		}
  		
        // Let user choose the center of each cluster in each window
        //centerSubCluster = new SubProj2DClickDialog(new JFrame(), level, true,
        //		                                    plotPanels, subPointNum);
  		// Let user choose the center of the cluster that has more sub-clusters
  		dialog0 = new MultiProj2DClickDialog(new JFrame(), level, true,
  		        		                            plotPanels, subPointNum, centers_up);
        //int cnt = 0;
        for (int i=0; i<numUpClusters; i++) {
        	//if (endFlag_up[i] == false) {
        		subPointNum[i] = dialog0.numPoints[i];
        		//cnt++;
        	//} else {
        	//	subPointNum[i] = 1;
        	//}
  		}
        
        ArrayList<OneClusterAttributes> list = new ArrayList();
        
        for (int i=0; i<numUpClusters; i++) {
        	// Sub level EM in x-space
        	System.out.print("-----------------------------------------\n");
        	System.out.print("| Sublevel_"+level+"_SubCluster_"+i+" EM in x-space(2D) starts ...\n");
            
            int smallK = subPointNum[i];
            int largeK = subPointNum[i];
            double[] mdls = new double[1];
            int[] ks = new int[1];
            int mdlCnt=0;
            OneClusterAttributes[] attributes = new OneClusterAttributes[1];
            
            //if (toDoMDL && !endFlag_up[i]) {
            if (toDoMDL && (subPointNum[i] > 1)) {
            	smallK = subPointNum[i] - 1;
                largeK = subPointNum[i] + 1;
                if (smallK < 1) {
            		mdls = new double[2];
        			ks = new int[2];
        			attributes = new OneClusterAttributes[2];
        		} else {
        			mdls = new double[3];
        			ks = new int[3];
        			attributes = new OneClusterAttributes[3];
            	}
            } 
            
            double[][] MU2_0 = new double[subPointNum[i]][2];
            for (int j = 0; j < subPointNum[i]; j++) {
            	MU2_0[j][0] = dialog0.cluster2DCenters[curSubClusters+j][0];
            	MU2_0[j][1] = dialog0.cluster2DCenters[curSubClusters+j][1];
            }
            
            for (int K=smallK; K<largeK+1; K++) {
            	//if ((K < 1) || (endFlag_up[i] && (K!=subPointNum[i]))) {
            	if (K < 1) {
            		continue;
            	} 
        	  
            	MU2 = new double[K][2];
            	if (K == 1) {
            		MU2[0] = plotPanels[i].getCenterDot();
            	} else if (K != subPointNum[i]) {
            		Proj2DClickDialog dialog1 = new Proj2DClickDialog(new JFrame(), true, level, plotPanels[i], K);  
            		MU2 = dialog1.cluster2DCenters;         		
            	} else {
            		MU2 = MU2_0;
            	}
            	
            	CFMCore cfmcore2DSub = new CFMCore(numSamples, 2, K);
            	double w0_Init[] = new double[K];
            	for (int j = 0; j < K; j++) {
            		w0_Init[j] = (double)1 / (double)K;
            	}
          
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
      	  		// initialize mdl to be positive so that the MDL calculation will be performed.
      	  		cfmcore2DSub.mdl = 1.0;   
      	  		// x-space EM
      	  		cfmcore2DSub.veSubNew();
      	  		
      	  		if (cfmcore2DSub.isValid == false) {
      	  			rntClusterId = i;
      	  			return 0;
      	  		}
      	  		System.out.println("Sub MDL = "+cfmcore2DSub.mdl);
      	  		mdls[mdlCnt] = cfmcore2DSub.mdl;
      	  		ks[mdlCnt] = K;	   	
      	  		
      	  		double mu0_subi[][] = transpose(times(WW[i], transpose(MU2)));
      	  		double eye_t[][];
	  			eye_t = one(K, 1, 1.0);
	  			double MU_i[][] = new double[1][numFeatures];
	  			MU_i[0] = MU_up[i];
	  			double MU_add[][] = times(eye_t, MU_i);
    
	  			double[] w0_tmp = new double[K];
	  			double[][] mu_tmp = new double[K][numFeatures];
	  			for (int j = 0; j < K; j++) {
	  				w0_tmp[j] = cfmcore2DSub.W0_t[j] * w_up[i];
	  				for (int t = 0; t < numFeatures ; t++) {
	  					mu_tmp[j][t] = mu0_subi[j][t] + MU_add[j][t];
	  				}            	
	  			}
      	  		attributes[mdlCnt] = new OneClusterAttributes(i, K, 
							w0_tmp, mu_tmp, cfmcore2DSub.Zjk, MU2 );
      	  		mdlCnt++;
            }
        
            int finalK;
            finalK = subPointNum[i];
            //if (toDoMDL && !endFlag_up[i]) {
            if (toDoMDL && (subPointNum[i] > 1)) {
            	String name = "Cluster "+upClustersSymbol[i];
            	MDLDialog mdlDialog = new MDLDialog(new JFrame(), true, name, mdls, ks);
            	finalK = mdlDialog.selectedK;
            }
         
            for (int j=0; j<ks.length; j++) {
            	if (finalK == attributes[j].numSubClusters) {
            		list.add(attributes[j]);
            	}     	  		
            }
            
      	  	curSubClusters += subPointNum[i];
      	  	
        }
        
        if (list.size() != numUpClusters) {
        	JOptionPane.showMessageDialog(null, 
         		   "There is an error during the sub-level model selection", 
         		   "Error", 
         		   JOptionPane.ERROR_MESSAGE);
        }
        numSubClusters = 0;
        for (int i=0; i<numUpClusters; i++) {
        	numSubClusters += list.get(i).numSubClusters;
        	
        }
        mu0 = new double[numSubClusters][numFeatures];
        w0 = new double[numSubClusters];
        Zjk0 = new double[numSamples][numSubClusters];
        centers = new double[numSubClusters][2];
        numChildForUpClusters = new int[numUpClusters];
        
        int s = 0;
        for (int i=0; i<numUpClusters; i++) {
        	OneClusterAttributes a = list.get(i);
        	for (int j=0; j<a.numSubClusters; j++) {
        		w0[s+j] = a.w0[j];
        		for (int t = 0; t < numFeatures ; t++) {
        			mu0[s+j][t] = a.mu0[j][t];
        		}
        		for (int t = 0; t < numSamples ; t++) {
        			Zjk0[t][s+j] = a.zjk0[t][j];
        		}
        		for (int t = 0; t < 2 ; t++) {
        			centers[s+j][t] = a.centers[j][t]; 			
        		}
        	}
        	s += a.numSubClusters;
        	numChildForUpClusters[i] = a.numSubClusters;
        }

        return 1;
  	}
  	
  	
  	/**
     * The internal class for keeping the various attribute values of certain cluster
     */
  	public class OneClusterAttributes {
  		
  		int clusterId;
  		int numSubClusters;
  		double[] w0;
  		double[][] mu0;
  		double[][] zjk0;
  		double[][] centers;
  		
  		public OneClusterAttributes(int index, int k, double[] w, double[][] mu, double[][] zjk, double[][] c) {
  			clusterId = index;
  			numSubClusters = k;
  			w0 = copy(w);
  			mu0 = copy(mu);
  			zjk0 = copy(zjk);
  			centers = copy(c);
  		}
  		
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
