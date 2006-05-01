#include <stdio.h>
#include <stdlib.h>
#include "mathtool.h"

/*--------------------------------------------------------------------*/
/*                         utility functions                          */
/*--------------------------------------------------------------------*/

/**********************************************************************
 * str_get_double(char *str, int n, double *data)
 * 
 * Get $n double values  from a string pointed to by $str and save the
 * values in the array $data.
 * 
 * Parameters
 * IN  str  - string to be parsed;
 *     n    - the desired number of double values to be read. The 
 *            actual number may be samller than $n;
 * OUT data - the array where the double values are to be stored;
 *
 * Return Values
 *   The actual number of double values parsed.
 *********************************************************************/
int
str_get_double(char *str, int n, double *data)
{
    double d;
    char   *cur, *next;
    int    nd = 0;  /* number of values actually read */

    next = cur = str;

    d = strtod(cur, &next);
    
    while (!(d == 0. && next == cur) && nd != n )
    {
        data[nd++] = d;
        cur = next;
        d = strtod(cur, &next);
        /* equivalent to d = strtod(cur, &next) */
    }

    return nd;
}


/**********************************************************************/
/*                           sample methods                           */
/**********************************************************************/

/*----------------------------------------------------------------------
 * int sample_load(const char *datfile, sample_t *sample)
 *
 * Load data from file whose name is stored in $datfile.
 *
 * Parameters:
 * IN       datfile - string of the filename to be loaded;
 * IN, OUT  sample  - the #sampt_t object.
 *
 * Return Values:
 * 1 of load successfully; 0 if failed.
 *--------------------------------------------------------------------*/
int
sampleLoad(const char *datfile, matrix *sample)
{
    int    num;  /* number of samples */
    int    dim;  /* sample dimension */
    FILE   *fp;
    double *val;
    char   *buff;
    int    i, k, u;
    int    buffsize = 100;
    int    rtn = 0;

    fp = fopen(datfile, "r");

    if ( NULL == fp)
    {
        fprintf(stdout, "sample_load(): Failed to open data file.\n");
        return 0;
    }

    /* first get "num" and "dim" from data file */

    //buff = (char*)malloc(sizeof(char) * buffsize);
    //val  = (double*)malloc(sizeof(double) * 2);
	buff = new char[buffsize];
	val = new double[2];

    fgets(buff, buffsize, fp);

    if (str_get_double(buff, 2, val) < 2)
    {
        fprintf(stdout, "sample_load(): Bad data file format.\n");
        goto datLoadSampleExit0;
    }

    num = (int)val[0];
    dim = (int)val[1];

    //free(val);
    //free(buff);
	delete[] buff;
	delete[] val;

    /* try to read data and labels. Each line contains a vector and
     * its label. The label is the last value. */

    buffsize = 50 * (dim + 1);
    //buff = (char*)malloc(sizeof(char) * buffsize);
    //val  = (double*)malloc(sizeof(double) * (dim));
	buff = new char[buffsize];
	val = new double[dim];

    //sample->dim      = dim;
    //sample->num      = num;
    //sample->label    = (int*)malloc(sizeof(int) * num);
    //sample->vec      = mat_new(num, dim);
	mnew(sample, num, dim);
    
    for (k = 0; k < num; k++)
    {
        if (NULL == fgets(buff, buffsize, fp))
            goto datLoadSampleExit1;

        if ( dim != str_get_double(buff, dim, val) )
            goto datLoadSampleExit1;
		
		//memcpy(sample->vec[k], val, sizeof(double) * dim);

        //sample->label[k] = (int)val[dim];

		u = k * dim;
		for (i = 0; i < dim; i++) {
			sample->pr[u+i] = val[i];
		}
        
    }

    rtn = 1;
    goto datLoadSampleExit0;

 datLoadSampleExit1:
    fprintf(stdout, "sample_load(): Bad data file format.\n");
    //free(sample->vec[0]);
    //free(sample->vec);
    //free(sample->label);
	mdelete(sample);

 datLoadSampleExit0:
    fclose(fp);
    //free(val);
    //free(buff);
	delete[] buff;
	delete[] val;

    return rtn;
}
