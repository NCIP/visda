package edu.vt.cbil.visda.comp;

import edu.vt.cbil.visda.util.*;
import static org.math.array.DoubleArray.*;

/**
 * Created with Eclipse
 * Author: Jiajing Wang
 * Date: July 14, 2005
 *
 */

/**
 * the class to perform clustering model formulation (parameter estimation
 * using EM) tasks
 *
 */
public class CFMCore
{

	// input interface
	int numFeatures;
	int numSamples;
	int numClusters;
	double Data[][];
	double Mean0_x[][];
	double W0[];
	double vv;
	double errorThresh; 
	double uc[];  // ?????
	double Zjk_up[];
	
	// output interface
	double Mean1_x[][];
	double W0_t[];
	double Zjk[][];
	double Zjk_x[][];  // ???
	double Cov[][];
	double Inv_cov[][];
	double Cov_mat[][];  // [p][p*k]
	double Det_cov;
    
  // Declaration of the Native (C) function
  private native int veCov(int n, int p, double[] cov,
  		double[] inv_cov, double[] cov_mat, double det_cov);
  
  private native int veModel(int n, int p, int k, double[] D, 
  		double[] mean0_x, double[] Var0, double[] w0,
		double[] Gxn, double[] Fx);
  
  private native int veSubEM(int n, int p, int k, double[] D, 
  		double[] mean0_x, double[] w0, double vv, double error, double[] Zjk_up,     
		double[] mean1_x, double[] w0_t, double[] cov_mat, double[] Zjk);
  
  
  	static
    {
      // The runtime system executes a class's static
      // initializer when it loads the class.
      System.loadLibrary("VISDA_CJavaInterface");
    }

  	public CFMCore(int n, int p, int k)
    {
  		numSamples = n;
  		numFeatures = p;
  		numClusters = k;
  		Zjk_up = one(n, 1.0);
    }
  
  	public void veSubNew()
  	{
  		int retCode = 1;
  		double[] data;
  		double[] mean0_x;
  		double[] mean1_x;
  		double[] cov_mat;
  		double[] zjk;
  		
  		data = Common.array2Dto1D(numSamples, numFeatures, Data);
  		mean0_x = Common.array2Dto1D(numClusters, numFeatures, Mean0_x); 		
  		mean1_x = new double[numSamples*numFeatures];
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
