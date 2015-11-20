/* 
 * File:   main.c
 * Author: robert
 *
 * Created on 31 października 2015, 17:28
 */

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

// build: gcc -I/usr/include -L/usr/lib64 main.c -lpthread


typedef enum {false, true} bool;

typedef struct Pub {
    int kufle;
    int sprzedane;
} Pub;

Pub pub;

void * watek_klient (void *arg);

pthread_mutex_t m;

int main() {
    
    
    //pub.kufle = 10;
    pub.sprzedane = 0;
    
    int n, k;
    printf("podaj ilosc klientow: ");
    scanf ("%d", &n);
    printf("podaj ilosc kufli: ");
    scanf("%d", &k);
    
    pub.kufle=k;
    
    pthread_mutex_init(&m, NULL);
    pthread_t watki[n];
    int i, id[n];
    
    for (i=0;i<n;i++) {
        id[i]=i+1;
        pthread_create(&watki[i], NULL, watek_klient, &id[i]);
    }
    
    for (i=0;i<n;i++) 
        pthread_join(watki[i], NULL);
    
    printf("\n\tBar zamkniety.\nMielismy dzisiaj %d klientow. Sprzedano %d piw.\n", n, pub.sprzedane);
    
    pthread_exit(NULL);
    //return (EXIT_SUCCESS);
}

void * watek_klient (void *arg) {
    int id = *((int*)arg);
    int wypite = 0;
    bool czy_kupil = false;
    
    while (wypite<2) {
    
        pthread_mutex_lock(&m);
        if (pub.kufle > 0) {
            pub.kufle--;
            pub.sprzedane++;
            printf("klient #%d kupil piwo\n", id);
            sleep(3); // 3s trwa lanie piwa - 1 kran
            czy_kupil = true;
        }
        pthread_mutex_unlock(&m);
        if (czy_kupil == true) {
            sleep(8); // pije 8s
            wypite++;
            czy_kupil = false;
            
            //oddaj kufel
            pthread_mutex_lock(&m);
            pub.kufle++;            
            pthread_mutex_unlock(&m);
            printf("klient #%d oddal kufel\n", id);
        }
        sleep(1); 
    }
}