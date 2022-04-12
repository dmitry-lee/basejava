package com.dmitrylee.webapp.exception;

public class ExistStorageException extends StorageException {

    public ExistStorageException(String uuid) {
        super("ERROR: resume " + uuid + " is already in storage", uuid);
    }

    public ExistStorageException(Exception e) {
        super(e);
    }
}
