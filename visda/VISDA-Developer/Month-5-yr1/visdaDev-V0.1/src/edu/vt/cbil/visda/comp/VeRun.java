package edu.vt.cbil.visda.comp;

import org.math.plot.Plot2DPanel;

import edu.vt.cbil.visda.util.*;
import static org.math.array.LinearAlgebra.*;
import static org.math.array.StatisticSample.*;

/**
 * Created with Eclipse
 * Author: Jiajing Wang
 * Date: July 14, 2005
 *
 */

/**
 * the top entrance to perform operation on data
 *
 */
public class VeRun
{

	int N;
	int P;
	int levelDeep;
	double[][] data;
	int [] labels;
	double thre_Zjk;
	
	//	 data display options
	//	0 -- plain, no labels
	//	1 -- with labels  
	//	2 -- no labels, but with `depth coloring'
	//  3 -- with labels, and `depth coloring'
	int vl1;

	//   0 -- black background
	//   1 -- white background 
	int vl2;

	//   0 -- no label# displayed
	//   1 -- with label# displayed 
	int vl3;
	
  

	public VeRun(double[][] data, int[] labels, int n, int p, int l, double zjk, int vl1, int vl2, int vl3) {
		super();
		// TODO Auto-generated constructor stub
		this.data = data;
		this.labels = labels;
		N = n;
		P = p;
		levelDeep = l;
		thre_Zjk = zjk;
		this.vl1 = vl1;
		this.vl2 = vl2;
		this.vl3 = vl3;
	}



// The main program
  //public static void main(String[] args)
  public void veExe()
    {
	  double Zjk[]= new double[N];
	  double mu[] = new double[P];
	  double cov[][] = new double[P][P];
	  double vv = 0.5;
	  // for top-level
	  DRMCore drmcoreTop;
	  // for sub-level
	  DRMCore drmcoreSub[];
	  double WW[][][];
	  Plot2DPanel plotPanelsSub[];
	  
	  // randomize input
      for (int i = 0; i < N; i++){
      	Zjk[i] = 1.0;
      }
      
      // Center the data in the t-space
      //    the means of D are zeros after centering
      mu = mean(data); 
      Common.centerData(N, P, data, mu);
      
      double R[] = new double[N];
  	  int blob_size = 2;
  	  int flag_tc = 0;
  	  float p_color[] = new float[3];
  	  
  	  for (int i=0; i< N; i++) {
  		  R[i] = 1;
  	  }
  	  if (flag_tc == 0) {
  		  p_color[0] = (float) 1.0;
  		  p_color[1] = (float) 1.0;
  		  p_color[2] = (float) 1.0;
  	  } else {
  		  p_color[0] = (float) 0.5;
		  p_color[1] = (float) 0.3;
		  p_color[2] = (float) 0.4;
  	  }
  	  
  	  //----------------------------------------
      // Top-level Operations
      //----------------------------------------
      // Data Projection on top-level
      drmcoreTop = new DRMCore(N, P, 1, 1);
      drmcoreTop.data = data;
  	  //drmcoreTop.covCluster = covData;
  	  drmcoreTop.meanCluster = mu;
  	  drmcoreTop.Zjk = Zjk;
  	  drmcoreTop.veProjection(vl1, vl2, vl3, R, labels, thre_Zjk, blob_size, flag_tc, p_color);
  	  //drmcore.print();
       
  	  // Sub-Clusters initialization on Up-level plot
      CVMCore cvmcore = new CVMCore(N, P);
      cvmcore.veMselect(drmcoreTop.plotPanel, data, drmcoreTop.top2Components);
      cvmcore.print();
      
      System.out.print("\n** OK **  Generating Top Level Mixture Model.\n");
      
      double vk0[]; //every point selected is belong to picture 1(top level)
      vk0 = one(cvmcore.numUpClusters, 1.0);
      
      // the sub-level clusters number
      int subK;
      
      //----------------------------------------
      // Sub-level Operations
      //----------------------------------------
  	  for (int l=2; l<levelDeep+1; l++) { 
  		  
  		  subK = cvmcore.numSubClusters;
  		  
  		  // EM (model formation)
  		  CFMCore cfmcoreSub = new CFMCore(N, P, subK);
  		  cfmcoreSub.uc = vk0;
  		  cfmcoreSub.Data = data;
  		  cfmcoreSub.Mean0_x = cvmcore.MU_t;
  		  cfmcoreSub.W0 = cvmcore.w0;
  		  cfmcoreSub.vv = vv;
  		  cfmcoreSub.errorThresh = 0.01;
  		  cfmcoreSub.veSubNew();
  		  
  		  // Plot multiple sublevel projections
  		  WW = new double[subK][P][2];
  		  plotPanelsSub = new Plot2DPanel[subK];
  		  drmcoreSub = new DRMCore[subK];
  		  for (int j = 0; j<subK; j++) {
  			  mu = getRowCopy(cfmcoreSub.Mean1_x, j);
  			  Zjk = getColumnCopy(cfmcoreSub.Zjk, j); 
  			  cov = getColumnsRangeCopy(cfmcoreSub.Cov_mat, P*j, P*(j+1)-1);
  			  //centerData(N, P, data, mu);
  			  
  			  drmcoreSub[j] = new DRMCore(N, P, l, j+1);
  			  drmcoreSub[j].data = data;
  			  drmcoreSub[j].covCluster = cov;
  			  drmcoreSub[j].meanCluster = mu;
  			  drmcoreSub[j].Zjk = Zjk;
    		  drmcoreSub[j].veProjection(vl1, vl2, vl3, R, labels, thre_Zjk, blob_size, flag_tc, p_color);
    		  //drmcoreSub[j].print();
    		  WW[j] = drmcoreSub[j].top2Components;
    		  /*if (j<2) {
    			  for (int i = 0; i < P; i++){
    				  WW[j][i][0] = drmcoreSub[j].top2Components[i][0];
    				  WW[j][i][1] = -drmcoreSub[j].top2Components[i][1];
    			  }
    		  } else if (j==2) {
    			  for (int i = 0; i < P; i++){
    				  WW[j][i][0] = -drmcoreSub[j].top2Components[i][0];
    				  WW[j][i][1] = drmcoreSub[j].top2Components[i][1];
    			  }
    		  }*/
    		  plotPanelsSub[j] = drmcoreSub[j].plotPanel;
  		  }
  		  
  		  System.out.print("\n** OK **  Generating the Level "+l+" Mixture Model.\n");
  		
  		  // Sub-Clusters initialization on Up-level plot
  		  cvmcore.level = l;
  		  cvmcore.veMselect2(plotPanelsSub, data, WW, cfmcoreSub.Zjk);
  		  cvmcore.print();
  	  }
  	  
  	  System.out.print("\n**** The END ****\n");
  	  	   
    }
  	
}
