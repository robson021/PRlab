#include<stdlib.h>
#include<stdio.h>
#include<pthread.h>

//build: gcc -I/usr/include -L/usr/lib64 main.c -lpthread

typedef struct Pub {
    int kufle;
    int sprzedane;
    int krany;
} Pub;

Pub pub;

typedef enum {
    false, true
} bool;

pthread_mutex_t mutex;
pthread_cond_t warunek, warunek_kran;
void * klient2(void *arg); // lock / wait-signal
void * klient3(void *arg); // krany, lock/wait-signal

void * klient(void *arg) { // busy wait
    int id = *((int*) arg);
    int wypite = 0;
    bool dostal = false;

    while (wypite < 2) {
        pthread_mutex_lock(&mutex);
        if (pub.kufle > 0) {
            // nalewa
            printf("BARMAN:  Nalewam piwo dla #%d \n", id);
            usleep(3);
            pub.kufle--;
            pub.sprzedane++;
            dostal = true;
        }
        pthread_mutex_unlock(&mutex);

        if (dostal == true) {
            wypite++;
            usleep(20);
            pthread_mutex_lock(&mutex);
            // oddaje kufel
            pub.kufle++;
            printf("KLIENT #%d oddal kufel.\n", id);
            pthread_mutex_unlock(&mutex);
            dostal = false;
        }
        usleep(1);
    }

}

int main() {
    pthread_mutex_init(&mutex, NULL);
    int i, n, k, kr;
    pthread_cond_init(&warunek, NULL);
    pthread_cond_init(&warunek_kran, NULL);
    printf("Podaj ilosc kufli: ");
    scanf("%d", &k);
    pub.kufle = k;
    printf("Podaj ilosc kranow: ");
    scanf("%d", &kr);
    pub.krany = kr;
    printf("Podaj ilosc klientow: ");
    scanf("%d", &n);
    int id_tab[n];
    pthread_t watki[n];

    for (i = 0; i < n; i++) {
        id_tab[i] = i + 1;
        pthread_create(&watki[i], NULL, klient3, &id_tab[i]);
    }

    for (i = 0; i < n; i++)
        pthread_join(watki[i], NULL);

    printf("Pub zamkniety. Mielismy %d klientow, sprzedano %d piw.\n", n, pub.sprzedane);
    pthread_exit(NULL);
}

//------------------------------------

void * klient2(void *arg) {
    int id = *((int*) arg);
    int wypite = 0;
    //bool dostal = false;

    while (wypite < 2) {
        pthread_mutex_lock(&mutex);
        while (pub.kufle <= 0)
            pthread_cond_wait(&warunek, &mutex);

        pub.kufle--;
        pub.sprzedane++;
        wypite++;
        printf("BARMAN:  Nalewam piwo dla #%d \n", id);
        usleep(3);
        pthread_mutex_unlock(&mutex);
        usleep(20); // pije

        pthread_mutex_lock(&mutex);
        pub.kufle++;
        printf("KLIENT #%d oddal kufel.\n", id);
        pthread_cond_signal(&warunek);
        pthread_mutex_unlock(&mutex);

    }
    return (NULL);
}

void * klient3(void *arg) {
    int id = *((int*) arg);
    int wypite = 0;
    //bool dostal = false;

    while (wypite < 2) {
        pthread_mutex_lock(&mutex);
        while (pub.kufle <= 0)
            pthread_cond_wait(&warunek, &mutex);
        while (pub.krany <= 0)
            pthread_cond_wait(&warunek_kran, &mutex);

        pub.krany--;

        pub.kufle--;
        pub.sprzedane++;
        wypite++;
        printf("BARMAN:  Nalewam piwo dla #%d \n", id);
        usleep(3);
        pub.krany++;
        pthread_cond_signal(&warunek_kran);
        pthread_mutex_unlock(&mutex);
        usleep(20); // pije

        pthread_mutex_lock(&mutex);
        pub.kufle++;
        printf("KLIENT #%d oddal kufel.\n", id);
        pthread_cond_signal(&warunek);
        pthread_mutex_unlock(&mutex);

    }
    return (NULL);
}