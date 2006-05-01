package edu.vt.cbil.visda.comp;

import edu.vt.cbil.visda.util.*;
import edu.vt.cbil.visda.view.*;

import org.math.plot.*;

import java.awt.*;
import javax.swing.*;


/**
 * Created with Eclipse
 * Date: July 14, 2005
 */

/**
 * The class performs a portfolio of discriminatory data projections
 * for exploratory cluster visualization
 * @author Jiajing Wang
 * @version visda_v0.1
 */
public class DRMCore
{

	// input interface
	/**
     * the number of features
     */
	public int numFeatures;
	
	/**
     * the number of samples
     */
	public int numSamples;
	
	/**
     * the index of current level
     */
	int levelId;
	
	/**
     * the index of current sub-cluster at the current level
     */
	int clusterId;
	
	/**
     * the data matrix
     */
	public double data[][];
	
	/**
     * the covariance matrix estimated at the upper level
     */
	public double covCluster[][];
	
	/**
     * the means of sample data estimated at the upper level
     */
	public double meanCluster[];
	
	/**
     * the Posterior probabilities of all samples belong to current cluster, 
     * which are estimated at the upper level
     */
	public double Zjk[];
	
	// output interface
	/**
     * the top TWO components for projection
     */
	public double top2Components[][];
	
	/**
     * the 2D projection panel
     */
	//Plot2DPanel plotPanel;
	
	/**
     * the top TWO components from PCA; the 2 PCA components with largest eigenvalues are selected.
     */
	public double top2PCVectors[][];
	
	/**
     * the top TWO components from PCA-PPM; the 2 PCA components with smallest kurtosis values are selected.
     */
	public double top2PCPPMVectors[][];
	
    
	String projTypes[];
	                 
    // Declaration of the Native (C) function
    /**
	 * This Native (C++) function performs the Top-Level PCA(Principle Components Analysis)
	 * @param n 		the number of rows of matrix data
     * @param p			the number of columns of matrix data 
     * @param data      the matrix data is stored in one-dimension array 
     * 					with row major order
     * @param pcavec_re the real part of eigenvectors (in columns) is stored in one-dimension array 
     * 					with row major order
     * @param pcavec_im the imaginary part of eigenvectors (in columns) is stored in one-dimension array 
     * 					with row major order
     * @param pcaval_re the array containing the real part of eigenvalues
     * @param pcaval_im the array containing the imaginary part of eigenvalues                    
     * @param pcadata_re 	the real part of data projected onto the space defined by the PCA;
     * 						it is stored in one-dimension array with row major order
     * @param pcadata_im	the imaginary part of data projected onto the space defined by the PCA;
     * 						it is stored in one-dimension array with row major order
     * @return '1'- successfully exit;
     * 		   '0'- exit with waring/error
     */
    public native int vePCA(int n, int p, double[] data, 
  		double[] pcavec_re, double[] pcavec_im,
  		double[] pcaval_re, double[] pcaval_im, 
		double[] pcadata_re, double[] pcadata_im);
  
    /**
	 * This Native (C++) function performs the Sub-Level PCA(Principle Components Analysis);
	 * At the sub-level, PCA for each cluster is performed using the covariance matrix of 
	 * the cluster estimated at the upper level so that the PCA is probabilistic.
	 * @param n 		the number of rows of matrix data
     * @param p			the number of columns of matrix data 
     * @param data      the matrix data is stored in one-dimension array 
     * 					with row major order
     * @param covData	the covariance matrix data, which is stored in one-dimension array 
     * 					with row major order
     * @param pcavec_re the real part of eigenvectors (in columns) is stored in one-dimension array 
     * 					with row major order
     * @param pcavec_im the imaginary part of eigenvectors (in columns) is stored in one-dimension array 
     * 					with row major order
     * @param pcaval_re the array containing the real part of eigenvalues
     * @param pcaval_im the array containing the imaginary part of eigenvalues                    
     * @param pcadata_re 	the real part of data projected onto the space defined by the PCA;
     * 						it is stored in one-dimension array with row major order
     * @param pcadata_im	the imaginary part of data projected onto the space defined by the PCA;
     * 						it is stored in one-dimension array with row major order
     * @return '1'- successfully exit;
     * 		   '0'- exit with waring/error
     */
    public native int veSubPCA(int n, int p, double[] data, double[] covData, 
  		double[] pcavec_re, double[] pcavec_im,
  		double[] pcaval_re, double[] pcaval_im, 
		double[] pcadata_re, double[] pcadata_im);
  
