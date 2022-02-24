package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getResumeSearchKey(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }

    @Override
    protected void addResumeToArray(Resume r) {
        storage[size] = r;
    }
}
