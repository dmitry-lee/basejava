package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.Config;
import com.dmitrylee.webapp.ResumeTestData;
import com.dmitrylee.webapp.exception.ExistStorageException;
import com.dmitrylee.webapp.exception.NotExistStorageException;
import com.dmitrylee.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {

    protected static final File STORAGE_DIR = Config.get().getStorageDir();

    public static final String UUID_1 = UUID.randomUUID().toString();
    public static final String UUID_2 = UUID.randomUUID().toString();
    public static final String UUID_3 = UUID.randomUUID().toString();
    public static final String NAME_1 = "AAA";
    public static final String NAME_2 = "BBB";
    public static final String NAME_3 = "CCC";
    public static final Resume RESUME_1 = ResumeTestData.getResumeWithTestData(UUID_1, NAME_1);
    public static final Resume RESUME_2 = ResumeTestData.getResumeWithTestData(UUID_2, NAME_2);
    public static final Resume RESUME_3 = ResumeTestData.getResumeWithTestData(UUID_3, NAME_3);
    protected final Storage storage;

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void getAllSorted() {
        List<Resume> actual = storage.getAllSorted();
        List<Resume> expected = Arrays.asList(RESUME_1, RESUME_2, RESUME_3);
        assertEquals(expected, actual);
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
        assertGet(RESUME_2);
        assertGet(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test
    public void save() {
        Resume resume = new Resume("test", "test");
        storage.save(resume);
        assertGet(resume);
        assertSize(4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(new Resume(UUID_1, NAME_1));
    }

    @Test
    public void update() {
        Resume resume = new Resume(UUID_1, "New Name");
        storage.update(resume);
        assertEquals(resume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(new Resume("dummy"));
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(UUID_1);
        assertSize(2);
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete("dummy");
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }
}