package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.model.Resume;

import java.util.Map;

public class MapResumeStorage extends AbstractMapStorage {

    @Override
    protected String getResumeSearchKey(String uuid) {
        if (storage.containsKey(uuid)) {
            return storage.get(uuid).getFullName();
        }
        return null;
    }

    @Override
    protected Resume getResume(Object searchKey) {
        for (Map.Entry<String, Resume> entry: storage.entrySet()) {
            if (entry.getValue().getFullName() == searchKey) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    protected void removeResume(Object searchKey) {
        storage.entrySet().removeIf(stringResumeEntry -> stringResumeEntry.getValue().getFullName().equals(searchKey));
    }
}
