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
        updateResume(resume, index);
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
        return getResume(index);
    }

    @Override
    public void delete(String uuid) {
        int index = findResumeIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        removeResume(index);
    }

    protected abstract void addResume(Resume r);

    protected abstract int findResumeIndex(String uuid);

    protected abstract Resume getResume(int index);

    protected abstract void updateResume(Resume resume, int index);

    protected abstract void removeResume(int index);
}
