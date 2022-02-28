package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.ExistStorageException;
import com.dmitrylee.webapp.exception.NotExistStorageException;
import com.dmitrylee.webapp.model.Resume;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    protected static final Comparator<Resume> RESUME_COMPARATOR = (o1, o2) -> {
        if (o1.getFullName() == null || o2.getFullName() == null || o1.getFullName().equals(o2.getFullName())) {
            return o1.getUuid().compareTo(o2.getUuid());
        }
        return o1.getFullName().compareTo(o2.getFullName());
    };

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

    @Override
    public List<Resume> getAllSorted() {
        Resume[] resumes = getResumeArray();
        Arrays.sort(resumes, RESUME_COMPARATOR);
        return Arrays.asList(resumes);
    }

    private Object checkResumeExistence(String uuid, boolean checkExists) {
        Object searchKey = getResumeSearchKey(uuid);
        if (!checkExists && searchKey == null) {
            throw new NotExistStorageException(uuid);
        }
        if (checkExists && searchKey != null) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    protected abstract void addResume(Resume r);

    protected abstract Object getResumeSearchKey(String uuid);

    protected abstract Resume getResume(Object searchKey);

    protected abstract void updateResume(Object searchKey, Resume resume);

    protected abstract void removeResume(Object searchKey);

    protected abstract Resume[] getResumeArray();
}
