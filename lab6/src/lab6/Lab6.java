/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab6;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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
    private static final int RANGE_FROM = 33, RANGE_TO = 95;
    private static final int RANGE = RANGE_TO - RANGE_FROM;
    
        
    private Random random;
    private Picture picture;    
    private AtomicInteger[] counterOfMarks;
    private AtomicInteger taskEned = new AtomicInteger(0);
    
    ExecutorService pool; // thread pool
    
    public Lab6() throws InterruptedException {
        
        random = new Random();
        picture = new Picture();
        pool = Executors.newFixedThreadPool(POOL_SIZE);        
        counterOfMarks = new AtomicInteger[RANGE];
        // ------------PART A------------------
        for (int i=0;i<RANGE;i++)
            counterOfMarks[i] = new AtomicInteger(0);
        
        for (int i=0;i<RANGE;i++) {            
            // adding new threads to pool
            pool.submit(new MarksCounterRunnable(i+RANGE_FROM));
            System.out.println("task #" + i + " has been started");
            
        }
        
        boolean tof = pool.awaitTermination(15, TimeUnit.SECONDS);
        if (tof) {
            System.out.println("All tasks has been ended");
        } else {
            System.out.println("Pool closed. Not all tasks has been ended");
            System.out.println("Task started: " + RANGE + ", task ended: " + taskEned.get());
        } System.out.println("---------------------------------------");
        
        for (AtomicInteger i : counterOfMarks) {
            int x = i.get();
            char c = (char) (x+RANGE_FROM);          
            System.out.print(c + ": ");
            for (int j=0;j<x;j++)
                System.out.print("=");
            System.out.println(" "+x);
        }
        
        
        // -----------------PART B -------------------
        System.exit(0);
                      
    }
    
    private class Picture {
        char[][] marks;

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
        private int counter = 0;
        public MarksCounterRunnable(int ascii) {
            asciiCode = ascii;
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
            counterOfMarks[asciiCode - RANGE_FROM].set(counter);
            System.out.println("task #" + (asciiCode-RANGE_FROM) + " has been ended");
            taskEned.incrementAndGet();
        }
//        int getCounter() {
//            return this.counter;
//        }        
    }
    
    
    
    
    
    
    
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        try {        
            Lab6 lab = new Lab6();
        } catch (InterruptedException ex) {
            Logger.getLogger(Lab6.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
