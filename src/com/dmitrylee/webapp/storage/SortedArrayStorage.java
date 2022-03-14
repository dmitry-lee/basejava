package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getResumeSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid, "");
        int index = Arrays.binarySearch(storage, 0, size, searchKey, Comparator.comparing(Resume::getUuid));
        return index < 0 ? null : index;
    }

    @Override
    protected void addResumeToArray(Resume r) {
        int index = Arrays.binarySearch(storage, 0, size, r, Comparator.comparing(Resume::getUuid));
        int insertionPoint = -(index + 1);
        System.arraycopy(storage, insertionPoint, storage, insertionPoint + 1, size - insertionPoint);
        storage[insertionPoint] = r;
    }
}
