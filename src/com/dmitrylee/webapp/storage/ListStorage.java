package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    protected List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Integer getResumeSearchKey(String uuid) {
        int index = storage.indexOf(new Resume(uuid, null));
        if (index < 0) {
            return null;
        }
        return index;
    }

    @Override
    protected void addResume(Resume r) {
        storage.add(r);
    }

    @Override
    protected Resume getResume(Integer searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void updateResume(Integer searchKey, Resume resume) {
        storage.set(searchKey, resume);
    }

    @Override
    protected void removeResume(Integer searchKey) {
        storage.remove(storage.get(searchKey));
    }

    @Override
    protected List<Resume> getResumeList() {
        return storage;
    }
}
