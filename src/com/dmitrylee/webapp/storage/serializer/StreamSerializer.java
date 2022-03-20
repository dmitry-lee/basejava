package com.dmitrylee.webapp.storage.serializer;

import com.dmitrylee.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StreamSerializer {

    void writeResume(Resume r, OutputStream os) throws IOException;

    Resume readResume(InputStream is) throws IOException;
}
