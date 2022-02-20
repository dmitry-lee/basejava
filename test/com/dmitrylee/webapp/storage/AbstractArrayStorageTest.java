package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.StorageException;
import com.dmitrylee.webapp.model.Resume;
import org.junit.Test;

import static com.dmitrylee.webapp.storage.AbstractArrayStorage.STORAGE_LIMIT;
import static org.junit.Assert.fail;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest{

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void saveStorageOverflow(){
        while (storage.size() < STORAGE_LIMIT) {
            try {
                storage.save(new Resume());
            } catch (StorageException e) {
                fail("Storage overflow too early!");
            }
        }
        storage.save(new Resume());
    }
}