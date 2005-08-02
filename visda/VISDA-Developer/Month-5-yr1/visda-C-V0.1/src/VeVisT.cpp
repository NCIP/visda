#include <jni.h>         /* Java Native Interface headers */
#include "VeVisT.h"   /* Auto-generated header created by javah -jni */
#include "mathtool.h" 

// for veSNR()
JNIEXPORT jint JNICALL Java_edu_vt_cbil_visda_view_VeVisT_csort
  (JNIEnv *env, jobject obj, jint n, jdoubleArray data, jintArray new_id, jchar order)
{

	jboolean isCopy;
	vector vdata;
	jint *jnew_id;
	int *cnew_id;
	jint result;
	
	cnew_id = new int[n];

	jnew_id = (env)->GetIntArrayElements(new_id, &isCopy);

	vdata.l = n;
	vdata.pr = (env)->GetDoubleArrayElements(data, &isCopy);
	
	//call c function
	result = sort(&vdata, cnew_id, order);

	for (int i=0; i<n; i++) {
		jnew_id[i] = (jint) cnew_id[i];
	}

	(env)->ReleaseDoubleArrayElements(data, vdata.pr, 0);
	(env)->ReleaseIntArrayElements(new_id, jnew_id, 0);

	delete []cnew_id;

	return result;
	
}
	
