package com.zeetaminds.assgn5sept.fileio.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public interface Serializer<T extends Serializable> {
    void serialize(T obj, OutputStream os) throws IOException;
    T deserialize(InputStream is) throws IOException, ClassNotFoundException;
}