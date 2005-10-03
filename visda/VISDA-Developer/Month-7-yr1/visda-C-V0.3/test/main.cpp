#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
//#include <string.h>
#include "mathtool.h"
#include "sampleLoad.h"

#define M 5
#define N 4
#define K 4

#define numSamp 63
#define numGene 10

/* Main */ 
int main()
{
	// matrix X 
    double X[M][N] = 
	{
                  //{0.9501,    0.4447,    0.4103,    0.6038,    0.8462,    0.5028,    0.1509,    0.6449,    0.8385,    0.9568},
                  //  {0.2311,    0.6154,    0.8936,    0.2722,    0.5252,    0.7095,    0.6979,    0.8180,    0.5681,    0.5226},
                  //  {0.6068,    0.7919,    0.0579,    0.1988,    0.2026,    0.4289,    0.3784,    0.6602,    0.3704,    0.8801},
                   // {0.4860,    0.9218,    0.3529,    0.0153,    0.6721,    0.3046,    0.8600,    0.3420,    0.7027,    0.1730},
                   // {0.8913,    0.7382,    0.8132,    0.7468,    0.8381,    0.1897,    0.8537,    0.2897,    0.5466,    0.9797},
                    //{0.7621,    0.1763,    0.0099,    0.4451,    0.0196,    0.1934,    0.5936,    0.3412,    0.4449,    0.2714},
                    //{0.4565,    0.4057,    0.1389,    0.9318,    0.6813,    0.6822,    0.4966,    0.5341,    0.6946,    0.2523},
                    //{0.0185,    0.9355,    0.2028,    0.4660,    0.3795,    0.3028,    0.8998,    0.7271,    0.6213,    0.8757},
                    //{0.8214,    0.9169,    0.1987,    0.4186,    0.8318,    0.5417,    0.8216,    0.3093,    0.7948,    0.7373}
            {1, 0, 0, 1},
            {0, 0, 1, 0},
            {0, -1, 0, 0},
            {0, 0, 0, 0.5},
            {0, 1, 0, 0}

	};

	double cov_X[N][N] =
	//{		{1, 0, 0, 0},
    //        {0, 0, 1, 0},
    //        {0, -1, 0, 0},
    //        {0, 0, 0, 0.5}
	//};
	{{0.9501,    0.8913,    0.8214,    0.9218},
	{0.2311,    0.7621,    0.4447,    0.7382},
    //{0.6068,    0.4565,    0.6154,    0.1763},
	{0.11555,   0.38105,   0.22235,   0.3691},
    {0.4860,    0.0185,    0.7919,    0.4057}};

	matrix MX;

	MX.m = M;
	MX.n = N;
	MX.pr = *X;
	
	matrix cov_MX;
	
	cov_MX.m = N;
	cov_MX.n = N;
	cov_MX.pr = *cov_X;

	matrix XT;
	mnew(&XT, N, M);
	transpose(&MX, &XT);
	mprint(&XT);
	
	matrix invX;
	mnew(&invX, N, N);
	inv(&cov_MX, &invX);
	printf("\n Inverse of X:\n");
	mprint(&invX);
	mdelete(&invX);

	double detv;

	detv = det(&cov_MX);
	printf("\n Determiant of covX: %f \n", detv);

	matrix pcvec_re, pcvec_im;
	matrix pcdata_re, pcdata_im;
    vector pcval_re, pcval_im; 

	mnew(&pcvec_re, N, N);
	mnew(&pcvec_im, N, N);
	
	mnew(&pcdata_re, M, N);
	mnew(&pcdata_im, M, N);

	vnew(&pcval_re, N);
	vnew(&pcval_im, N);


	/**** PCA ****/
	//vePCA(&MX, &pcvec_re, &pcvec_im, &pcval_re, &pcval_im, &pcdata_re, &pcdata_im);

	/**** SubPCA ****/
    veSubPCA(&MX, &cov_MX, &pcvec_re, &pcvec_im, &pcval_re, &pcval_im, &pcdata_re, &pcdata_im);

    printf("\n real part of PCVector of X:\n");
    mprint(&pcvec_re);
    printf("\n imaginary part of PCVector of X:\n");
    mprint(&pcvec_im);

    printf("\n real part of PCValue of X:\n");
    vprint(&pcval_re);
    printf("\n imaginary part of PCValue of X:\n");
    vprint(&pcval_im);

	printf("\n real part of PCData of X:\n");
    mprint(&pcdata_re);
    printf("\n imaginary part of PCData of X:\n");
    mprint(&pcdata_im);



	/**** PCA-PPM ****/
    matrix pcappmvec_re;
    matrix pcappmvec_im;

	mnew(&pcappmvec_re, N, N);
	mnew(&pcappmvec_im, N, N);

	
    //vePCAPPM(&pcdata_re, &pcvec_re, &pcappmvec_re);
	//printf("\n real part of PCAPPMVector of X:\n");
    //mprint(&pcappmvec_re);


	/**** Sub PCA-PPM ****/
    matrix subpcappmvec_re;
    matrix subpcappmvec_im;
	vector Zjk;

	mnew(&subpcappmvec_re, N, N);
	mnew(&subpcappmvec_im, N, N);
	vnew(&Zjk, M);

	vones(&Zjk);

	veSubPCAPPM(&pcdata_re, &pcdata_im, &pcvec_re, &pcvec_im, &pcval_re, &pcval_im, &Zjk, &subpcappmvec_re, &subpcappmvec_im); 
	printf("\n real part of SubPCAPPMVector of X:\n");
    mprint(&subpcappmvec_re);
	printf("\n imaginary part of SubPCAPPMVector of X:\n");
    mprint(&subpcappmvec_im);


	/**** EM ****/
	matrix data; 
	sampleLoad("data.txt", &data);
	mprint(&data);

	matrix mean0_x;
    sampleLoad("mu.txt", &mean0_x);
	mprint(&mean0_x);
		
	double w[K] = { 0.3342,  0.1270,  0.3230,  0.2158};

	matrix mean1_x;
	matrix Var0[K];
	vector w0;
	vector w0_t;
	double vv = 0.5;
	double error = 0.01;
	matrix mZjk;
	vector Zjk_up;

	mnew(&mean1_x, K, data.n);
	for(int i=0; i<K; i++) {
		mnew(Var0+i, data.n, data.n);
		meye(Var0+i);
	}
	w0.l = K;
	w0.pr = w;
	vnew(&w0_t, K);
	vnew(&Zjk_up, data.m);
	vones(&Zjk_up);
	mnew(&mZjk, data.m, K);

	veSubEM(&data, &mean0_x, &w0, vv, error, &Zjk_up,       
			&mean1_x, &w0_t, Var0, &mZjk);

	printf("\n the EM mean:\n");
    mprint(&mean1_x);
	printf("\n the EM w0: \n");
	vprint(&w0_t);
	//printf("\n the EM cov:\n"); 
	//for(i=0; i<K; i++) {
	//	mprint(Var0+i);
	//}
	printf("\n the EM Zjk:\n");
    mprint(&mZjk);

	for(i=0; i<K; i++) {
		mdelete(Var0+i);
	}
	//mdelete(&mean0_x);
	//vdelete(&w0);
	mdelete(&mean1_x);
	vdelete(&w0_t);
	mdelete(&mZjk);


	FILE * pfile;
    pfile = fopen("example.txt", "w");

	double Z[numSamp][numGene];

	for (i=0; i<numSamp; i++) {
		for (int j=0; j<numGene; j++) {
			Z[i][j] = ((double) rand()) / ((double)(RAND_MAX));
		}
	}

	matrix MZ;
	MZ.m = numSamp;
	MZ.n = numGene;
	MZ.pr = *Z;

	double *pi;
	for (i=0; i<(MZ.m); i++) {
		pi = MZ.pr + i*(MZ.n);
        for (int j=0; j<(MZ.n); j++) {
            fprintf(pfile, "%e\t", *(pi+j));
        }
        fprintf(pfile, "\n");
    }

	int labels[numSamp];

	for (i = 0; i< 63; i++) {
    	if (i < 23) {
    		labels[i] = 1;
    	} else if (i < 31) {
    		labels[i] = 2;
    	} else if (i < 43) {
    		labels[i] = 3;
    	} else {
    		labels[i] = 4;
    	}
    }

	for (i=0; i<numSamp; i++) {
	    fprintf(pfile, "%d\t", labels[i]);
    }
	fprintf(pfile, "\n");

	int top_label[4];
	vector snr_sorted;

	
	vnew(&snr_sorted, numGene);

	veSNR(&MZ, labels, 4, 
		  top_label, &snr_sorted);

	
	for (i=0; i<numGene; i++) {
	    fprintf(pfile, "%e\t", snr_sorted.pr[i]);
    }
	fprintf(pfile, "\n");
	fprintf(pfile, "\n %d\t%d\t%d\t%d \n", top_label[0], top_label[1], top_label[2], top_label[3]);
	vdelete(&snr_sorted);
	
	fclose(pfile);
	
	mdelete(&pcvec_re);
	mdelete(&pcvec_im);
	mdelete(&pcdata_re);
	mdelete(&pcdata_im);
	vdelete(&pcval_re);
	vdelete(&pcval_im);
	mdelete(&pcappmvec_re);
	mdelete(&pcappmvec_im);
	mdelete(&subpcappmvec_re);
	mdelete(&subpcappmvec_im);
	vdelete(&Zjk);
	
    return 0;

}
