package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.ExistStorageException;
import com.dmitrylee.webapp.exception.NotExistStorageException;
import com.dmitrylee.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<SK> implements Storage {

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

    private SK checkResumeExistence(String uuid, boolean checkExists) {
        SK searchKey = getResumeSearchKey(uuid);
        if (!checkExists && searchKey == null) {
            throw new NotExistStorageException(uuid);
        }
        if (checkExists && searchKey != null) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumeList = getResumeList();
        resumeList.sort(Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid));
        return resumeList;
    }

    protected abstract void addResume(Resume r);

    protected abstract SK getResumeSearchKey(String uuid);

    protected abstract Resume getResume(SK searchKey);

    protected abstract void updateResume(SK searchKey, Resume resume);

    protected abstract void removeResume(SK searchKey);

    protected abstract List<Resume> getResumeList();
}
