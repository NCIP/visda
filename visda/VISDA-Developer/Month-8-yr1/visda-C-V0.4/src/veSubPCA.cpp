/////////////////////////////////////////////////////////////////////
//	File:			veSubPCA.cpp
//	Project:		VISDA
//	Description:	for sub-level PCA computing
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


/*******************************************************************
 Subroutine to do the Sub-Level PCA
   matrix *X:      the pointer to the matrix data
   matrix *cov_X:  the pointer to the estimated covariance matrix
   matrix *pcavec_re:  the pointer to a matrix containing the real part 
                       of eigenvector of the covariance matrix of X
   matrix *pcavec_im:  the pointer to a matrix containing the imaginary part 
                       of eigenvector of the covariance matrix of X
   vector *pcaval_re:  the pointer to a vector containing the real part
                       of eigenvalues
   vector *pcaval_im:  the pointer to a vector containing the imaginary part
                       of eigenvalues                    
   matrix *pcadata_re: the pointer to the new matrix containing the real part
                       of data projected onto the space defined by the PCA
   matrix *pcadata_im: the pointer to the new matrix containing the imaginary part
                       of data projected onto the space defined by the PCA

 return value: '1' - successfully exit
               '0' - exit with waring/error
*******************************************************************/
int veSubPCA(matrix *X, matrix *cov_X, matrix *pcavec_re, matrix *pcavec_im, 
		  vector *pcaval_re, vector *pcaval_im, matrix *pcadata_re, matrix *pcadata_im)
{
	matrix eigvec_re;
	matrix eigvec_im;
    int *pca_order;
	int eig_info;

	mnew(&eigvec_re, X->n, X->n);
    mnew(&eigvec_im, X->n, X->n);
    pca_order = new int[X->n];
    
    // the eigenvector and eigenvalue of covariance matrix
    eig_info = eig(cov_X, &eigvec_re, &eigvec_im, pcaval_re, pcaval_im);

	/* force to exit....*/
	if (!eig_info) {
		printf(" The eigenvalue computation failed! \n");
		return 0;
		//....
	}	

    // sort the eigenvalue by descending order and reorder the eigenvector
	int flag;

	flag = sorteig(pcaval_re, pcaval_im, pca_order);

	if (flag == 2) {

		sortcols(pca_order, &eigvec_re, pcavec_re);
		// pca_data = X * pca_vec
		mmMul(X, pcavec_re, pcadata_re);

	} if (flag == 1) {

		sortcols(pca_order, &eigvec_re, pcavec_re);
		sortcols(pca_order, &eigvec_im, pcavec_im);

		// pca_data = X * pca_vec
		matrix X_im;
		mnew(&X_im, X->m, X->n);
		cmmMul(X, &X_im, pcavec_re, pcavec_im, pcadata_re, pcadata_im);

		mdelete(&X_im);

    }

	mdelete(&eigvec_re);
	mdelete(&eigvec_im);
    delete []pca_order;

    return 1;
}
