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

    protected int findResumeIndex(String uuid) {
        return storage.indexOf(new Resume(uuid));
    }

    @Override
    protected void addResumeToStorage(Resume r) {
        storage.add(r);
    }

    @Override
    protected Resume getResume(String uuid) {
        int index = findResumeIndex(uuid);
        if (index >= 0) {
            return storage.get(index);
        }
        return null;
    }

    @Override
    protected void updateResume(Resume resume) {
        storage.set(findResumeIndex(resume.getUuid()), resume);
    }

    @Override
    protected void removeResume(Resume resume) {
        storage.remove(resume);
    }
}
