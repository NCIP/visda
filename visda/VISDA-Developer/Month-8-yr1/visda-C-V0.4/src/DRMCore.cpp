/////////////////////////////////////////////////////////////////////
//	File:			DRMCore.cpp
//	Project:		VISDA
//	Description:	to integrate dimenstion reduction C++ functions with Java program
//	Author:			Jiajing Wang
//	Create Date:	July 15, 2005
//
//	History Log:	
/////////////////////////////////////////////////////////////////////

#include <jni.h>         /* Java Native Interface headers */
#include "DRMCore.h"   /* Auto-generated header created by javah -jni */
#include "mathtool.h" 

// for vePCA()
JNIEXPORT jint JNICALL Java_edu_vt_cbil_visda_comp_DRMCore_vePCA
  (JNIEnv *env, jobject obj, jint m, jint n, jdoubleArray data, 
   jdoubleArray pcvec_re, jdoubleArray pcvec_im, 
   jdoubleArray pcval_re, jdoubleArray pcval_im, 
   jdoubleArray pcdata_re, jdoubleArray pcdata_im)
{

	jboolean isCopy;
	matrix mdata;
	vector vpcval_re;
	vector vpcval_im;
	matrix mpcvec_re;
	matrix mpcvec_im;
	matrix mpcdata_re;
	matrix mpcdata_im;
	jint result;

	mdata.m = m;
	mdata.n = n;
	mdata.pr = (env)->GetDoubleArrayElements(data, &isCopy);
	
	vpcval_re.l = n;
	vpcval_re.pr = (env)->GetDoubleArrayElements(pcval_re, &isCopy);

	vpcval_im.l = n;
	vpcval_im.pr = (env)->GetDoubleArrayElements(pcval_im, &isCopy);

	mpcvec_re.m = n;
	mpcvec_re.n = n;
	mpcvec_re.pr = (env)->GetDoubleArrayElements(pcvec_re, &isCopy);

	mpcvec_im.m = n;
	mpcvec_im.n = n;
	mpcvec_im.pr = (env)->GetDoubleArrayElements(pcvec_im, &isCopy);

	mpcdata_re.m = m;
	mpcdata_re.n = n;
	mpcdata_re.pr = (env)->GetDoubleArrayElements(pcdata_re, &isCopy);

	mpcdata_im.m = m;
	mpcdata_im.n = n;
	mpcdata_im.pr = (env)->GetDoubleArrayElements(pcdata_im, &isCopy);
	
	//call c function
	result = vePCA(&mdata, &mpcvec_re, &mpcvec_im, &vpcval_re, &vpcval_im, &mpcdata_re, &mpcdata_im);

	(env)->ReleaseDoubleArrayElements(data, mdata.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcval_re, vpcval_re.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcval_im, vpcval_im.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcvec_re, mpcvec_re.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcvec_im, mpcvec_im.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcdata_re, mpcdata_re.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcdata_im, mpcdata_im.pr, 0);

	return result;
	
}
	

// for veSubPCA()
JNIEXPORT jint JNICALL Java_edu_vt_cbil_visda_comp_DRMCore_veSubPCA
  (JNIEnv *env, jobject obj, jint m, jint n, jdoubleArray data, jdoubleArray covdata,
   jdoubleArray pcvec_re, jdoubleArray pcvec_im, 
   jdoubleArray pcval_re, jdoubleArray pcval_im, 
   jdoubleArray pcdata_re, jdoubleArray pcdata_im)
{

	jboolean isCopy;
	matrix mdata;
	matrix mcovdata;
	vector vpcval_re;
	vector vpcval_im;
	matrix mpcvec_re;
	matrix mpcvec_im;
	matrix mpcdata_re;
	matrix mpcdata_im;
	jint result;

	mdata.m = m;
	mdata.n = n;
	mdata.pr = (env)->GetDoubleArrayElements(data, &isCopy);
	
	mcovdata.m = n;
	mcovdata.n = n;
	mcovdata.pr = (env)->GetDoubleArrayElements(covdata, &isCopy);

	vpcval_re.l = n;
	vpcval_re.pr = (env)->GetDoubleArrayElements(pcval_re, &isCopy);

	vpcval_im.l = n;
	vpcval_im.pr = (env)->GetDoubleArrayElements(pcval_im, &isCopy);

	mpcvec_re.m = n;
	mpcvec_re.n = n;
	mpcvec_re.pr = (env)->GetDoubleArrayElements(pcvec_re, &isCopy);

	mpcvec_im.m = n;
	mpcvec_im.n = n;
	mpcvec_im.pr = (env)->GetDoubleArrayElements(pcvec_im, &isCopy);

	mpcdata_re.m = m;
	mpcdata_re.n = n;
	mpcdata_re.pr = (env)->GetDoubleArrayElements(pcdata_re, &isCopy);

	mpcdata_im.m = m;
	mpcdata_im.n = n;
	mpcdata_im.pr = (env)->GetDoubleArrayElements(pcdata_im, &isCopy);
	
	//call c function
	result = veSubPCA(&mdata, &mcovdata, &mpcvec_re, &mpcvec_im, &vpcval_re, &vpcval_im, &mpcdata_re, &mpcdata_im);

	(env)->ReleaseDoubleArrayElements(data, mdata.pr, 0);
	(env)->ReleaseDoubleArrayElements(covdata, mcovdata.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcval_re, vpcval_re.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcval_im, vpcval_im.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcvec_re, mpcvec_re.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcvec_im, mpcvec_im.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcdata_re, mpcdata_re.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcdata_im, mpcdata_im.pr, 0);

	return result;
	
}


