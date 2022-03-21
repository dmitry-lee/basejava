package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.StorageException;
import com.dmitrylee.webapp.model.Resume;
import com.dmitrylee.webapp.storage.serializer.StreamSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final StreamSerializer streamSerializer;

    protected PathStorage(String dir, StreamSerializer streamSerializer) {
        directory = Paths.get(dir);
        this.streamSerializer = streamSerializer;
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    private Stream<Path> getPaths() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Path list read error", null);
        }
    }

    @Override
    public void clear() {
        getPaths().forEach(this::removeResume);
    }

    @Override
    public int size() {
        return (int) getPaths().count();
    }

    @Override
    protected Path getResumeSearchKey(String uuid) {
        Path path = directory.resolve(uuid);
        return Files.exists(path) ? path : null;
    }

    @Override
    protected void updateResume(Path path, Resume r) {
        try {
            streamSerializer.writeResume(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path write error", r.getUuid(), e);
        }
    }

    @Override
    protected void addResume(Resume r) {
        Path path = directory.resolve(r.getUuid());
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Couldn't create Path " + path.toAbsolutePath(), r.getUuid(), e);
        }
        updateResume(path, r);
    }

    @Override
    protected Resume getResume(Path path) {
        try {
            return streamSerializer.readResume(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path read error", path.toFile().getName(), e);
        }
    }

    @Override
    protected void removeResume(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Couldn't delete path " + path.toAbsolutePath(), path.getFileName().toString(), e);
        }
    }

    @Override
    protected List<Resume> getResumeList() {
        return getPaths().map(this::getResume).collect(Collectors.toList());
    }
}
