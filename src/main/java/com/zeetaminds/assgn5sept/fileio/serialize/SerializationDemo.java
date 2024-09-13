package com.zeetaminds.assgn5sept.fileio.serialize;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SerializationDemo {

    private static final Logger logger = LogManager.getLogger(SerializationDemo.class.getName());

    public static void main(String[] args) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = null;
        try {
            Person person = new Person("Praveen", 21, "Zeetaminds");

            SerializationAndDeserialization.serialize(person, baos);
            bais = new ByteArrayInputStream(baos.toByteArray());
            Person deserializedPerson = (Person) SerializationAndDeserialization.deserialize(bais);

            logger.info("Deserialized Person:");
            logger.info(deserializedPerson.toString());

        } catch (IOException | ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
