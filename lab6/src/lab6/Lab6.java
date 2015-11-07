/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab6;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Robert N.
 * 
 * Histogram.
 * Liczenie wystąpień znaków w obrazie i prezentacja wyników za pomocą wykresów w konsoli.
 * 
 */
public class Lab6 {
    private static final int MARKS_SIZE = 20;
    private static final int POOL_SIZE = 8;
    private static final int RANGE_FROM = 33, RANGE_TO = 97;
    //private static final int RANGE = RANGE_TO - RANGE_FROM;
    private static final int THREAD_COUNT = 4;
    private static final int TASKS_SIZE = MARKS_SIZE / THREAD_COUNT;
    
    private static ReentrantLock lock = new ReentrantLock();
    private Thread[] threads;

    
        
    private Random random;
    private Picture picture;    
    private int[] counterOfMarks;
    private AtomicInteger taskEned = new AtomicInteger(0);
    
    ExecutorService pool; // thread pool
    
    public Lab6() throws InterruptedException {        
        random = new Random();
        picture = new Picture();
        pool = Executors.newFixedThreadPool(POOL_SIZE);        
        counterOfMarks = new int[RANGE_TO];
        
        
        // ------------ PART A------------------
        for (int i=0;i<RANGE_TO;i++)
            counterOfMarks[i] = 0;
        
        for (int i=0;i<RANGE_TO;i++) {            
            // adding new threads to pool
            pool.submit(new MarksCounterRunnable(i+RANGE_FROM));
            //System.out.println("task #" + i + " has been started");
            //Thread.sleep(100);
        }
        // threads are Runnable objects, not Callable-Future. Will always return false
        boolean tof = pool.awaitTermination(5, TimeUnit.SECONDS); // wait 15s for threads
        if (tof) {
            System.out.println("All tasks has been ended");
        } else {
            System.out.println("Pool closed. Not all tasks has been ended");
            System.out.println("Task started: " + RANGE_TO + ", task ended: " + taskEned.get());            
        } System.out.println("---------------------------------------");
        pool=null;
        
        
        // ----------------- PART B -------------------
        
        for (int i=0;i<counterOfMarks.length;i++)
            counterOfMarks[i] = 0;        
        
        threads = new Thread[THREAD_COUNT];
        for (int i=0;i<threads.length;i++) {
            threads[i] = new Thread(new MarksCounter2Runnable(i));
            threads[i].start();
            //Thread.sleep(3);
        }
        
        for (int i=0;i<threads.length;i++)
            threads[i].join();
        
        for (int val, i=0;i<counterOfMarks.length;i++) {
            char c = (char) (i+RANGE_FROM);
            val = counterOfMarks[i];
            System.out.print(c + ": ");
            for (int j=0;j<val;j++) {
                System.out.print("=");
            } System.out.println(" x"+val);
        }
                      
    }
    
    private class Picture {
        private char[][] marks;

        public Picture() {
            marks = new char[MARKS_SIZE][MARKS_SIZE];
            for (int i=0;i<MARKS_SIZE;i++) {
                for (int j=0;j<MARKS_SIZE;j++) {
                    marks[i][j] = (char) (random.nextInt(RANGE_TO) + RANGE_FROM);
                    System.out.print(marks[i][j] + " ");
                }
                System.out.println("");
            }
            System.out.println("---------------------------------------");
        }
        
        public char getCharOnIndex (int i, int j) {
            return marks[i][j];
        }
        
    }
    
    private class MarksCounterRunnable implements Runnable {       
        private final int asciiCode;
        private final char c;
        private int counter = 0;
        public MarksCounterRunnable(int ascii) {
            asciiCode = ascii;
            c = (char) ascii;
            //System.out.println("Searching for: " + (char) asciiCode);
        }       

        @Override
        public void run() {
            for (int i=0;i<MARKS_SIZE;i++)
                for (int j=0;j<MARKS_SIZE;j++) {
                    int x = picture.getCharOnIndex(i, j);
                    if (x == asciiCode)
                        counter++;
                }
            
            //System.out.print((char)asciiCode + " wystąpienia: " + counter);
//            lock.lock();
//            try {
//                counterOfMarks[asciiCode - RANGE_FROM] = this.counter;                 
//            } finally {
//                lock.unlock();
//            }
            
            
                System.out.print("'"+c+"'"+": ");
                for (int i=0;i<counter;i++) {
                    System.out.print("=");
                } System.out.println(" x" + counter);
            
            
            
            //System.out.println("task #" + (asciiCode-RANGE_FROM) + " has been ended");           
            taskEned.incrementAndGet();
           
        }
     
    }
    
    private class MarksCounter2Runnable implements Runnable {
        private final int ID;
        private final int STARTING_POINT;
        private final int ENDING_POINT;
        
        public MarksCounter2Runnable(int id) {
            ID=id;         
            STARTING_POINT=id*TASKS_SIZE;
            ENDING_POINT=STARTING_POINT + TASKS_SIZE;
        }

        @Override
        public void run() {
            System.out.println("thread #" +(ID+1)+ " started");
            int x, index;
            for (int i=STARTING_POINT;i<ENDING_POINT;i++)
                for (int j=0;j<MARKS_SIZE;j++) 
                {
                    x=picture.getCharOnIndex(i, j);
                    index = x - RANGE_FROM;
                    lock.lock();
                    try {  
                        //System.out.println("index: "+x);
                        counterOfMarks[index]++;                        
                    } finally {
                        lock.unlock();
                    }
                }
        }
        
    }
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        try {        
            Lab6 lab = new Lab6();
            System.exit(0);
        } catch (InterruptedException ex) {
            Logger.getLogger(Lab6.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}