package lab7_readers_writers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author robert
 */



public class ReadingRoom {
    private static final int NUM_THREADS = 500;
    
    private final Lock lock = new ReentrantLock();
    private final Condition readerCondition = lock.newCondition(),
                            writerCondition = lock.newCondition();
    
    private int readersInside = 0, writersInside = 0;
    //private int writersWaiting = 0;
    private int counter = 0;
    private long total_readers = 0, total_writers = 0;
    
    private List<String> book = new ArrayList<>();
    
    public String wantToRead (long id) throws InterruptedException {
        lock.lock();        
        try {
            while ( (writersInside /*+ writersWaiting*/) > 0 || book.isEmpty())
                readerCondition.await();
            
            readersInside++;    
            total_readers++;
            System.out.println(readersInside + " " + writersInside);
            lock.unlock();
            read(id);        
            //printTotal();
            lock.lock();

            writerCondition.signal();       
            
            return book.toString() +" -- "+ readersInside + " "+ writersInside;
        }finally {                
            readersInside--;
            lock.unlock();
        }
    }
    
    public void wantToWrite (String msg, long id) throws InterruptedException {
        lock.lock();
        //writersWaiting++;
        try {
            while ((readersInside + writersInside) > 0)
                writerCondition.await();
            
            writersInside++;
            total_writers++;
            System.out.println("@@@ Writers, readers: " + (writersInside +" "+ readersInside));
            //printTotal();
            //writersWaiting--;
            write(msg, id);
            
            writersInside--;            
        } finally {
            readerCondition.signalAll();
            lock.unlock();
        }
    }
    
    private void read(long id) throws InterruptedException {
        System.out.println("Reader #"+id+" is reading.");    
        //Thread.sleep(10);
    }
    private void write (String msg, long id) throws InterruptedException {
        System.out.println("Writer #"+id+" is writing.");
        //Thread.sleep(15);
        if (book.size() > 40)
            book = new ArrayList<>();
        book.add(msg);
    }
    
    public void openReadingRoom() {
        new Thread(new Writer(this)).start();
        new Thread(new Writer(this)).start();
        for (int i=0; i<NUM_THREADS;i++) {
            new Thread(new Reader(this)).start();
            
            if ((i+1) % 250 == 0)
                new Thread(new Writer(this)).start();
        }        
    }
    public void printTotal() {
        System.out.println("total readers: "+total_readers+"; total writers: "+total_writers);
    }

    public static void main(String[] args) {
        ReadingRoom rr = new ReadingRoom();
        rr.openReadingRoom();
        try {
            Thread.sleep(5_000);
            Reader.stopWork();
            Writer.stopWork();
            Thread.sleep(3_000);            
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        rr.printTotal();
        System.out.println("processors: "+Runtime.getRuntime().availableProcessors());
        System.exit(0);
    }

}