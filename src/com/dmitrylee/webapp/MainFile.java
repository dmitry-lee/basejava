package com.dmitrylee.webapp;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        printFiles(new File(".\\src\\com\\dmitrylee\\webapp"), "");
    }

    private static void printFiles(File file, String offset) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    System.out.println(offset + f.getName());
                    printFiles(f, offset + " ");
                } else {
                    System.out.println(offset + f.getName());
                }
            }
        }
    }
}
