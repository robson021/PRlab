/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab7_rwlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author robert
 */
public class ReadingRoom {
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(true);
    private final Lock readLock = rwLock.readLock();
    private final Lock writeLock = rwLock.writeLock();
    private AtomicInteger readersTotal = new AtomicInteger(0),
                          readersInside = new AtomicInteger(0),
                          writersInside = new AtomicInteger(0);
    private static final int READERS_TOTAL = 500;
    
    
    private static final Random rng = new Random();
    
    private final List<Thread> threads;
    private final List<String> messages;
    
    public ReadingRoom() throws InterruptedException {
        threads = new ArrayList<>();
        messages = new ArrayList<>();
        
        threads.add(new Thread(new Writer(this)));
        for (int i=0;i<READERS_TOTAL;i++) {
            threads.add(new Thread(new Reader(this)));
            if (rng.nextInt(75) == 0) {
                threads.add (new Thread(new Writer(this)));
                Thread.sleep(1); // to avoid same number generation
            }
        }
    }
    
    public void openReadingRoom() throws InterruptedException {
        for (Thread t : threads)
            t.start();
        for (Thread t : threads)
            t.join();
        
    }
    
    /**
     *
     * @return Messages as String from message List.
     */
    public String getMessages() throws InterruptedException {
        readLock.lock();
        try {            
            System.out.println("Readers inside: "+readersInside.incrementAndGet());
            Thread.sleep(rng.nextInt(5)+1);
            return messages.toString();
        } finally {
            if (readersTotal.incrementAndGet() == READERS_TOTAL)
                Writer.stopAll();
            readersInside.decrementAndGet();
            readLock.unlock();
        }
    }    
    
    /**
     *
     * @param msg the message that will be added to the List.
     */
    public void addNewMessage(String msg) throws InterruptedException {
        writeLock.lock();
        try {
            System.out.println("Writers inside: "+writersInside.incrementAndGet());
            Thread.sleep(rng.nextInt(10)+3);
            messages.add(msg);            
        } finally {
            writersInside.decrementAndGet();
            writeLock.unlock();
        }
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new ReadingRoom().openReadingRoom();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }
    
}
