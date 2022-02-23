package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected String getResumeSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid);
        int index = Arrays.binarySearch(storage, 0, size, searchKey);
        if (index < 0) {
            return null;
        }
        return Integer.toString(index);
    }

    @Override
    protected void addResumeToArray(Resume r) {
        int index = Arrays.binarySearch(storage, 0, size,  new Resume(r.getUuid()));
        int insertionPoint = -(index + 1);
        System.arraycopy(storage, insertionPoint, storage, insertionPoint + 1, size - insertionPoint);
        storage[insertionPoint] = r;
    }
}
