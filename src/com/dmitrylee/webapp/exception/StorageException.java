package com.dmitrylee.webapp.exception;

public class StorageException extends RuntimeException {
    private final String uuid;

    public StorageException(String message, String uuid) {
        this(message, uuid, null);
    }

    public StorageException(String message, String uuid, Exception e) {
        super(message, e);
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }
}
