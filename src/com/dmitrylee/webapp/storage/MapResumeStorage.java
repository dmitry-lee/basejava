package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

public class MapResumeStorage extends AbstractMapStorage {

    @Override
    protected Resume getResumeSearchKey(String uuid) {
        if (storage.containsKey(uuid)) {
            return storage.get(uuid);
        }
        return null;
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return (Resume) searchKey;
    }

    @Override
    protected void removeResume(Object searchKey) {
        storage.remove(((Resume) searchKey).getUuid());
    }
}
