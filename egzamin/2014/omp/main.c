#include <stdlib.h>
#include <stdio.h>
#include <omp.h>

/*
 * I termin 2014
 * Program równoległy obliczający sumę komórek macierzy MxN
 *
 * build: gcc -fopenmp main.c
 */

const int SIZE = 100;

int matrix[100][100];


pthread_mutex_t mutex;

int suma_sekwencyjnie();



int main() {

	#ifdef _OPENMP
	printf("rozpoznano open mp\n");
	#endif

	int i,j;
	for (i=0;i<SIZE;i++)
	for (j=0;j<SIZE;j++)
		matrix[i][j] = rand()%999;

printf ("\n\twynik sekwencyjnie: %d\n", suma_sekwencyjnie());

i=j=0;
int sum = 0;

// row parallel
#pragma omp parallel for default (none) shared (matrix) private (i,j) reduction (+:sum) num_threads(8)
for (i=0; i<SIZE; i++)
	for (j=0; j<SIZE; j++) {
		sum += matrix[i][j];
	}

printf("\n\tWynik obliczony rownolegle: %d\n", sum);



return 0;
}

//--------------------------------------------
int suma_sekwencyjnie() {
	int s = 0, i, j;
	for (i=0;i<SIZE;i++)
	for (j=0;j<SIZE;j++)
		s += matrix[i][j];

	return s;
}
