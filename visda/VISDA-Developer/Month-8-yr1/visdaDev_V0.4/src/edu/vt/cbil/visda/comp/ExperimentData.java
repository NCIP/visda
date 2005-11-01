package edu.vt.cbil.visda.comp;

import static org.math.array.LinearAlgebra.*;
import static org.math.array.StatisticSample.*;

import java.util.ArrayList;

import edu.vt.cbil.visda.data.DefaultSample;
import edu.vt.cbil.visda.data.FloatDataSet;
import edu.vt.cbil.visda.util.Common;

/**
 * Created with Eclipse
 * Date: Aug 21, 2005
 */

/**
 * The class stores all the informations of experiment data
 * 
 * @author Jiajing Wang
 * @version visda_v0.4
 * 
 * Version Log:
 * 	visda_v0.2 (08/30/2005):	The file is created and basic function is implemented.
 *  visda_v0.3 (09/30/2005):
 * 	visda_v0.4 (10/29/2005):  	Change the member 'attributeOfSubLevels' and 'visualiztionOfSubLevels'
 * 								to be ArrayList type;
 * 								Add 'toDoMDL' member; 
 * 								Remove the member 'visualiztionOfSubLevels';
 * 			
 */
public class ExperimentData {
	
	/**
     * the number of the dataset
     */
	private String datasetName;
	
	/**
     * the number of all samples
     */
	public int numRawSamples;
	
	/**
     * the number of all features
     */
	public int numRawFeatures;
	
	/**
     * the number of samples
     */
	public int numSamples;
	
	/**
     * the number of features
     */
	public int numFeatures;
	
	/**
     * the name of all samples
     */
	public ArrayList rawSampleNames;
	
	/**
     * the name of all features
     */
	public ArrayList rawFeatureNames;
	
	/**
     * the name of analysised samples
     */
	public ArrayList sampleNames;
	
	/**
     * the name of analysised features
     */
	public ArrayList featureNames;
	
	/**
     * the attributes of data
     */
	public ArrayList<String> attributes;
	
	/**
     * the keys of sample labels
     */
	public ArrayList<String> labelKeys;
	
	/**
     * the raw data matrix
     */
	public double rawData[][];
	
	/**
     * the centered data matrix
     */
	public double data[][];
	
	/**
     * the labels for samples, each indicates that which class this sample belongs to
     */
	private int[] labels;
	
	/**
     * the sorted SNR values with descending order 
     */
	private double SNR[];
	
	/**
     * the means of selected genes with its average over each class 
     */
	private double orgMean[][];
	
	/**
     * the variance of selected genes with its average over each class  
     */
	private double orgVar[][];
	
	private int numLevel;
	
	/**
	 * whether to perform MDL
	 * True: to do MDL;
	 * False: not to do MDL;
	*/
	private boolean toDoMDL;
	
	int levelID;
	
	private ArrayList<PerLevelAttribute> attributeOfSubLevels;
	
	public ExperimentData(int samples, int features, double[][] data, int[] labels) {
		super();
		// TODO Auto-generated constructor stub
		this.data = data;
		this.labels = labels;
		numFeatures = features;
		numSamples = samples;
		toDoMDL = false;
	}
	
	public ExperimentData(int samples, int features, double[][] data,
			ArrayList samplesname, ArrayList featuresname, ArrayList attributes) {
		super();
		// TODO Auto-generated constructor stub
		this.rawData = copy(data);
		this.data = data;
		numFeatures = features;
		numSamples = samples;
		sampleNames = samplesname;
		featureNames = featuresname;
		this.attributes = attributes;
		toDoMDL = false;
	}
	
	public ExperimentData(FloatDataSet floatData) {
		super();
		// TODO Auto-generated constructor stub
		this.datasetName = floatData.getDatasetName();
		this.rawSampleNames = (ArrayList) floatData.getAllSamples();
		this.rawFeatureNames = (ArrayList) floatData.getAllGenes();
		this.attributes = (ArrayList) floatData.getAttributeNames();
		this.labelKeys = (ArrayList) floatData.getLabelKeyNames();
		
		numRawFeatures = rawFeatureNames.size();
		numRawSamples = rawSampleNames.size();
		rawData = new double[numRawFeatures][numRawSamples];
		
		for (int i = 0; i < numRawFeatures; i++) {
			for (int j = 0; j < numRawSamples; j++) {
				rawData[i][j] = (double) floatData.getDouble(i, j); 
			}
		}	
		
		toDoMDL = false;
	}
	