    /**
	 * This Native (C++) function performs the Top-Level PCA-PPM;
	 * Rank principal components resulted from PCA based on the kurtosis values
	 * of the data projection on the principal components. The kurtosis values 
	 * are sorted by ascending order.
	 * @param n 		the number of rows of matrix data
     * @param p			the number of columns of matrix data 
     * @param pcadata   the data projected onto the space defined by the PCA, which is stored 
     * 					in one-dimension array with row major order
     * @param pcavec	the eigenvectors (in columns) from PCA analysis, which is stored in 
     * 					one-dimension array with row major order
     * @param pcappmvec the eigenvectors (in columns) from PCA-PPM analysis, which is stored in 
     * 					one-dimension array with row major order
     * @return '1'- successfully exit;
     * 		   '0'- exit with waring/error
     */
    public native int vePCAPPM(int n, int p, double[] pcadata, 
  		double[] pcavec, double[] pcappmvec);
  
    /**
	 * This Native (C++) function performs the Sub-Level PCA-PPM;
	 * At the sub-level, the covariance matrix of the cluster and the Posterior probabilities 
	 * of samples estimated at the upper level are used to calculate the current level kurtosis.
	 * @param n 		the number of rows of matrix data
     * @param p			the number of columns of matrix data 
     * @param pcadata_re 	the real part of data projected onto the space defined by the PCA;
     * 						it is stored in one-dimension array with row major order
     * @param pcadata_im	the imaginary part of data projected onto the space defined by the PCA;
     * 						it is stored in one-dimension array with row major order
     * @param pcavec_re 	the real part of eigenvectors (in columns) from PCA is stored in one-dimension array 
     * 						with row major order
     * @param pcavec_im 	the imaginary part of eigenvectors (in columns) from PCA is stored in one-dimension array 
     * 						with row major order
     * @param pcaval_re 	the array containing the real part of eigenvalues
     * @param pcaval_im 	the array containing the imaginary part of eigenvalues  
     * @param Zjk			the Posterior probabilities of all samples belonging to the cluster at the upper level                  
     * @param subpcappmvec_re	the real part of eigenvectors (in columns) after kurtosis ranking; it is stored in 
     * 							one-dimension array with row major order
     * @param subpcappmvec_im 	the imaginary part of eigenvectors (in columns) after kurtosis ranking; it is stored 
     * 							in one-dimension array with row major order	
     * @return '1'- successfully exit;
     * 		   '0'- exit with waring/error
     */
    public native int veSubPCAPPM(int n, int p, 
  		double[] pcadata_re, double[] pcadata_im,  // pcadata might be complex?
  		double[] pcavec_re, double[] pcavec_im,
		double[] pcaval_re, double[] pcaval_im, double[] Zjk,
		double[] subpcappmvec_re, double[] subpcappmvec_im);
  
  	static
    {
      // The runtime system executes a class's static
      // initializer when it loads the class.
  		System.loadLibrary("VISDA_CJavaInterface");
    }

  	/**
     * Create a DRMCore instance
     * @param n		the number of samples
     * @param p		the number of genes
     * @param l		the index of current level
     * @param k 	the index of sub-cluster at the current level										
     */
  	public DRMCore(int n, int p, int l, int k)
    {
  		numSamples = n;
  		numFeatures = p;
  		levelId = l;
  		clusterId = k;
  		top2PCVectors = new double[numFeatures][2];
  		top2PCPPMVectors = new double[numFeatures][2];
  		top2Components = new double[numFeatures][2];
    }
  
