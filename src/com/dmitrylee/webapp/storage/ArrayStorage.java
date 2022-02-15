package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage{

    public void save(Resume r) {
        int resumeIndex = findResumeIndex(r.getUuid());
        if (resumeIndex == -1) {
            if (size == storage.length) {
                System.out.println("ERROR: storage is full!");
                return;
            }
            storage[size] = r;
            size++;
            return;
        }
        System.out.printf("ERROR: resume %s is already in storage\n", storage[resumeIndex].getUuid());
    }

    protected int findResumeIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
