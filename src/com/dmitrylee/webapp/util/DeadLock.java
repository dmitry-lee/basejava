package com.dmitrylee.webapp.util;

public class DeadLock implements Runnable {

    public static void main(String[] args) {
        DeadLock deadLock = new DeadLock();
        Thread first = new Thread(deadLock);
        Thread second = new Thread(deadLock);
        first.start();
        second.start();
    }

    public void doLock(Object lock1, Object lock2) {
        synchronized (lock1) {
            System.out.println("Acquired " + lock1 + " in thread " + Thread.currentThread().getName());
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock2) {
                System.out.println("Acquired " + lock2 + " in thread " + Thread.currentThread().getName());
            }
        }
    }

    @Override
    public void run() {
        final String lock1 = "lock1";
        final String lock2 = "lock2";
        doLock(lock1, lock2);
        doLock(lock2, lock1);
    }
}