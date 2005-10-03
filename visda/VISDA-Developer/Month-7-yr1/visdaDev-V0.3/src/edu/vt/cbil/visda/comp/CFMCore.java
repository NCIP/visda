package edu.vt.cbil.visda.comp;

import edu.vt.cbil.visda.util.*;
import static org.math.array.DoubleArray.*;

/**
 * Created with Eclipse
 * Date: July 14, 2005
 */

/**
 * The class performs clustering model formulation (parameter estimation
 * using EM) tasks
 * @author Jiajing Wang
 * @version visda_v0.1
 */
public class CFMCore
{

	// input interface
	/**
     * the number of features
     */
	int numFeatures;
	
	/**
     * the number of samples
     */
	int numSamples;
	
	/**
     * the number of clusters at the current level
     */
	int numClusters;
	
	/**
     * the data matrix
     */
	double Data[][];
	
	/**
     * the initial Means of clusters; each row is for one cluster
     */
	double Mean0_x[][];
	
	/**
     * the initial mixing proportions of all clusters
     */
	double W0[];
	
	/**
	 * the value for initializing the Covariance matrix of clusters
	 */
	double vv;
	
	/**
	 * the error threshold to stop the loop of EM algorithm
	 */
	double errorThresh; 
	
	//double uc[];  // ?????
	
	/**
	 * the posterior probabilities of samples belonging to the cluster at the upper level
	 */
	double Zjk_up[];
	
	// output interface
	/**
     * the Means of clusters in t-space;; each row is for one cluster
     */
	double Mean1_x[][];
	
	/**
	 * the mixing proportions of the identified clusters in t-space
	 */
	double W0_t[];
	
	/**
	 * the Covariance matrixes for all clusters in t-space;
	 * they are concatenated on columns, i.e., if there are k clusters,
	 * this will be a 2-dimension array with [p][p*k]
     */
	double Cov_mat[][];  
	
	/**
	 * the posterior probabilities of all samples belonging to all clusters 
	 * at the current level; each column is for one cluster
	 */
	double Zjk[][];
	
	//double Zjk_x[][];  // ???
	//double Cov[][];
	//double Inv_cov[][];	
	//double Det_cov;
    
	// return value of the native method
	private int retCode;
	
  // Declaration of the Native (C) function
	private native int veCov(int n, int p, double[] cov,
  		double[] inv_cov, double[] cov_mat, double det_cov);
  
	/**
	 * This Native (C++) function calculates the mixed Gaussian Model
	 * @param n 		the number of rows of matrix data
	 * @param p			the number of columns of matrix data 
	 * @param k			the number of clusters
	 * @param D      	the matrix data is stored in one-dimension array 
	 * 					with row major order
	 * @param mean0_x	the Means of clusters; each row is for one cluster;
	 * 					it is stored in one-dimension array with row major order
	 * @param Var0		a group of matrixes containing the Covariance matrix of all clusters;
     * 					the covariance matrixes are concatenated on columns
     * @param w0		the mixing proportions of all clusters
     * @param Gxn		the probability per sample per cluster
     * @param Fx		the probability per sample
     * @return '1'- successfully exit;
     * 		   '0'- exit with waring/error
     */
	public native int veModel(int n, int p, int k, double[] D, 
  		double[] mean0_x, double[] Var0, double[] w0,
		double[] Gxn, double[] Fx);
  
	/**
	 * This Native (C++) function performs the EM algorithm
	 * @param n 		the number of rows of matrix data
	 * @param p			the number of columns of matrix data 
	 * @param k			the number of clusters
	 * @param D      	the matrix data is stored in one-dimension array 
	 * 					with row major order
	 * @param mean0_x	the initial Means of clusters; each row is for one cluster;
	 * 					it is stored in one-dimension array with row major order
     * @param w0		the initial mixing proportions of all clusters
     * @param vv       	the value for initializing the Covariance matrix of clusters
     * @param error		the error threshold
     * @param Zjk_up	the posterior probabilities of samples belonging to the cluster at the upper level
     * @param mean1_x	the Means of clusters in t-space; each row is for one cluster;
	 * 					it is stored in one-dimension array with row major order
     * @param w0_t		the mixing proportions of the identified clusters in t-space
     * @param cov_mat	the Covariance matrixes of all clusters in t-space; they are concatenated on columns; 
     * 					the concatenated matrix is stored in one-dimension array with row major order
     * @param Zjk	    the posterior probabilities of all samples belonging to all clusters at the current level;
     * 					each column is for one cluster
     * @return '1'- successfully exit;
     * 		   '0'- exit with waring/error
	 */
	public native int veSubEM(int n, int p, int k, double[] D, 
  		double[] mean0_x, double[] w0, double vv, double error, double[] Zjk_up,     
		double[] mean1_x, double[] w0_t, double[] cov_mat, double[] Zjk);
  
  
  	static
    {
      // The runtime system executes a class's static
      // initializer when it loads the class.
      System.loadLibrary("VISDA_CJavaInterface");
    }

  	/**
     * Create a CFMCore instance
     * @param n		the number of samples
     * @param p		the number of genes
     * @param k 	the number of clusters at the current level										
     */
  	public CFMCore(int n, int p, int k)
    {
  		numSamples = n;
  		numFeatures = p;
  		numClusters = k;
  		Zjk_up = one(n, 1.0);
    }
  
  	/**
     * The method calls the EM native function and collects the results									
     */
  	public void veSubNew()
  	{
  		double[] data;
  		double[] mean0_x;
  		double[] mean1_x;
  		double[] cov_mat;
  		double[] zjk;
  		
  		data = Common.array2Dto1D(numSamples, numFeatures, Data);
  		mean0_x = Common.array2Dto1D(numClusters, numFeatures, Mean0_x); 		
  		mean1_x = new double[numClusters*numFeatures];
  		cov_mat = new double[numFeatures*numFeatures*numClusters];
  		zjk = new double[numSamples*numClusters];
  		W0_t = new double[numClusters];
  		
  		retCode = veSubEM(numSamples, numFeatures, numClusters, data, 
  				          mean0_x, W0, vv, errorThresh, Zjk_up,
		                  mean1_x, W0_t, cov_mat, zjk);
  		
  		Mean1_x = new double[numClusters][numFeatures];
  		Cov_mat = new double[numFeatures][numFeatures*numClusters];
  		Mean1_x = Common.array1Dto2D(numClusters, numFeatures, mean1_x);
  		Cov_mat = Common.array1Dto2D(numFeatures, numFeatures*numClusters, cov_mat);
  		Zjk = Common.array1Dto2D(numSamples, numClusters, zjk);
  
  	}
  	
  	
  	/**
     * This method prints the result of the EM algorithm
     */
	public void print()
	{
  		if (retCode == 1)
        {
  			// 	 Print the result
  			System.out.print("------------------- EM result ----------------\n");      
  			System.out.print("EM mean :\n");
  			Common.printMatrix(numClusters, numFeatures, Mean1_x);
        
  			System.out.print("EM w0 :\n");
  			Common.printVector(numClusters, W0_t);
        
  			//System.out.print("EM Cov :\n");
  			//Common.printMatrix(numFeatures, numFeatures*numClusters, Cov_mat);
        
  			//System.out.print("EM Zjk Up level :\n");
  			//Common.printVector(numSamples, Zjk_up);
  			
  			System.out.print("EM Zjk :\n");
  			Common.printMatrix(numSamples, numClusters, Zjk);
        }
  	
  	}
	
}
