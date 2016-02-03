#include<stdlib.h>
#include<stdio.h>
#include<pthread.h>
#include<time.h>
#include <math.h>


/*
 * Sumowanie poszczególnych wierszy macierzy
 * (sum[i]) z użyciem dekompozycji kolumnowej
 * pthread
 * 
 */
 
 
 // build: gcc -I/usr/include -L/usr/lib64 main.c -lpthread

#define N 30
#define M 37
int task_size;
const int num_threads = 8;
int matrix[N][M];

int sum[N];

pthread_mutex_t mutex;

void * thread_task(void *arg);
void suma_sekwencyjnie();

int main() {
	
	pthread_t threads[num_threads];
	int threadID[num_threads];	
	
	int i,j;
	for (i=0;i<N;i++) {
		sum[i] = 0;
		for (j=0; j<M; j++)
			matrix[i][j] = rand() % 10; }
	
	printf("\nSuma sekwencyjnie:\n");
	suma_sekwencyjnie();
	
	task_size = M/num_threads +1;
	
	for (i=0;i<num_threads;i++) {
		threadID[i] = i;
		pthread_create (&threads[i], NULL, thread_task, &threadID[i]);
	}
	for (i=0;i<num_threads;i++)
		pthread_join(threads[i], NULL);
		
	printf("suma rownolegle (kolumnowo):\n");
	for (i=0;i<N;i++)
	{
		printf("%d\n", sum[i]);
	}	
	
printf ("\nKoniec programu.\n");	
pthread_exit(NULL);	
}

// -----------------------------------------------------------

void * thread_task(void *arg) {
	int id = *((int*)arg);
	
	int start = id * task_size;
	int end = start + task_size;
	if (end > M) end = M;
	
	printf("\t watek %d; iteracje kolumn %d - %d\n", id, start, end);
	
	int s[N];
	int i,j;
	for (i=0;i<N;i++) {
		s[i]=0;
		for (j=start;j<end;j++)
		{
			s[i]+= matrix[i][j];
			//printf("local: %d\n", s[i]);
		}	
	}
	
	pthread_mutex_lock(&mutex);
	for (i=0; i<N; i++)
		sum[i] += s[i];
	pthread_mutex_unlock(&mutex);
	return(NULL);	
}

void suma_sekwencyjnie() {
	int s[N], i, j;
	for (i=0;i<N;i++) {
	for (j=0;j<M;j++)
		s[i] += matrix[i][j];
	printf("%d\n", s[i]);	
	}
	
}

