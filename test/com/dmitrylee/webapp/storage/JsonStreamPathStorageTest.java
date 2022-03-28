package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.storage.serializer.JsonStreamSerializer;

public class JsonStreamPathStorageTest extends AbstractStorageTest {

    public JsonStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new JsonStreamSerializer()));
    }
}