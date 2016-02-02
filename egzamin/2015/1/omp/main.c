#include <stdlib.h>
#include <stdio.h>
#include <omp.h>

/*
 * I termin 2015
 * 
 * 2. Napisać krótki program pokazujący różnicę pomiędzy klauzula private,
 * a dyrektywą threadprivate w OpenMP 
 * 
 * 
 * build: gcc -fopenmp main.c
 * 
 */

int a = 0; // threadprivate
int b = 0; // private

int main() {
	#ifdef _OPENMP
	printf("rozpoznano open mp\n");
	#endif
	
	
#pragma omp threadprivate (a)
	
	omp_set_dynamic(0);     // Explicitly disable dynamic teams
	omp_set_num_threads(8);

	
#pragma omp default (none)  // -------------------- open
{
#pragma omp parallel private (b)
	{
		int id = omp_get_thread_num();
		//printf("\tobszar rownolegly, watek #%d\n", id);
		a = id + 10;
		b=a;
		printf("\tobszar rownolegly, watek #%d - moja zmienna threadprivate = %d,\n\tprivate = %d\n", id, a, b); 
	}
	
	printf("\n\n");

#pragma omp barrier
#pragma omp single
{
	printf ("\nb = %d\n", b);
}


#pragma omp parallel private (b)
{
	// kolejny obszar rownolegly, zmienna threadprivate powinna zachowac wartosci z poprzedniego obszaru
	a += 100;
	b += 100;
	printf("\tobszar rownolegly nr II, watek #%d - moja zmienna threadprivate = %d,\n\tprivate = %d\n", omp_get_thread_num(), a, b);
}	

} // ---------------------------------------------- close
return 0;	
}
