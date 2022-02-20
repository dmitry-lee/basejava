package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage extends AbstractStorage {
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

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    protected boolean storageOverflow() {
        return size == STORAGE_LIMIT;
    }

    @Override
    protected Resume getResume(String uuid) {
        int resumeIndex = findResumeIndex(uuid);
        if (resumeIndex < 0) {
            return null;
        }
        return storage[resumeIndex];
    }

    @Override
    protected void updateResume(Resume resume) {
        storage[findResumeIndex(resume.getUuid())] = resume;
    }

    @Override
    protected void removeResume(Resume resume) {
        int index = findResumeIndex(resume.getUuid());
        if (index < size - 1) {
            System.arraycopy(storage, index + 1, storage, index, size - 1 - index);
        }
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected void addResumeToStorage(Resume r) {
        addResumeToArray(r);
        size++;
    }

    protected abstract void addResumeToArray(Resume r);

    protected abstract int findResumeIndex(String uuid);
}
