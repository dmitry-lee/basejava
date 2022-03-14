package com.dmitrylee.webapp;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        printFiles(new File(".\\"));
    }

    private static void printFiles(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    printFiles(f);
                } else {
                    System.out.println(f.getName());
                }
            }
        }
    }
}
