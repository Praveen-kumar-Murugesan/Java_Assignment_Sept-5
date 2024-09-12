package com.zeetaminds.assgn5sept.fileio.serialize;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SerializationDemo {

    private static final Logger logger = Logger.getLogger(SerializationDemo.class.getName());

    public static void main(String[] args) {
        String fileName = "/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/fileio/serialize/person.ser";

        try {
            Person person = new Person("Praveen", 21, "Zeetaminds");

            SerializationAndDeserialization.serialize(person, fileName);

            Person deserializedPerson = (Person) SerializationAndDeserialization.deserialize(fileName);

            logger.info("Deserialized Person:");
            logger.info(deserializedPerson.toString());

        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "An error occurred", e);
        }
    }
}
