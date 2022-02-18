package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.exception.ExistStorageException;
import com.dmitrylee.webapp.exception.NotExistStorageException;
import com.dmitrylee.webapp.exception.StorageException;
import com.dmitrylee.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;

import static org.junit.Assert.*;

public abstract class AbstractArrayStorageTest {

    public static final String UUID_1 = "uuid1";
    public static final String UUID_2 = "uuid2";
    public static final String UUID_3 = "uuid3";
    public static final int STORAGE_LIMIT = 10000;
    private final Storage storage;

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        assertEquals(storage.size(), resumes.length);
    }

    @Test
    public void get() {
        Resume resume = storage.get(UUID_1);
        assertEquals(resume, new Resume(UUID_1));
    }

    @Test
    public void save() {
        Resume resume = new Resume("test");
        storage.save(resume);
        assertEquals(resume, storage.get("test"));
    }

    @Test
    public void update() {
        Resume resume = new Resume(UUID_1);
        storage.update(resume);
        assertEquals(resume, storage.get(UUID_1));
    }

    @Test
    public void delete() {
        storage.delete(UUID_1);
        assertEquals(2, storage.size());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist(){
        storage.delete("dummy");
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist(){
        storage.update(new Resume("dummy"));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist(){
        storage.get("dummy");
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist(){
        storage.save(new Resume(UUID_1));
    }

    @Test(expected = StorageException.class)
    public void saveStorageOverflow(){
        while (storage.size() < STORAGE_LIMIT + 1) {
           try {
               storage.save(new Resume());
           } catch (Exception e) {
               fail("Storage overflow too early!");
           }
        }
        storage.save(new Resume());
    }
}