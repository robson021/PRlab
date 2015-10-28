#include<stdlib.h>
#include<stdio.h>
#include<pthread.h>
#include<math.h>
#define WATKI 4
#define DOKLADNOSC 1000

float dx;
float xp;
float xk;
float wynik=0;
pthread_mutex_t muteks;
pthread_t watki[WATKI];

inline double funkcja (double x)
{
return x*x + x + 4;
}

void *zadanie_watkow(void *arg_wsk)
{
int i,id;

float j,suma=0;
id=*((int*)arg_wsk);
j=DOKLADNOSC/WATKI;

for(i=(j*id)+1;i<=j*(id+1);++i)
suma+=funkcja(xp+i*dx);

pthread_mutex_lock(&muteks);
wynik+=suma;
pthread_mutex_unlock(&muteks);
pthread_exit((void*)0);
}

int main()
{

int i;
int indeksy[WATKI];

printf("Podaj poczatek przedzialu calkowania\n");
scanf("%f", &xp);
 
printf("Podaj koniec przedzialu calkowania\n");
scanf("%f", &xk);


dx=(xk-xp)/DOKLADNOSC;
printf("dx= %f\n",dx);
for(i=0;i<WATKI;++i)
indeksy[i]=i;

pthread_mutex_init(&muteks,NULL);

for(i=0;i<WATKI;++i)
pthread_create(&watki[i],NULL,zadanie_watkow,(void *)&indeksy[i]);

for(i=0;i<WATKI;++i)
pthread_join(watki[i],NULL);


wynik +=(funkcja(xp)+ funkcja(xk))/2;
wynik*=dx;

printf("Wartosc calki wnosi w przyblizeniu %f\n",wynik);

return 0;
}

