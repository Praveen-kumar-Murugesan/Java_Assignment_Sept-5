package com.zeetaminds.assgn5sept.misc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static com.zeetaminds.assgn5sept.misc.BloomFilter.fileName;

class BloomFilterTest {
    public static void main(String[] args) {
        int bitSetSize = 100000; // Adjust size as needed
        int numHashFunctions = 3; // Adjust number of hash functions

        BloomFilter bloomFilter = new BloomFilter(bitSetSize, numHashFunctions);
        DictionaryLoader.loadDictionary(bloomFilter, fileName);

        Set<String> dictionaryWords = loadWords(fileName);

        Random rand = new Random();
        Set<String> falsePositives = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            String randomWord = generateRandomWord(5);
            if (bloomFilter.mightContain(randomWord) && !dictionaryWords.contains(randomWord)) {
                falsePositives.add(randomWord);
            }
        }

        System.out.println("False positives: " + falsePositives.size());
    }

    private static Set<String> loadWords(String filePath) {
        Set<String> words = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line.trim().toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    private static String generateRandomWord(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            char c = (char) ('a' + rand.nextInt(26));
            sb.append(c);
        }
        return sb.toString();
    }
}
