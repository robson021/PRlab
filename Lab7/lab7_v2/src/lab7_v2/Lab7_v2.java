package lab7_v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Robert N.
 */
public class Lab7_v2 {
    private static final int READERS_COUNT = 1_000;
    private static final int MAX_PEOPLE_INSIDE = 10;
    
    private static final Random rng = new Random();
    private static final int RANGE = 10;
    private final List<Thread> threads;
    
    private final Lock lock;
    private final Condition readerCondition, writerCondition;
    
    private int readersInside = 0, writersInside = 0;
    private int writersTotal = 0, readersTotal = 0;
    private int isWriterWaiting = 0;
    
    public Lab7_v2() {
        lock = new ReentrantLock(true); // fair lock
        readerCondition = lock.newCondition();
        writerCondition = lock.newCondition();
        threads = new ArrayList<>();
    }
    
    private void openReadingRoom() throws InterruptedException {        
        for (int i=0;i<READERS_COUNT;i++) {
            Thread t = new Thread (new Reader());
            threads.add(t);
            t.start();
            
            if (rng.nextInt(50) == 0) { // low chance to roll new Writer    
                t = new Thread (new Writer());
                threads.add(t);
                t.start();
            }
        }
        
        for (Thread t : threads)
            t.join();
        
        System.out.println("\nAll threads: "+threads.size());
        System.out.println("Finished: "+ readersTotal+" + "+writersTotal+" = "+(writersTotal+readersTotal));
    }
    
    private class Reader implements Runnable {
        private final int TIME = rng.nextInt(RANGE) + 3;
        private boolean isLocked = false;
        
        @Override
        public void run() {
            lock.lock();
            isLocked = true;
            try {
                while (isWriterWaiting > 0 || readersInside == MAX_PEOPLE_INSIDE ||
                        writersInside > 0)
                    readerCondition.await();
                
                readersInside++;
                readersTotal++;
                printInfo();  
                
                if (isWriterWaiting==0 && readersInside < MAX_PEOPLE_INSIDE) {
                    readerCondition.signal();
                    lock.unlock();
                    isLocked = false;
                }
                
                Thread.sleep(TIME);
                readersInside--;
                
            } catch (InterruptedException ex) {
            } finally {
                if (isLocked)
                    lock.unlock();                               
            }
        }
        
    }
    
    private class Writer implements Runnable {
        private final int TIME = rng.nextInt(RANGE) + 3;

        @Override
        public void run() {
            lock.lock();
            isWriterWaiting++;
            try {
                while ((readersInside + writersInside) > 0)
                    writerCondition.await();
                
                writersInside++;      
                writersTotal++;
                isWriterWaiting--;
                printInfo();
                                
                Thread.sleep(TIME);            
                writersInside--;
                if (isWriterWaiting > 0)
                    writerCondition.signal();
                else readerCondition.signalAll();
                
            } catch (InterruptedException ex) {
            } finally {
                lock.unlock();
            }
        }
        
    }
    
    private void printInfo() {
        System.out.println("Readers inside: "+readersInside +". Writers inside: "+writersInside);
        if (readersInside > MAX_PEOPLE_INSIDE || writersInside > 1)
            System.out.println("ERROR! TOO MANY PEOPLE INSIDE");
    }

    
    public static void main(String[] args) {
        try {
            new Lab7_v2().openReadingRoom();
        } catch (InterruptedException ex) {
        }
        System.exit(0);
    }

    
    
}
