package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

public class MapResumeStorage extends AbstractMapStorage<Resume> {

    @Override
    protected Resume getResumeSearchKey(String uuid) {
        if (storage.containsKey(uuid)) {
            return storage.get(uuid);
        }
        return null;
    }

    @Override
    protected Resume getResume(Resume searchKey) {
        return searchKey;
    }

    @Override
    protected void removeResume(Resume searchKey) {
        storage.remove((searchKey).getUuid());
    }
}
