/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Ka-zakhali CAT
 */
public class Consumer implements Runnable {
    private Queue<Integer> queue;
    private Semaphore mutex;
    private Semaphore empty;
    private Semaphore full;
    private int itemConsumed;
    
    public Consumer(Queue<Integer> queue, Semaphore mutex, Semaphore empty, Semaphore full,int itemComnsumed) {
        this.queue = queue;
        this.mutex = mutex;
        this.empty = empty;
        this.full = full;
    }

    @Override
    public void run() {

        while (true) {

            try {
                                
                full.acquire();     // Acquire full semaphore to get a filled slot
                mutex.acquire();    // Acquire mutex to enter critical section
                functions fun = new functions();
                int item = fun.get(queue,itemConsumed);
                System.out.println("Consumed " + item);

                mutex.release();    // Release mutex to exit critical section
                empty.release();    // Release empty semaphore to increase the number of empty slots

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
    
    
}