/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab7_czytelnia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Robert N.
 */
public class ReadingRoom {
    
    private final Lock lock = new ReentrantLock(true);
    private final Condition readerCondition = lock.newCondition(),
                            writerCondition = lock.newCondition();
    private boolean isNotified = false;
        
    private final List<String> message = new ArrayList<>();
    private int readersInside = 0;
    private int writersInside = 0;
    
    public String getMessage() throws InterruptedException {
        lock.lock();
        try {
            while (message.isEmpty() || writersInside > 0)
                readerCondition.await();
            
            readersInside++;            
           
            Thread.sleep(2);          
            
            readersInside--;
            return message.toString();            
        } finally { 
            if (message.isEmpty() && !isNotified) {
                isNotified = true;
                writerCondition.signal();
            }
            
            else readerCondition.signal();
            lock.unlock();
        }
    }
    
    public void writeMessage(String msg) throws InterruptedException {
        lock.lock();
        try {
            while ((readersInside + writersInside) > 0)
                writerCondition.await();
            
            writersInside++;
            
            message.add(msg);            
            isNotified = false;
            Thread.sleep(2);
            writersInside--;
            
        } finally {
            readerCondition.signalAll();
            lock.unlock();
        }
    }
    
    private void open() throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        
        
        for (int i=0;i<500;i++) {
            threads.add(new Thread(new Reader(this)));
            
            if ((i+1)%20 == 0)                
                new Thread(new Writer(this)).start();
            
        }
        for (Thread t : threads)
            t.start();        
        
        for (Thread t : threads)
            t.join();
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new ReadingRoom().open();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }
    
}
