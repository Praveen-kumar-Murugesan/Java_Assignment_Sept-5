package com.zeetaminds.assgn5sept.fileio.serialize;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SerializationDemo {

    private static final Logger LOG = LogManager.getLogger(SerializationDemo.class.getName());

    public static void main(String[] args) {

        SerializationAndDeserialization serializer = new SerializationAndDeserialization();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayInputStream bais = null;
        try {
            Person person = new Person("Praveen", 21, "Zeetaminds");

            serializer.serialize(person, baos);
            bais = new ByteArrayInputStream(baos.toByteArray());
            Person deserializedPerson = (Person) serializer.deserialize(bais);

            LOG.info("Deserialized Person:");
            LOG.info(deserializedPerson.toString());

        } catch (IOException | ClassNotFoundException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
