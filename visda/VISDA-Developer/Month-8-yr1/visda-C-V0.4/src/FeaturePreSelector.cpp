/////////////////////////////////////////////////////////////////////
//	File:			FeaturePreSelector.cpp
//	Project:		VISDA
//	Description:	to integrate feature selection C++ functions with Java program
//	Author:			Jiajing Wang
//	Create Date:	July 15, 2005
//
//	History Log:	
/////////////////////////////////////////////////////////////////////

#include <jni.h>         /* Java Native Interface headers */
#include "FeaturePreSelector.h"   /* Auto-generated header created by javah -jni */
#include "mathtool.h" 

// for veSNR()
JNIEXPORT jint JNICALL Java_edu_vt_cbil_visda_comp_FeaturePreSelector_veSNR
  (JNIEnv *env, jobject obj, jint m, jint n, jdoubleArray data, jintArray label, jint dim_num, 
   jintArray top_label, jdoubleArray sorted_snr, jint k, jdoubleArray mean, jdoubleArray var)
{

	jboolean isCopy;
	matrix mdata;
	jint *jlabel;
	jint *jtop_label;
	vector vsorted_snr;
	vector vmean;
	vector vvar;
	jint result;
	int i;
	int *clabel;
	int *ctop_label;

	mdata.m = m;
	mdata.n = n;
	mdata.pr = (env)->GetDoubleArrayElements(data, &isCopy);
	
	jlabel = (env)->GetIntArrayElements(label, &isCopy);

	jtop_label = (env)->GetIntArrayElements(top_label, &isCopy);

	vsorted_snr.l = n;
	vsorted_snr.pr = (env)->GetDoubleArrayElements(sorted_snr, &isCopy);

	vmean.l = n*k;
	vmean.pr = (env)->GetDoubleArrayElements(mean, &isCopy);

	vvar.l = n*k;
	vvar.pr = (env)->GetDoubleArrayElements(var, &isCopy);

	clabel = new int[m];
	ctop_label = new int[dim_num];
	for (i=0; i<m; i++) {
		clabel[i] = (int) jlabel[i];
	}
	for (i=0; i<dim_num; i++) {
		ctop_label[i] = (int) jtop_label[i];
	}
	
	//call c function
	result = veSNR(&mdata, clabel, dim_num,       
				   ctop_label, &vsorted_snr, &vmean, &vvar);

	for (i=0; i<dim_num; i++) {
		jtop_label[i] = (jint) ctop_label[i];
	}

	(env)->ReleaseDoubleArrayElements(data, mdata.pr, 0);
	(env)->ReleaseIntArrayElements(label, jlabel, 0);
	(env)->ReleaseIntArrayElements(top_label, jtop_label, 0);
	(env)->ReleaseDoubleArrayElements(sorted_snr, vsorted_snr.pr, 0);
	(env)->ReleaseDoubleArrayElements(mean, vmean.pr, 0);
	(env)->ReleaseDoubleArrayElements(var, vvar.pr, 0);

	delete []clabel;
	delete []ctop_label;

	return result;
	
}
	
