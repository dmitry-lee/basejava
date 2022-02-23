package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

import java.util.Map;
import java.util.TreeMap;

public class MapStorage extends AbstractStorage {
    Map<String, Resume> storage = new TreeMap<>();

    @Override
    protected void addResume(Resume r) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected int findResumeIndex(String uuid) {
        if (storage.containsKey(uuid)) {
            return 1;
        }
        return -1;
    }

    @Override
    protected Resume getResume(int index, String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void updateResume(int index, Resume resume) {
        storage.replace(resume.getUuid(), resume);
    }

    @Override
    protected void removeResume(int index, String uuid) {
        storage.remove(uuid);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }
}
