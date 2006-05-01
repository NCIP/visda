/////////////////////////////////////////////////////////////////////
//	File:			mathtool.cpp
//	Project:		VISDA
//	Description:	for fundamental matrix operations
//	Author:			Jiajing Wang
//	Create Date:	July 1st, 2005
//
//	History Log:	add new function rank() and svd(). (July 15, 2005)
/////////////////////////////////////////////////////////////////////

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <time.h>
//#include <string.h>
#include "f2c.h"
#include "clapack.h"
#include "mathtool.h"

#ifdef VISDALINUX
extern "C" {
int dgeev_(char *, char *, integer *, doublereal *, integer *,
                  doublereal *, doublereal *, doublereal *, integer *,
                  doublereal *, integer *, doublereal *, integer *,
                  integer *);

int dgetrf_(integer *, integer *, doublereal *, integer *, 
                   integer *, integer *);

int dgetri_(integer *, doublereal *, integer *, integer *,
                   doublereal *, integer *, integer *);

int dgesvd_(char *, char *, integer *, integer *, doublereal *, 
                   integer *, doublereal *, doublereal *, integer *, 
                   doublereal *, integer *, doublereal *, integer *,
                   integer *);
}
#endif // VISDALINUX


/* Subroutine to initialize matrix */ 
void mnew(matrix *A, int m, int n)
{
	int i;

	A->m = m;
	A->n = n;
	A->pr = new double[m*n];

	for (i=0; i<m*n; i++) {
		*(A->pr + i) = 0;
	}

}


/* Subroutine to delete matrix */ 
void mdelete(matrix *A)
{
	delete [](A->pr);
}


/* Subroutine to copy data from matrix A to matrix B */ 
void mcopy(matrix *A, matrix *B)
{
	int i;
	int m;
	int n;
	double *ap;
	double *bp;

	m = A->m;
	n = A->n;
	ap = A->pr;
	bp = B->pr;

	if ((B->m == m) && (B->n == n)) {
		for (i=0; i<m*n; i++) {
			bp[i] = ap[i];
		}
	} else {
		printf(" Warning: Matrix Copy is failed since the size of matrix A and B are different. \n");
	}

}


/* Subroutine to set Zero to each matrix element*/ 
void mzero(matrix *A)
{
	int i, size;

	size = (A->m) * (A->n);
	for (i=0; i<size; i++) {
		*(A->pr + i) = 0;
	}
}


/* Subroutine to set one to each matrix element*/ 
void mones(matrix *A)
{
	int i, size;

	size = (A->m) * (A->n);
	for (i=0; i<size; i++) {
		*(A->pr + i) = 1;
	}
}


/* Subroutine to set the diagonal element '1' and others '0' */ 
void meye(matrix *A)
{
	int i, j, u;

	if (A->m != A->n) {
		printf(" Warning: meye() is failed since the matrix is not square. \n");
	}
	for (i=0; i<A->n; i++) {
		u = i * (A->n);
		for (j=0; j<A->n; j++) {
			if (j==i) {
				*(A->pr + u + j) = 1;
			} else {
				*(A->pr + u + j) = 0;
			}
		}
	} 
}

/* Subroutine to print matrix data */ 
void mprint(matrix *A)
{
	int i, j;
	double *pi;

	for (i=0; i<(A->m); i++) {
		pi = A->pr + i*(A->n);
        for (j=0; j<(A->n); j++) {
            printf("%e\t", *(pi+j));
        }
        printf("\n");
    }
}


/* Subroutine to get the specified row vector of one matrix */
void getrowvec(matrix *A, int row_id, vector *row_vec)
{
	int col_num;
	int i;

	col_num = A->n;

	if (col_num != row_vec->l) {
		printf(" Warning: getrowvec() is failed since the length of row vector is not equal to matrix. \n");
	}

	for (i=0; i<col_num; i++) {
		*(row_vec->pr + i) = *(A->pr + row_id*col_num + i);
	}
}


/* Subroutine to get the specified column vector of one matrix */
void getcolvec(matrix *A, int col_id, vector *col_vec)
{
	int row_num;
	int col_num;
	int i;

	row_num = A->m;
	col_num = A->n;

	if (row_num != col_vec->l) {
		printf(" Warning: getcolvec() is failed since the length of column vector is not equal to matrix. \n");
	}

	for (i=0; i<row_num; i++) {
		*(col_vec->pr + i) = *(A->pr + i*col_num + col_id);
	}
}


/* Subroutine to initialize vector */ 
void vnew(vector *V, int l)
{
	int i;

	V->l = l;
	V->pr = new double[l];

	for (i=0; i<l; i++) {
		*(V->pr + i) = 0;
	}

}


