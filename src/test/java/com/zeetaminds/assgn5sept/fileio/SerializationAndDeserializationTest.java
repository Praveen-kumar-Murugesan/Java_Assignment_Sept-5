package com.zeetaminds.assgn5sept.fileio;

import com.zeetaminds.assgn5sept.fileio.serialize.BinarySerializer;
import com.zeetaminds.assgn5sept.fileio.serialize.Person;
import com.zeetaminds.assgn5sept.fileio.serialize.SerializationAndDeserialization;
import com.zeetaminds.assgn5sept.fileio.serialize.Serializer;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SerializationAndDeserializationTest {

    @Test
    void serialization() {
        Person originalPerson = new Person("Praveen", 21, "Zeetaminds");
        SerializationAndDeserialization<Person> serializer = new SerializationAndDeserialization<>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            serializer.serialize(originalPerson, baos);
            assertTrue(baos.size() > 0, "Serialized data should be written to the ByteArrayOutputStream.");
        } catch (IOException e) {
            fail("Serialization failed: " + e.getMessage());
        }
    }

    @Test
    void deserialization() {
        Person originalPerson = new Person("Praveen", 21, "Zeetaminds");
        Serializer<Person> serializer = new BinarySerializer<>();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            serializer.serialize(originalPerson, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

            Person deserializedPerson = (Person) serializer.deserialize(bais);

            assertEquals(originalPerson, deserializedPerson, "Deserialized person should be equal to the original person.");

        } catch (IOException | ClassNotFoundException e) {
            fail("Deserialization failed: " + e.getMessage());
        }
    }
}
