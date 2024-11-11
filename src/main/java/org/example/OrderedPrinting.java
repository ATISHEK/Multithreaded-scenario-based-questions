package org.example;// "static void main" must be defined in a public class.

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


class OrderedPrinting {
    private boolean first = false, sec = false, third = false;
    private ReentrantLock lock = new ReentrantLock();
    private Condition firstWaiting = lock.newCondition();
    private Condition secondWaiting = lock.newCondition();
    private Condition thirdWaiting = lock.newCondition();
    public OrderedPrinting() {

    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello World!");

        OrderedPrinting.trigger(new OrderedPrinting());
    }

    public static void trigger(OrderedPrinting op) throws InterruptedException {
        // final  OrderedPrinting op = new OrderedPrinting();
        try {
            Set<Thread> threads = new HashSet<>();

            for (int i = 0; i < 20; i++) {

                Thread t1 = new Thread(new Runnable() {

                    public void run() {
                        try {
                            op.callFirst();
                        } catch (Exception e) {

                        }
                    }
                });

                t1.setName("First Thread -> " + i);

                threads.add(t1);

                Thread t2 = new Thread(new Runnable() {

                    public void run() {
                        try {
                            op.callSec();
                        } catch (Exception e) {

                        }
                    }
                });
                t2.setName("Second Thread -> " + i);

                threads.add(t2);

                Thread t3 = new Thread(new Runnable() {

                    public void run() {
                        try {
                            op.callThird();
                        } catch (Exception e) {

                        }
                    }

                });
                t3.setName("Third Thread -> " + i);

                threads.add(t3);
            }

            for (Thread t : threads) {
                t.start();
            }

            for (Thread t : threads) {
                t.join();
            }
        } catch (Exception e) {

        }

    }

    public void printFirst() throws InterruptedException{
        System.out.print("First" + "\n");
        Thread.sleep(5000);
    }

    public void printSecond()  throws InterruptedException{
        System.out.print("Second" + "\n");
        Thread.sleep(5000);
    }

    public void printThird()  throws InterruptedException{
        System.out.print("Third" + "\n");
        Thread.sleep(5000);
    }

    public void callFirst() throws InterruptedException {

        lock.lock();
        while (first == true) {
            firstWaiting.await();
        }

        printFirst();
        // Thread.sleep(1000);
        first = true;
        secondWaiting.signal();
        lock.unlock();

    }

    public void callSec() throws InterruptedException {

        lock.lock();
        while (first == false || (first == true && sec == true)) {
            secondWaiting.await();
        }

        printSecond();
        // Thread.sleep(1000);

        sec = true;
        thirdWaiting.signal();
        lock.unlock();

    }

    public void callThird() throws InterruptedException {

        lock.lock();
        while ((first == false && sec == false) || (first == true && sec == false)) {
            thirdWaiting.await();
        }

        printThird();
        // Thread.sleep(1000);

        first = false;
        sec = false;
        firstWaiting.signal();
        lock.unlock();


    }
}