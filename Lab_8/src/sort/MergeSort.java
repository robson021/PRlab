package sort;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Robert N.
 */
public class MergeSort {
    private static final Random random = new Random();
    
    
    
    
    private void merge(
            int[] tab1,
            int[] tab2,
            int[] scal_tab) {

        int i = 0, j = 0, k = 0;

        while ((i < tab1.length) && (j < tab2.length)) {

            if (tab1[i] < tab2[j]) {
                scal_tab[k] = tab1[i++];
            } else {
                scal_tab[k] = tab2[j++];
            }

            k++;
        }

        if (i == tab1.length) {

            for (int a = j; a < tab2.length; a++) {
                scal_tab[k++] = tab2[a];
            }

        } else {

            for (int a = i; a < tab1.length; a++) {
                scal_tab[k++] = tab1[a];
            }

        }
    }
    
    public static void main(String[] args) {
        MergeSort ms = new MergeSort();
        int[] array = new int[100];
        for (int i=0;i<array.length;i++)
            array[i] = random.nextInt(999999);
        
        System.exit(0);
    }
}
