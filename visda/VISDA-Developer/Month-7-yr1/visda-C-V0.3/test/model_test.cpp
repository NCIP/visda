#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
//#include <string.h>
#include "mathtool.h"
#include "sampleLoad.h"

#define numSamp 200
#define numGene 8

/* Main */ 
int main()
{
	matrix MX;
	mnew(&MX, 200, 8);
	sampleLoad("data_200x8.txt", &MX);
	printf("data=\n");
	mprint(&MX);

	double mean[2][8]=
	{{4.427227e-001,	7.671556e-001,	-9.523772e-001,	3.867558e-001,	-7.916976e-001,	1.165247e-001,	-1.261666e-001,	8.550054e-002},	
	{-2.383899e-001,	-4.130850e-001,	5.128200e-001,	-2.082537e-001,	4.263000e-001,	-6.274427e-002,	6.793605e-002,	-4.603889e-002}};
	matrix MUK;
	mnew(&MUK, 2, 8);
	MUK.pr = *mean;

	matrix *Var0;
	Var0 = new matrix[2];
	for(int i=0; i<2; i++) {
		mnew(Var0+i, 8, 8);
	}
	sampleLoad("var0.txt", Var0);
	printf("var0=\n");
	mprint(Var0);
	sampleLoad("var1.txt", Var0+1);
	printf("var1=\n");
	mprint(Var0+1);
	
	double W[] = {3.500007e-001,	6.499993e-001};
	vector w0;
	vnew(&w0, 2);
	w0.pr = W;

	matrix Gxn;
	mnew(&Gxn, 200, 2);
	vector Fx;
	vnew(&Fx, 200);
	veModel(&MX, &MUK, Var0, &w0, &Gxn, &Fx);

	printf("Gxn=\n");
	mprint(&Gxn);
	printf("Fx=\n");
	vprint(&Fx);

	matrix tmp;
	mnew(&tmp, 1, 8);
	matrix tmp2;
	mnew(&tmp2, 1, 1);
	matrix cen_Dj;
	mnew(&cen_Dj, 1, 8);
	matrix cen_Dj_t;
	mnew(&cen_Dj_t, 8, 1);

	for(i=0; i<8; i++) {
			*(cen_Dj.pr+i) = *(MX.pr+0+i) - *(MUK.pr+8+i);
	}
	transpose(&cen_Dj, &cen_Dj_t);
	printf("\ncen_Dj=\n");
	mprint(&cen_Dj);
	mprint(&cen_Dj_t);

	matrix inv_Var1;
	mnew(&inv_Var1, 8, 8);
	sampleLoad("inv_var1.txt", &inv_Var1);
	mprint(&inv_Var1);

	mmMul(&cen_Dj, &inv_Var1, &tmp);
	printf("\ntmp=\n");
	mprint(&tmp);
	mmMul(&tmp, &cen_Dj_t, &tmp2);
	printf("\ntmp2=\n");
	mprint(&tmp2);
	double val = *(tmp2.pr);
	printf("\nval=%e\n", val);

	mdelete(&MX);
	mdelete(&MUK);
	mdelete(Var0);
	mdelete(Var0+1);
	vdelete(&w0);
	mdelete(&Gxn);
	vdelete(&Fx);

	return 0;

}
