package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    protected List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected String getResumeSearchKey(String uuid) {
        int index = storage.indexOf(new Resume(uuid));
        if (index < 0) {
            return null;
        }
        return Integer.toString(index);
    }

    @Override
    protected void addResume(Resume r) {
        storage.add(r);
    }

    @Override
    protected Resume getResume(String searchKey) {
        return storage.get(Integer.parseInt(searchKey));
    }

    @Override
    protected void updateResume(String searchKey, Resume resume) {
        storage.set(Integer.parseInt(searchKey), resume);
    }

    @Override
    protected void removeResume(String searchKey) {
        storage.remove(Integer.parseInt(searchKey));
    }
}