/* Subroutine to delete vector */ 
void vdelete(vector *V)
{
	delete [](V->pr);
}


/* Subroutine to copy data from vector A to vector B */ 
void vcopy(vector *A, vector *B)
{
	int i;

	if (B->l == A->l) {
		for (i=0; i<(A->l); i++) {
			*(B->pr+i) = *(A->pr+i);
		}
	} else {
		printf(" Warning: Vector Copy is failed since the lenth of vector A and B are different. \n");
	}
}


/* Subroutine to set Zero to each matrix element*/ 
void vzero(vector *A)
{
	int i;

	for (i=0; i<A->l; i++) {
		*(A->pr + i) = 0;
	}
}


/* Subroutine to set one to each matrix element*/ 
void vones(vector *A)
{
	int i;

	for (i=0; i<A->l; i++) {
		*(A->pr + i) = 1;
	}
}


/* Subroutine to print vector data */ 
void vprint(vector *V)
{
	int i;

	for (i=0; i<(V->l); i++) {
	    printf("%e\t", *(V->pr+i));
    }
	printf("\n");
}


/* Subroutine to compute the Sum of vector */ 
double vsum(vector *X)
{
    int i;
    double sum=0;

    for (i=0; i<(X->l); i++) {
        sum += *(X->pr+i);
    }

    return sum;
	
}


/* Subroutine to compute the Mean of vector */ 
double vmean(vector *X)
{
    double sum=0, result;

    sum = vsum(X);
    result = sum/(X->l);
    return result;

}


/* Subroutine to compute the Covariance of two vectors */ 
double vcovar(vector *X, vector *Y)
{
    int i;
    double mean_x, mean_y, sum=0, result;

    mean_x = vmean(X);
    mean_y = vmean(Y);

	if (X->l != Y->l) {
		printf(" Warning: cov() is failed since the lenth of vector X and Y are different. \n"); 
		return 0;	
	}			

    for (i=0; i<(X->l); i++) {
        sum += (*(X->pr+i)-mean_x)*(*(Y->pr+i)-mean_y);
    }

    result = sum/(X->l-1);

    return result;
}


/* Subroutine to compute the maximum value in vector */ 
double vmax(vector *X)
{
    int i;
    double max;

	max = X->pr[0];

	for (i=1; i<(X->l); i++) {
		if (max < X->pr[i]) {
			max = X->pr[i];
		}
	}

	return max;
}


/* Subroutine to compute the minimum value in vector */ 
double vmin(vector *X)
{
    int i;
    double min;

	min = X->pr[0];

	for (i=1; i<(X->l); i++) {
		if (min > X->pr[i]) {
			min = X->pr[i];
		}
	}

	return min;
}


/*******************************************************************
 Subroutine to compute the Sum of Matrix
   matrix *X:     the pointer to the matrix
   char direction: 'c' - compute the sum of each column
                   'r' - compute the sum of each row
   vector *sum:  the pointer to the output vector
*******************************************************************/
int msum(matrix *X, char direction, vector *sum)
{
	int row_l, row_n;	
	int i, j;
	int u, v;
	double s;
	double *xp;
	double *sp;
    int result;

    row_l = X->n;
	row_n = X->m;
	xp = X->pr;
	sp = sum->pr;
 
	if (direction == 'c') {  // compute the sum of each column
        for (i=0; i<row_l; i++) {
			v = i;
			s = 0;
            for (j=0; j<row_n; j++) {
                s += xp[v];
				v += row_l;
            }   
            sp[i] = s;
        }
		sum->l = row_l;
        result = 1;
    } else if (direction == 'r') { // compute the sum of each row
        for (j=0; j<row_n; j++) {
			u = row_l*j;
			s = 0;
            for (i=0; i<row_l; i++) {
                s += xp[u+i];
            }   
            sp[j] = s;
        }
		sum->l = row_n;
        result = 1;
    } else {
        result = 0;
        printf("the direction parameter should be 'c' or 'r'");
    }

    return result;
   
}


/*******************************************************************
 Subroutine to compute the Mean of Matrix
   matrix *X:     the pointer to the matrix
   char direction: 'c' - compute the mean of each column
                   'r' - compute the mean of each row
   vector *mean:  the pointer to the mean vector
*******************************************************************/
int mmean(matrix *X, char direction, vector *mean)
{
	int row_l, row_n;	
	int i;
    int result;

    row_l = X->n;
	row_n = X->m;
 
	if (direction == 'c') {  // compute the mean of each column
		mean->l = row_l;
		msum(X, 'c', mean);
        for (i=0; i<row_l; i++) {
            *(mean->pr + i) /= row_n;
        }
        result = 1;
    } else if (direction == 'r') { // compute the mean of each row
		mean->l = row_n;
		msum(X, 'r', mean);
        for (i=0; i<row_n; i++) {
	        *(mean->pr + i) /= row_l;		
        }
        result = 1;
    } else {
        result = 0;
        printf("the direction parameter should be 'c' or 'r'");
    }

    return result;
   
}


