/////////////////////////////////////////////////////////////////////
//	File:			veCov.cpp
//	Project:		VISDA
//	Description:	for computing the inverse matrix and determinant
//	Author:			Jiajing Wang
//	Create Date:	July 1st, 2005
//
//	History Log:	use rank() function to determine whether the matrix 
//                    is singular. (August 15, 2005)
/////////////////////////////////////////////////////////////////////

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
//#include <string.h>
#include "mathtool.h"


#define TOLERANCE 1.0e-10  

/*******************************************************************
 Subroutine to compute the inverse matrix and determinant
   matrix *cov:        the pointer to the covariance matrix
   matrix *inv_cov:    the pointer to the inverse covariance matrix
   matrix *cov_mat:    the pointer to the approximate covariance matrix
                       when singular. If unsingular, it equals to cov
   double *det_cov:    the pointer to determinant

 return value: '1' - successfully exit
               '0' - exit with waring/error
*******************************************************************/
int veCov(matrix *cov, matrix *inv_cov, matrix *cov_mat, 
		  double *det_cov)
{
	int i; //, j;
	matrix eigvec_re;
	matrix eigvec_im;
	vector eigval_re;
	vector eigval_im;
    int *eig_order;
	int eig_info;
	int num_v;  // the number of eigenvalue
	int rank_c; 
	double sum_v;
	double factor = 0.02;
	double ass_value;
	double min_real;

    mnew(&eigvec_re, cov->m, cov->n);
    mnew(&eigvec_im, cov->m, cov->n);
	vnew(&eigval_re, cov->n);
	vnew(&eigval_im, cov->n);
    eig_order = new int[cov->n];
    

    // the eigenvector and eigenvalue of covariance matrix
    eig_info = eig(cov, &eigvec_re, &eigvec_im, &eigval_re, &eigval_im);
	#ifdef _DEBUG
	printf("\n original eigenval_re = \n");
	vprint(&eigval_re);
	printf("\n original eigenval_im = \n");
	vprint(&eigval_im);
	printf("\n original eigenvec_re = \n");
	mprint(&eigvec_re);
	printf("\n original eigenvec_im = \n");
	mprint(&eigvec_im);
	#endif

	if (!eig_info) {
		printf(" The eigenvalue computation failed! \n");
		return 0;
		//....
	}
	
	// the rank of covariance matrix
	num_v = cov->n;

	rank_c = rank(cov, TOLERANCE);

	// compute the inverse and determinate
    if (rank_c == num_v) {  // nonsingular
		inv(cov, inv_cov);
		mcopy(cov, cov_mat);
		*det_cov = det(cov);
	
	} else {  // singular
		min_real = pow(10, (((double)-250) / ((double) cov->m)));

		/*for (i=0; i<num_v; i++) {
			if ((*(eigval_re.pr+i) < ZEROTHRESH) || (*(eigval_im.pr+i) != 0)) {
				*(eigval_re.pr+i) = 0;  // ???? keep the real part of complex or not
				*(eigval_im.pr+i) = 0;
			}
		}
		sort(&eigval_re, eig_order, 'd'); */

		for (i=0; i<num_v; i++) {
			// when negtive real eigenvalue, change to absolute value
			//   to ensure all the real eigenvalues are positive
			if ((eigval_re.pr[i] < 0) && (eigval_im.pr[i] == 0)) {
				//eigval_re.pr[i] *= -1;
				eigval_re.pr[i] = 0;
			}
		}

		// sort real eigenvalues descendingly, put complex ones at the end
		sorteig(&eigval_re, &eigval_im, eig_order);

		sum_v = 0;
		for (i=0; i<rank_c; i++) {
			sum_v += *(eigval_re.pr+i);
		}
		//for (i=rank_c; i<num_v; i++) {
		//		*(eigval_re.pr+i) = 0;
		//		*(eigval_im.pr+i) = 0;
		//}

		//sum_v = vsum(&eigval_re);

		ass_value = factor * sum_v / (num_v - rank_c);

		if (ass_value < (0.5 * (*(eigval_re.pr+rank_c)) * (1 - factor))) {
			if (ass_value > min_real) {
				for (i=rank_c; i<num_v; i++) {
					*(eigval_re.pr+i) = ass_value;
				}
				for (i=0; i<rank_c; i++) {
					*(eigval_re.pr+i) *= 1 - factor;
				}
			} else {
				for (i=rank_c; i<num_v; i++) {
					*(eigval_re.pr+i) = min_real;
				}
			}
		} else {
			ass_value = 0.5 * (*(eigval_re.pr+rank_c)) * (1 - factor);
			if (ass_value > min_real) {
				for (i=rank_c; i<num_v; i++) {
					*(eigval_re.pr+i) = ass_value;
				}
				for (i=0; i<rank_c; i++) {
					*(eigval_re.pr+i) = *(eigval_re.pr+i) - ass_value * (num_v - rank_c) * (*(eigval_re.pr+i)) / sum_v;
				}
			} else {
				for (i=rank_c; i<num_v; i++) {
					*(eigval_re.pr+i) = min_real;
				}
			}
		}
        
		matrix eigvec_re_sorted;
		matrix eigvec_re_sorted_t;
		mnew(&eigvec_re_sorted, num_v, num_v);
		mnew(&eigvec_re_sorted_t, num_v, num_v);

		sortcols(eig_order, &eigvec_re, &eigvec_re_sorted);
		transpose(&eigvec_re_sorted, &eigvec_re_sorted_t); 

		#ifdef _DEBUG
		printf("\n modified eigenval_re = \n");
		vprint(&eigval_re);
		printf("\n modified eigenval_im = \n");
		vprint(&eigval_im);
		printf("\n modified eigenvec_re = \n");
		mprint(&eigvec_re_sorted);
		#endif

		matrix inv_eig_vl_s;
		mnew(&inv_eig_vl_s, num_v, num_v);
		for (i=0; i<num_v; i++) {
			*(inv_eig_vl_s.pr + i*num_v + i) = 1 / (*(eigval_re.pr+i));    
		}
		
		matrix tmp;
		mnew(&tmp, num_v, num_v);

		mmMul(&eigvec_re_sorted, &inv_eig_vl_s, &tmp);
		mmMul(&tmp, &eigvec_re_sorted_t, inv_cov);

		matrix diag_eigval;
		mnew(&diag_eigval, num_v, num_v);
		for (i=0; i<num_v; i++) {
			*(diag_eigval.pr + i*num_v + i) = *(eigval_re.pr+i);    
		}
		mmMul(&eigvec_re_sorted, &diag_eigval, &tmp);
		mmMul(&tmp, &eigvec_re_sorted_t, cov_mat);

		*det_cov = 1;
		for (i=0; i<num_v; i++) {
			*det_cov = (*det_cov) * (*(eigval_re.pr+i)); 
		}

		mdelete(&inv_eig_vl_s);
		mdelete(&eigvec_re_sorted);
		mdelete(&eigvec_re_sorted_t);
		mdelete(&tmp);
		mdelete(&diag_eigval);

	}

	#ifdef _DEBUG
	printf("\n rank = %d \n", rank_c);
	printf("\n det_cov = %e \n", *det_cov);
	printf("\n inv_cov = \n");
	mprint(inv_cov);
	printf("\n cov_mat = \n");
	mprint(cov_mat);
	#endif

    mdelete(&eigvec_re);
	mdelete(&eigvec_im);
	vdelete(&eigval_re);
	vdelete(&eigval_im);
    delete []eig_order;

    return 1;
}
