package integral;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Robert N.
 */
public class Integral {
    private final int NUM_THREADS;
    private ExecutorService pool; // thred pool
    //private List<Thread> threadList;
    private double integralValue = .0;
    private Object lock = new Object();
    private double n;
    
    public Integral() {
        int num_processors = Runtime.getRuntime().availableProcessors();
        //if (num_processors < 4)
            //num_processors = 4; // force at least 4 threads
        NUM_THREADS = num_processors;
        
        pool = Executors.newFixedThreadPool(NUM_THREADS);
        //threadList = new ArrayList<>();
    }
    
    private class IntegralTask_LoopParallelism implements Runnable {
        private int a, b, id;

        private IntegralTask_LoopParallelism(int a, int b, int i) {
            this.a=a; this.b=b; this.id=i;
        }

        @Override
        public void run() {
            int i, j;
            double sum = .0;
            double h = (b - a) / n;

            j = (int) (n / NUM_THREADS);
            i = (j * id) + 1;

            double base_a = function(a * i * h), base_b;
            
            System.out.println(i +" -> "+(j*(id+1))+" ; id = "+id);

            for (; i <= j * (id + 1); ++i) {
                base_b = function(a + h * i);
                sum += (base_a + base_b);
                base_a = base_b;
            }
            sum = sum * 0.5 * h; // (a+b)*h/2
            System.out.println("\t"+sum);
            synchronized (lock) {
                integralValue += sum;
            }
        }
        
    }
    
    public void calculateIntegral () throws IOException, InterruptedException {
        integralValue = .0;
        int a, b;        
        Scanner in = new Scanner(System.in);
        
        System.out.print("a: ");
        a = in.nextInt();
        System.out.print("b: ");
        b = in.nextInt();
        System.out.print("n: ");
        n = in.nextInt();
        
        if(b<a)
            System.exit(1);
                
        int size = (int) ((b-a)/NUM_THREADS);        
        for (int i=0; i<NUM_THREADS; i++) {
            b += size;
            pool.submit(new IntegralTask_LoopParallelism(a, b, i));
            a=b;
        }
        Thread.sleep(10);
        //pool.awaitTermination(5, TimeUnit.SECONDS);
        pool.shutdown();
        System.out.println("Integral value = "+integralValue);
        
    }
    
    private double function (double x) {
        return ( x*x + 4 );
    }    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new Integral().calculateIntegral();
        } catch (IOException ex) {
        } catch (InterruptedException ex) {
        }
        
        System.exit(0);
    }
    
}
