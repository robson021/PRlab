/* 
 * File:   main.c
 * Author: Robert N.
 *
 * Created on 11 listopada 2015, 12:44
 */

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

/*
 * build: gcc -I/usr/include -L/usr/lib64 main.c -lpthread
 */


const int SIZE = 800;
const int SIZE_2 = 5; // 800/150 = 5
pthread_mutex_t mutex;
pthread_cond_t reader_condition, writer_condition;
int writers_waiting = 0;

void * reader_task (void *arg);
void * writer_task (void *arg);

int main() {
    
    pthread_t reader_threads[SIZE];
    pthread_t writer_threads[SIZE_2];
    pthread_mutex_init(&mutex, NULL);
    pthread_cond_init(&reader_condition, NULL);
    pthread_cond_init(&writer_condition, NULL);
    
    int i;
    int j=0;
    for (i=0;i<SIZE;i++) {
        pthread_create(&reader_threads[i], NULL, reader_task, NULL);
        if ((i+1)%150 == 0)
            pthread_create(&writer_threads[j++], NULL, writer_task, NULL);
    }
    
    for (i=0;i<SIZE;i++)
        pthread_join(reader_threads[i], NULL);
    for (i=0;i<SIZE_2;i++)
        pthread_join (writer_threads[i], NULL);
    
    pthread_exit(NULL);
    //return (EXIT_SUCCESS);
}

void * reader_task (void *arg) {
    
    return (NULL);
}

void * writer_task (void *arg) {
    return (NULL);
}
