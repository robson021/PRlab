/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab7_rwlock;

/**
 *
 * @author robert
 */
public class Reader implements Runnable {
    ReadingRoom readingRoom;
    
    public Reader(ReadingRoom rr) {
        readingRoom = rr;
    }

    @Override
    public void run() {
        String msg = null;
        try {
            msg = readingRoom.getMessages();
            System.out.println("Reader #"+Thread.currentThread().getId()+" is reading:\n"+msg);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
}
