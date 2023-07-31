/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Ka-zakhali CAT
 */
public class Producer implements Runnable {

    private Queue<Integer> queue;
    private Semaphore mutex;
    private Semaphore empty;
    private Semaphore full;

    public Producer(Queue<Integer> queue, Semaphore mutex, Semaphore empty, Semaphore full) {
        this.queue = queue;
        this.mutex = mutex;
        this.empty = empty;
        this.full = full;
    }

    @Override
    public void run() {

        functions fun = new functions();
        //List of all the text files
        String filesList[] = fun.getFile("src\\files\\");
        System.out.println("Producer adding file names into the buffer:");
        int i = 0;
        for (String fileName : filesList) {
            try {
                
                empty.acquire();    // Acquire empty semaphore to get slot for a new item
                mutex.acquire();    // Acquire mutex to enter critical section
                
                System.out.println(fileName);
                fileName = fileName.replaceAll("[^0-9]", "");
                queue.add(Integer.parseInt(fileName));
                System.out.println("Produced " + fileName + " File number: " + i);
                mutex.release();    // Release mutex to exit critical section
                full.release();     // Release full semaphore to increase the number of filled slots
                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    
}
