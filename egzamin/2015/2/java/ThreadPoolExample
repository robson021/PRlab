import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Termin II 2015
 * 
 * 1.Korzystając z puli wątków napisać program przeglądania listy
 * (z funkcją przetwórz_węzeł(int id) i id=następny(id), początek jako id==1,
 * koniec jakom id==0)
 * 
 */

public class ThreadPoolExample {
	
	private List<Integer> list; 
	private int n; // num_threads
	private Random random;
	
	private ExecutorService threadPool;
	
	public ThreadPoolExample() {
		n = Runtime.getRuntime().availableProcessors();
		if (n<4) n=4;
		
		list = new ArrayList<>();
		random = new Random();
		
		for (int i=0;i<100;i++) 
			list.add(random.nextInt(9999));
		
		threadPool = Executors.newFixedThreadPool(n);
	}
	
	
	public void runProgram() {
		for (int i=0; i<15; i++) {
			Runnable r = new WorkerRunnable();
			threadPool.execute(r);			
		}
		threadPool.shutdown();
	}
	
	private class WorkerRunnable implements Runnable {
		
		@Override
		public void run() {
			long ID = Thread.currentThread().getId();	
			System.out.println("Thread #"+ID+"created.");
			for (int i = 0; i < 5; i++)
			{
				//przegladaj
				int val = list.get(random.nextInt(list.size()));
				System.out.println("Thread #"+ID + "; val: "+val);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Thread #"+ID+" done.");
		}
		
	}

	public static void main(String[] args) {
		new ThreadPoolExample().runProgram();
		System.out.println("\n\t END");
	}

}