/*******************************************************************
 Subroutine to compute the Variance of Matrix
   matrix *X:     the pointer to the matrix
   char direction: 'c' - compute the mean of each column
                   'r' - compute the mean of each row
   vector *mean:  the pointer to the mean vector
*******************************************************************/
int mvar(matrix *X, char direction, vector *var)
{
	int row_l, row_n;	
	int i;
    int result;
	vector col_vec;
	vector row_vec;

    row_l = X->n;
	row_n = X->m;
 
	vnew(&col_vec, row_n);
	vnew(&row_vec, row_l);

	if (direction == 'c') {  // compute the variance of each column
		var->l = row_l;
        for (i=0; i<row_l; i++) {
			getcolvec(X, i, &col_vec);
            *(var->pr + i) = vcovar(&col_vec, &col_vec);
        }
        result = 1;
    } else if (direction == 'r') { // compute the variance of each row
		var->l = row_n;
        for (i=0; i<row_n; i++) {
			getrowvec(X, i, &row_vec);
	        *(var->pr + i) = vcovar(&row_vec, &row_vec);;		
        }
        result = 1;
    } else {
        result = 0;
        printf("the direction parameter should be 'c' or 'r'");
    }

	vdelete(&col_vec);
	vdelete(&row_vec);

    return result;
   
}


/*******************************************************************
 Subroutine to compute the Covariance Matrix (CM)
   matrix *X:     the pointer to the matrix
   matrix *covm:  the pointer to the output covariance matrix
*******************************************************************/
void mcovar(matrix *X, matrix *covm)
{
	int i, j, k;
    vector Coli;  // the pointer to the 'i'th column vector
    vector Colj;  // the pointer to the 'j'th column vector
	double *pxi;
	double *pxj;
	double *pci;

    vnew(&Coli, X->m);
    vnew(&Colj, X->m);
	covm->m = X->n;
	covm->n = X->n;

    for (i=0; i<(X->n); i++) {  // i -- row index of CM
        for (j=0; j<(X->n); j++) {  // j -- column index of CM
            for (k=0; k<(X->m); k++) {
				pxi = X->pr + i;
				pxj = X->pr + j;
                *(Coli.pr+k) = *(pxi+(X->n)*k);
                *(Colj.pr+k) = *(pxj+(X->n)*k);
            }

            // the value of CM[i,j]
			pci = covm->pr + (covm->m)*i;
            *(pci+j) = vcovar(&Coli, &Colj);
        }
    }

	vdelete(&Coli);
	vdelete(&Colj);

}


/*******************************************************************
 Subroutine to sort the data in a vector with specific order. The original
index of each element is also moved, and composes an index array.
Here, the predefined function qsort() in c library is used.
   vector *a:    the pointer to the vector data
   int *a_id:    the pointer to the integer array which will store the
                   original index of the element
   char order:   'a' - sorted by incresing order
                 'd' - sorted by decreasing order
*******************************************************************/
class ElementObj
{
public:
    double Data;
    int Index;
    ElementObj()
	{
            Data = 0;
            Index = 0;
	}
    ElementObj& operator=(const ElementObj& x)
	{
            Data = x.Data;
            Index = x.Index;
            return *this;
	}
    bool operator<(const ElementObj& x)
	{
            return Data<x.Data;
	}
    bool operator==(const ElementObj& x)
	{
            return Data==x.Data;
	}
};

int compare(const void *elem1, const void *elem2 ) 
{
    ElementObj *pt1 = (ElementObj*)elem1;
    ElementObj *pt2 = (ElementObj*)elem2;
    if(pt1->Data < pt2->Data) return -1;
    else if(pt1->Data == pt2->Data) return 0;
    else return 1;
}

int sort(vector *a, int *a_id, char order)
{
	int i;
	int num;
	double *ap;
    ElementObj *a_obj;
    int result=1;
   
	num = a->l;
	ap = a->pr;

    a_obj = new ElementObj[num];
    for (i=0; i<num; i++) {
        a_obj[i].Data = ap[i];
        a_obj[i].Index = i;
    }

    qsort(a_obj, num, sizeof(ElementObj), compare);

    for (i=0; i<num; i++) {
        if (order == 'd') { //descending sort
            ap[i] = a_obj[num-1-i].Data;
            a_id[i] = a_obj[num-1-i].Index;
            result = 1;
        } else if (order == 'a') { //ascending sort
            ap[i] = a_obj[i].Data;
            a_id[i] = a_obj[i].Index;
            result = 1;
        } else {
            result = 0;
            printf(" Warning: the order parameter should be 'a' or 'd'");
        }
    }

    delete []a_obj;

    return result;
}


