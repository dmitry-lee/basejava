package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private Resume[] storage = new Resume[10000];
    private int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void update(Resume resume) {
        int resumeIndex = findResumeIndex(resume.getUuid());
        if (resumeIndex == -1) {
            System.out.printf("ERROR: resume %s is not in storage\n", resume.getUuid());
            return;
        }
        storage[resumeIndex] = resume;
    }

    public void save(Resume r) {
        int resumeIndex = findResumeIndex(r.getUuid());
        if (resumeIndex == -1) {
            if (size == storage.length) {
                System.out.println("ERROR: storage is full!");
                return;
            }
            storage[size] = r;
            size++;
            return;
        }
        System.out.printf("ERROR: resume %s is already in storage\n", storage[resumeIndex].getUuid());
    }

    public Resume get(String uuid) {
        int resumeIndex = findResumeIndex(uuid);
        if (resumeIndex == -1) {
            System.out.printf("ERROR: resume %s is not in storage\n", uuid);
            return null;
        }
        return storage[resumeIndex];
    }

    public void delete(String uuid) {
        int index = findResumeIndex(uuid);
        if (index == -1) {
            System.out.printf("ERROR: resume %s is not in storage\n", uuid);
        } else if (index == size - 1) {
            storage[index] = null;
            size--;
        } else if (index < size - 1) {
            System.arraycopy(storage, index + 1, storage, index, size -1 - index);
            size--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int findResumeIndex(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
