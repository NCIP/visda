package edu.vt.cbil.visda.comp;

import java.util.ArrayList;
import java.util.Vector;
import edu.vt.cbil.visda.data.DefaultSample;
import edu.vt.cbil.visda.data.FloatDataSet;
import edu.vt.cbil.visda.data.*;

/**
 * Created with Eclipse
 * Date: Aug 21, 2005
 */

/**
 * The class pre-processes the raw data, including feature selection.
 * 
 * @author Jiajing Wang
 * @version visda_v0.2
 */
public class DataPreProcessor {
	
	public DataPreProcessor(ExperimentData experimentData, int clusterType, int numSelectedFeatures) {
		
		// get class label
		int[] label = experimentData.getLabels();
		
		double[][] data;
		
		//-----------------------------------
		// the following added by JJW
		//----------------------------------- 	
		if (clusterType == 1) { // gene clustering
			data = new double[numSelectedFeatures][experimentData.numRawFeatures];
		} else if (clusterType == 0)  {// phenotype clustering
			data = new double[experimentData.numRawSamples][numSelectedFeatures];
		}
		// feature selection
		FeaturePreSelector preSel = new FeaturePreSelector(clusterType, experimentData.rawData, 
				experimentData.numRawFeatures, experimentData.numRawSamples, 
				numSelectedFeatures, label);
		preSel.snrSupervised();
		//preSel.print();
		data = preSel.selectedData;
		
		experimentData.setSelData(clusterType, numSelectedFeatures, data, preSel.selectedFeatureIndex);
		experimentData.setSNRs(preSel.sortedPerformanceIndex);
		experimentData.setOrgMeans(preSel.featureMean);
		experimentData.setOrgVars(preSel.featureVar);
		
	}
	
}