// for vePCAPPM()
JNIEXPORT jint JNICALL Java_edu_vt_cbil_visda_comp_DRMCore_vePCAPPM
  (JNIEnv *env, jobject obj, jint m, jint n, jdoubleArray pcdata, jdoubleArray pcvec, jdoubleArray pcppmvec)
{

	jboolean isCopy;
	matrix mpcdata;
	matrix mpcvec;
	matrix mpcppmvec;
	jint result;

	mpcdata.m = m;
	mpcdata.n = n;
	mpcdata.pr = (env)->GetDoubleArrayElements(pcdata, &isCopy);

	mpcvec.m = n;
	mpcvec.n = n;
	mpcvec.pr = (env)->GetDoubleArrayElements(pcvec, &isCopy);

	mpcppmvec.m = n;
	mpcppmvec.n = n;
	mpcppmvec.pr = (env)->GetDoubleArrayElements(pcppmvec, &isCopy);

	//call c function
	result = vePCAPPM(&mpcdata, &mpcvec, &mpcppmvec);

	(env)->ReleaseDoubleArrayElements(pcdata, mpcdata.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcvec, mpcvec.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcppmvec, mpcppmvec.pr, 0);

	return result;
	
}



// for veSubPCAPPM()
JNIEXPORT jint JNICALL Java_edu_vt_cbil_visda_comp_DRMCore_veSubPCAPPM
  (JNIEnv *env, jobject obj, jint m, jint n, 
    jdoubleArray pcdata_re, jdoubleArray pcdata_im,
	jdoubleArray pcvec_re, jdoubleArray pcvec_im,
	jdoubleArray pcval_re, jdoubleArray pcval_im, jdoubleArray Zjk,
	jdoubleArray subpcppmvec_re, jdoubleArray subpcppmvec_im)
{

	jboolean isCopy;
	matrix mpcdata_re;
	matrix mpcdata_im;
	matrix mpcvec_re;
	matrix mpcvec_im;
	vector vpcval_re;
	vector vpcval_im;
	vector vZjk;
	matrix msubpcppmvec_re;
	matrix msubpcppmvec_im;
	jint result;

	mpcdata_re.m = m;
	mpcdata_re.n = n;
	mpcdata_re.pr = (env)->GetDoubleArrayElements(pcdata_re, &isCopy);

	mpcdata_im.m = m;
	mpcdata_im.n = n;
	mpcdata_im.pr = (env)->GetDoubleArrayElements(pcdata_im, &isCopy);

	mpcvec_re.m = n;
	mpcvec_re.n = n;
	mpcvec_re.pr = (env)->GetDoubleArrayElements(pcvec_re, &isCopy);

	mpcvec_im.m = n;
	mpcvec_im.n = n;
	mpcvec_im.pr = (env)->GetDoubleArrayElements(pcvec_im, &isCopy);

	vpcval_re.l = n;
	vpcval_re.pr = (env)->GetDoubleArrayElements(pcval_re, &isCopy);

	vpcval_im.l = n;
	vpcval_im.pr = (env)->GetDoubleArrayElements(pcval_im, &isCopy);

	vZjk.l = m;
	vZjk.pr = (env)->GetDoubleArrayElements(Zjk, &isCopy);

	msubpcppmvec_re.m = n;
	msubpcppmvec_re.n = n;
	msubpcppmvec_re.pr = (env)->GetDoubleArrayElements(subpcppmvec_re, &isCopy);

	msubpcppmvec_im.m = n;
	msubpcppmvec_im.n = n;
	msubpcppmvec_im.pr = (env)->GetDoubleArrayElements(subpcppmvec_im, &isCopy);

	//call c function
	result = veSubPCAPPM(&mpcdata_re, &mpcdata_im,
						 &mpcvec_re, &mpcvec_im,
						 &vpcval_re, &vpcval_im, 
						 &vZjk,
						 &msubpcppmvec_re, &msubpcppmvec_im);

	(env)->ReleaseDoubleArrayElements(pcdata_re, mpcdata_re.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcdata_im, mpcdata_im.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcvec_re, mpcvec_re.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcvec_im, mpcvec_im.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcval_re, vpcval_re.pr, 0);
	(env)->ReleaseDoubleArrayElements(pcval_im, vpcval_im.pr, 0);
	(env)->ReleaseDoubleArrayElements(Zjk, vZjk.pr, 0);	
	(env)->ReleaseDoubleArrayElements(subpcppmvec_re, msubpcppmvec_re.pr, 0);
	(env)->ReleaseDoubleArrayElements(subpcppmvec_im, msubpcppmvec_im.pr, 0);

	return result;
	
}
