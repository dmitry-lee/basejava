package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

public class MapUuidStorage extends AbstractMapStorage<String> {

    @Override
    protected String getResumeSearchKey(String uuid) {
        if (storage.containsKey(uuid)) {
            return uuid;
        }
        return null;
    }

    @Override
    protected Resume getResume(String searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void removeResume(String searchKey) {
        storage.remove(searchKey);
    }
}
