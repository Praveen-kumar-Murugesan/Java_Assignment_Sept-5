package com.zeetaminds.assgn5sept.fileio.serialize;

import java.io.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SerializationAndDeserialization {

    private static final Logger LOG = LogManager.getLogger(SerializationAndDeserialization.class);

    public void serialize(Object obj, OutputStream os) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(os)) {
            out.writeObject(obj);
            LOG.info("Serialized data has been written to the provided OutputStream.");
        }
    }

    public Object deserialize(InputStream is) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(is)) {
            return in.readObject();
        }
    }
}
