package com.zeetaminds.assgn5sept.fileio.serialize;

import java.io.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SerializationAndDeserialization <T extends Serializable> {

    private static final Logger LOG = LogManager.getLogger(SerializationAndDeserialization.class);

    public void serialize(T obj, OutputStream os) throws IOException {
        if (obj == null) {
            LOG.error("Cannot serialize a null object");
            throw new IllegalArgumentException("Object to be serialized is null");
        }
        try (ObjectOutputStream out = new ObjectOutputStream(os)) {
            out.writeObject(obj);
            LOG.info("Serialized data has been written to the provided OutputStream.");
        }
    }

    public T deserialize(InputStream is) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(is)) {
            return (T) in.readObject();
        }
    }
}