	public void setSelData(int clusterType, int numSelectedFeatures, double[][] selData,
						   int[] selectedFeatureIndex) {
		if (clusterType == 1) { // gene clustering
			numSamples = numSelectedFeatures;
			numFeatures = numRawFeatures;
			sampleNames = new ArrayList();
			for (int j = 0; j <numSelectedFeatures; j++) {
				sampleNames.add(rawSampleNames.get(selectedFeatureIndex[j]));
			}
			featureNames = rawFeatureNames;
		} else if (clusterType == 0) { // phenotype clustering
			numSamples = numRawSamples;
			numFeatures = numSelectedFeatures;	
			featureNames = new ArrayList();
			for (int j = 0; j <numSelectedFeatures; j++) {
  				featureNames.add(rawFeatureNames.get(selectedFeatureIndex[j]));
			}
			sampleNames = rawSampleNames;
		}
		this.data = copy(selData);
	}
	
	
	public void initAnalysisResult(int levelDeep) {
		//levelNum = levelDeep;
		levelID = 1;
		attributeOfSubLevels = new ArrayList();
	
		double mu[] = new double[numFeatures];
		mu = mean(data);
		
		//	  Center the data in the t-space
	    //    the means of D are zeros after centering
	    Common.centerData(numSamples, numFeatures, data, mu);
	    mu = mean(data);
	    //Common.printMatrix(numSamples, numFeatures, data);
	    
		double w0[] = {1.0};
		
		double mu0[][] = new double[1][numFeatures];
		//mu0[0] = copy(mu);
		//****** modified on Sep 6, 2005 *****//
		for (int i=0; i<numFeatures; i++) {
			mu0[0][i] = 1;
		}
		
		double Zjk0[][] = new double[numSamples][1];
		for (int i=0; i<numSamples; i++) {
			Zjk0[i][0] = 1.0;
		}
		
		double cov0[][] = new double[numFeatures][numFeatures];
		cov0 = copy(covariance(data));
		
		PerLevelAttribute fstLevelAttribute = new PerLevelAttribute(1, numSamples, numFeatures, 1);
		fstLevelAttribute.setClustersAttribute(w0, mu0, cov0, Zjk0);
		boolean endFlag_toplevel[] = new boolean[1];
		endFlag_toplevel[0] = false;
		fstLevelAttribute.setAllClusterEndFlags(endFlag_toplevel);
		fstLevelAttribute.getSubClusterAttribute(1).setClusterTreeLoc("1");
		attributeOfSubLevels.add(fstLevelAttribute);
		//attributeOfSubLevels[0] = new PerLevelAttribute(1, numSamples, numFeatures, 1);		
		//attributeOfSubLevels[0].setClustersAttribute(w0, mu0, cov0, Zjk0);
		//attributeOfSubLevels[0].setAllClusterEndFlags(endFlag_toplevel);	

		
	}
	
	public void setSubLevelAttribute(int levelIndex, 
			double w[], double mean[][], double cov[][], double zjk[][]) {
		
		//attributeOfSubLevels[levelIndex-1] = new PerLevelAttribute(levelIndex, 
		//		numSamples, numFeatures, w.length);
		//attributeOfSubLevels[levelIndex-1].setClustersAttribute(w, mean, cov, zjk);
		PerLevelAttribute subLevelAttribute = new PerLevelAttribute(levelIndex, 
						numSamples, numFeatures, w.length);
		subLevelAttribute.setClustersAttribute(w, mean, cov, zjk);
		attributeOfSubLevels.add(subLevelAttribute);
	}
	
	public PerLevelAttribute getSubLevelAttribute(int levelIndex) {
		//return attributeOfSubLevels[levelIndex-1];
		return attributeOfSubLevels.get(levelIndex-1);
	}
	
	public int[] getLabels() {
		labels = new int[numRawSamples];
		String labeldata  = (((DefaultSample) rawSampleNames.get(0)).getSampleLabel());
		ArrayList labelTypes = new ArrayList();
		labelTypes.add(labeldata);
		labels[0] = 1;
		for (int j = 1; j < numRawSamples; j++) {
			labeldata  = (((DefaultSample) rawSampleNames.get(j)).getSampleLabel());
			if (labelTypes.contains(labeldata)) {
				labels[j] = labelTypes.indexOf(labeldata)+1;
			} else {
				labelTypes.add(labeldata);
				labels[j] = labelTypes.size();
			}
		}
		return labels;
	}
	
	public void setSNRs(double[] SNRPerformance) {
		SNR = copy(SNRPerformance);
	}
	
	public double[] getSNRs() {
		return SNR;
	}
	
	public void setOrgMeans(double[][] mean) {
		orgMean = copy(mean);
	}
	
	public double[][] getOrgMeans() {
		return orgMean;
	}
	
	public void setOrgVars(double[][] var) {
		orgVar = copy(var);
	}
	
	public double[][] getOrgVars() {
		return orgVar;
	}
	
	public String getDatasetName() {
		return datasetName;
	}
	
	public int getLevelCount() {
		return attributeOfSubLevels.size();
	}
	
	public void setMDLFlag(boolean doMDL) {
		toDoMDL = doMDL;
	}
	
	public boolean getMDLFlag() {
		return toDoMDL;
	}
	
}
