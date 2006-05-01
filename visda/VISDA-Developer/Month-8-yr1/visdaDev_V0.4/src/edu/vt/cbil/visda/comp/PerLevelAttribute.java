package edu.vt.cbil.visda.comp;

import static org.math.array.LinearAlgebra.*;
import edu.vt.cbil.visda.view.Plot2DViewer;

/**
 * Created with Eclipse
 * Date: Aug 21, 2005
 */

/**
 * The class stores the analysis results at one clustering level
 * 
 * @author Jiajing Wang
 * @version visda_v0.4
 * <p>
 * Version Log:
 * 	<br>visda_v0.2 (08/30/2005):	The file is created and basic function is implemented.
 *  <br>visda_v0.3 (09/30/2005):
 * 	<br>visda_v0.4 (10/29/2005):  	Add member 'endFlags', and 'centers2D'
 * 
 */

public class PerLevelAttribute {
	
	public final static int PCA = 1;

    public final static int PCAPPM = 2;

    public final static int DCA = 3;
    
	int subLevelID;
	
	public int subClustersNum;
	
	public double w[];
	
	public double mu[][];
			
	public double covMat[][];
	
	public double Zjk[][];
	
	public PerLevelPerClusterAttribute attributeOfSubClusters[];
	
	/**
	 * The flag to indicate whether the corresponding cluster can be partitioned into more sub-clusters
	 * 	True - no more sub-clusters
	 *  False - has more sub-clusters
	 */
	private boolean endFlags[];
	

	/**
	 * The coordinate of the click selected cluster centers in 2D-space	
	 */
	private double centers2D[][];
	
	
	
	
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
		endFlags = new boolean[clustersNum];
		for (int i=0; i<clustersNum; i++) {
			endFlags[i] = true;		// by default, assume each cluster can NOT be sub-clustered
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
		attributeOfSubClusters[clusterId-1].top2Comp[0] = copy(pcaVector);
		attributeOfSubClusters[clusterId-1].top2Comp[1] = copy(pcappmVector);
	}
	
	public PerLevelPerClusterAttribute getSubClusterAttribute(int clusterid) {
		return attributeOfSubClusters[clusterid-1];
	}
	
	public void setClusterNOTEndFlag(int clusterId) {
		endFlags[clusterId-1] = false;
	}
	
	public void setClusterEndFlag(int clusterId) {
		endFlags[clusterId-1] = true;
	}
	
	public boolean getClusterEndFlag(int clusterId) {
		return endFlags[clusterId-1];
	}
	
	public void setAllClusterEndFlags(boolean flags[]) {
		endFlags = flags;
	}
	
	public boolean[] getAllClusterEndFlags() {
		return endFlags;
	}
	
	public void set1Cluster2DCenter(int clusterId, double x, double y) {
		centers2D[clusterId-1][0] = x;
		centers2D[clusterId-1][1] = y;
	}
	
	public void setCluster2DCenters(double centers[][]) {
		centers2D = copy(centers);
	}
	
	public double[][] getCluster2DCenters() {
		return centers2D;
	}
	
	public void setClusterProjtype(int clusterId, int projtype) {
		attributeOfSubClusters[clusterId-1].setProjType(projtype);
	}
	
	public int getClusterProjtype(int clusterId) {
		return attributeOfSubClusters[clusterId-1].getProjType();
	}
	
	public void setAllClusterProjtypes(int projtypes[]) {
		for (int i=0; i<subClustersNum; i++) {
			attributeOfSubClusters[i].setProjType(projtypes[i]);
		}
	}
	
	public int[] getAllClusterProjtypes() {
		int projTypes[] = new int[subClustersNum];
		for (int i=0; i<subClustersNum; i++) {
			projTypes[i] = attributeOfSubClusters[i].getProjType();
		}
		return projTypes;
	}
	
	public Plot2DViewer[] getAllClusterProjPlots() {
		Plot2DViewer[] plots = new Plot2DViewer[subClustersNum];
		for (int i=0; i<subClustersNum; i++) {
			plots[i] = attributeOfSubClusters[i].getProjPlot();
		}
		return plots;
	}
	
	public double[][][] getAllClusterProjComps() {
		int P = mu[0].length;
		double[][][] WWW = new double[subClustersNum][P][2];
		for (int i=0; i<subClustersNum; i++) {
			WWW[i] = attributeOfSubClusters[i].getProjComp();
		}
		return WWW;
	}
	
	public void setAllClusterTreeloc(String loc[]) {
		for (int i=0; i<subClustersNum; i++) {
			attributeOfSubClusters[i].setClusterTreeLoc(loc[i]);
		}
	}
	
	public String[] gettAllClusterTreeloc() {
		String loc[] = new String[subClustersNum];
		for (int i=0; i<subClustersNum; i++) {
			loc[i] = attributeOfSubClusters[i].getClusterTreeLoc();
		}
		return loc;
	}
}
