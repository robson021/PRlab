#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include "pomiar_czasu.h"
#include <time.h>
#include <math.h>
//#include <unistd.h


// build: gcc -I/usr/include -L/usr/lib64 main.c -lpthread -L pomiar_czasu.h pomiar_czasu.c


typedef struct Dane {int a,b; int n, id, k; } Dane;
double wynik = .0;

pthread_mutex_t m;

inline double fun (double x) {
    return x*x + x + 4;
}

double licz_pole (int a, int b, int n) {
  double h = (b-a)/(double)n; //wysokosć trapezów
  double S = 0.0; //zmienna będzie przechowywać sumę pól trapezów
  double podstawa_a = fun(a), podstawa_b;
  
  int i;
  for(i=1;i<=n;i++)
  {
    podstawa_b = fun(a+h*i);
    S += (podstawa_a+podstawa_b);
    podstawa_a = podstawa_b;
  }
  
  return (.5 * S * h);
}

void * watek_licz_pole (void *arg) {
    
  Dane *dane = arg;  
  
  int a = dane->a;
  int b = dane->b;
  double n = dane->n;
  
/*
  Dane dane = *((Dane*) arg);
  double a = dane.a;
  double b = dane.b;
  double n = dane.n;  
*/
    
  double h = (b-a)/n; //wysokosć trapezów
  double S = 0.0; //zmienna będzie przechowywać sumę pól trapezów
  double podstawa_a = fun(a), podstawa_b;
  
  int i;
  for(i=1;i<=n;i++)
  {
    podstawa_b = fun(a+h*i);
    S += (podstawa_a+podstawa_b);
    podstawa_a = podstawa_b;
  }
  
  // synchronizacja:
  
  pthread_mutex_lock (&m);
  wynik += (.5 * S * h);
  pthread_mutex_unlock (&m);
  
 
  return (NULL);
}

void * watek_licz_pole2 (void * arg) {
  Dane *dane = arg;
  int a = dane->a;
  int b = dane->b;
  double n = dane->n;
  int i, j, id = dane->id;
  int k = dane->k;
  
  double S =.0;
  double h = (b-a)/n;
  
  j = n/k;  
  i=(j*id)+1;
  
  double podstawa_a = fun(a*i*h), podstawa_b;
  
  for( ;i<=j*(id+1);++i) {
    podstawa_b = fun(a+h*i);
    S += (podstawa_a+podstawa_b);
    podstawa_a = podstawa_b;
  }
  
  // synchronizacja:  
  pthread_mutex_lock (&m);
  wynik += (.5 * S * h);
  pthread_mutex_unlock (&m);
  
  return (NULL);
}

int main() {
    int n, a, b;
    printf("Podaj a: ");
    scanf("%d", &a);
    printf("Podaj b: ");
    scanf("%d", &b);
    printf("Podaj dokladnosc: ");
    scanf("%d", &n);
    
    if (b<a) {
        return 0;
    }       
    
    pthread_mutex_init (&m, NULL);
    // sekwencyjnie
    inicjuj_czas();
    double w = licz_pole(a, b, n);
    printf("\tWynik calki sekwencyjnie: %f\n", w);
    drukuj_czas();  
    
    int k=2;
    
    for ( ;k<=6;k+=2) {
    
    wynik = .0;    
        
    int i; //k=4;
    Dane dane[k];    
    for (i=0;i<k;i++) {
        dane[i].a=a;
        dane[i].b=a;
        dane[i].n=n;
    }
    // dekompozycja
    pthread_t watki[k];
    double x = (b-a)/k; // x-wielkosc przedzialow dla k-watkow
    inicjuj_czas();
    for(i=0;i<k;i++){        
        dane[i].b += x;
        pthread_create(&watki[i], NULL, watek_licz_pole, &dane[i]);
        //sleep(1);
        dane[i+1].a = dane[i].b;
        dane[i+1].b = dane[i].b;
    }   
    // czekamy na watki    
    for (i=0;i<k;i++) {
        pthread_join(watki[i], NULL);
    }
    printf("\tWynik calki liczonej na %d watkach (dekompozycja): %f \n", k, wynik);
    drukuj_czas();
    
    // zrownoleglenie petli
    
    wynik = .0;
    Dane dane2[k];
    for (i=0;i<k;i++) {
        dane2[i].a=a;
        dane2[i].b=b;
        dane2[i].n=n;
        dane2[i].id=i;
        dane2[i].k=k;
    }    
    pthread_t threads[k];
    inicjuj_czas();
    for (i=0;i<k;i++) {
        pthread_create(&threads[i], NULL, watek_licz_pole2, &dane2[i]);
    }    
    for (i=0;i<k;i++)
        pthread_join(threads[i], NULL);
    
    printf("\tWynik calki liczonej na %d watkach (loop paralleism): %f \n", k, wynik);
    drukuj_czas();
    printf("\n");
    
    }
    
    pthread_exit(NULL);
    //return (EXIT_SUCCESS);
}

