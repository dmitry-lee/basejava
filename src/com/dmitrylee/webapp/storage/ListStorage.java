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
        Resume[] resumes = new Resume[storage.size()];
        return storage.toArray(resumes);
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
    protected Resume getResume(int index) {
        return storage.get(index);
    }

    @Override
    protected void updateResume(Resume resume, int index) {
        storage.set(index, resume);
    }

    @Override
    protected void removeResume(int index) {
        storage.remove(index);
    }
}
