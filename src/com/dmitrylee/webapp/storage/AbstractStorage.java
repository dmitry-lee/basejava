package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.ExistStorageException;
import com.dmitrylee.webapp.exception.NotExistStorageException;
import com.dmitrylee.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void update(Resume resume) {
        if (getResume(resume.getUuid()) == null) {
            throw new NotExistStorageException(resume.getUuid());
        }
        updateResume(resume);
    }

    @Override
    public void save(Resume r) {
        if (getResume(r.getUuid()) != null) {
            throw new ExistStorageException(r.getUuid());
        }
        addResumeToStorage(r);
    }

    @Override
    public Resume get(String uuid) {
        Resume resume = getResume(uuid);
        if (resume == null) {
            throw new NotExistStorageException(uuid);
        }
        return resume;
    }

    @Override
    public void delete(String uuid) {
        Resume resume = getResume(uuid);
        if (resume == null) {
            throw new NotExistStorageException(uuid);
        }
        removeResume(resume);
    }

    protected abstract void addResumeToStorage(Resume r);

    protected abstract Resume getResume(String uuid);

    protected abstract void updateResume(Resume resume);

    protected abstract void removeResume(Resume resume);
}
