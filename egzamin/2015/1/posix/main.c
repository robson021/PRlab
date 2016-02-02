#include<stdlib.h>
#include<stdio.h>
#include<pthread.h>
#include<time.h>

//build: gcc -I/usr/include -L/usr/lib64 main.c -lpthread


/* termin I 2015
 * 1. Korzystając ze standardu Pthreada (muteksy, zmienne warunku),
 * napisać funkcję realizującą barierę. (kod z komentarzem)  
 */


// global data:
pthread_mutex_t mutex;
pthread_cond_t cond;

int done = 0;
const int n = 10; // threads num


//-------------------------

void * task (void *arg) {
	int id = *((int*) arg);
	
	// thread task is to sleep
	usleep( rand()%100000 + 999 );
	
	pthread_mutex_lock(&mutex);
	done++;
	
	 // Am I the last thread? Wake up other threads. 
	if (done >= n) {
		printf("\nALL THREADS ARE DONE\n\n");		
		pthread_cond_broadcast(&cond);
	} else // no?
	while (done < n) { // other threads have not finished job yet
		printf ("\tThread #%d condition wait. So far %d tasks are done.\n", id, done);
		
		/*
		 * barrier
		 * conditon wait until last thread will wake up others.
		 */
		pthread_cond_wait(&cond, &mutex);
	}
	
	pthread_mutex_unlock(&mutex);
	
	printf("\tThread #%d has finished his job.\n", id);
	return (NULL);
}




//-------------------------
int main() {
	pthread_mutex_init(&mutex, NULL);
	pthread_cond_init(&cond, NULL);
	
	srand( time( NULL ) );
	
	
	pthread_t threads[n];
	int ids[n];
	int i;
	for (i=0;i<n;i++) {
		ids[i]=i; // set thread ID
		pthread_create (&threads[i], NULL, task, &ids[i]);
	}
	
	for (i=0;i<n;i++) 
		pthread_join(threads[i], NULL);

printf ("\nKoniec programu.\n");	
pthread_exit(NULL);	
}