/*******************************************************************
 Subroutine to sort the matrix columns based on the specific column
sequence.
   int *col_id:   the pointer to the integer array which stores the new
                  order of columns
   matrix *X:     the pointer to the original matrix
   matrix *Y:     the pointer to the matrix with new order
*******************************************************************/
void sortcols(int *col_id, matrix *X, matrix *Y)
{
	int row_n, row_l;
    int i, j;
	int u;
	double *xp;
	double *yp;

	row_n = X->m;
	row_l = X->n;
	if ((Y->m != row_n) || (Y->n != row_l)) {
		printf(" Warning: the second matrix has different size with the sorted (first) matrix!");
	}

	xp = X->pr;
	yp = Y->pr;

    // [i,j] -> [i, col_id[j]]
    for (i=0; i<row_n; i++) {  // row index
		u = i*row_l;
        for (j=0; j<row_l; j++) {  // column index
            yp[u+j] = xp[u+col_id[j]];
        }
    }
   
}   


/* Subroutine to do complex operation */
void cpx_add(const dcomplex * const a, const dcomplex * const b,
             dcomplex * const c)
{
    c->re = a->re + b->re;
    c->im = a->im + b->im;
}

void cpx_sub(const dcomplex * const a, const dcomplex * const b,
             dcomplex * const c)
{
    c->re = a->re - b->re;
    c->im = a->im - b->im;
}

void cpx_mul(const dcomplex * const a, const dcomplex * const b,
             dcomplex * const c)
{
    c->re = (a->re)*(b->re) - (a->im)*(b->im);
    c->im = (a->re)*(b->im) + (a->im)*(b->re);
}

/*******************************************************************
 Subroutine to do the real Matrix Matrix Multiplication operation
 C[m][n] = A[m][k]*B[k][n]
    matrix *A: the pointer to the matrix A
    matrix *B: the pointer to the matrix B
    matrix *C: the pointer to the matrix C
 return value: '1' - successfully exit
               '0' - some error occurs
*******************************************************************/
int mmMul(matrix *A, matrix *B, matrix *C)
{
	int m, n, k;
    int i, j, t;
	int u, v;

    double sum; 

	m = A->m;
	k = A->n;
	n = B->n;
	if (B->m != k) {
        printf(" Warning: mmMul() is failed since the number of columns of matrix A is different with the number of rows of matrix B");
		return 0;
	}
	if (C->m != m) {
        printf(" Warning: mmMul() is failed since the number of rows of matrix A is different with the number of rows of matrix C");
		return 0;
	}
	if (C->n != n) {
        printf(" Warning: mmMul() is failed since the number of columns of matrix B is different with the number of columns of matrix C");
		return 0;
	}

    for (i=0; i<m; i++) {  //c[i,j]
        for (j=0; j<n; j++) {
            u = i*k;
			v = j;
            sum = 0;
            for (t=0; t<k; t++) {
                sum += (*(A->pr+u+t)) * (*(B->pr+v));
				v += n;
            }
	 
            *(C->pr+i*n+j) = sum;

        }
    }

	return 1;

}


