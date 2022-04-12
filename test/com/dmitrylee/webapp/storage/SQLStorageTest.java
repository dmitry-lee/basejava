package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.Config;

public class SQLStorageTest extends AbstractStorageTest {

    public SQLStorageTest() {
        super(Config.get().getSQLStorage());
    }
}