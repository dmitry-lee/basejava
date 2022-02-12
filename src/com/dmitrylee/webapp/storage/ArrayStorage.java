package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    private int findResume(String uuid) {
        int result = -1;
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                result = i;
            }
        }
        return result;
    }

    public void update(Resume resume) {
        int resumeIndex = findResume(resume.getUuid());
        if (resumeIndex > -1) {
            storage[resumeIndex] = resume;
        } else {
            System.out.printf("ERROR: resume %s is not in storage\n", resume.getUuid());
            System.out.println();
        }
    }

    public void save(Resume r) {
        int resumeIndex = findResume(r.getUuid());
        if (resumeIndex == -1) {
            storage[size] = r;
            size++;
        } else {
            System.out.printf("ERROR: resume %s is already in storage\n", storage[resumeIndex].getUuid());
        }
    }

    public Resume get(String uuid) {
        int resumeIndex = findResume(uuid);
        if (resumeIndex == -1) {
            return null;
        }
        return storage[resumeIndex];
    }

    public void delete(String uuid) {
        int index = findResume(uuid);
        if (index >= 0) {
            for (int i = index; i < size; i++) {
                if (i < size - 1) {
                    storage[i] = storage[i + 1];
                }
            }
            storage[size - 1] = null;
            size--;
        } else {
            System.out.printf("ERROR: resume %s is not in storage\n", uuid);
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
}
