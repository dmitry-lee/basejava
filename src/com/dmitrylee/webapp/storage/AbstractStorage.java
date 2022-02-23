package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.ExistStorageException;
import com.dmitrylee.webapp.exception.NotExistStorageException;
import com.dmitrylee.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {

    @Override
    public void update(Resume resume) {
        updateResume(checkResumeExistence(resume.getUuid(), false), resume);
    }

    @Override
    public void save(Resume r) {
        checkResumeExistence(r.getUuid(), true);
        addResume(r);
    }

    @Override
    public Resume get(String uuid) {
        return getResume(checkResumeExistence(uuid, false));
    }

    @Override
    public void delete(String uuid) {
        removeResume(checkResumeExistence(uuid, false));
    }

    private String checkResumeExistence(String uuid, boolean checkExists) {
        String searchKey = getResumeSearchKey(uuid);
        if (!checkExists && searchKey == null) {
            throw new NotExistStorageException(uuid);
        }
        if (checkExists && searchKey != null) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract void addResume(Resume r);

    protected abstract String getResumeSearchKey(String uuid);

    protected abstract Resume getResume(String searchKey);

    protected abstract void updateResume(String searchKey, Resume resume);

    protected abstract void removeResume(String searchKey);
}