/*******************************************************************
 Subroutine to do the complex Matrix Matrix Multiplication operation
 C[m][n] = A[m][k]*B[k][n]
    matrix *A_re: the pointer to the real part of matrix A
	matrix *A_im: the pointer to the imaginary part of matrix A
    matrix *B_re: the pointer to the real part of matrix B
	matrix *B_im: the pointer to the imaginary part of matrix B
    matrix *C_re: the pointer to the real part of matrix C
	matrix *C_im: the pointer to the imaginary part of matrix C
 return value: '1' - successfully exit
               '0' - some error occurs
*******************************************************************/
int cmmMul(matrix *A_re, matrix *A_im,
		   matrix *B_re, matrix *B_im, 
		   matrix *C_re, matrix *C_im)
{
	int m, n, k;
    int i, j, t;
    int u, v;

    double sum_re; 
    double sum_im; 

	m = A_re->m;
	k = A_re->n;
	n = B_re->n;
	if (B_re->m != k) {
        printf(" Warning: cmmMul() is failed since the number of columns of matrix A is different with the number of rows of matrix B");
		return 0;
	}
	if (C_re->m != m) {
        printf(" Warning: cmmMul() is failed since the number of rows of matrix A is different with the number of rows of matrix C");
		return 0;
	}
	if (C_re->n != n) {
        printf(" Warning: cmmMul() is failed since the number of columns of matrix B is different with the number of columns of matrix C");
		return 0;
	}

    for (i=0; i<m; i++) {  //c[i,j]
		double *pcr;
		double *pci;

		pcr = C_re->pr+i*n;
		pci = C_im->pr+i*n;

        for (j=0; j<n; j++) {
			u = i*k;
            v = j;
            sum_re = 0;
            sum_im = 0;
            for (t=0; t<k; t++) {
                sum_re += (*(A_re->pr+u+t)) * (*(B_re->pr+v)) - (*(A_im->pr+u+t)) * (*(B_im->pr+v));
                sum_im += (*(A_re->pr+u+t)) * (*(B_im->pr+v)) + (*(A_im->pr+u+t)) * (*(B_re->pr+v));
                v += n;
            }
	 
            *(pcr+j) = sum_re;
            *(pci+j) = sum_im;
        }
    }
	return 1;
}   


/*******************************************************************
 Subroutine to do the Matrix Element Multiplication operation
 C[m][n] = A[m][n] .* B[m][n]
    matrix *A: the pointer to the matrix A
    matrix *B: the pointer to the matrix B
    matrix *C: the pointer to the matrix C
 return value: '1' - successfully exit
               '0' - some error occurs
*******************************************************************/
int mmDotMul(matrix *A, matrix *B, matrix *C)
{
	int m, n;
    int i;

	m = A->m;
	n = A->n;
	if ((B->m != m) || (B->n != n)){
        printf(" Warning: mmDotMul() is failed since the size of matrix B is different with matrix A");
		return 0;
	}
	if ((C->m != m) || (C->n != n)){
        printf(" Warning: mmDotMul() is failed since the size of matrix C is different with matrix A");
		return 0;
	}

    for (i=0; i<m*n; i++) {
		*(C->pr + i) = (*(A->pr + i)) * (*(B->pr + i)); 
    }

	return 1;

}


/*******************************************************************
 Subroutine to do the Matrix Complex Element Multiplication operation
 C[m][n] = A[m][n] .* B[m][n]
    matrix *A_re: the pointer to the real part of matrix A
    matrix *A_im: the pointer to the imaginary part of matrix A
    matrix *B_re: the pointer to the real part of matrix B
	matrix *B_im: the pointer to the imaginary part of matrix B
    matrix *C_re: the pointer to the real part of matrix C
	matrix *C_im: the pointer to the imaginary part of matrix C
 return value: '1' - successfully exit
               '0' - some error occurs
*******************************************************************/
int cmmDotMul(matrix *A_re, matrix *A_im,
		      matrix *B_re, matrix *B_im, 
		      matrix *C_re, matrix *C_im)
{
	int m, n;
    int i;

	m = A_re->m;
	n = A_re->n;
	if ((B_re->m != m) || (B_re->n != n)){
        printf(" Warning: cmmDotMul() is failed since the size of matrix B is different with matrix A");
		return 0;
	}
	if ((C_re->m != m) || (C_re->n != n)){
        printf(" Warning: cmmDotMul() is failed since the size of matrix C is different with matrix A");
		return 0;
	}

    for (i=0; i<m*n; i++) {
		*(C_re->pr + i) = (*(A_re->pr + i)) * (*(B_re->pr + i)) - (*(A_im->pr + i)) * (*(B_im->pr + i));
		*(C_im->pr + i) = (*(A_re->pr + i)) * (*(B_im->pr + i)) + (*(A_im->pr + i)) * (*(B_re->pr + i));
    }

	return 1;

}


/*******************************************************************
 Subroutine to do the Vector Vector Multiplication operation
 C[m][n] = A[m]* B[n]
    vector *A: the pointer to the vector A
    vector *B: the pointer to the vector B
    matrix *C: the pointer to the matrix C
 return value: '1' - successfully exit
               '0' - some error occurs
*******************************************************************/
int vvMul(vector *A, vector *B, matrix *C)
{
	int m, n;
    int i, j;

	m = A->l;
	n = B->l;
	if (C->m != m) {
        printf(" Warning: vvmul() is failed since the number of rows of matrix A is different with the number of rows of matrix C");
		return 0;
	}
	if (C->n != n) {
        printf(" Warning: vvmul() is failed since the number of columns of matrix B is different with the number of columns of matrix C");
		return 0;
	}

    for (i=0; i<m; i++) {  //c[i,j]
        for (j=0; j<n; j++) {
			*(C->pr+i*n+j) = (*(A->pr+i)) * (*(B->pr+j));
        }
    }

	return 1;

}


