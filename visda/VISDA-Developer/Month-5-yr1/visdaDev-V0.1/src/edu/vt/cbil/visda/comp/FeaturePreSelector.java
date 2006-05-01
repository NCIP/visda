package edu.vt.cbil.visda.comp;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.vt.cbil.visda.util.*;
import static org.math.array.LinearAlgebra.*;

/**
 * Created with Eclipse
 * Author: Jiajing Wang
 * Date: July 15, 2005
 *
 */

/**
 * the class to perform a feature pre-selection to reduce the dimensionality
 *
 */
public class FeaturePreSelector
{

	// input interface
	int numSelectedFeatures;
	int clustingIndicator;
	double Data[][];
	public double selectedData[][];
	int selectedFeatureIndex[];
	int numGenes;
	int numSamples;
	String geneNames[];
	String geneIDs[];
	String geneAccessNums[];
	String sampleNames[];
	int sampleLabels[];
	double sortedPerformanceIndex[];
	//JFileChoose choose;
	
  // Declaration of the Native (C) function
  private native int veSNR(int m, int n, double[] data, int[] label, int dim_num,
  							int[] top_label, double[] snr_sorted);

  	static
    {
      // The runtime system executes a class's static
      // initializer when it loads the class.
      System.loadLibrary("VISDA_CJavaInterface");
    }	
  
  	

	public FeaturePreSelector(int indicator, double[][] data, int genes, int samples, int features, int[] labels) {
		super();
		// TODO Auto-generated constructor stub
		clustingIndicator = indicator;
		Data = data;
		numGenes = genes;
		numSamples = samples;
		numSelectedFeatures = features;
		sampleLabels = labels;
		selectedFeatureIndex = new int[numSelectedFeatures];
		if (indicator == 0) {
			sortedPerformanceIndex = new double[numSamples];
			selectedData = new double[numGenes][numSelectedFeatures];
		} else {
			sortedPerformanceIndex = new double[numGenes];
			selectedData = new double[numSamples][numSelectedFeatures];
		}
	}

	public void snrSupervised()
  	{
  		int retCode = 1;
  		double[][] Data_t = new double[numSamples][numGenes];;
  		double[] data;
  		
  		if (clustingIndicator == 0) {// phenotype clustering
  			data = Common.array2Dto1D(numGenes, numSamples, Data);
  		} else {
  			Data_t = transpose(Data);
  			data = Common.array2Dto1D(numSamples, numGenes, Data_t);
  		}
  			
  		retCode = veSNR(numSamples, numGenes, data, sampleLabels, numSelectedFeatures, 
  					    selectedFeatureIndex, sortedPerformanceIndex);
  		
  		if (retCode == 0) {
  			System.out.print(" Warning: snrSupervised() failed \n");
  		}
  		
  		if (clustingIndicator == 0) {
  			for (int i = 0; i <numGenes; i++) {
  				for (int j = 0; j <numSelectedFeatures; j++) {
  					selectedData[i][j] = Data[i][selectedFeatureIndex[j]];
  				}
  			}
  		} else {
  			for (int i = 0; i <numSamples; i++) {
  				for (int j = 0; j <numSelectedFeatures; j++) {
  					selectedData[i][j] = Data_t[i][selectedFeatureIndex[j]];
  				}
  			}
  		}
  		
  	}
  	
  	public void print()
  	{
 
  		// 	 Print the result
  		System.out.print("------------------- Feature Selector results ----------------\n");      
  		System.out.print("top label :\n");
  		for (int j = 0; j <numSelectedFeatures; j++)
  			System.out.print("  " + selectedFeatureIndex[j]);
  	    System.out.println();
  	    System.out.println();
  			
  		System.out.print("sorted SNR :\n");
  		Common.printVector(numGenes, sortedPerformanceIndex);
  		
  		System.out.print("selected Data :\n");
  		if (clustingIndicator == 0) {
  			Common.printMatrix(numGenes, numSelectedFeatures, selectedData);
  		} else {
  			Common.printMatrix(numSamples, numSelectedFeatures, selectedData);
  		}
  	
  	}
}
