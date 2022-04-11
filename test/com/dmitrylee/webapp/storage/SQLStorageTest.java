package com.dmitrylee.webapp.storage;

import com.dmitrylee.webapp.Config;

import static org.junit.Assert.*;

public class SQLStorageTest extends AbstractStorageTest {

    public SQLStorageTest() {
        super(new SQLStorage(Config.get().getDbUrl(),
                Config.get().getDbUser(),
                Config.get().getDbPassword()));
    }
}