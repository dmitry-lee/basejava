package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.StorageException;
import com.dmitrylee.webapp.model.Resume;
import com.dmitrylee.webapp.storage.serializer.StreamSerializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {

    private final File directory;
    private final StreamSerializer streamSerializer;

    public FileStorage(File directory, StreamSerializer streamSerializer) {
        this.streamSerializer = streamSerializer;
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
    protected Resume getResume(File file) {
        try {
            return streamSerializer.readResume(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Couldn't read file", file.getName(), e);
        }
    }

    @Override
    protected void updateResume(File file, Resume resume) {
        try {
            streamSerializer.writeResume(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Couldn't write file", resume.getUuid(), e);
        }
    }

    @Override
    protected void removeResume(File file) {
        if (!file.delete()) {
            throw new StorageException("Couldn't delete file", file.getName());
        }
    }

    @Override
    protected List<Resume> getResumeList() {
        File[] files = getFiles();
        List<Resume> list = new ArrayList<>(files.length);
        for (File file : files) {
            list.add(getResume(file));
        }
        return list;
    }

    @Override
    public void clear() {
        for (File file : getFiles()) {
            removeResume(file);
        }
    }

    @Override
    public int size() {
        return getFiles().length;
    }

    private File[] getFiles() {
        File[] list = directory.listFiles();
        if (list == null) {
            throw new StorageException("Directory read error", null);
        }
        return list;
    }
}
