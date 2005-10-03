package edu.vt.cbil.visda.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.math.plot.Plot2DPanel;
import org.math.plot.PlotPanel;

import edu.vt.cbil.visda.comp.ExperimentData;
import static org.math.array.LinearAlgebra.*;
import edu.vt.cbil.visda.util.*;
import edu.vt.cbil.visda.data.*;

/**
 * Created with Eclipse
 * Date: July 19, 2005
 */

/**
 * the class to plot one single projection combined the Posterior probabilities of samples
 * 
 * @author Jiajing Wang
 * @version visda_v0.2
 */
public class Plot2DViewer extends JPanel implements MouseListener
{
	
	/**
     * the panel for plotting
     */
	Plot2DPanel plotPanel;
	
	ExperimentData expData;
	
	/**
     * Posterior probabilities of samples
     */
	double[] Zjk;
	
	/**
     * If one samples's Posterior probability is less than this field,
     * then its annotation message won't be shown.
     */
	double annotateThreshold = 0;
	
	/**
     * If one samples's Posterior probability is less than this field,
     * then its sample won't be drawed in the plot.
     */
	double displayThreshold;
	
	/**
     * the color list for display different classes
     */
	Color[] COLORLIST = { Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.PINK, Color.CYAN, Color.MAGENTA, Color.YELLOW};
	
	private native int csort(int num, double[] a, int[] a_id, char order);
	
	static
    {
      // The runtime system executes a class's static
      // initializer when it loads the class.
  	  System.loadLibrary("VISDA_CJavaInterface");
    }
	
	/**
	 * Create a Plot2DViewer
	 * @param D			the data matrix
	 * @param W			the top TWO components for projection
	 * @param MU		the means of sample data
	 * @param labels	the labels of sample data
	 * @param Zjk		the Posterior probabilities of all samples belong to current cluster
	 * @param displaySetting	the display setting of parameteres
	 * @param expdata	the struct to contain all the analysis results
	 */
	public Plot2DViewer(double[][] D, double[][] W, double[] MU, 
    		   int[] labels, double[] Zjk, DisplayParam displaySetting,
    		   ExperimentData expdata)
	{
		int N = D.length;
		int P = W.length;
		int Q = 2;
		
		this.Zjk = copy(Zjk);
		expData = expdata;
		plotPanel = new Plot2DPanel();
		displayThreshold = displaySetting.thresholdZjk;
		
		double X[][];
		int LABELS3[]; 
		Color color;
		
		// The following defines the number of colour gradations. Could increase this
		// for 32k or 16M colour screens
		int COL_STEP = 16;
		
		double THRESHOLD = 1/(2 * (double)COL_STEP);

		double sortedZjk[] = new double[N];
		int new_order[] = new int[N];

		if (displaySetting.vl3 == 0) {
			displaySetting.blob_size = 2;
		}
		
		double R[];
		R = divide(displaySetting.R, max(max(displaySetting.R)));

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

		if (displaySetting.vl1 == 3) {  // display with labels, and `depth coloring'
			X1 = times(tmp1, W);

			// sort Zjk ascendingly
			/*sortedZjk = copy(Zjk);		
			csort(N, sortedZjk, new_order, 'a');

			// sort data points based on Zjk
			int size = 0;
			for (int i = 0; i < N; i++) {
				if (sortedZjk[i] >= displaySetting.thresholdZjk) {
					size ++;
				}
			}
			X = new double[size][2];
			LABELS3 = new int[size]; 
			int j = 0;
			for (int i=0; i< N; i++) {
				if (sortedZjk[i] >= displaySetting.thresholdZjk) {
					X[j] = X1[new_order[i]];
					LABELS3[j] = labels[new_order[i]];
					j++;
				}
			}*/
			
			sortedZjk = copy(Zjk);
			for (int i = 0; i < N; i++) {
				if (sortedZjk[i] < displaySetting.thresholdZjk) {
					sortedZjk[i] = -1;
				}
			}
			X = copy(X1);
			LABELS3 = new int[N];
			LABELS3 = labels;
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

		if (displaySetting.blob_size > 0) {
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
			//Common.printMatrix(N, 2, X);
			
			for (int i=first; i<N; i++) {
				
			  if (sortedZjk[i] != -1) {

				color = COLORLIST[LABELS3[i]-1];
				if (displaySetting.vl1 != 9) {			
					double d[][] = new double[1][];
					d[0] = X[i];
					int id = ((DefaultSample)expData.sampleNames.get(i)).getIndex();
					Integer integerObject = new Integer(id);
					plotPanel.addPlot("SCATTER", integerObject.toString(), color, d);
				}
				
				// with labels, and `depth coloring'
				// change the color of points according to its posterior probabilities 
				if (displaySetting.vl1 == 3) {
					double zzjjkk;
					float cols[] = new float[3];
					
					if (sortedZjk[i] < 0.08) {
						zzjjkk = 0.08;  
					} else {   
						zzjjkk = sortedZjk[i];   
					}
					if (displaySetting.flag_tc == 0) {
						color.getRGBColorComponents(cols);
					} else {
						for (int j = 0; j < 3; j++)
							cols[j] = displaySetting.p_color[j];
					}
					for (int j = 0; j < 3; j++) {
						cols[j] =  cols[j] + (1 - cols[j]) * (1 - (float)zzjjkk);
			        	//System.out.print("  " + cols[j]);
					}
					//System.out.print("\n");
					Color newColor = new Color(cols[0], cols[1], cols[2]);
					int plotIndex = plotPanel.plotCanvas.plots.size()-1;
					plotPanel.changePlotColor(plotIndex, newColor);
				}
			  
			  }
			  
			}
		}
		  
		plotPanel.removePlotToolBar();
		//plotPanel.addQuantiletoPlot(0, 0, 0.5);
		//plotPanel.setLegendOrientation(PlotPanel.SOUTH);
		plotPanel.plotCanvas.addMouseListener(this);
		add(plotPanel);
		
		  
	}
		
	
	public void mousePressed(MouseEvent e){} 
	// dont use these so leave them empty 
	
	/**
	 * Get the coordinate of the pointer when it is selected with mouse click
	 */
	public void mouseClicked(MouseEvent e){
		
		if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
			int dotIndex = plotPanel.plotCanvas.selectedPlotId;
			
			if (dotIndex >= 0) {
				int validDotCnt = -1;
				for (int i=0; i<Zjk.length; i++) {
					if (Zjk[i] >= displayThreshold) {
						validDotCnt++;
						if (validDotCnt == dotIndex) {
							if (Zjk[i]>=annotateThreshold) {
								Color dotColor = plotPanel.plotCanvas.plots.get(dotIndex).getColor();
								AnnotationDialog adialog = new AnnotationDialog(new JFrame(),
										true, i, expData, Zjk[i]);
							}
						}
					}
				}
			}
		}
		
	} 
	
	
	public void mouseEntered(MouseEvent e){} 
	public void mouseExited(MouseEvent e){} 
	public void mouseReleased(MouseEvent e){} 
	
}
