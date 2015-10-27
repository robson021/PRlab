#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

// build: gcc -I/usr/include -L/usr/lib64 main.c -lpthread

typedef struct Dane {double a,b; int n; } Dane;
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
  
  double a = dane->a;
  double b = dane->b;
  double n = dane->n;
    
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
  
  // synchronizacja:
  
  //pthread_mutex_lock (&m);
  wynik += .5 * S * h;
  //pthread_mutex_unlock (&m);
  
  
  //dane->w = S*0.5*h;
  //arg = dane;
  
  //return arg;
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
    
    Dane dane;
    dane.n=n;
    
    pthread_mutex_init (&m, NULL);
    // sekwencyjnie
    double w = licz_pole(a, b, n);
    printf("Wynik calki sekwencyjnie: %f\n", w);
 
    // podzadania na watkow
    int i, k=5;
    dane.a=a;
    dane.b=a;
    
    pthread_t watki[k];
    double x = (b-a)/k; // x przedzialow dla k watkow
    for(i=0;i<k;i++){
        dane.b += x;
        pthread_create(&watki[i], NULL, watek_licz_pole, &dane);
        sleep(1);
        dane.a = dane.b;
    }   
    // czekamy na watki    
    for (i=0;i<k;i++) {
        pthread_join(watki[i], NULL);
    }
    printf("Wynik calki liczonej na %d watkach: %f \n", k, wynik);
    
    pthread_exit(NULL);
    //return (EXIT_SUCCESS);
}

