package org.example;

import java.util.concurrent.Semaphore;

class FizzBuzz {

    int n = 31;
    private Semaphore fizz, buzz, fizzBuzz, nothing;

    FizzBuzz() {

        nothing = new Semaphore(1);
        fizz = new Semaphore(0);

        buzz = new Semaphore(0);

        fizzBuzz = new Semaphore(0);
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello World");
        FizzBuzz.trigger();
    }

    public static void trigger() throws InterruptedException {
        FizzBuzz trigger = new FizzBuzz();
        Thread thread1 = new Thread(() -> {
            try {
                trigger.start();

            } catch (Exception e) {

            }
        }
        );
        Thread thread2 = new Thread(() -> {
            try {
                trigger.fizzz();

            } catch (Exception e) {

            }
        }
        );
        Thread thread3 = new Thread(() -> {
            try {
                trigger.fizzBuzz();

            } catch (Exception e) {

            }
        }
        );

        Thread thread4 = new Thread(() -> {
            try {
                trigger.buzz();

            } catch (Exception e) {

            }
        }
        );
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();


    }

    public void start() throws InterruptedException {


        for (int i = 1; i <=n; i++) {

            nothing.acquire();

            if (i % 3 == 0 && i % 5 != 0) {
                fizz.release();
            } else if (i % 5 == 0 && i % 3 != 0) {
                buzz.release();
            } else if (i % 3 == 0 && i % 5 == 0) {
                fizzBuzz.release();
            }else nothing.release();

        }


    }

    public void fizzz() throws InterruptedException {

        for (int i = 3; i < n; i += 3) {

            fizz.acquire();

            System.out.println(i + "fizz" + "\n");
            Thread.sleep(10000);
            nothing.release();
        }

    }

    public void buzz() throws InterruptedException {

        for (int i = 5; i < n; i += 5) {

            buzz.acquire();

            System.out.println(i + "buzz" + "\n");
            Thread.sleep(10000);
            nothing.release();
        }

    }

    public void fizzBuzz() throws InterruptedException {
        for (int i = 15; i < n; i += 15) {

            fizzBuzz.acquire();

            System.out.println(i + "fizzBuzz" + "\n");
            Thread.sleep(10000);
            nothing.release();
        }

    }
}