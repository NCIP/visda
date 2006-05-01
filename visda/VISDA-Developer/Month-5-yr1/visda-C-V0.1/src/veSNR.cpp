#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
//#include <string.h>
#include "mathtool.h"


/*******************************************************************
 Subroutine to perform dimension pre-screening using signal-to-noise ratio (SNR)
   matrix *cov:        the pointer to the covariance matrix
   matrix *inv_cov:    the pointer to the inverse covariance matrix
   matrix *cov_mat:    the pointer to the approximate covariance matrix
                       when singular. If unsingular, it equals to cov
   double *det_cov:    the pointer to determinant

 return value: '1' - successfully exit
               '0' - exit with waring/error
*******************************************************************/
int veSNR(matrix *data, int *label, int dim_num, 
		  int *top_label, vector *snr_sorted)
{

	int i, j, k, t, u;
    int num_class;
	int m, n;
	vector count;
	vector *data_mean;
	vector *variance;
	vector pro;
	vector snr;
	int *new_order;
	int row_T;
	double tmp;

	m = data->m;
	n = data->n;

	num_class = 0;
	for (j=0; j<m; j++) {
		if (num_class < label[j]) {
			num_class = label[j];
		}
	}

	vnew(&count, num_class);
    vnew(&pro, num_class);
    vnew(&snr, n);
	data_mean = new vector[num_class];
	for (i=0; i<num_class; i++) {
		vnew(data_mean+i, n);
	}
	variance = new vector[num_class];
	for (i=0; i<num_class; i++) {
		vnew(variance+i, n);
	}
	new_order = new int[n];

    for (i=0; i<num_class; i++) {
		
		// distingush each class
		row_T = 0;
		for (j=0; j<m; j++) {
			if (label[j] == i+1) {
				row_T ++;
			}
		}
		int *T;
		T = new int[row_T];
		t = 0;
		for (j=0; j<m; j++) {
			if (label[j] == i+1) {
				T[t++] = j;
			}
		}
        pro.pr[i] = ((double) row_T) / ((double) m);

		// compute data_mean for each class
		matrix data_i;
		mnew(&data_i, row_T, n);
		
		for (t=0; t<row_T; t++) {
			u = t*n;
			for (k=0; k<n; k++) {
				data_i.pr[u+k] = data->pr[(T[t])*n+k];
			}
		}

		
		mmean(&data_i, 'c', data_mean+i);
		mvar(&data_i, 'c', variance+i);
	
		delete []T;
		mdelete(&data_i);

    }

	//jj = [];
    for (i=0; i<num_class-1; i++) {
        for (j=i; j<num_class; j++) {
            for (k=0; k<n; k++) {
				tmp = ((variance+i)->pr[k] * pro.pr[i] + (variance+j)->pr[k] * pro.pr[j]) / (pro.pr[i] + pro.pr[j]);
                if (tmp < 1.0000e-3) {
                    //jj = [jj k]; 
					//printf(" Warning: \n ");
                } else {
                    snr.pr[k] += pro.pr[i] * pro.pr[j] * pow(((data_mean+i)->pr[k] - (data_mean+j)->pr[k]), 2) / tmp;  
				}   
			}
		}
	}
      
	sort(&snr, new_order, 'd');

	for (k=0; k<dim_num; k++) {
		top_label[k] = new_order[k];
	}

	vcopy(&snr, snr_sorted);


	//vdelete(&count);
    vdelete(&pro);
    vdelete(&snr);
	for (i=0; i<num_class; i++) {
		vdelete(data_mean+i);
	}
	for (i=0; i<num_class; i++) {
		vdelete(variance+i);
	}
	delete []new_order;

	return 1;

}

    
