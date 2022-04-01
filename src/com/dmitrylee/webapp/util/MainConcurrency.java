package com.dmitrylee.webapp.util;

public class MainConcurrency {
    public static void main(String[] args) {
        DeadLock deadLock = new DeadLock();
        Thread first = new Thread(deadLock);
        Thread second = new Thread(deadLock);
        first.start();
        second.start();
    }
}
