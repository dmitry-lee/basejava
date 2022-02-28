package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getResumeSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid, null);
        int index = Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARATOR);
        if (index < 0) {
            return null;
        }
        return index;
    }

    @Override
    protected void addResumeToArray(Resume r) {
        int index = Arrays.binarySearch(storage, 0, size,  new Resume(r.getUuid(), null), RESUME_COMPARATOR);
        int insertionPoint = -(index + 1);
        System.arraycopy(storage, insertionPoint, storage, insertionPoint + 1, size - insertionPoint);
        storage[insertionPoint] = r;
    }
}
