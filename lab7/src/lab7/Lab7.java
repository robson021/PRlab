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
 * Czytelnia. Problem pisarzy i czytelników.
 * 
 */


public class Lab7 {
    
    private static final int READERS_TOTAL = 8_000;
    //private static final int MAX_PEOPLE_INSIDE = 15;
    
    private static final Random rng = new Random();
    private static final int RANGE = 10;
    private List<Thread> threads;    
    
    private Lock lock;
    private Condition readerCondition;
    private Condition writerCondition;
    
    private boolean isWriterWaiting = false;
    
    private int readersThatEntered = 0;
    private int writersThatEntered = 0;   
    
    private int readersTotal=0, writersTotal=0;
    private int totalPeopleToday = 0;
    
    public Lab7() {
        lock = new ReentrantLock(true);
        readerCondition = lock.newCondition();
        writerCondition = lock.newCondition();
        threads = new ArrayList<>();
    }
   

    private void openReadingRoom() throws InterruptedException {
        /* to high "READERS_TOTAL" count may lead to 
        "OutOfMemoryError" due to too high thread count*/
        
        for (int i=0; i<READERS_TOTAL;i++) {
            Thread t = new Thread(new Reader());
            threads.add(t);
            t.start();
            
            if ((i+1)%(rng.nextInt(10)+1) == 0) { // every X readers add new writer to queue
                Thread t2 = new Thread(new Writer());
                threads.add(t2);
                t2.start();
            }
        }
        
        for (Thread t : threads) 
            t.join();
        
        // check out
        System.out.println("\n"+readersTotal+" + "+writersTotal+" = "+(writersTotal+readersTotal));
        System.out.println("Total peple today: "+totalPeopleToday);
    }
    
    private class Reader implements Runnable {
        private final int TIME = rng.nextInt(RANGE) + 3;

        @Override
        public void run() {
            
            lock.lock();
            try {
                while (writersThatEntered > 0 || isWriterWaiting)
                    readerCondition.await();
                
                readersThatEntered++;
                totalPeopleToday++;
                readersTotal++;
                printInfo();
                
                readerCondition.signal();
                readerCondition.await(TIME, TimeUnit.MILLISECONDS); // reading time      
                
                
                
                
            } catch (InterruptedException ex) {
            } finally {
                readersThatEntered--;
                if (readersThatEntered == 0)
                    writerCondition.signalAll();
                lock.unlock();
            }
        }
        
    }
    
    private class Writer implements Runnable {
        private final int TIME = rng.nextInt(RANGE) + 3;

        @Override
        public void run() {
            
            lock.lock();
            try {
                isWriterWaiting = true;
                while (readersThatEntered > 0)
                    writerCondition.await();
                
                isWriterWaiting = false;
                writersThatEntered++;
                totalPeopleToday++;
                writersTotal++;
                printInfo();
                
                writerCondition.signal();
                writerCondition.await(TIME, TimeUnit.MILLISECONDS); // writing time
                
                
            } catch (InterruptedException ex) {
            } finally {
                writersThatEntered--;
                if  (writersThatEntered == 0)
                    readerCondition.signalAll();
                lock.unlock();
            }            
        }                      
    }
    
    private void printInfo() {
        System.out.println("Readers inside: "+readersThatEntered +". Writers inside: "+writersThatEntered);
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
