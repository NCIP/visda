/////////////////////////////////////////////////////////////////////
//	File:			veModel.cpp
//	Project:		VISDA
//	Description:	for computing the finite normal mixture
//	Author:			Jiajing Wang
//	Create Date:	July 1st, 2005
//
//	History Log:	
/////////////////////////////////////////////////////////////////////

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
//#include <string.h>
#include "mathtool.h"

#define PI  3.14159265

/*******************************************************************
 Subroutine to calculate the mixed Gaussian Model
   matrix *D:       the pointer to the matrix data
   matrix *mean0_x: the pointer to a matrix containing Means of clusters
   matrix *Var0:    the pointer to a group of matrixs containing the Covariance
                      matrix of clusters
   vector *w0:		the pointer to a vector containing the mixing proportion of clusters
   matrix *Gxn:		the pointer to the matrix containing the probability per sample per cluster
   vector *Fx:		the pointer to the vector containing the probability per sample
   
 return value: '1' - successfully exit
               '0' - exit with waring/error
*******************************************************************/
int veModel(matrix *D, matrix *mean0_x, matrix *Var0, vector *w0, //input
			matrix *Gxn, vector *Fx)  //output
{
	int k0, kc, n, p;
	int i, j, kk, u, t;
	matrix Var0_mat;
	matrix inv_Var0;
	double det_Var0 = 0;
	matrix cen_Dj;
	matrix cen_Dj_t;
	matrix tmp;
	matrix tmp2;
	double val;

	n = D->m;
	p = D->n;
	k0 = mean0_x->m;
	kc = mean0_x->n;

    mnew(&Var0_mat, p, p);
	mnew(&inv_Var0, p, p);
	mnew(&cen_Dj, 1, p);
	mnew(&cen_Dj_t, p, 1);
	mnew(&tmp, 1, p);
	mnew(&tmp2, 1, 1);

	mzero(Gxn);
	vzero(Fx);

	for (kk=0; kk<k0; kk++) {
		u = kk*p;
		
		veCov(Var0+kk, &inv_Var0, &Var0_mat, &det_Var0);
		mcopy(&Var0_mat, Var0+kk);
		 
		for (j=0; j<n; j++) {
			t = j*p;
			
			for(i=0; i<p; i++) {
				*(cen_Dj.pr+i) = *(D->pr+t+i) - *(mean0_x->pr+u+i);
			}

			transpose(&cen_Dj, &cen_Dj_t);
			mmMul(&cen_Dj, &inv_Var0, &tmp);			
			mmMul(&tmp, &cen_Dj_t, &tmp2);			
			val = *(tmp2.pr);

			*(Gxn->pr+j*k0+kk) = pow((sqrt(1/(2*PI))),p) * exp(-val/2) / sqrt(det_Var0);
			//double expv = exp(-val/2);
			//if (expv == 0) {
			//	expv = 1.0e-30;
			//}
			//*(Gxn->pr+j*k0+kk) = pow((sqrt(1/(2*PI))),p) * expv / sqrt(det_Var0);
		}

		for (j=0; j<n; j++) {
			*(Fx->pr+j) += (*(w0->pr+kk)) * (*(Gxn->pr+j*k0+kk));   
		}
    }  
    
    mdelete(&Var0_mat);
	mdelete(&inv_Var0);
	mdelete(&cen_Dj);
	mdelete(&cen_Dj_t);
	mdelete(&tmp);
	mdelete(&tmp2);

    return 1;
}