/*******************************************************************
Subroutine to do the Complex Vector Vector Multiplication operation
 C[m][n] = A[m]* B[n]
	vector *A_re: the pointer to the real part of vector A
    vector *A_im: the pointer to the imaginary part of vector A
    vector *B_re: the pointer to the real part of vector B
	vector *B_im: the pointer to the imaginary part of vector B
    matrix *C_re: the pointer to the real part of matrix C
	matrix *C_im: the pointer to the imaginary part of matrix C
 return value: '1' - successfully exit
               '0' - some error occurs
*******************************************************************/
int cvvMul(vector *A_re, vector *A_im,
		   vector *B_re, vector *B_im, 
		   matrix *C_re, matrix *C_im)
{
	int m, n;
    int i, j, u;

	m = A_re->l;
	n = B_re->l;
	if ((C_re->m != m) || (C_im->m != m)) {
        printf(" Warning: cvvmul() is failed since the number of rows of matrix A is different with the number of rows of matrix C");
		return 0;
	}
	if ((C_re->n != n) || (C_im->n != n)) {
        printf(" Warning: cvvmul() is failed since the number of columns of matrix B is different with the number of columns of matrix C");
		return 0;
	}

    for (i=0; i<m; i++) {  //c[i,j]
		u = i*n;
        for (j=0; j<n; j++) {
			*(C_re->pr + u + j) = (*(A_re->pr + i)) * (*(B_re->pr + j)) - (*(A_im->pr + i)) * (*(B_im->pr + j));
			*(C_im->pr + u + j) = (*(A_re->pr + i)) * (*(B_im->pr + j)) + (*(A_im->pr + i)) * (*(B_re->pr + j));
        }
    }

	return 1;

}


/*******************************************************************
 Subroutine to transpose matrix
   matrix *A:       the pointer to the matrix
   matrix *AT:		the pointer to the transpose matrix
 return value: '1' - successfully exit
               '0' - exit with error/warning
*******************************************************************/
int transpose(matrix *A, matrix *AT)
{
	int m, n;
	int i, j,t;

	m = A->m;
	n = A->n;

	if ((m != AT->n) || (n != AT->m)) {
		printf(" Warning: transpose() is failed since the size of output matrix is not transposed. \n");
		return 0;
	}

	for (i=0; i<n; i++) {
		t = m * i;
        for (j=0; j<m; j++) {
            *(AT->pr+t+j) = *(A->pr+n*j+i);
        }		
    }
	return 1;
}


/*******************************************************************
 Subroutine to compute the Eigenvalue and Eigenvector
 by using CLAPACK subroutine - dgeev_()
   matrix *A:          the pointer to the matrix
   matrix *eigvec_re:  the pointer to the real part of eigenvectors
   matrix *eigvec_im:  the pointer to the imaginary part of eigenvectors
   vector *eigval_re:  the pointer to the real part of eigenvalues
   vector *eigval_im:  the pointer to the imaginary part of eigenvalues
 return value: '1' - successfully exit
               '0' - cannot get the valid eigenvalue
*******************************************************************/
int eig(matrix *A, matrix *eigvec_re, matrix *eigvec_im, 
		vector *eigval_re, vector *eigval_im)
{
    char jobvl, jobvr;
    integer n, lda, ldvl, ldvr, lwork, info, i, j, size;
    double *AT;
    double *dummy;
    double *vrT;
    double *vr;
    double *work;
    double *ap;
	double *eigrp;
	double *eigip;

	if (A->m != A->n) {
		printf(" Warning: Eig() is failed since the matrix is not square matrix. \n");
		return 0;
	}

    n = A->n;
    lda = n;
    ldvl = n;
    ldvr = n;
    lwork = 5*n;
    size = n*n;

    ap = A->pr;
	eigrp = eigval_re->pr;
	eigip = eigval_im->pr;

    // only compute the right eigenvector
    jobvl = 'N';  
    jobvr = 'V';

    AT = new double[size];
    dummy = new double[size];
    vrT = new double[size];
    vr = new double[size];
    work = new double[5*n];
   
    // to call a Fortran routine from C we have to transform the matrix
    for (i=0; i<n; i++) {				
        for (j=0; j<n; j++) {
            AT[n*i+j] = ap[n*j+i];
        }		
    }
   
    dgeev_(&jobvl, &jobvr, &n, AT, &lda, eigrp, eigip, dummy, &ldvl, vrT, 
           &ldvr, work, &lwork, &info);

	if (info != 0) {
		printf(" Warning: Eig() is failed. \n");
		return 0;
	}

    // to output a Fortran matrix to C we have to transform the matrix
    for (i=0; i<n; i++) {				
        for (j=0; j<n; j++) {
            vr[n*i+j] = vrT[n*j+i];
        }		
    }

    // If the j-th eigenvalue is real, then v(j) = VR(:,j),
    //     the j-th column of VR.
    // If the j-th and (j+1)-st eigenvalues form a complex
    //     conjugate pair, then v(j) = VR(:,j) + i*VR(:,j+1) and
    //     v(j+1) = VR(:,j) - i*VR(:,j+1).
	double *eigvec_rp;
	double *eigvec_ip;

	j = 0;
    while (j < n) {
		eigvec_rp = eigvec_re->pr + j;
		eigvec_ip = eigvec_im->pr + j;

        if (*(eigip+j) == 0) {   // j-th real eigenvector		
            for (i=0; i<n; i++) {
                *(eigvec_rp + i*n) = vr[i*n+j];
                *(eigvec_ip + i*n) = 0;
            };
            j++;
        } else {            // j-th and (j+1)-st complex eigenvector
            for (i=0; i<n; i++) {
                *(eigvec_rp + i*n) = vr[i*n+j];
                *(eigvec_ip + i*n) = vr[i*n+j+1];
            };
            for (i=0; i<n; i++) {
                *(eigvec_rp + i*n + 1) = vr[i*n+j];
                *(eigvec_ip + i*n + 1) = -vr[i*n+j+1];
            };
            j += 2;
        }
    }
   
    delete []AT;
    delete []dummy;
    delete []vrT;
    delete []vr;
    delete []work;
    return 1;
   
}


