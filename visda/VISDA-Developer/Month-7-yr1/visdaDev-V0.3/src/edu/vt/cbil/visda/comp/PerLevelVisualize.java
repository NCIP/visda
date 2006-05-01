package edu.vt.cbil.visda.comp;

import edu.vt.cbil.visda.view.*;

/**
 * Created with Eclipse
 * Date: Aug 21, 2005
 */

/**
 * The class stores the visualization results at one clustering level
 * 
 * @author Jiajing Wang
 * @version visda_v0.1
 */

public class PerLevelVisualize {
	
	int subLevelID;
	
	public int subClustersNum;
	
	public Plot2DViewer subLevelPlots[];
	
	public double WW[][][];
	
	public PerLevelVisualize() {
		super();
	}
	
	public PerLevelVisualize(int levelID) {
		super();
		// TODO Auto-generated constructor stub
		subLevelID = levelID;
	}
	
	public PerLevelVisualize(int levelID, int p, int k) {
		super();
		// TODO Auto-generated constructor stub
		subLevelID = levelID;
		subClustersNum = k;
		subLevelPlots = new Plot2DViewer[k];
		WW = new double[p][2][k];
	}
}