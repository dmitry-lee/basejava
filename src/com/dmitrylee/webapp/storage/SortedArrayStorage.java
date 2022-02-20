package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int findResumeIndex(String uuid) {
        Resume searchKey = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void addResumeToArray(Resume r) {
        int insertionPoint = -(findResumeIndex(r.getUuid()) + 1);
        System.arraycopy(storage, insertionPoint, storage, insertionPoint + 1, size - insertionPoint);
        storage[insertionPoint] = r;
    }
}
