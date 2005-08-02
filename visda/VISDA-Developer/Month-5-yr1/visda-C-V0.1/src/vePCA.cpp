#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
//#include <string.h>
#include "mathtool.h"


/*******************************************************************
 Subroutine to sort eigenvalues.
 Real and complex values will be seperated. And real value will be
 sorted by descending order.
   vector *eigval_re:  the pointer to a vector containing the real 
                       part of eigenvalues
   vector *eigval_re:  the pointer to a vector containing the real 
                       part of eigenvalues
   int *pca_order:     the pointer to a integer array containing the 
                       new order of original eigenvalue index
 return value: '2' - successfully exit, and all the eigenvalues are real
               '1' - successfully exit, and some eigenvalues are complex
               '0' - less than 2 eigenvalues are real, exit with waring/error
*******************************************************************/
int sorteig(vector *eigval_re, vector *eigval_im, int *pca_order)
{
	int i;
	int eig_num;  // the number of eigenvalue
	int realeig_num;  // the number of real eigenvalue
	int u=0, v=0;
	vector pcaval_re;
	vector pcaval_im;
	int result;

	eig_num = eigval_re->l;
	vnew(&pcaval_re, eig_num);
	vnew(&pcaval_im, eig_num);

	realeig_num = eig_num;
	for (i=0; i<eig_num; i++) {
		if (*(eigval_im->pr+i) != 0) {
			realeig_num--;
		}
	};

	if (realeig_num == eig_num) {
		printf(" All the eigenvalues are real.\n");
		sort(eigval_re, pca_order, 'd');
		result = 2;

	} else if (realeig_num < 2) {
		printf(" Warning: the number of real eigenvalues is less than TWO!\n");
		result = 0;

	} else {
		printf(" Complex eigenvalues exist, and there are at least TWO real eigenvalues!\n");
		vector realeig;
		int *realeig_id;
		int *compeig_id;
		int *new_order;

		vnew(&realeig, realeig_num);
		realeig_id = new int[realeig_num];
		compeig_id = new int[eig_num-realeig_num];
		new_order = new int[realeig_num];

		for (i=0; i<eig_num; i++) {
			if (*(eigval_im->pr+i) == 0) {
				realeig_id[u] = i;
				*(realeig.pr+u) = *(eigval_re->pr+i);
				u++;
			} else {	
				compeig_id[v] = i;
				v++;
			}
		}
	
		sort(&realeig, new_order, 'd');

		int *tmp;

		tmp = new int[realeig_num];
		for (i=0; i<realeig_num; i++) {
			tmp[i] = realeig_id[i];
		}
		for (i=0; i<realeig_num; i++) {
			realeig_id[i] = tmp[new_order[i]];
		}
		delete []tmp;

		vcopy(eigval_re, &pcaval_re);
		vcopy(eigval_im, &pcaval_im);
		for (i=0; i<realeig_num; i++) {
			pca_order[i] = realeig_id[i];
			*(eigval_re->pr+i) = *(realeig.pr+i);
			*(eigval_im->pr+i) = 0;
		}
		for (i=0; i<eig_num-realeig_num; i++) {
			pca_order[i+realeig_num] = compeig_id[i];
			*(eigval_re->pr+i+realeig_num) = *(pcaval_re.pr + compeig_id[i]);
			*(eigval_im->pr+i+realeig_num) = *(pcaval_im.pr + compeig_id[i]);
		}
		
		vdelete(&realeig);
		delete []realeig_id;
		delete []compeig_id;
		delete []new_order;

		result = 1;

    }

	vdelete(&pcaval_re);
	vdelete(&pcaval_im);

	return result;

}


/*******************************************************************
 Subroutine to do the Top-Level PCA
   matrix *X:      the pointer to the matrix data
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
int vePCA(matrix *X, matrix *pcavec_re, matrix *pcavec_im, 
		  vector *pcaval_re, vector *pcaval_im, 
		  matrix *pcadata_re, matrix *pcadata_im)
{
    matrix cov_X; // the covariance matrix
	matrix eigvec_re;
	matrix eigvec_im;
    int *pca_order;
	int eig_info;

    mnew(&cov_X, X->n, X->n);
    mnew(&eigvec_re, X->n, X->n);
    mnew(&eigvec_im, X->n, X->n);
    pca_order = new int[X->n];
    

    // the covariance matrix of X
    mcovar(X, &cov_X);

    // the eigenvector and eigenvalue of covariance matrix
    eig_info = eig(&cov_X, &eigvec_re, &eigvec_im, pcaval_re, pcaval_im);

	if (!eig_info) {
		printf(" The eigenvalue computation failed! \n");
		return 0;
		//....
	}	

    // sort the eigenvalue by descending order and reorder the eigenvector
	int flag;

	flag = sorteig(pcaval_re, pcaval_im, pca_order);

	if (flag == 2) {  // no imaginary part

		sortcols(pca_order, &eigvec_re, pcavec_re);
		// pca_data = X * pca_vec
		mmMul(X, pcavec_re, pcadata_re);

	} else if (flag == 1) {  // has imaginary part

		sortcols(pca_order, &eigvec_re, pcavec_re);
		sortcols(pca_order, &eigvec_im, pcavec_im);

		// pca_data = X * pca_vec
		matrix X_im;
		mnew(&X_im, X->m, X->n);
		cmmMul(X, &X_im, pcavec_re, pcavec_im, pcadata_re, pcadata_im);

		mdelete(&X_im);

    }

    
    mdelete(&cov_X);
	mdelete(&eigvec_re);
	mdelete(&eigvec_im);
    delete []pca_order;

    return 1;
}
