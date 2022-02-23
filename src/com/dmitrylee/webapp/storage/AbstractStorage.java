package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.ExistStorageException;
import com.dmitrylee.webapp.exception.NotExistStorageException;
import com.dmitrylee.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void update(Resume resume) {
        int index = findResumeIndex(resume.getUuid());
        if (index < 0) {
            throw new NotExistStorageException(resume.getUuid());
        }
        updateResume(index, resume);
    }

    @Override
    public void save(Resume r) {
        int index = findResumeIndex(r.getUuid());
        if (index >= 0) {
            throw new ExistStorageException(r.getUuid());
        }
        addResume(r);
    }

    @Override
    public Resume get(String uuid) {
        int index = findResumeIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return getResume(index, uuid);
    }

    @Override
    public void delete(String uuid) {
        int index = findResumeIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        removeResume(index, uuid);
    }

    protected abstract void addResume(Resume r);

    protected abstract int findResumeIndex(String uuid);

    protected abstract Resume getResume(int index, String uuid);

    protected abstract void updateResume(int index, Resume resume);

    protected abstract void removeResume(int index, String uuid);
}
