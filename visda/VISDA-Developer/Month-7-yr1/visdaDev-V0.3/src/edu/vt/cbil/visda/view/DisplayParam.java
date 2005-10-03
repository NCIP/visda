package edu.vt.cbil.visda.view;

/**
 * Created with Eclipse
 * Date: Aug 21, 2005
 */

/**
 * The class contains all the parameters for display 
 * 
 * @author Jiajing Wang
 * @version visda_v0.1
 */

public class DisplayParam {
	
	/**	 
	 *  data display options:
	 *	0 -- plain, no labels; 
	 *	1 -- with labels;  
	 *	2 -- no labels, but with `depth coloring'; 
	 *  3 -- with labels, and `depth coloring'; 
	 * ('3' is valid in current version)
	 */
	int vl1;

	/**	 
	 *  data display options:
	 *  0 -- black background; 
	 *  1 -- white background;  
	 *  ('1' is valid in current version)
	 */ 
	int vl2;

	/** 
	 *  data display options:
	 *  0 -- no label# displayed; 
	 *  1 -- with label# displayed;  
     *	('0' is valid in current version)
     */
	int vl3;
	
	/**
	 * for deep level display
	 */
	double R[];
	
	/**
	 * 1: time course data without color;
	 * 0: phenotype data with color
	 */
	int flag_tc;
	
	/**
	 * [1 1 1] There is no designated color, it is phenotype;
	 * Others, There is designated color, it is timecourse.
	 */
	float p_color[];
	
	/**
	 * the threshold of posterior probability for display
	 */
	double thresholdZjk;

	int blob_size;
	
	public DisplayParam(double zjk, int vl1, int vl2, int vl3, int flag_tc, double[] r, int blobsize) {
		super();
		// TODO Auto-generated constructor stub
		this.flag_tc = flag_tc;
		R = r;
		thresholdZjk = zjk;
		this.vl1 = vl1;
		this.vl2 = vl2;
		this.vl3 = vl3;
		this.blob_size = blobsize;
		p_color = new float[3];
		if (flag_tc == 0) {
	  		p_color[0] = (float) 1.0;
	  		p_color[1] = (float) 1.0;
	  		p_color[2] = (float) 1.0;
	  	} else {
	  		p_color[0] = (float) 0.5;
	  		p_color[1] = (float) 0.3;
	  		p_color[2] = (float) 0.4;
	  	}
	}
	
}