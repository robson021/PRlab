/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab7_rwlock;

import java.util.Random;

/**
 *
 * @author robert
 */
public class Writer implements Runnable {
    private static final Random rng = new Random();
    private final ReadingRoom readingRoom;
    private final String[] messsages = {"dsada", "fsfs", "eqweq", "asdada", "bvceq"};
    private static boolean isWorking = true;
    public Writer(ReadingRoom rr) {
        readingRoom = rr;
    }

    @Override
    public void run() {
        while (isWorking) {            
            String msg = messsages[rng.nextInt(messsages.length)]; //get random msg form array
            try {            
                readingRoom.addNewMessage(msg);
                System.out.println("Writer #"+Thread.currentThread().getId()+" has added new message: "+msg);
                Thread.sleep(2);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void stopAll() {
        isWorking = false;
    }
}
