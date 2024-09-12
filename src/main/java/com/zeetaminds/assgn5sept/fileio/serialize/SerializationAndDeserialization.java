package com.zeetaminds.assgn5sept.fileio.serialize;

import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SerializationAndDeserialization {

    private static final Logger logger = LogManager.getLogger(SerializationAndDeserialization.class);

    public static void serialize(Object obj, String fileName) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(obj);
            logger.info("Serialized data is saved in " + fileName);
        }
    }

    public static Object deserialize(String fileName) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return in.readObject();
        }
    }
}
