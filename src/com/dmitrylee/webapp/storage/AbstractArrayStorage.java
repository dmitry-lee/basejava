package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage{
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

    public void update(Resume resume) {
        int resumeIndex = findResumeIndex(resume.getUuid());
        if (resumeIndex < 0) {
            System.out.printf("ERROR: resume %s is not in storage\n", resume.getUuid());
            return;
        }
        storage[resumeIndex] = resume;
    }

    public Resume get(String uuid) {
        int resumeIndex = findResumeIndex(uuid);
        if (resumeIndex < 0) {
            System.out.printf("ERROR: resume %s is not in storage\n", uuid);
            return null;
        }
        return storage[resumeIndex];
    }

    public void delete(String uuid) {
        int index = findResumeIndex(uuid);
        if (index < 0) {
            System.out.printf("ERROR: resume %s is not in storage\n", uuid);
            return;
        } else if (index == size - 1) {
            storage[index] = null;
        } else if (index < size - 1) {
            System.arraycopy(storage, index + 1, storage, index, size - 1 - index);
        }
        size--;
    }

    protected abstract int findResumeIndex(String uuid);
}
