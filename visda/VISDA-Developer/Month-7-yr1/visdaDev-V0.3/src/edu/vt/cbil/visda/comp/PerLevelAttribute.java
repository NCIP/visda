package edu.vt.cbil.visda.comp;

import static org.math.array.LinearAlgebra.*;

/**
 * Created with Eclipse
 * Date: Aug 21, 2005
 */

/**
 * The class stores the analysis results at one clustering level
 * 
 * @author Jiajing Wang
 * @version visda_v0.1
 */

public class PerLevelAttribute {
	
	int subLevelID;
	
	public int subClustersNum;
	
	public double w[];
	
	public double mu[][];
			
	public double covMat[][];
	
	public double Zjk[][];
	
	public PerLevelPerClusterAttribute attributeOfSubClusters[];

	public PerLevelAttribute() {
		super();
	}
	
	public PerLevelAttribute(int levelID) {
		super();
		// TODO Auto-generated constructor stub
		subLevelID = levelID;
	}
	
	public PerLevelAttribute(int levelID, int samplesNum, int featuresNum, int clustersNum) {
		super();
		subLevelID = levelID;
		subClustersNum = clustersNum;
		w = new double[clustersNum];
		mu = new double[clustersNum][featuresNum];
		covMat = new double[featuresNum][featuresNum*clustersNum];
		Zjk = new double[samplesNum][clustersNum];
		attributeOfSubClusters = new PerLevelPerClusterAttribute[clustersNum];
		for (int i=0; i<clustersNum; i++) {
			attributeOfSubClusters[i] = new PerLevelPerClusterAttribute(levelID, i+1,
					samplesNum, featuresNum);
		}
	}
	
	public void setClustersAttribute(double w[], double mean[][], 
			double cov[][], double zjk[][]) {
		this.w = copy(w);
		this.mu = copy(mean);
		this.covMat = copy(cov);
		this.Zjk = copy(zjk);
		int P = mean[0].length;
		for (int i=0; i<subClustersNum; i++) {
			attributeOfSubClusters[i].w = w[i];
			attributeOfSubClusters[i].mu = copy(mean[i]);
			attributeOfSubClusters[i].covMat = getColumnsRangeCopy(cov, P*i, P*(i+1)-1);
			attributeOfSubClusters[i].Zjk = getColumnCopy(zjk, i);
		}
	}
	
	public void setProjCompAttribute(int clusterId, double pcaVector[][], double pcappmVector[][]) {
		attributeOfSubClusters[clusterId].top2Comp[0] = copy(pcaVector);
		attributeOfSubClusters[clusterId].top2Comp[1] = copy(pcappmVector);
	}
	
	public PerLevelPerClusterAttribute getSubClusterAttribute(int clusterid) {
		return attributeOfSubClusters[clusterid-1];
	}
	
}