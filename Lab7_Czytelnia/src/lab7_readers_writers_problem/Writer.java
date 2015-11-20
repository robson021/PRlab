/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab7_readers_writers_problem;

import java.util.Random;

/**
 *
 * @author robert
 */
public class Writer implements Runnable {
    private final ReadingRoom readingRoom;
    private static final String[] messages = {"a","b","c","d","e"};
    private static final Random rng = new Random();
    private static boolean isWorking = true;
    private long id;

    Writer(ReadingRoom rr) {
        readingRoom = rr;
    }

    @Override
    public void run() {
        id = Thread.currentThread().getId();
        while (isWorking)
        {
            int index = rng.nextInt(messages.length);
            try {
                readingRoom.wantToWrite(messages[index], id);
                System.out.println("Writer #"+id+": "+messages[index]);
                Thread.sleep(5);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void stopWork() {
        isWorking = false;
    }    
}
