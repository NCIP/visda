package edu.vt.cbil.visda.util;

import static org.math.array.LinearAlgebra.times;

/**
 * Created with Eclipse
 * Author: Jiajing Wang
 * Date: July 14, 2005
 *
 */

/**
 * Common methods for matrix operation and print
 *
 */
public class Common
{
	public static double[] array2Dto1D(int m, int n, double[][] a)
    {
  		int i, j;
  		double b[];
  		
  		b = new double[m*n];
  		for (i = 0; i < m; i++)
  	       	 for (j = 0; j < n; j++)
  	       	 	b[i*n+j] = a[i][j];
  		
  		return b;
    }
  
	public static double[][] array1Dto2D(int m, int n, double[] b)
    {
  		int i, j;
  		double a[][];
  		
  		a = new double[m][n];
  		for (i = 0; i < m; i++)
  	       	 for (j = 0; j < n; j++)
  	       	 	a[i][j] = b[i*n+j];
  		
  		return a;
    }
	
	public static void printMatrix(int m, int n, double[][] a)
    {
		int i, j;
		
		for (i = 0; i < m; i++) {
          	 for (j = 0; j < n; j++)
          	 	System.out.print("  " + a[i][j]);
          	 System.out.println();
        }
        System.out.println();
    }
  
	public static void printMatrix(int m, int n, double[] a)
    {
		int i, j;
		
		for (i = 0; i < m; i++) {
          	 for (j = 0; j < n; j++)
          	 	System.out.print("  " + a[i*n+j]);
          	 System.out.println();
        }
        System.out.println();
    }
  
	public static void printVector(int l, double[] b)
	{
		int j;
	
        for (j = 0; j < l; j++)
        	System.out.print("  " + b[j]);
        System.out.println();
        System.out.println();
	}
  
	/**
	 * Center the data
	 * the means of D are zeros after centering
	 */  
  	public static void centerData(int N, int P, double[][] data, double[] mean)
  	{
  		double v1[][] = new double[N][1];
  		for (int i=0; i< N; i++) {
  			v1[i][0] = 1;
  		}
  		double v2[][] = new double[1][P];
  		v2[0] = mean;
  		double tmp1[][] = new double[N][P];
  		tmp1 = times(v1, v2);
  		for (int i=0; i< N; i++) {
  			for (int j=0; j< P; j++) {
  				data[i][j] = data[i][j] - tmp1[i][j];
  			}
  		}
  	}
  	
  	/**
  	 * Determine whether a double value is a real number.
  	 * @param a		the double value
  	 * @return	true	is real
  	 * 			false	is Not real
  	 */
  	public static boolean isReal(double a) {
  		if (Double.isInfinite(a) || Double.isNaN(a)) {
  			return false;
  		} else {
  			return true;
  		}	
  	}
  	
  	public static boolean isReal(double[][] A)
    {
		int m = A.length;
		int n = A[0].length;
		int i, j;
		
		for (i = 0; i < m; i++) {
          	 for (j = 0; j < n; j++) {
          		 if (isReal(A[i][j]) == false) {
          			 return false;
          		 }
          	 }
		}
		return true;
    }
  
	public static boolean isReal(double[] A)
    {
		int l = A.length;
		int i;
		
		for (i = 0; i < l; i++) {
			if (isReal(A[i]) == false) {
     			 return false;
     		}
        }
        return true;
    }
	
}
