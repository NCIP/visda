/////////////////////////////////////////////////////////////////////
//	File:			vePCAPPM.cpp
//	Project:		VISDA
//	Description:	for top-level PCAPPM computing
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
 Subroutine to do the Top-Level PCA-PPM
   matrix *pca_data: the pointer to the new matrix containing the data
                     projected onto the space defined by the PCA
   matrix *pca_vec:  the pointer to a matrix containing the eigenvector
   matrix *pcappm_vec:  the pointer to a matrix containing the sorted
                        eigenvectors by kurtosis rank
   
 return value: '1' - successfully exit
               '0' - exit with waring/error
*******************************************************************/
int vePCAPPM(matrix *pca_data, matrix *pca_vec, matrix *pcappm_vec)
{
    int m, n;
	int i, j, u;
    vector mean_data;  // the mean of each column of pca_data
    matrix data_pow2; 
    matrix data_pow4;
    vector mean_pow2;
    vector mean_pow4;
    double cen_data;
    vector kurt;
    int *kurt_id;

	m=pca_data->m;
	n=pca_data->n;

    vnew(&mean_data, n);
	mnew(&data_pow2, m, n);
	mnew(&data_pow4, m, n);
    vnew(&mean_pow2, n);
    vnew(&mean_pow4, n);
    vnew(&kurt, n);

    kurt_id = new int[n];
    
    // center the data set its mean
    mmean(pca_data, 'c', &mean_data);

    for (i=0; i<m; i++) {
		u = i*n;
		for (j=0; j<n; j++) {
			cen_data = *(pca_data->pr+u+j) - *(mean_data.pr+j);
			*(data_pow2.pr+u+j) = pow(cen_data, 2);
			*(data_pow4.pr+u+j) = pow(cen_data, 4);
		}
	}

    // calculate kurtosis : kurt(y) = E{y^4}-3(E{y^2})^2
    mmean(&data_pow4, 'c', &mean_pow4);
    mmean(&data_pow2, 'c', &mean_pow2);

    for (j=0; j<n; j++) {
       *(kurt.pr+j) = *(mean_pow4.pr+j) - 3*(pow(*(mean_pow2.pr+j), 2));
    }

    // sort kurt value in ascending order and reorder the pca_vec
    sort(&kurt, kurt_id, 'a');
	//printf(" the kurt value is : \n");
    //vprint(&kurt);
	//printf(" the kurt id is : \n");
    //for (i=0; i<n; i++) {
    //   printf("%d\t", kurt_id[i]);
    //}
    sortcols(kurt_id, pca_vec, pcappm_vec);

    vdelete(&mean_data);
    mdelete(&data_pow2);
    mdelete(&data_pow4);
    vdelete(&mean_pow2);
    vdelete(&mean_pow4);
    vdelete(&kurt);
    delete []kurt_id;
    
    return 1;
}
