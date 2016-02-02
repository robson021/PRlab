#include<stdlib.h>
#include<stdio.h>
#include <math.h>
#include<pthread.h>

/*
 * I termin 2014
 * Program równoległy obliczający sumę komórek macierzy MxN
 * 
 * build: gcc -I/usr/include -L/usr/lib64 main.c -lpthread
 * 
 */

const int SIZE = 100;

int matrix[100][100];
int task_size;
int sum_global;

pthread_mutex_t mutex;

int suma_sekwencyjnie();
void* sum_task(void *arg);

/* typedef struct arg_fun {
	int id;
	int task_size;
} arg_fun; */

int main () {	
int i, j;


// random init
for (i=0;i<SIZE;i++)
	for (j=0;j<SIZE;j++)
		matrix[i][j] = rand()%999;
		
printf ("\nwynik sekwencyjnie: %d\n", suma_sekwencyjnie());

int n = 8; // thread_num
pthread_t threads[n];
int id_array[n];

task_size = SIZE/n +1; // zrobic sufit
printf("rozmiary zadan (wierszy): %d\n", task_size);

sum_global = 0;

// n threads creation
for (i=0;i<n;i++) {
	id_array[i]=i;
	pthread_create (&threads[i], NULL, sum_task, &id_array[i]);
}		

for (i=0;i<n;i++) 
	pthread_join(threads[i], NULL);
	
printf("Suma liczona na %d watkach:   %d\n", n, sum_global);

pthread_exit(NULL);
}


// funkcje
// -------------------------------------------------------
int suma_sekwencyjnie() {
	int s = 0, i, j;
	for (i=0;i<SIZE;i++)
	for (j=0;j<SIZE;j++)
		s += matrix[i][j];
		
	return s;	
}

void* sum_task(void *arg) {
	int my_id = *((int*) arg);
	
	int start = my_id * task_size;
	int end = start + task_size;
	
	if (end > SIZE)
		end = SIZE;
	
	printf ("\twatek #%d, moje przedzialy wierszy: %d do %d\n", my_id, start, end);
		
	int j, sum_local = 0;
	
	for (; start < end; start++)
		for (j=0;j<SIZE;j++) {
			sum_local += matrix[start][j];
		}
	
	pthread_mutex_lock(&mutex);
	sum_global += sum_local;
	pthread_mutex_unlock(&mutex);
	
	
	return (NULL);
}