  	/**
     * This method performs dimension reduction with PCA/PCAPPM method. 
     * 
     */ 
  	public void veProjection()
    {
  		double pcValRe[];
  	    double pcValIm[];
  	    double pcVectorRe[];
  	    double pcVectorIm[];
  	    double pcDataRe[];
  	    double pcDataIm[];
  	    double pcPPMVectorRe[];
  	    double pcPPMVectorIm[];
  		double X[];
		double covX[];
		int retCode1 = 1;
		int retCode2 = 1;
		
		pcValRe = new double[numFeatures];
        pcValIm = new double[numFeatures];
        pcVectorRe = new double[numFeatures*numFeatures];
        pcVectorIm = new double[numFeatures*numFeatures];
        pcDataRe = new double[numSamples*numFeatures];
        pcDataIm = new double[numSamples*numFeatures];
        pcPPMVectorRe = new double[numFeatures*numFeatures];
        pcPPMVectorIm = new double[numFeatures*numFeatures];
        //top2Components = new double[numSamples][2];
        
        X = Common.array2Dto1D(numSamples, numFeatures, data);
        
        // Call method DRMCore of object drmcore
        if (levelId == 1) {
            retCode1 = vePCA(numSamples, numFeatures, X, pcVectorRe, pcVectorIm, 
            		        pcValRe, pcValIm, pcDataRe, pcDataIm);
            
            retCode2 = vePCAPPM(numSamples, numFeatures, pcDataRe, pcVectorRe, pcPPMVectorRe);
            
        } else if (levelId > 1) {
            covX = Common.array2Dto1D(numFeatures, numFeatures, covCluster);
            
        	retCode1 = veSubPCA(numSamples, numFeatures, X, covX, pcVectorRe, pcVectorIm, 
        			           pcValRe, pcValIm, pcDataRe, pcDataIm);
        	
        	retCode2 = veSubPCAPPM(numSamples, numFeatures, pcDataRe, pcDataIm, 
        					      pcVectorRe, pcVectorIm, pcValRe, pcValIm, Zjk,
							      pcPPMVectorRe, pcPPMVectorIm);
        }
        
        // compute top2 vectors
        if (retCode1 == 1) {
        	for (int i = 0; i < numFeatures; i++){
            	top2PCVectors[i] = new double[] {pcVectorRe[i*numFeatures], pcVectorRe[i*numFeatures+1]};
            }
        } else {
        	System.out.print("PCA is not applicable here, use the default projection directions.\n");
        }
        
        if (retCode2 == 1) {
        	for (int i = 0; i < numFeatures; i++){
            	top2PCPPMVectors[i] = new double[] {pcPPMVectorRe[i*numFeatures], pcPPMVectorRe[i*numFeatures+1]};
            }
        } else {
        	System.out.print("PCA-PPM is not applicable here, use PCA directions.\n");
        	for (int i = 0; i < numFeatures; i++){
        		for (int j = 0; i < 2; j++){
        			top2PCPPMVectors[i][j] = top2PCVectors[i][j]; 
        		}
        	}    	
        }
       
    }
  
  	/**
     * This method prints the top 2 components from PCA/PCA-PPM
     */
	public void print()
	{
		//		 Print the result
    	System.out.print("\n------------- Current Level "+levelId+" Cluster "+clusterId+"----------\n");
    	//		Print input data
    	System.out.println("Input matrix data:");
    	Common.printMatrix(numSamples, numFeatures, data); 	
    	//      Print the covariance matrix and zjk vector for sub-level compute
    	if (levelId > 1) {
    		System.out.println("Covariance matrix:");
        	Common.printMatrix(numFeatures, numFeatures, covCluster); 
        	
        	System.out.println("Zjk:");
        	Common.printVector(numSamples, Zjk);
    	}
        
        System.out.print("Top 2 PCA vector :\n");
        Common.printMatrix(numFeatures, 2, top2PCVectors);
        
        System.out.print("Top 2 PCAPPM vector :\n");
        Common.printMatrix(numFeatures, 2, top2PCPPMVectors);
	}
	
}
