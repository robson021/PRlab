/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab7_monitor;

/**
 *
 * @author robert
 */
public class Reader implements Runnable {
    private final ReadingRoom readingRoom;
    
    public Reader(ReadingRoom cz) {
        readingRoom=cz;
    }
    

    @Override
    public void run() {
        
        try {
            String msg = readingRoom.getMessage();
            System.out.println(Thread.currentThread().getName() +
                    ": "+msg);
            
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
}
