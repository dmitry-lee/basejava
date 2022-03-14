package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.StorageException;
import com.dmitrylee.webapp.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {

    private final File directory;

    public AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected void addResume(Resume r) {
        File file = new File(directory, r.getUuid());
        try {
            file.createNewFile();
            saveFile(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }

    }

    @Override
    protected File getResumeSearchKey(String uuid) {
        File file = new File(directory, uuid);
        if (file.exists()) {
            return file;
        }
        return null;
    }

    @Override
    protected Resume getResume(File searchKey) {
        return readFile(searchKey);
    }

    @Override
    protected void updateResume(File searchKey, Resume resume) {
        saveFile(resume, searchKey);
    }

    @Override
    protected void removeResume(File searchKey) {
        searchKey.delete();
    }

    @Override
    protected List<Resume> getResumeList() {
        List<Resume> list = new ArrayList<>();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            list.add(readFile(file));
        }
        return list;
    }

    @Override
    public void clear() {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            file.delete();
        }
    }

    @Override
    public int size() {
        File[] files = directory.listFiles();
        if (files != null) {
            return files.length;
        }
        return 0;
    }

    protected abstract Resume readFile(File searchKey);

    protected abstract void saveFile(Resume resume, File file);
}
