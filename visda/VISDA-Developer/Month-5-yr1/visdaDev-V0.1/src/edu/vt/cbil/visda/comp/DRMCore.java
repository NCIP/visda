package edu.vt.cbil.visda.comp;

import edu.vt.cbil.visda.util.*;
import edu.vt.cbil.visda.view.*;

import org.math.plot.*;
import java.awt.*;
import javax.swing.*;


/**
 * Created with Eclipse
 * Author: Jiajing Wang
 * Date: July 14, 2005
 *
 */

/**
 * the class to perform a portfolio of discriminatory data projections
 * for exploratory cluster visualization
 *
 */
public class DRMCore
{

	// input interface
	int numFeatures;
	int numSamples;
	int levelId;
	int clusterId;
	double data[][];
	double covCluster[][];
	double meanCluster[];
	double Zjk[];
	
	// output interface
	double top2Components[][];
	Plot2DPanel plotPanel;
	
	//double dataPCA[][];
	//double pcVectors[][];
	double top2PCVectors[][];
	double top2PCPPMVectors[][];
	//double pcValues[];
	double pcValRe[];
    double pcValIm[];
    double pcVectorRe[];
    double pcVectorIm[];
    double pcDataRe[];
    double pcDataIm[];
    double pcPPMVectorRe[];
    double pcPPMVectorIm[];
    
  // Declaration of the Native (C) function
  private native int vePCA(int n, int p, double[] data, 
  		double[] pcavec_re, double[] pcavec_im,
  		double[] pcaval_re, double[] pcaval_im, 
		double[] pcadata_re, double[] pcadata_im);
  
  private native int veSubPCA(int n, int p, double[] data, double[] covData, 
  		double[] pcavec_re, double[] pcavec_im,
  		double[] pcaval_re, double[] pcaval_im, 
		double[] pcadata_re, double[] pcadata_im);
  
  private native int vePCAPPM(int n, int p, double[] pcadata, 
  		double[] pcavec, double[] pcappmvec);
  
  private native int veSubPCAPPM(int n, int p, 
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
  
  
  	public void veProjection(int vl1, int vl2, int vl3, double[] R, 
    		   int[] labels, double thre_Zjk, 
       		   int blob_size, int flag_tc, float[] p_color)
    {
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
      
        // Plot the projections
        // ....
        String projTypes[] = {"PCA", "PCAPPM"};
        Plot2DPanel ProjPanels[] = new Plot2DPanel[2]; 
        
        ProjPanels[0] = new Plot2DPanel();
        VeVisT PCAPlot = new VeVisT(vl1, vl2, vl3, numSamples, numFeatures, 2, ProjPanels[0]);
        PCAPlot.vePlot(data, top2PCVectors, meanCluster, R, 
      		  		  labels, Zjk, thre_Zjk, blob_size, flag_tc, p_color);
    
        ProjPanels[1] = new Plot2DPanel();
        VeVisT PCAPPMPlot = new VeVisT(vl1, vl2, vl3, numSamples, numFeatures, 2, ProjPanels[1]);
        PCAPPMPlot.vePlot(data, top2PCPPMVectors, meanCluster, R, 
      		  		  labels, Zjk, thre_Zjk, blob_size, flag_tc, p_color);
        
        String projFrameTitle = "Level_"+levelId+"_Cluster_"+clusterId+" Projections";
        Proj2DPlotFrame projF = new Proj2DPlotFrame(projFrameTitle, projTypes, ProjPanels);
        
        // Let user choose one of the projections and the center of each cluster
        // ....
        Dimension d = projF.getSize();
        ProjSelectionDialog selDialog = new ProjSelectionDialog(new JFrame(), true, 0, d.height);
        //if(selDialog.showModal() == JOptionPane.CANCEL_OPTION)
        //	System.out.print("cancel to choose a projection\n");
        
        projF.dispose();
        if (selDialog.isPCA == true) {
        	top2Components = top2PCVectors;
        	plotPanel = ProjPanels[0];
        } else {
        	top2Components = top2PCPPMVectors;
        	plotPanel = ProjPanels[1];
        }
        
    }
  
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
    	
        System.out.print("PCA value real:\n");
        Common.printVector(numFeatures, pcValRe);
        
        System.out.print("PCA value imag:\n");
        Common.printVector(numFeatures, pcValIm);
        
        System.out.print("PCA data real:\n");
        Common.printMatrix(numSamples, numFeatures, pcDataRe);
        
        //System.out.print("PCA data imag:\n");
        //Common.printMatrix(numSamples, numFeatures, pcDataIm);
        
        System.out.print("PCA vector real:\n");
        Common.printMatrix(numFeatures, numFeatures, pcVectorRe);
        
        //System.out.print("PCA vector imag:\n");
        //Common.printMatrix(numFeatures, numFeatures, pcVectorIm);
        
        System.out.print("PCAPPM vector real:\n");
        Common.printMatrix(numFeatures, numFeatures, pcPPMVectorRe);
        
        //System.out.print("PCAPPM vector imag:\n");
        //Common.printMatrix(numFeatures, numFeatures, pcPPMVectorIm);
        
        System.out.print("Top 2 PCA vector :\n");
        Common.printMatrix(numFeatures, 2, top2PCVectors);
        
        System.out.print("Top 2 PCAPPM vector :\n");
        Common.printMatrix(numFeatures, 2, top2PCPPMVectors);
	}
	
}
