#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
//#include <string.h>

#include "mathtool.h"

/*******************************************************************
  Part of Sub-level Kurtosis Calculate:
     sum(Zjk*ones(1,p).*(data_proj))./sum(Zjk)
*******************************************************************/
void kurtmodel(matrix *mZjk, double sumZjk, matrix *data, vector *meanZjk)
{
	int i;
	matrix Mt;

	mnew(&Mt, data->m, data->n);
	mmDotMul(mZjk, data, &Mt);
	msum(&Mt, 'c', meanZjk);
	for (i=0; i<(meanZjk->l); i++) {
		*(meanZjk->pr + i) /= sumZjk;
	};
	mdelete(&Mt);
}


void ckurtmodel(matrix *mZjk, double sumZjk,
				matrix *data_re, matrix *data_im, 
				vector *meanZjk_re, vector *meanZjk_im)
{
	int i;
	matrix Mt_re;
	matrix Mt_im;
	matrix mZjk_im;

	mnew(&Mt_re, data_re->m, data_re->n);
	mnew(&Mt_im, data_im->m, data_im->n);
	mnew(&mZjk_im, mZjk->m, mZjk->n);

	cmmDotMul(mZjk, &mZjk_im, data_re, data_im, &Mt_re, &Mt_im);
	msum(&Mt_re, 'c', meanZjk_re);
	msum(&Mt_im, 'c', meanZjk_im);
	for (i=0; i<(meanZjk_re->l); i++) {
		*(meanZjk_re->pr + i) /= sumZjk;
		*(meanZjk_im->pr + i) /= sumZjk;
	};
	mdelete(&Mt_re);
	mdelete(&Mt_im);
	mdelete(&mZjk_im);
}


