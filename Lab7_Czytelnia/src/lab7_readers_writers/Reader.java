/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab7_readers_writers;

/**
 *
 * @author robert
 */
public class Reader implements Runnable{
    private final ReadingRoom readingRoom;
    private long id;
    private static boolean isWorking = true;

    Reader(ReadingRoom rr) {
        readingRoom = rr;
    }

    @Override
    public void run() {
        id = Thread.currentThread().getId();
        while (isWorking)
        {
            try {
                String msg = readingRoom.wantToRead(id);
                System.out.println("Reader #"+id+": "+msg);
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void stopWork() {
        isWorking = false;
    }
    
}
