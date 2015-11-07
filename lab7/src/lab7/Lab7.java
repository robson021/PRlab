/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab7;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author robert
 */
public class Lab7 {
    private static final int TOTAL_READERS = 100;  
    private static final int MAX_PEOPLE_INSIDE = 3;
    private static final Random random = new Random();
    private List<Thread> threads;
    
    private int readers=0, writers=0, totalPeople=0;
    //private Semaphore semaphore;
    
    private Lock lock;
    private Condition enteranceCondition; 
    private Condition insideCondition;
    
    //private ExecutorService pool;
    
    public Lab7() {
        lock = new ReentrantLock(true);        
        enteranceCondition = lock.newCondition();
        insideCondition = lock.newCondition();
        //semaphore = new Semaphore();
        threads = new ArrayList<>();
        //pool = Executors.newFixedThreadPool(12);
    }
    
    
    
    public void openReadingRoom() throws InterruptedException {  
        //new Thread(semaphore).start();
        
        for (int i=0;i<TOTAL_READERS;i++) {
            Thread t = new Thread(new Reader());
            threads.add(t);
            t.start();
            
            if ( ((i+1) % 10) == 0) {
                Thread t2 = new Thread(new Writer());
                threads.add(t2);
                t2.start();
            }
        }
        
        for (Thread t : threads)
            t.join();  
        //semaphore.stop();
        System.out.println("Reading room closed. Total people today: "+totalPeople);
    }
    
    
    private class Reader implements Runnable {
        private final int TIME = random.nextInt(150);

        @Override
        public void run() {
            lock.lock();
            try {
                while (writers>0 || (MAX_PEOPLE_INSIDE <=(readers+writers)))
                    enteranceCondition.await();
                
                
                totalPeople++;
                readers++;
                System.out.print("Reader #"+Thread.currentThread().getId()+" has entered for "+TIME+" ms. ");
                System.out.println("Readers inside: "+readers+". Writers inside: "+writers);
                insideCondition.await(TIME, TimeUnit.MILLISECONDS);
                
                readers--;
                enteranceCondition.signal();                
                
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                System.out.println("Reader #"+Thread.currentThread().getId()+" exited.");
                lock.unlock();                
            }            
        }

    }    
    
    private class Writer implements Runnable {
        private final int TIME = random.nextInt(250);

        @Override
        public void run() {
            lock.lock();
            try {
                while ( readers > 0 ) 
                    enteranceCondition.await();
                
                writers++;
                totalPeople++;
                System.out.print("Writer #"+Thread.currentThread().getId()+" has entered for "+TIME+" ms.");
                System.out.println(" Readers inside: "+readers+". Writers inside: "+writers);
                insideCondition.await(TIME, TimeUnit.MILLISECONDS);
                
                writers--;
                enteranceCondition.signal();
                
            } catch (InterruptedException ex) {
            } finally {
                System.out.println("Writer #"+Thread.currentThread().getId()+" exited.");
                lock.unlock();
            }
        }
        
    }
    
    
       
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new Lab7().openReadingRoom();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }        
        System.exit(0);
    }

    
}
