#include<stdlib.h>
#include<stdio.h>
#include<pthread.h>

//build: gcc -I/usr/include -L/usr/lib64 pub.c -lpthread

void * f_klient (void * arg);

int licznik=0;

typedef struct Pub {
int kufle;
//int goscie;
int sprzedane_piwa;
} Pub;

typedef enum { false, true } bool;

pthread_mutex_t muteks;

int main()
{
struct Pub pub;
// poczatkowe wartosci pubu:
pub.sprzedane_piwa = 0;

pthread_mutex_init(&muteks, NULL);

int n, i;
printf("Podaj ilu jest klientow w barze: ");
scanf ("%d", &n);
printf("Podaj ile jest wszystkich kufli: ");
scanf ("%d", &pub.kufle);
	
pthread_t watki[n];
for (i=0;i<n;i++) {
    pthread_create(&watki[i], NULL, f_klient, &pub);
    //printf("iteracja: %d\n", i);
    sleep(5); // czas na uruchomienie
}


for (i=0; i<n; i++) { // czekamy az wszyscy dopija browary
    pthread_join(watki[i], NULL);
}

printf("\nDzisiaj bylo %d klientow. Kupili w sumie %d piw.\n", n, pub.sprzedane_piwa);

	//printf("\twatek glowny: koniec pracy, watek odlaczony pracuje dalej\n");
	pthread_exit(NULL); // co stanie sie gdy uzyjemy exit(0)?
}

void * f_klient (void * arg) {

struct Pub *pub = arg;
int wypite = 0;
bool czy_kupil = false;
int id = ++licznik;

while (wypite < 2) {
	pthread_mutex_lock(&muteks);
	if (pub->kufle > 0) {
		//moze kupic piwo
                pub->kufle--;
                pub->sprzedane_piwa++;
                printf("klient #%d kupil piwo\n", id);
                sleep(3); // napelnianie kufla 3s
                czy_kupil = true;
	}
        pthread_mutex_unlock(&muteks);
        if (czy_kupil == true) {
            sleep(8); // pije 8s;
            wypite++;
            czy_kupil = false;
            // oddaje kufel
            pthread_mutex_lock(&muteks);
            pub->kufle++;
            pthread_mutex_unlock(&muteks);

            printf("klient #%d oddal kufel\n", id);            
        }
	sleep(1);	
}


return (NULL);
}


