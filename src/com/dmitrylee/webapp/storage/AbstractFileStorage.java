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

    protected abstract Resume readFile(File searchKey) throws IOException;

    protected abstract void saveFile(Resume resume, File file) throws IOException;

    @Override
    protected void addResume(Resume r) {
        File file = new File(directory, r.getUuid());
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
        updateResume(file, r);
    }

    @Override
    protected File getResumeSearchKey(String uuid) {
        File file = new File(directory, uuid);
        return file.exists() ? file : null;
    }

    @Override
    protected Resume getResume(File searchKey) {
        try {
            return readFile(searchKey);
        } catch (IOException e) {
            throw new StorageException("Couldn't read file", searchKey.getName(), e);
        }
    }

    @Override
    protected void updateResume(File searchKey, Resume resume) {
        try {
            saveFile(resume, searchKey);
        } catch (IOException e) {
            throw new StorageException("Couldn't write file", resume.getUuid(), e);
        }
    }

    @Override
    protected void removeResume(File searchKey) {
        if (!searchKey.delete()) {
            throw new StorageException("Couldn't delete file " + searchKey.getAbsolutePath(), searchKey.getName());
        }

    }

    @Override
    protected List<Resume> getResumeList() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory read error", null);
        }
        List<Resume> list = new ArrayList<>(files.length);
        for (File file : files) {
            list.add(getResume(file));
        }
        return list;
    }

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                removeResume(file);
            }
        }
    }

    @Override
    public int size() {
        String[] list = directory.list();
        if (list == null) {
            throw new StorageException("Directory read error", null);
        }
        return list.length;
    }
}