/*******************************************************************
 Subroutine to do the Sub-Level PCA-PPM
   matrix *pcadata_re: the pointer to the new matrix containing the real part of data
                       projected onto the space defined by the PCA
   matrix *pcadata_re: the pointer to the new matrix containing the imaginary part of data
                       projected onto the space defined by the PCA
   matrix *pcavec_re:  the pointer to a matrix containing the real part 
                       of eigenvector
   matrix *pcavec_im:  the pointer to a matrix containing the imaginary part 
                       of eigenvector
   vector *pcaval_re:  the pointer to a vector containing the real part
                       of eigenvalues
   vector *pcaval_im:  the pointer to a vector containing the imaginary part
                       of eigenvalues  
   vector *Zjk:  the pointer to a vector containing the Zjk values		   
   matrix *subpcappmvec_re:  the pointer to a matrix containing the real part of
                             sorted eigenvectors by sub kurtosis rank
   matrix *subpcappmvec_re:  the pointer to a matrix containing the imaginary part of
                             sorted eigenvectors by sub kurtosis rank
   
 return value: '1' - successfully exit
               '0' - exit with waring/error
*******************************************************************/
int veSubPCAPPM(matrix *pcadata_re, matrix *pcadata_im,
				matrix *pcavec_re, matrix *pcavec_im,
				vector *pcaval_re, vector *pcaval_im, 
				vector *Zjk,
				matrix *subpcappmvec_re, matrix *subpcappmvec_im)
{
    int m, n;
	int i, j, u=0, v=0;
	vector X1n, Xm1;
    matrix mZjk; 
    matrix M1;
    matrix data_pow2;
	matrix data_pow4;
    vector V1;
	vector V2;
	vector V4;
    vector kurt;
    int* kurt_id;
	double sumZjk;
	double cen_data;
	bool allreal = true;

	m=pcadata_re->m;
	n=pcadata_im->n;

	vnew(&X1n, n);
	vnew(&Xm1, m);
	mnew(&mZjk, m, n);
	mnew(&M1, m, n);
	mnew(&data_pow2, m, n);
	mnew(&data_pow4, m, n);
    vnew(&V1, n);
    vnew(&V2, n);
    vnew(&V4, n);
    vnew(&kurt, n);
    kurt_id = new int[n];


	vector V1_im;
	vector Xm1_im;
	matrix M1_im;
	double cen_data_im;
	matrix data_pow2_im;
	matrix data_pow4_im;
	vector V2_im;
	vector V4_im;
	vector kurt_im;

	vnew(&Xm1_im, m);
	mnew(&M1_im, m, n);
	mnew(&data_pow2_im, m, n);
	mnew(&data_pow4_im, m, n);
	vnew(&V1_im, n);
	vnew(&V2_im, n);
	vnew(&V4_im, n);
	vnew(&kurt_im, n);
    
	// whether complex eigenvalue exists
	for (i=0; i<n; i++) {
		if (*(pcaval_im->pr+i) != 0) {
			allreal = false;
			break;
		}
	}

    // center the data set its means
	// data_proj = data_proj - ones(n,1)*(sum(Zjk*ones(1,p).*(data_proj))./sum(Zjk));
    vones(&X1n);
	vones(&Xm1);

	vvMul(Zjk, &X1n, &mZjk);
	sumZjk = vsum(Zjk);

	if (allreal==true) {
		kurtmodel(&mZjk, sumZjk, pcadata_re, &V1);
		vvMul(&Xm1, &V1, &M1);
	
		for (i=0; i<m*n; i++) {
			cen_data = *(pcadata_re->pr + i) - *(M1.pr + i);
			//*(data->pr + i) = cen_data;
			*(data_pow2.pr+i) = pow(cen_data, 2);
			*(data_pow4.pr+i) = pow(cen_data, 4);
		}

		// calculate kurtosis : kurt(y) = E{y^4}-3(E{y^2})^2
		//kurt = sum(Zjk*ones(1,p).*(data_proj.^4))./sum(Zjk)...
		//- 3*(sum(Zjk*ones(1,p).*(data_proj.^2))./sum(Zjk)).^2; %Not normalized Kurtosis
		kurtmodel(&mZjk, sumZjk, &data_pow2, &V2);
		kurtmodel(&mZjk, sumZjk, &data_pow4, &V4);

		for (j=0; j<n; j++) {
			*(kurt.pr+j) = *(V4.pr+j) - 3*(pow(*(V2.pr+j), 2));
		}
	
	} else {

		ckurtmodel(&mZjk, sumZjk, pcadata_re, pcadata_im, &V1, &V1_im);
		cvvMul(&Xm1, &Xm1_im, &V1, &V1_im, &M1, &M1_im);
	
		for (i=0; i<m*n; i++) {
			cen_data = *(pcadata_re->pr + i) - *(M1.pr + i);
			cen_data_im = *(pcadata_im->pr + i) - *(M1_im.pr + i);
			//*(data->pr + i) = cen_data;
			*(data_pow2.pr+i) = pow(cen_data, 2) - pow(cen_data_im, 2);
			*(data_pow2_im.pr+i) = 2 * cen_data * cen_data_im;
			*(data_pow4.pr+i) = pow(*(data_pow2.pr+i), 2) - pow(*(data_pow2_im.pr+i), 2);
			*(data_pow4_im.pr+i) = 2 * (*(data_pow2.pr+i)) * (*(data_pow2_im.pr+i));
		}

		// calculate kurtosis : kurt(y) = E{y^4}-3(E{y^2})^2
		//kurt = sum(Zjk*ones(1,p).*(data_proj.^4))./sum(Zjk)...
		//- 3*(sum(Zjk*ones(1,p).*(data_proj.^2))./sum(Zjk)).^2; %Not normalized Kurtosis
		ckurtmodel(&mZjk, sumZjk, &data_pow2, &data_pow2_im, &V2, &V2_im);
		ckurtmodel(&mZjk, sumZjk, &data_pow4, &data_pow4_im, &V4, &V4_im);

		for (j=0; j<n; j++) {
			*(kurt.pr+j) = *(V4.pr+j) - 3*(pow(*(V2.pr+j), 2) - pow(*(V2_im.pr+j), 2));
			*(kurt_im.pr+j) = *(V4_im.pr+j) - 3 * 2 * (*(V2.pr+j)) * (*(V2_im.pr+j));
		}

	}


    

    // sort kurt value in ascending order and reorder the pca_vec
	int realeig_num;
	int *realeig_id;
	int *compeig_id;
	vector realkurt;
    int *real_order;

	realeig_num = n;
	for (i=0; i<n; i++) {
		if (*(pcaval_im->pr+i) != 0) {
			realeig_num--;
		}
	}
	vnew(&realkurt, realeig_num);

	realeig_id = new int[realeig_num];
	compeig_id = new int[n-realeig_num];
	real_order = new int[realeig_num];

	for (i=0; i<n; i++) {
		if (*(pcaval_im->pr+i) == 0) {
			realeig_id[u] = i;
			*(realkurt.pr+u) = *(kurt.pr+i);
			u++;
		} else {	
			compeig_id[v] = i;
			v++;
		}
	}

    sort(&realkurt, real_order, 'a');
	int *tmp;

	tmp = new int[realeig_num];
	for (i=0; i<realeig_num; i++) {
		tmp[i] = realeig_id[i];
	}
	for (i=0; i<realeig_num; i++) {
		realeig_id[i] = tmp[real_order[i]];
	}
	delete []tmp;

	vector kurt0;
	vector kurt0_im;
	vnew(&kurt0, kurt.l);
	vcopy(&kurt, &kurt0);
	vnew(&kurt0_im, kurt.l);
	vcopy(&kurt_im, &kurt0_im);
	for (i=0; i<realeig_num; i++) {
		kurt_id[i] = realeig_id[i];
		*(kurt.pr+i) = *(realkurt.pr+i);
		*(kurt_im.pr+i) = 0;
	}
	for (i=0; i<n-realeig_num; i++) {
		kurt_id[i+realeig_num] = compeig_id[i];
		*(kurt.pr+i+realeig_num) = *(kurt0.pr + compeig_id[i]);
		*(kurt_im.pr+i+realeig_num) = *(kurt0_im.pr + compeig_id[i]);
	}

	//printf(" the real part of kurt value is : \n");
    //vprint(&kurt);
	//printf(" the imaginary part of kurt value is : \n");
    //vprint(&kurt_im);
	//printf(" the kurt id is : \n");
    //for (i=0; i<n; i++) {
    //   printf("%d\t", kurt_id[i]);
    //}
    sortcols(kurt_id, pcavec_re, subpcappmvec_re);
    sortcols(kurt_id, pcavec_im, subpcappmvec_im);


	vdelete(&X1n);
	vdelete(&Xm1);
	mdelete(&mZjk);
	mdelete(&M1);
	mdelete(&data_pow2);
	mdelete(&data_pow4);
    vdelete(&V1);
    vdelete(&V2);
    vdelete(&V4);
    vdelete(&kurt);
    vdelete(&kurt0);
    delete []kurt_id;
    vdelete(&realkurt);
	delete []realeig_id;
	delete []compeig_id;
	delete []real_order;
	vdelete(&Xm1_im);
	mdelete(&M1_im);
	mdelete(&data_pow2_im);
	mdelete(&data_pow4_im);
    vdelete(&V1_im);
    vdelete(&V2_im);
    vdelete(&V4_im);
    vdelete(&kurt_im);
    vdelete(&kurt0_im);

    return 1;
}
