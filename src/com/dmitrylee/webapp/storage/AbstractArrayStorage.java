package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.ExistStorageException;
import com.dmitrylee.webapp.exception.NotExistStorageException;
import com.dmitrylee.webapp.exception.StorageException;
import com.dmitrylee.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
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

    public Resume get(String uuid) {
        int resumeIndex = findResumeIndex(uuid);
        if (resumeIndex < 0) {
            throw new NotExistStorageException(uuid);
        }
        return storage[resumeIndex];
    }

    public void save(Resume r) {
        if (size == STORAGE_LIMIT) {
            System.out.println("ERROR: storage is full!");
            throw new StorageException("ERROR: storage is full!", r.getUuid());
        }
        int resumeIndex = findResumeIndex(r.getUuid());
        if (resumeIndex < 0) {
            addResumeToArray(resumeIndex, r);
            size++;
        } else {
            throw new ExistStorageException(r.getUuid());
        }
    }

    public void update(Resume resume) {
        int resumeIndex = findResumeIndex(resume.getUuid());
        if (resumeIndex < 0) {
            throw new NotExistStorageException(resume.getUuid());
        }
        storage[resumeIndex] = resume;
    }

    public void delete(String uuid) {
        int index = findResumeIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else if (index == size - 1) {
            storage[index] = null;
        } else if (index < size - 1) {
            System.arraycopy(storage, index + 1, storage, index, size - 1 - index);
        }
        size--;
    }

    protected abstract int findResumeIndex(String uuid);

    protected abstract void addResumeToArray(int index, Resume r);
}
