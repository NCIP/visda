#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
//#include <string.h>
#include "mathtool.h"


/*******************************************************************
 Subroutine to do the EM algorithm
   matrix *D:       the pointer to the matrix data
   matrix *mean0_x: the pointer to a matrix containing the initial Means of clusters
   vector *w0:		the pointer to a vector containing the initial mixing proportion of clusters
   double vv:       the value for initializing the Covariance matrix of clusters
   double error:    the error threshold
   vector *Zjk_up:  the pointer to a vector containing Posterior probabilities of the up-level 
                         cluster samples
   matrix *mean1_x: the pointer to a matrix containing the Means of clusters in t-space
   vector *w0_t:	the pointer to a vector containing the mixing proportions of the identified 
                         clusters in t-space
   matrix *cov_mat: the pointer to a group of matrixs containing the Covariance
                         matrix of clusters in t-space
   matrix *Zjk:     the pointer to a matrix containing Posterior probabilities of all samples 
                         belonging to all the sub-level clusters, each column is for one cluster.
   
 return value: '1' - successfully exit
               '0' - exit with waring/error
*******************************************************************/
int veSubEM(matrix *D, matrix *mean0_x, vector *w0, double vv, double error, vector *Zjk_up, //input
			matrix *mean1_x, vector *w0_t, matrix *cov_mat, matrix *Zjk)  //output
{
	int k0, kc, n, p;
	int i, j, k, u, s;
	matrix *Var0;
	matrix Gxn;
	vector Fx;
	matrix MUK;
	matrix MU1;
	int zeroFx_num = 1;
	//double error = 0.01;
	double err = error + (double)1;
	vector Zjk_temp;

	n = D->m;
	p = D->n;
	k0 = mean0_x->m;
	kc = mean0_x->n;
	
	Var0 = new matrix[k0];
	for(i=0; i<k0; i++) {
		mnew(Var0+i, p, p);
	}
	mnew(&Gxn, n, k0);
	vnew(&Fx, n);
	vnew(&Zjk_temp, n);
	mnew(&MUK, k0, p);
	mcopy(mean0_x, &MUK);
	mnew(&MU1, k0, p);

	vector D_j;
	vector Zjk_k;
	double sum_tmp = 0;
	matrix Ck;
	vector D_i;
	vector MUK_k;
	vector cen_D_i;
	matrix mtmp;
	vector vtmp;

	vnew(&D_j, n);
	vnew(&Zjk_k, n);
	mnew(&Ck, p, p);
	vnew(&D_i, p);
	vnew(&MUK_k, p);
	vnew(&cen_D_i, p);
	mnew(&mtmp, p, p);
	vnew(&vtmp, n);

	//Initializing the parameters of mixture of Gaussians
	//Initinalize the covariance matrix
	//Use EM algorithm to perform the local training.
	
	//Test intialization of covarinace matrix 
	//printf("Testing covariance matrix initialization... \n");

	while (zeroFx_num != 0) {
		for(i=0; i<k0; i++) {
			meye(Var0+i);
			for (j=0; j<p; j++) {
				*((Var0+i)->pr+j*p+j) = vv;
			}
		}
	
		veModel(D, mean0_x, Var0, w0, &Gxn, &Fx);
		//printf("\n Gxn = :\n");
		//mprint(&Gxn);
		//printf("\n Fx = :\n");
		//vprint(&Fx);

		zeroFx_num = 0;
		for (i=0; i<n; i++) {
			if (*(Fx.pr+i) == 0) {
				zeroFx_num++;
			}
		}

		vv *= 2;
	
	}

	vones(&Zjk_temp);

	//printf("\n EM in t-space starts ... \n");
	//printf("\n Data = \n");
	//mprint(D);

	int l = 0;
	while (err > error) {
		
		#ifdef _DEBUG
		printf(" \n...... in EM loop %d ......\n", ++l);

		printf("\n L%d: w0 = \n", l);
		vprint(w0);
		printf("\n L%d: MUK = \n", l);
		mprint(&MUK);
		printf("\n L%d: Var0 = \n", l);
		for(i=0; i<k0; i++) {
			mprint(Var0+i);
			printf("\n");
		}
		printf("\n L%d: Zjk = \n", l);
		mprint(Zjk);
		#endif

		veModel(D, &MUK, Var0, w0, &Gxn, &Fx);
		
		#ifdef _DEBUG
		printf("\n L%d: Gxn = \n", l);
		mprint(&Gxn);
		printf("\n L%d: Fx = \n", l);
		vprint(&Fx);
		#endif

		for (k=0; k<k0; k++) {
			u = k*p;

			double zz = 0;
			double zz_up = 0;
			for (i=0; i<n; i++) {
				*(Zjk->pr+i*k0+k) = (*(w0->pr+k)) * Zjk_up->pr[i] * (*(Gxn.pr+i*k0+k)) / (*(Fx.pr+i));
				zz += *(Zjk->pr+i*k0+k);
				zz_up += Zjk_up->pr[i];
			}
			*(w0->pr+k) = zz/zz_up;

			for (j=0; j<p; j++) {
				getcolvec(D, j, &D_j);
				getcolvec(Zjk, k, &Zjk_k);
				sum_tmp = 0;
				for (i=0; i<n; i++) {
					sum_tmp += (*(Zjk_k.pr+i)) * (*(D_j.pr+i));
				}
				*(MU1.pr+u+j) = sum_tmp / zz;
			}

			mzero(&Ck);
			for (i=0; i<n; i++) {
				getrowvec(D, i, &D_i);
				getrowvec(&MUK, k, &MUK_k);
				for (j=0; j<p; j++) {
					*(cen_D_i.pr+j) = *(D_i.pr+j) - *(MUK_k.pr+j);
				}

				vvMul(&cen_D_i, &cen_D_i, &mtmp);
				
				for (j=0; j<p; j++) {
					for (s=0; s<p; s++) {
						*(Ck.pr+j*p+s) += (*(Zjk->pr+i*k0+k)) * (*(mtmp.pr+j*p+s));
					}
				}
			}
			for (j=0; j<p; j++) {
				for (s=0; s<p; s++) {
					*(Var0[k].pr+j*p+s) = (*(Ck.pr+j*p+s)) / zz;
				}
			}
		}   // for (k...

		mcopy(&MU1, &MUK);

		for (i=0; i<n; i++) {
			*(vtmp.pr+i) = fabs(*(Zjk_k.pr+i) - *(Zjk_temp.pr+i));
		}
		err = vmean(&vtmp);
		vcopy(&Zjk_k, &Zjk_temp);
		
		
    }  // while

	vcopy(w0, w0_t);
	mcopy(&MUK, mean1_x);
	for(i=0; i<k0; i++) {
		mcopy(Var0+i, cov_mat+i);
	}

	for(i=0; i<k0; i++) {
		mdelete(Var0+i);
	} 
	mdelete(&Gxn);
	vdelete(&Fx);
	vdelete(&Zjk_temp);
	mdelete(&MUK);
	mdelete(&MU1);
    vdelete(&D_j);
	vdelete(&Zjk_k);
	mdelete(&Ck);
	vdelete(&D_i);
	vdelete(&MUK_k);
	vdelete(&cen_D_i);
	mdelete(&mtmp);
	vdelete(&vtmp);

    return 1;
}
