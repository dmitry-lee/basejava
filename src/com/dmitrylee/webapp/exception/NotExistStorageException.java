package com.dmitrylee.webapp.exception;

public class NotExistStorageException extends StorageException {

    public NotExistStorageException(String uuid) {
        super("ERROR: resume " + uuid + " is not in storage", uuid);
    }
}
