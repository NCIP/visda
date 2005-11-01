/////////////////////////////////////////////////////////////////////
//	File:			CFMCore.cpp
//	Project:		VISDA
//	Description:	to integrate EM algorithm C++ functions with Java program 
//	Author:			Jiajing Wang
//	Create Date:	July 15, 2005
//
//	History Log:	
//  (October 18, 2005):	add 'mdl' interface and MDL calculation codes
//
/////////////////////////////////////////////////////////////////////

#include <jni.h>         /* Java Native Interface headers */
#include "CFMCore.h"   /* Auto-generated header created by javah -jni */
#include "mathtool.h" 

// for veSubEM()
JNIEXPORT jint JNICALL Java_edu_vt_cbil_visda_comp_CFMCore_veSubEM
  (JNIEnv *env, jobject obj, jint n, jint p, jint k, jdoubleArray data, 
   jdoubleArray mean0_x, jdoubleArray w0, jdouble vv, jdouble error, jdoubleArray Zjk_up,
   jdoubleArray mean1_x, jdoubleArray w0_t, jdoubleArray cov_mat, jdoubleArray Zjk, jdoubleArray mdl)
{

	jboolean isCopy;
	matrix mdata;
	matrix mmean0_x;
	vector vw0;
	matrix mmean1_x;
	vector vw0_t;
	matrix mcov_mat;
	matrix *mcov_mat_array;
	vector vZjk_up;
	matrix mZjk;
	int i, j, kk, u, t;
	jint result;
	double *pmdl;

	mdata.m = n;
	mdata.n = p;
	mdata.pr = (env)->GetDoubleArrayElements(data, &isCopy);
	
	mmean0_x.m = k;
	mmean0_x.n = p;
	mmean0_x.pr = (env)->GetDoubleArrayElements(mean0_x, &isCopy);

	vw0.l = k;
	vw0.pr = (env)->GetDoubleArrayElements(w0, &isCopy);

	mmean1_x.m = k;
	mmean1_x.n = p;
	mmean1_x.pr = (env)->GetDoubleArrayElements(mean1_x, &isCopy);

	vw0_t.l = k;
	vw0_t.pr = (env)->GetDoubleArrayElements(w0_t, &isCopy);

	u = p * k;
	mcov_mat.m = p;
	mcov_mat.n = u;
	mcov_mat.pr = (env)->GetDoubleArrayElements(cov_mat, &isCopy);

	mcov_mat_array = new matrix[k];
	for(kk=0; kk<k; kk++) {
		mnew(mcov_mat_array + kk, p, p);
		t = kk * p;
		for(i=0; i<p; i++) {
			for(j=0; j<p; j++) {
				(mcov_mat_array+kk)->pr[i*p+j] = mcov_mat.pr[i*u+t+j];
			}
		}
	}

	vZjk_up.l = n;
	vZjk_up.pr = (env)->GetDoubleArrayElements(Zjk_up, &isCopy);

	mZjk.m = n;
	mZjk.n = k;
	mZjk.pr = (env)->GetDoubleArrayElements(Zjk, &isCopy);
	
	pmdl = (env)->GetDoubleArrayElements(mdl, &isCopy);

	//call c function
	result = veSubEM(&mdata, &mmean0_x, &vw0, vv, error, &vZjk_up,      
					 &mmean1_x, &vw0_t, mcov_mat_array, &mZjk, pmdl);

	for(kk=0; kk<k; kk++) {
		t = kk * p;
		for(i=0; i<p; i++) {
			for(j=0; j<p; j++) {
				mcov_mat.pr[i*u+t+j] = (mcov_mat_array+kk)->pr[i*p+j];
			}
		}
	}

	(env)->ReleaseDoubleArrayElements(data, mdata.pr, 0);
	(env)->ReleaseDoubleArrayElements(mean0_x, mmean0_x.pr, 0);
	(env)->ReleaseDoubleArrayElements(w0, vw0.pr, 0);
	(env)->ReleaseDoubleArrayElements(mean1_x, mmean1_x.pr, 0);
	(env)->ReleaseDoubleArrayElements(w0_t, vw0_t.pr, 0);
	(env)->ReleaseDoubleArrayElements(cov_mat, mcov_mat.pr, 0);
	(env)->ReleaseDoubleArrayElements(Zjk_up, vZjk_up.pr, 0);
	(env)->ReleaseDoubleArrayElements(Zjk, mZjk.pr, 0);
	(env)->ReleaseDoubleArrayElements(mdl, pmdl, 0);


	for(kk=0; kk<k; kk++) {
		mdelete(mcov_mat_array + kk);
	}

	return result;
	
}
	
