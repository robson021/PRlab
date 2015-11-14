/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab7_czytelnia;

import java.util.Random;

/**
 *
 * @author robert
 */
public class Writer implements Runnable{
    
    private static final Random random = new Random();
    private static final String[] messages = {"dasda", "gdfgd", "qewqe", "hjkhk", "yuiuyi"};
    private final ReadingRoom readingRoom;
    public Writer(ReadingRoom c) {
        readingRoom=c;
    }

    @Override
    public void run() {
        while (true) {
            int x = random.nextInt(5);            
            try {
                readingRoom.writeMessage(messages[x]);
                System.out.println("Writer "+ Thread.currentThread().getId() +
                        " "+messages[x]);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
                
    }
    
}
