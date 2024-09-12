package com.zeetaminds.assgn5sept.misc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class DictionaryLoader {
    public static void loadDictionary(BloomFilter bloomFilter, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                bloomFilter.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
