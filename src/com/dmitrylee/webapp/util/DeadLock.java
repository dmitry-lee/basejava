package com.dmitrylee.webapp.util;

public class DeadLock implements Runnable {

    private final String lock1 = "lock1";
    private final String lock2 = "lock2";

    public void doFirst() {
        synchronized (lock1) {
            System.out.println("Acquired lock1 in thread " + Thread.currentThread().getName());
            synchronized (lock2) {
                System.out.println("Acquired lock2 in thread " + Thread.currentThread().getName());
            }
        }
    }

    public void doSecond() {
        synchronized (lock2) {
            System.out.println("Acquired lock2 in thread " + Thread.currentThread().getName());
            synchronized (lock1) {
                System.out.println("Acquired lock1 in thread " + Thread.currentThread().getName());
            }
        }
    }

    @Override
    public void run() {
        doFirst();
        doSecond();
    }
}