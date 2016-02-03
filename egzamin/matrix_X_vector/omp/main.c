#include <stdlib.h>
#include <stdio.h>
#include <omp.h>

/*
 * parallel multiplication
 * matrix * vector
 * 
 * open mp
 * 
 * build: gcc -fopenmp main.c
 * 
 */
 
 #define N 15
 #define M 25
 
 int * sequential_solve(int[][M], int[M]);
 
 int main() {
	 #ifdef _OPENMP
	 printf("rozpoznano open mp\n");
	 #endif
	 
	 int i, j;
	 int matrix[N][M]; 
	 for (i=0; i<N; i++)
		for (j=0; j<M; j++)
			matrix[i][j] = rand() % 10;
			
	 int vector[M];
	 for (i=0; i<M; i++)
		vector[i] = rand() % 10;
	 
	 printf ("sekwencyjnie:\n");
	 int * vect = sequential_solve(matrix, vector);	
	 
	 // parallel
	 
	omp_set_dynamic(0);
	omp_set_num_threads(4);
	int sum[N];
	int sum_local;
	printf("\n dekompozycja wierszowa:\n");
	
	#pragma omp parallel for default (none) private (i, j, sum_local) shared (sum, vector, matrix) //schedule (dynamic)
	 for (i=0; i<N; i++)
	 {	
		sum_local = 0;	
		
		for (j=0;j<M;j++)
		{
			sum_local += matrix[i][j] * vector[j];
		}
		sum[i] = sum_local;
		//printf ("\twatek #%d \n", omp_get_thread_num());
	 }
	 
	for (i=0;i<N;i++) {
		printf("%d\n", sum[i]); sum[i]=0; }
	
	printf("\nkolumnowa:\n");
	sum_local = i = j = 0;
	
	#pragma omp parallel default (none) shared (sum, vector, matrix), private (sum_local, i, j)
	{
		for (i=0;i<N;i++)
		{
			sum_local = 0;
			#pragma omp for
			for (j=0; j<M; j++)
			{
				sum_local += matrix[i][j] * vector[j];
			}
			#pragma omp atomic // mozna tez redukcje wprowadzic
			sum[i] += sum_local;
			
			//#pragma omp barrier
		}
		
	} // end of prallel region 	
	
	for (i=0;i<N;i++) {
		printf("%d\n", sum[i]); sum[i]=0; }
		
	printf("\nblokowa:\n");
	sum_local = i = j = 0;
	
	omp_set_nested(1);
	#pragma omp parallel for default (none) private(i, j, sum_local) shared(sum, vector, matrix)
	for (i=0;i<N;i++)
	{
		#pragma omp parallel default (none) firstprivate(i) shared(sum, vector, matrix) private (j, sum_local)
		{ // ------------- nested parallel region
			sum_local = 0;
		#pragma omp for 
		for (j=0;j<M;j++)
		{
			sum_local += matrix[i][j] * vector[j];			
		}		
		#pragma omp atomic
		sum[i] += sum_local;
		
		
		} // ------ end of nested parallel region
	} 	
	 
	 
	for (i=0;i<N;i++)
		printf("%d\n", sum[i]);
	 
	 return 0;
 }
 
//-----------------------------------------------------------

 int * sequential_solve(int matrix[][M], int vector[M]) {
	int * vec = (int*) malloc (N * sizeof(int));
	
	int i,j,s;
	for (i=0; i < N; i++)
	{
		s=0;
		for (j=0;j < M; j++)
		{
			s += matrix[i][j] * vector[j];
		}
		vec[i] = s;		
		printf("%d \n", s);
	}
		
	return vec;
 }
 
