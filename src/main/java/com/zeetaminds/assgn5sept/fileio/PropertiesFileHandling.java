package com.zeetaminds.assgn5sept.fileio;
import  java.io.*;
import java.util.Properties;

public class PropertiesFileHandling {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try(InputStream in = new BufferedInputStream(new FileInputStream("/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/fileio/config.properties"))) {
            properties.load(in);

        }catch (IOException e) {
            e.printStackTrace();
        }

        print(properties);

        String addKey = "database.admin";
        String addValue = "admin";
        updateProperty(properties, addKey, addValue);

        String keyToUpdate = "database.password";
        String newValue = "password123";
        updateProperty(properties, keyToUpdate, newValue);

        print(properties);
    }

    public static void print(Properties properties){
        for (Object key : properties.keySet()) {
            System.out.println(key + " = " + properties.getProperty(key.toString()));
        }
    }

    public static void updateProperty(Properties properties, String keyToUpdate, String newValue) {
        properties.setProperty(keyToUpdate, newValue);
        try (OutputStream output = new FileOutputStream("/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/fileio/config.properties")) {
            properties.store(output, "Updated properties file with new key");
            System.out.println("Property updated.");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
