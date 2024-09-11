package com.zeetaminds.assgn5sept.fileio;
import java.io.*;

public class SerializationAndDeserialization implements Serializable {
    private static final long serialVersionUID = 1L; // This is a version control for serialization

    private String name;
    private int age;
    private String address;

    // Constructor
    public SerializationAndDeserialization(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", address='" + address + "'}";
    }

    public static void main(String[] args) {

        String fileName = "/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/fileio/person.ser";
        Serialization(fileName);
        Deserialization(fileName);
    }

    public static void Serialization(String fileName) {
        SerializationAndDeserialization person = new SerializationAndDeserialization("Praveen", 21, "Zeetaminds");

        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(person);
            System.out.println("Serialized data is saved in person.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }


    public static void Deserialization(String fileName) {
        SerializationAndDeserialization person = null;

        try (FileInputStream fileIn = new FileInputStream(fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            person = (SerializationAndDeserialization) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (person != null) {
            System.out.println("Deserialized Person:");
            System.out.println(person);
        }
    }

}