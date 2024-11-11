package org.example;


import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchExample {


    private static final CountDownLatch latch = new CountDownLatch(4);


    private  void print() throws InterruptedException {

        Thread.sleep(4000);

        System.out.println(Thread.currentThread().getName() + " decreasing counter" + "\n");

        latch.countDown();

    }

    public static void main(String[] args) throws InterruptedException {

        CountDownLatchExample eg = new CountDownLatchExample();

        Set<Thread> threads = new HashSet<>();
        for (int i = 0; i < 3; i++) {

            Thread thread = new Thread(() -> {

                try {
                    eg.print();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            });
            thread.setName("Thread " + i);
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        latch.await();
        System.out.println(Thread.currentThread().getName() + " is done!!");


    }
}
