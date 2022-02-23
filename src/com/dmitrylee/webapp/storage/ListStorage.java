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
    protected int findResumeIndex(String uuid) {
        return storage.indexOf(new Resume(uuid));
    }

    @Override
    protected void addResume(Resume r) {
        storage.add(r);
    }

    @Override
    protected Resume getResume(int index, String uuid) {
        return storage.get(index);
    }

    @Override
    protected void updateResume(int index, Resume resume) {
        storage.set(index, resume);
    }

    @Override
    protected void removeResume(int index, String uuid) {
        storage.remove(index);
    }
}
