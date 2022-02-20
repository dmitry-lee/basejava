package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.ExistStorageException;
import com.dmitrylee.webapp.exception.NotExistStorageException;
import com.dmitrylee.webapp.exception.StorageException;
import com.dmitrylee.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected Storage storage;

    @Override
    public void update(Resume resume) {
        if (getResume(resume.getUuid()) == null) {
            throw new NotExistStorageException(resume.getUuid());
        }
        updateResume(resume);
    }

    @Override
    public void save(Resume r) {
        if (storageOverflow()) {
            throw new StorageException("ERROR: storage is full!", r.getUuid());
        }
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

    @Override
    public abstract void clear();

    @Override
    public abstract Resume[] getAll();

    @Override
    public abstract int size();

    protected abstract int findResumeIndex(String uuid);

    protected abstract boolean storageOverflow();

    protected abstract void addResumeToStorage(Resume r);

    protected abstract Resume getResume(String uuid);

    protected abstract void updateResume(Resume resume);

    protected abstract void removeResume(Resume resume);
}
