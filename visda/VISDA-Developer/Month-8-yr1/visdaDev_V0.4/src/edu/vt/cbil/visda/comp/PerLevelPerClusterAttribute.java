package edu.vt.cbil.visda.comp;

import edu.vt.cbil.visda.view.Plot2DViewer;

/**
 * Created with Eclipse
 * Date: Aug 21, 2005
 */

/**
 * The class stores the analysis results of one sub-cluster at one level
 * 
 * @author Jiajing Wang
 * @version visda_v0.4
 * <p>
 * Version Log:
 * 	<br>visda_v0.2 (08/30/2005):	The file is created and basic function is implemented.
 * 	<br>visda_v0.4 (10/29/2005):  	Add member 'projType', 'projPlot' and 'WW';
 * 
 */

public class PerLevelPerClusterAttribute {
	
	int levelID;
	
	int clusterID;
	
	double w;
	
	public double mu[];
			
	public double covMat[][];
	
	public double Zjk[];

	public double top2Comp[][][];
	
	/**
	 * the projection type used for the visualization of this cluster
	 * 	1 - PCA
	 *  2 - PCAPPM
	 *  3 - DCA
	 */
	private int projType;
	
	/**
	 * The projection plot of the cluster
	 * 
	 */
	private Plot2DViewer projPlot;
	
	/**
	 * The top 2 components used for projection
	 * 
	 */
	private double[][] WW;
	
	
	/*
	 * The number of sub-clusters from the current cluster
	 */
	private int numSubClusters; 
	
	/*
	 * The string indicates the location of the current cluster in the hierachical tree 
	 * For example, 1-1-2
	 */
	private String clusterTreeLoc;
	
	public PerLevelPerClusterAttribute() {
		super();
	}
	
	public PerLevelPerClusterAttribute(int clusterid, int levelid) {
		super();
		// TODO Auto-generated constructor stub
		clusterID = clusterid;
		levelID = levelid;
		numSubClusters = 1;
	}


	public PerLevelPerClusterAttribute(int clusterid, int levelid, int samplesNum, int featuresNum) {
		super();
		clusterID = clusterid;
		levelID = levelid;
		mu = new double[featuresNum];
		covMat = new double[featuresNum][featuresNum];
		Zjk = new double[samplesNum];
		top2Comp = new double[2][featuresNum][2];  // if DCA is implemented, it will be [3][featuresNum][2]
		projType = 1; // PCA by default
	}
	
	public void setNumSubClusters(int num) {
		numSubClusters = num;
	}
	
	public int getNumSubClusters() {
		return numSubClusters;
	}
	
	public void setProjType(int t) {
		projType = t;
		WW = top2Comp[t-1];
	}
	
	public int getProjType() {
		return projType;
	}
	
	public void setProjPlot(Plot2DViewer p) {
		projPlot = p;
	}
	
	public Plot2DViewer getProjPlot() {
		return projPlot;
	}
	
	public double[][] getProjComp() {
		return WW;
	}
	
	public void setClusterTreeLoc(String l) {
		clusterTreeLoc = l;
	}
	
	public String getClusterTreeLoc() {
		return clusterTreeLoc;
	}
}
