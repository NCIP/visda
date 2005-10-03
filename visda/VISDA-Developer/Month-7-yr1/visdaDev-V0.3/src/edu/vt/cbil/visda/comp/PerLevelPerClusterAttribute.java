package edu.vt.cbil.visda.comp;

/**
 * Created with Eclipse
 * Date: Aug 21, 2005
 */

/**
 * The class stores the analysis results of one sub-cluster at one level
 * 
 * @author Jiajing Wang
 * @version visda_v0.1
 */

public class PerLevelPerClusterAttribute {
	
	int levelID;
	
	int clusterID;
	
	double w;
	
	public double mu[];
			
	public double covMat[][];
	
	public double Zjk[];

	public double top2Comp[][][];
	
	
	public PerLevelPerClusterAttribute() {
		super();
	}
	
	public PerLevelPerClusterAttribute(int clusterid, int levelid) {
		super();
		// TODO Auto-generated constructor stub
		clusterID = clusterid;
		levelID = levelid;
	}


	public PerLevelPerClusterAttribute(int clusterid, int levelid, int samplesNum, int featuresNum) {
		super();
		clusterID = clusterid;
		levelID = levelid;
		mu = new double[featuresNum];
		covMat = new double[featuresNum][featuresNum];
		Zjk = new double[samplesNum];
		top2Comp = new double[2][featuresNum][2];  // if DCA is implemented, it will be [3][featuresNum][2]
	}
}