package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void save(Resume r) {
        if (size == STORAGE_LIMIT) {
            System.out.println("ERROR: storage is full!");
            return;
        }
        int index = findResumeIndex(r.getUuid());
        if (index < 0) {
            int insertionPoint = -(index + 1);
            System.arraycopy(storage, insertionPoint, storage, insertionPoint + 1, size - insertionPoint);
            storage[insertionPoint] = r;
            size++;
        } else {
            System.out.printf("ERROR: resume %s is already in storage\n", r.getUuid());
        }
    }

    @Override
    protected int findResumeIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }
}
