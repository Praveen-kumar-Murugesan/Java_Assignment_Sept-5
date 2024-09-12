package com.zeetaminds.assgn5sept.fileio.bufferedstreams;

import java.io.*;

public class FileCopy {
    public static void main(String[] args) {
        String sourceFile = "/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/fileio/bufferedstreams/source.txt";
        String destinationFile = "/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/fileio/bufferedstreams/";

        copyFileUsingFileStreams(sourceFile, destinationFile+"destination.txt");

        copyFileUsingBufferedStreams(sourceFile, destinationFile+"buffereddestination.txt");

        int[] bufferSizes = {128, 256, 512, 1024}; // Different buffer sizes to experiment with
        for (int bufferSize : bufferSizes) {
            copyFileUsingBufferedStreamsWithSize(sourceFile, destinationFile + "destination"+ bufferSize + ".txt", bufferSize);
        }
    }

    public static void copyFileUsingFileStreams(String sourceFile, String destinationFile) {
        long startTime = System.currentTimeMillis();
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(destinationFile)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Time taken with FileInputStream/FileOutputStream: " + (endTime - startTime) + " milliseconds");
    }

    public static void copyFileUsingBufferedStreams(String sourceFile, String destinationFile) {
        long startTime = System.currentTimeMillis();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destinationFile))) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Time taken with BufferedInputStream/BufferedOutputStream: " + (endTime - startTime) + " milliseconds");
    }

    public static void copyFileUsingBufferedStreamsWithSize(String sourceFile, String destinationFile, int bufferSize) {
        long startTime = System.currentTimeMillis();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile), bufferSize);
             BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destinationFile), bufferSize)) {

            byte[] buffer = new byte[bufferSize];
            int length;
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Time taken with BufferedInputStream/BufferedOutputStream with buffer size " + bufferSize + ": " + (endTime - startTime) + " milliseconds");
    }
}
