package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

public class MapUuidStorage extends AbstractMapStorage {

    @Override
    protected String getResumeSearchKey(String uuid) {
        if (storage.containsKey(uuid)) {
            return uuid;
        }
        return null;
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return storage.get(searchKey.toString());
    }

    @Override
    protected void removeResume(Object searchKey) {
        storage.remove(searchKey.toString());
    }
}
