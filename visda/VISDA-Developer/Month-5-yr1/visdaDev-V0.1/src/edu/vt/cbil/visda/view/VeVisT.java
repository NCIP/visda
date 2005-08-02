package edu.vt.cbil.visda.view;

import java.awt.Color;

import org.math.plot.Plot2DPanel;
import org.math.plot.*;
import edu.vt.cbil.visda.util.*;
import static org.math.array.LinearAlgebra.*;
import static org.math.array.DoubleArray.*;

/**
 * Created with Eclipse
 * Author: Jiajing Wang
 * Date: July 19, 2005
 *
 */

/**
 * the class to plot the projection combined the Posterior probabilities of samples
 *
 */

public class VeVisT
{

	// data display options
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
	
	//
	int N;
	
	//
	int P;
	
	//
	int Q;
	
	Plot2DPanel plotPanel;
	
	Color[] COLORLIST = { Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.PINK, Color.CYAN, Color.MAGENTA, Color.YELLOW};
	
	public VeVisT(int vl1, int vl2, int vl3, int n, int p, int q, Plot2DPanel panel)
	{
		super();
		// TODO Auto-generated constructor stub
		this.vl1 = vl1;
		this.vl2 = vl2;
		this.vl3 = vl3;
		this.N = n;
		this.P = p;
		this.Q = q;
		this.plotPanel = panel;
	}
	
	private native int csort(int num, double[] a, int[] a_id, char order);
	
	static
    {
      // The runtime system executes a class's static
      // initializer when it loads the class.
  	  System.loadLibrary("VISDA_CJavaInterface");
    }
	
	public void vePlot(double[][] D, double[][] W, double[] MU, double[] R, 
	           		   int[] labels, double[] Zjk, double thre_Zjk, 
	           		   int blob_size, int flag_tc, float[] p_color)
// w - Two principal components resulted from PCA, PCA-PPM or DCA for the current window.
// mu - Mean of the parent cluster or sub-cluster at the upper level. 
// r - for deep level display
// flag_tc -	1: time course data without color
//              0: phenotype data with color
// p_color - 	[1 1 1] There is no designated color, it is phenotype.
// 				Others, There is designated color, it is timecourse.
	{
		double X[][];
		int LABELS3[]; 
		Color color;
		
		// The following defines the number of colour gradations. Could increase this
		// for 32k or 16M colour screens
		int COL_STEP = 16;
		
		double THRESHOLD = 1/(2 * (double)COL_STEP);

		double sortedZjk[] = new double[N];
		int new_order[] = new int[N];

		if (vl3 == 0) {
			blob_size = 2;
		}

		R = divide(R, max(max(R)));

		//load cols.mat;
		//[cols_m cols_n] = size(cols);
		//cols  = cols/255;
		//colsw = ones(cols_m,3)-cols*0.75;
		//colsw(5,:) = [0.75 0.75 0.75];
		//symv = FONT_SYMBOLS;
		//sym  = setstr(symv);

		//THRESHOLD = 1/(2*COL_STEP);
		double[][] X1 = new double[N][2];
		double[][] tmp1 = new double[N][P];
		double[][] tmp2 = new double[N][P];
		double[][] tmp3 = new double[N][P];

		double[][] v1 = new double[N][1];
		double[][] v2 = new double[1][P];

		//  X1 = (D-ones(n,1)*MU) * W ;
		for (int i=0; i< N; i++) {
			v1[i][0] = 1;
		}
		for (int j=0; j< P; j++) {
			v2[0][j] = MU[j];
		}
		tmp1 = times(v1, v2);
		for (int i=0; i< N; i++) {
			for (int j=0; j< P; j++) {
				tmp1[i][j] = D[i][j] - tmp1[i][j];
			}
		}

		if (vl1 == 3) {  // display with labels, and `depth coloring'
			X1 = times(tmp1, W);

			// sort Zjk ascendingly
			sortedZjk = copy(Zjk);		
			csort(N, sortedZjk, new_order, 'a');

			// sort data points based on Zjk
			int size = 0;
			for (int i = 0; i < N; i++) {
				if (sortedZjk[i] >= thre_Zjk) {
					size ++;
				}
			}
			X = new double[size][2];
			LABELS3 = new int[size]; 
			int j = 0;
			for (int i=0; i< N; i++) {
				if (sortedZjk[i] >= thre_Zjk) {
					X[j] = X1[new_order[i]];
					LABELS3[j] = labels[new_order[i]];
					j++;
				}
			}
			
			//Common.printMatrix(size, 2, X);
			
		} else {
	
			// X = (D-ones(n,1)*MU).*(ones(p,1)*Zjk')' * W ;
			double[][] v3 = new double[P][1];
			for (int j=0; j< P; j++) {
				v3[j][0] = 1;
			}
			double[][] v4 = new double[1][N];
			for (int i=0; i< N; i++) {
				v4[0][i] = Zjk[i];
			}
			tmp2 = transpose(times(v3, transpose(v4)));
			for (int i=0; i< N; i++) {
				for (int j=0; j<P; j++) {
					tmp3[i][j] = tmp1[i][j] * tmp2[i][j];
				}
			}
			X = new double[N][2];
			X = times(tmp3, W);
			LABELS3 = new int[N];
			LABELS3 = labels;
		}

		if (blob_size > 0) {
			// sort R ascendingly
			int R_id[] = new int[N];
			int first = 0;
			csort(N, R, R_id, 'a');
			for (int i=0; i< N; i++) {
				if (R[i] > THRESHOLD) {
					first = i;
					break;
				}
			}
			for (int i=first; i<N; i++) {
				color = COLORLIST[LABELS3[i]-1];
				if (vl1 != 9) {			
					double d[][] = new double[1][];
					d[0] = X[i];
					plotPanel.addPlot("SCATTER", "Data_"+i, color, d);
				}
				
				// with labels, and `depth coloring'
				// change the color of points according to its posterior probabilities 
				if (vl1 == 3) {
					double zzjjkk;
					float cols[] = new float[3];
					
					if (sortedZjk[i] < 0.08) {
						zzjjkk = 0.08;  
					} else {   
						zzjjkk = sortedZjk[i];   
					}
					if (flag_tc == 0) {
						color.getRGBColorComponents(cols);
					} else {
						for (int j = 0; j < 3; j++)
							cols[j] = p_color[j];
					}
					for (int j = 0; j < 3; j++) {
						cols[j] =  cols[j] + (1 - cols[j]) * (1 - (float)zzjjkk);
			        	//System.out.print("  " + cols[j]);
					}
					//System.out.print("\n");
					Color newColor = new Color(cols[0], cols[1], cols[2]);
					plotPanel.changePlotColor(i-first, newColor);
				}
				
			}
		}
		  
		  
	}
	
}
