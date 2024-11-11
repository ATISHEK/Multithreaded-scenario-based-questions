package org.example;
/******************************************************************************

 Online Java Compiler.
 Code, Compile, Run and Debug java program online.
 Write your code in this editor and press "Run" button to execute it.

 *******************************************************************************/
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.HashSet;

import java.util.Set;
public class UberSeatingProblem {

    public static void main(String[] args) throws InterruptedException {
        UberSeatingProblem.runTest();
    }

    public UberSeatingProblem() {

    }

    int demoCount =0;
    int repCount = 0;

    Semaphore demsWaiting = new Semaphore(0);
    Semaphore repWaiting = new Semaphore(0);

    private ReentrantLock lock = new ReentrantLock();
    private CyclicBarrier barrier = new CyclicBarrier(4);

    public void seatDemo()  throws InterruptedException, BrokenBarrierException {

        boolean leader = false;
        lock.lock();
        demoCount++;

        if(demoCount==4) {
            demsWaiting.release(3);

            demoCount-=4;
            leader = true ;
        } else if(demoCount ==2 && repCount == 2) {

            demoCount-=2;
            repCount-=2;

            demsWaiting.release(1);
            repWaiting.release(2);
            leader = true ;

        } else {
            lock.unlock();
            demsWaiting.acquire();
        }

        System.out.println(Thread.currentThread().getName() + " seated");
        barrier.await();

        if(leader) {
            lock.unlock();

            System.out.println(Thread.currentThread().getName() + " starting the ride now");
        }

    }

    public void seatRep()throws InterruptedException, BrokenBarrierException {

        boolean leader = false;
        lock.lock();
        repCount++;

        if(repCount==4) {
            repWaiting.release(3);

            repCount-=4;
            leader = true ;
        } else if(demoCount ==2 && repCount == 2) {

            repCount-=2;
            demoCount-=2;

            demsWaiting.release(2);
            repWaiting.release(1);
            leader = true ;

        } else {
            lock.unlock();
            repWaiting.acquire();
        }

        System.out.println(Thread.currentThread().getName() + " seated");

        barrier.await();

        if(leader) {
            lock.unlock();

            System.out.println(Thread.currentThread().getName() + " starting the ride now");
        }
    }


    public static void runTest() throws InterruptedException {
        final UberSeatingProblem uberSeatingProblem = new UberSeatingProblem();
        Set<Thread> allThreads = new HashSet<Thread>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new Runnable() {
                public void run() {

                    try {
                        uberSeatingProblem.seatDemo();
                    } catch (InterruptedException ie) {
                        System.out.println("We have a problem");
                    } catch (BrokenBarrierException bbe) {
                        System.out.println("We have a cycle problem");
                    }
                }
            });
            thread.setName("Democrat_" + (i + 1));
            allThreads.add(thread);
            Thread.sleep(50);
        }
        for (int i = 0; i < 14; i++) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        uberSeatingProblem.seatRep();
                    } catch (InterruptedException ie) {
                        System.out.println("We have a problem");
                    } catch (BrokenBarrierException bbe) {
                        System.out.println("We have a cycle problem");
                    }
                }
            });
            thread.setName("Republican_" + (i + 1));
            allThreads.add(thread);
            Thread.sleep(20);
        }
        for (Thread t : allThreads) {
            t.start();
// 			Thread.sleep(2000);
        }
        for (Thread t : allThreads) {
            t.join();
        }
    }


}