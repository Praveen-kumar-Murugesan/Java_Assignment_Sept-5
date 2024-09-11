package com.zeetaminds.assgn5sept.fileio;
import java.io.*;

public class DirectoryTraversalAndFileSearch {


        public static void main(String[] args) {
            File directory = new File("/home/pk/IdeaProjects/Java-Assignment"); // Replace with your directory path
            String extension = ".txt";
            String keyword = "";
            keyword = "File";
            listFilesWithExtension(directory, extension, keyword);

        }

        private static void listFilesWithExtension(File dir, String extension, String keyword) {
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();

                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            listFilesWithExtension(file, extension, keyword);
                        } else if (file.getName().endsWith(extension)) {
                            boolean isExist = searchFileForKeyword(file, keyword);
                            System.out.println(file.getName()+ " path: " + file.getAbsolutePath()+ "is Keyword Present: "+ isExist);
                        }
                    }
                }
            }
        }

    private static boolean searchFileForKeyword(File file, String keyword) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(keyword)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