/*******************************************************************
 Subroutine to compute the Inverse Matrix
 by using CLAPACK subroutine - dgetri_() and dgetrf_()
   matrix *A:     the pointer to the matrix
   matrix *InvA:  the pointer to the inverse matrix
 return value: '1' - successfully exit
               '0' - inverse matrix does not exist
*******************************************************************/
int inv(matrix *A, matrix *InvA)
{
    integer m, n, lda, lwork, info1, info2, i, j, size;
    double *AT;
    double *work;
	integer *ipiv;

	m = A->m;
    n = A->n;

	if (m != n) {
		printf(" Warning: inv() is failed since the matrix is not square matrix. \n");
		return 0;
	}

    lda = m;
    lwork = 5*n;
    size = m*n;

  
    AT = new double[size];
    work = new double[5*n];
    ipiv = new integer[n];

    // to call a Fortran routine from C we have to transform the matrix
    for (i=0; i<m; i++) {				
        for (j=0; j<n; j++) {
            AT[n*i+j] = *(A->pr+m*j+i);
        }		
    }
   
	dgetrf_(&m, &n, AT, &lda, ipiv, &info1);
 
	dgetri_(&n, AT, &lda, ipiv, work, &lwork, &info2);

	if ((info1 != 0) || (info2 != 0)) {
		printf(" Warning: Inv() is failed. \n");
		return 0;
	}

	// to output a Fortran matrix to C we have to transform the matrix
    for (i=0; i<m; i++) {				
        for (j=0; j<n; j++) {
            *(InvA->pr+n*i+j) = AT[m*j+i];
        }		
    }

	return 1;

}


/*******************************************************************
 Subroutine to compute the Determinant of Matrix
 by using CLAPACK subroutine - dgetrf_() ( PLU decomposition:
 where P is a permutation matrix, L is lower triangular with unit
 diagonal elements (lower trapezoidal if m > n), and U is upper
 triangular (upper trapezoidal if m < n))

   matrix *A:     the pointer to the matrix   
 
   return value: the determinant of matrix
*******************************************************************/
double det(matrix *A)
{
    integer m, n, lda, info; 
	int i, j, size;
    double *AT;
	integer *ipiv;
	double detU=1;
	int num_permut=0;

	m = A->m;
    n = A->n;

	if (m != n) {
		printf(" Warning: det() is failed since the matrix is not square matrix. \n");
		return 0;
	}

    lda = m;
    size = m*n;

    AT = new double[size];
    ipiv = new integer[n];

    // to call a Fortran routine from C we have to transform the matrix
    for (i=0; i<m; i++) {				
        for (j=0; j<n; j++) {
            AT[n*i+j] = *(A->pr+m*j+i);
        }		
    }
   
	dgetrf_(&m, &n, AT, &lda, ipiv, &info);
 
	if (info < 0) {
		printf(" Warning: det() is failed. \n");
	}
	
	// the determinant of U
	for (i=0; i<n; i++) {
		detU *= AT[n*i+i];
	}

	// the determinant of P is either +1 or -1
	// depending of whether the number of row permutations is even or odd.
	for (i=0; i<n; i++) {
		if (ipiv[i] != i+1) {
			num_permut++;
		}
	}

	if (num_permut%2 == 0) {
		return detU;
	} else {
		return -detU;
	}

}



