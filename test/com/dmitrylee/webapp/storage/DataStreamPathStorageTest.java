package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.storage.serializer.DataStreamSerializer;

public class DataStreamPathStorageTest extends AbstractStorageTest {

    public DataStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new DataStreamSerializer()));
    }
}