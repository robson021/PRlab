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
                            //waitCondition = lock.newCondition();
        
    private final List<String> message = new ArrayList<>();
    private int readersInside = 0;
    private int writersInside = 0;
    private int writersWaiting = 0;
    
    public String getMessage() throws InterruptedException {
        lock.lock();   
        try {
            while (message.isEmpty() || writersInside > 0 || writersWaiting > 0)
                readerCondition.await();
            
            readersInside++;    
            
            //printInfo(); 
            //waitCondition.await();
            Thread.sleep(2);
            readersInside--;
                       
            return message.toString();  
            
        } finally { 
            if (message.isEmpty()) 
                writerCondition.signal();                        
            else readerCondition.signal();            
            lock.unlock();
        }
    }
    
    /**
     *
     * @param msg the new message, that will be added to the List
     * @throws InterruptedException
     */
    public void writeMessage(String msg) throws InterruptedException {
        lock.lock();
        writersWaiting++;
        try {
            while ((readersInside + writersInside) > 0) {
                //if (readersInside >=10)
                    //waitCondition.signalAll();
                writerCondition.await();
            }
            
            writersInside++;
            //printInfo();
            message.add(msg);            
            Thread.sleep(2);
            writersInside--;
            writersWaiting--;
            
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

//    private void printInfo() {
//        System.out.println("Readers: "+readersInside+". Writers: "+writersInside);
//    }
    
}