/*******************************************************************
 Subroutine to compute the Rank of Matrix
 by using CLAPACK subroutine - dgesvd_() ( SVD decomposition)

   matrix *A:	 the pointer to the matrix   
   double tol:   the tolerance to determine 'zero'	
   return value: the rank of matrix
*******************************************************************/
int rank(matrix *A, double tol)
{
	char jobu;
	char jobvt;
    integer m, n, lda, ldu, ldvt, lwork, info; 
	int i, j, size;
    double *AT;
	double *s;
	double *u;
	double *vt;
	double *work;
	int dim;
	int rank;
	double max_s;  // the maximum singular value
	double zero_thre;    

	m = A->m;
    n = A->n;
	dim = min(m, n);

    lda = m;
	ldu = m;
	ldvt = n;
	lwork = 5*m;
    size = m*n;

    AT = new double[size];
    // to call a Fortran routine from C we have to transform the matrix
    for (i=0; i<m; i++) {				
        for (j=0; j<n; j++) {
            AT[n*i+j] = *(A->pr+m*j+i);
        }		
    }

	jobu = 'A';
	jobvt = 'A';
	s = new double[dim];
	u = new double[m*m];
	vt = new double[n*n];
	work = new double[lwork];

	dgesvd_(&jobu, &jobvt, &m, &n, AT, &lda, s, u, &ldu, 
		    vt, &ldvt, work, &lwork, &info);
 
	if (info < 0) {
		printf(" Warning: svd() is failed, so rank() is also failed. \n");
	}
	
	/*printf("singular value:\n");
	for (i=0; i<dim; i++) {
		printf("%e\t", s[i]);
	}
	printf("\n");*/

	// rank equals to the number of non-zero singular values
	// singular values have been sorted descendingly
	rank = dim;
	max_s = s[0];
	zero_thre = max_s * tol;
	for (i=0; i<dim; i++) {
		if (s[i] < zero_thre) {
			rank = i;
			break;
		}
	}

	return rank;

}


/*******************************************************************
 Subroutine to compute the singular decomposition of Matrix
 by using CLAPACK subroutine - dgesvd_() ( SVD decomposition)

   matrix *A:	 the pointer to the matrix   
   matrix *U:	 the pointer to an M-by-M orthogonal matrix
   vector *S:    the pointer to an min(m,n) vector with non-Negative elements
   matrix *V:	 the pointer to an N-by-N orthogonal matrix

return value: '1' - successfully exit
               '0' - svd decomposition fails
*******************************************************************/
int svd(matrix *A, matrix *U, vector *S, matrix *V)
{
	char jobu;
	char jobvt;
    integer m, n, lda, ldu, ldvt, lwork, info; 
	int i, j, size;
    double *AT;
	double *s;
	double *u;
	double *vt;
	double *work;
	int dim;

	m = A->m;
    n = A->n;
	dim = min(m, n);

    lda = m;
	ldu = m;
	ldvt = n;
	lwork = 5*m;
    size = m*n;

    AT = new double[size];
    // to call a Fortran routine from C we have to transform the matrix
    for (i=0; i<m; i++) {				
        for (j=0; j<n; j++) {
            AT[n*i+j] = *(A->pr+m*j+i);
        }		
    }

	jobu = 'A';
	jobvt = 'A';
	s = new double[dim];
	u = new double[m*m];
	vt = new double[n*n];
	work = new double[lwork];

	dgesvd_(&jobu, &jobvt, &m, &n, AT, &lda, s, u, &ldu, 
		    vt, &ldvt, work, &lwork, &info);
 
	if (info < 0) {
		printf(" Warning: svd() is failed. \n");
		return 0;
	}

	// to output a Fortran matrix to C we have to transform the matrix
	//mnew(U, m, m);
    for (i=0; i<m; i++) {		
		int t = m*i;
        for (j=0; j<m; j++) {
            *(U->pr+t+j) = u[n*j+i];
        }		
    }

	//mnew(V, n, n);
    for (i=0; i<n; i++) {		
		int t = n*i;
        for (j=0; j<n; j++) {
            *(V->pr+t+j) = vt[t+j];
        }		
    }

	//vnew(S, dim);
    for (i=0; i<dim; i++) {		
	    *(S->pr+i) = s[i];	
    }

	return 1;

}
