package edu.vt.cbil.visda.comp;

import edu.vt.cbil.visda.util.*;
import static org.math.array.LinearAlgebra.*;
import java.util.ArrayList;

/**
 * Created with Eclipse
 * Date: July 15, 2005
 */

/**
 * The class performs a feature pre-selection to reduce the dimensionality
 * @author Jiajing Wang
 * @version visda_v0.1
 */
public class FeaturePreSelector
{

	/**
     * the number of selected features
     */
	int numSelectedFeatures;
	
	/**
     * the number of original gene features
     */
	int numGenes;
	
	/**
     * the number of data samples
     */
	int numSamples;
	
	/**
     * the type of clustering
     * '0' - phenotype clustering; '1' - gene clustering
     */
	int clustingIndicator;
	
	/**
     * the original data matrix
     */
	double Data[][];
	
	/**
     * the labels for samples, each indicates that which class this sample belongs to
     */
	int sampleLabels[];
	
	/**
     * the selected data matrix after feature selection
     */
	public double selectedData[][];
	
	/**
     * the sorted SNR values with descending order 
     */
	double sortedPerformanceIndex[];
	
	/**
     * the selected feature index 
     */
	int selectedFeatureIndex[];
	                      
	/**
     * the means of selected genes with its average over each class 
     */
	double featureMean[][];
	
	/**
     * the variance of selected genes with its average over each class 
     */
	double featureVar[][];
	
	// Declaration of the Native (C) function
	/**
	 * This Native (C++) function calculates the signal-to-noise ratio (SNR) performance
     * for each dimension individually, and sorted the SNR values by descending order.
     * @param m 		the number of rows of matrix data
     * @param n			the number of columns of matrix data
     * @param data		the matrix data is stored in one-dimension array 
     * 					with row major order  
     * @param label 	the label for each sample indicates that which class this sample belongs to
     * @param dim_num	the number of selected features
     * @param top_label	the indexs of selected features in original order
     * @param snr_sorted	the sorted SNR values in descending order
     * @return '1'- successfully exit;
     * 		   '0'- exit with waring/error			
     */	
	public native int veSNR(int m, int n, double[] data, int[] label, int dim_num,
  							int[] top_label, double[] snr_sorted, 
  							int class_num, double[] mean, double[] var);

  	static
    {
      // The runtime system executes a class's static
      // initializer when it loads the class.
      System.loadLibrary("VISDA_CJavaInterface");
    }	
  
  	
  	/**
     * Create a FeaturePreSelector instance
     * @param clusterType	the type of clustering: '0' - phenotype clustering;
     * 												'1' - gene clustering
     * @param data			the matrix data is stored in two-dimension array
     * @param genesNum		the number of genes
     * @param samplesNum	the number of samples
     * @param featuresNum	the number of features
     * @param labels		the label for each sample indicates that which class this sample belongs to 										
     */
	public FeaturePreSelector(int clusterType, double[][] data, int genesNum, int samplesNum, 
			int featuresNum, int[] labels) {
		super();
		// TODO Auto-generated constructor stub
		clustingIndicator = clusterType;
		Data = data;
		numGenes = genesNum;
		numSamples = samplesNum;
		numSelectedFeatures = featuresNum;
		sampleLabels = labels;
		
		if (clusterType == 1) {
			sortedPerformanceIndex = new double[numSamples];
			selectedData = new double[numGenes][numSelectedFeatures];	
		} else if (clusterType == 0) {
			sortedPerformanceIndex = new double[numGenes];
			selectedData = new double[numSamples][numSelectedFeatures];
			
		}
	}

	/**
     * This method performs supervised feature selection by using signal-to-noise 
     * ratio (SNR), and the features with larger SNR values will be selected. 
     */
	public void snrSupervised()
  	{	
  		int retCode = 1;
  		double[][] Data_t = new double[numSamples][numGenes];
  		double[] data;
  		int numClass = 0;
  		
  		selectedFeatureIndex = new int[numSelectedFeatures];
  		
  		if (clustingIndicator == 1) {// gene clustering
  			data = Common.array2Dto1D(numGenes, numSamples, Data);
  		} else {// phenotype clustering
  			Data_t = transpose(Data);
  			data = Common.array2Dto1D(numSamples, numGenes, Data_t);
  		}
  			
  		for (int i=0; i<numSamples; i++) {
			if (numClass < sampleLabels[i]) {
				numClass = sampleLabels[i];
			}
		}
  		
  		double[] mean = new double[numClass*numGenes];
  		double[] var = new double[numClass*numGenes];
  		featureMean = new double[numClass][numGenes];
  		featureVar = new double[numClass][numGenes];
  		
  		retCode = veSNR(numSamples, numGenes, data, sampleLabels, numSelectedFeatures, 
  					    selectedFeatureIndex, sortedPerformanceIndex,
  					    numClass, mean, var);
  		featureMean = Common.array1Dto2D(numClass, numGenes, mean);
  		featureVar = Common.array1Dto2D(numClass, numGenes, var);
  		
  		if (retCode == 0) {
  			System.out.print(" Warning: snrSupervised() failed \n");
  		}
  		
  		if (clustingIndicator == 1) { // gene clustering
  			for (int i = 0; i <numGenes; i++) {
  				for (int j = 0; j <numSelectedFeatures; j++) {
  					selectedData[i][j] = Data[i][selectedFeatureIndex[j]];
  				}
  			}
  		} else { // phenotype clustering 
  			for (int i = 0; i <numSamples; i++) {
  				for (int j = 0; j <numSelectedFeatures; j++) {
  					selectedData[i][j] = Data_t[i][selectedFeatureIndex[j]];
  				}
  			}
  		}
  		
  	}
  	
	/**
     * This method prints the selected features and labels
     */
  	public void print()
  	{
 
  		// 	 Print the result
  		System.out.print("------------------- Feature Selector results ----------------\n");
  		System.out.print("sorted SNR :\n");
  		Common.printVector(numGenes, sortedPerformanceIndex);
  		
  		System.out.print("selected Data :\n");
  		if (clustingIndicator == 1) {  // gene clustering
  			Common.printMatrix(numGenes, numSelectedFeatures, selectedData);
  		} else { // phenotype clustering
  			Common.printMatrix(numSamples, numSelectedFeatures, selectedData);
  		}
  	
  	}
}
