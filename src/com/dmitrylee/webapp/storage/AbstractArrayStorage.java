package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.StorageException;
import com.dmitrylee.webapp.model.Resume;

import java.util.Arrays;
import java.util.List;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    protected static final int STORAGE_LIMIT = 10000;
    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected Resume getResume(Integer searchKey) {
        return storage[searchKey];
    }

    @Override
    protected void updateResume(Integer searchKey, Resume resume) {
        storage[searchKey] = resume;
    }

    @Override
    protected void removeResume(Integer searchKey) {
        int index = searchKey;
        if (index < size - 1) {
            System.arraycopy(storage, index + 1, storage, index, size - 1 - index);
        }
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected List<Resume> getResumeList() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    @Override
    protected void addResume(Resume r) {
        if (size == STORAGE_LIMIT) {
            throw new StorageException("ERROR: storage is full!", r.getUuid());
        }
        addResumeToArray(r);
        size++;
    }

    protected abstract void addResumeToArray(Resume r);
}
