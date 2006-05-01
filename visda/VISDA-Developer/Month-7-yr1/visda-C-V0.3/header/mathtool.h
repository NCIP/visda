#ifndef MATHTOOL_H
#define MATHTOOL_H

typedef struct {
	double re;
	double im;
} dcomplex;


typedef struct {
	int m;  // the number of rows
	int n;  // the number of columns
	double* pr;  // the pointer to the real part of data 
	//double* pi;  // the pointer to the imaginary part of data
} matrix;


typedef struct {
	int l;  // the number of elements of the vector
	double* pr;  // the pointer to the real part of data 
	//double* pi;  // the pointer to the imaginary part of data
} vector;


void mnew(matrix *A, int m, int n);
void mdelete(matrix *A);
void mcopy(matrix *A, matrix *B);
void mzero(matrix *A);
void mones(matrix *A);
void meye(matrix *A);
void mprint(matrix *A);
void getrowvec(matrix *A, int row_id, vector *row_vec);
void getcolvec(matrix *A, int col_id, vector *col_vec);

void vnew(vector *V, int l);
void vdelete(vector *V);
void vcopy(vector *A, vector *B);
void vzero(vector *A);
void vones(vector *A);
void vprint(vector *V);
double vmax(vector *X);
double vmin(vector *X);

/* Sum of vector */
double vsum(vector *X);

/* Mean of vector */
double vmean(vector *X);

/* Covariance vector */
double vcovar(vector *X, vector *Y);

/* Sum of matrix */
int msum(matrix *X, char direction, vector *sum);

/* Mean of Matrix */
int mmean(matrix *X, char direction, vector *mean);

/* Variance of Matrix */
int mvar(matrix *X, char direction, vector *var);

/* Covariance Matrix */
void mcovar(matrix *X, matrix *covm);

/* sort */
int sort(vector *a, int *a_id, char order);

/* sort matrix columns */
void sortcols(int *col_id, matrix *X, matrix *Y);

/* matrix matrix multiplication */
int mmMul(matrix *A, matrix *B, matrix *C);

int cmmMul(matrix *A_re, matrix *A_im,
			matrix *B_re, matrix *B_im, 
			matrix *C_re, matrix *C_im);

int mmDotMul(matrix *A, matrix *B, matrix *C);

int cmmDotMul(matrix *A_re, matrix *A_im,
		      matrix *B_re, matrix *B_im, 
		      matrix *C_re, matrix *C_im);

int vvMul(vector *A, vector *B, matrix *C);

int cvvMul(vector *A_re, vector *A_im,
		   vector *B_re, vector *B_im, 
		   matrix *C_re, matrix *C_im);

/* Subroutine to transpose matrix */
int transpose(matrix *A, matrix *AT);

/* EigenVector and EigenValue */
int eig(matrix *A, matrix *eigvec_re, matrix *eigvec_im, 
		vector *eigval_re, vector *eigval_im);

/* Inverse matrix */
int inv(matrix *A, matrix *InvA);

/* Determinant of matrix */
double det(matrix *A);

/* Rank of matrix */
int rank(matrix *A, double tol);

/* Sort eigenvalues by descending order*/
int sorteig(vector *eigval_re, vector *eigval_im, int *pca_order);
 
/* PCA */
int vePCA(matrix *X, matrix *pcavec_re, matrix *pcavec_im, 
		  vector *pcaval_re, vector *pcaval_im, 
		  matrix *pcadata_re, matrix *pcadata_im);


/* SubPCA */
int veSubPCA(matrix *X, matrix *cov_X, 
			 matrix *pcavec_re, matrix *pcavec_im, 
			 vector *pcaval_re, vector *pcaval_im, 
			 matrix *pcadata_re, matrix *pcadata_im);


/* PCAPPM */
int vePCAPPM(matrix *pca_data, matrix *pca_vec, matrix *pcappm_vec);


/* SubPCAPPM */
int veSubPCAPPM(matrix *pcadata_re, matrix *pcadata_im,
				matrix *pcavec_re, matrix *pcavec_im,
				vector *pcaval_re, vector *pcaval_im, 
				vector *Zjk,
				matrix *subpcappmvec_re, matrix *subpcappmvec_im);

/* to calculate the inverse and the determinant of the covariance matrix */
int veCov(matrix *cov, matrix *inv_cov, matrix *cov_mat, 
		  double *det_cov);

/* to calculate Multivariate Gaussian pdf */
int veModel(matrix *D, matrix *mean0_x, matrix *Var0, vector *w0,
			matrix *Gxn, vector *Fx);

/* EM */
int veSubEM(matrix *D, matrix *mean0_x, vector *w0, double vv, double error, vector *Zjk_up,       
			matrix *mean1_x, vector *w0_t, matrix *cov_mat, matrix *Zjk);

/* feature slection by SNR */
int veSNR(matrix *data, int *label, int dim_num, 
		  int *top_label, vector *snr_sorted, vector *data_mean, vector *variance);

#endif

