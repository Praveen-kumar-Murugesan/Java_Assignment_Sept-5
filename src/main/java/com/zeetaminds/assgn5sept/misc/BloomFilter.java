package com.zeetaminds.assgn5sept.misc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;
import static com.zeetaminds.assgn5sept.misc.BloomFilter.fileName;


public class BloomFilter {
    static String fileName = "/home/pk/IdeaProjects/Java-Assignment/src/main/java/com/zeetaminds/assgn5sept/misc/word.txt";
    private BitSet bitSet;
    private int bitSetSize;
    private int numHashFunctions;

    public BloomFilter(int bitSetSize, int numHashFunctions) {
        this.bitSetSize = bitSetSize;
        this.numHashFunctions = numHashFunctions;
        this.bitSet = new BitSet(bitSetSize);
    }

    public void add(String word) {
        int[] hashes = getHashIndices(word);
        for (int hash : hashes) {
            bitSet.set(hash);
        }
    }

    public boolean mightContain(String word) {
        int[] hashes = getHashIndices(word);
        for (int hash : hashes) {
            if (!bitSet.get(hash)) {
                return false;
            }
        }
        return true;
    }

    private int[] getHashIndices(String word) {
        int[] hashIndices = new int[numHashFunctions];
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(word.getBytes());
            for (int i = 0; i < numHashFunctions; i++) {
                md.update((i + word).getBytes());
                byte[] hash = md.digest();
//                System.out.println(Arrays.toString(hash));
                int hashValue = ((hash[0] & 0xFF) << 24) | ((hash[1] & 0xFF) << 16) | ((hash[2] & 0xFF) << 8) | (hash[3] & 0xFF);
                System.out.println(hashValue);
                hashIndices[i] = Math.abs(hashValue % bitSetSize);
                System.out.println(Arrays.toString(hashIndices));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashIndices;
    }
}

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

class SpellChecker {
    private BloomFilter bloomFilter;

    public SpellChecker(BloomFilter bloomFilter) {
        this.bloomFilter = bloomFilter;
    }

    public boolean checkWord(String word) {
        return bloomFilter.mightContain(word.toLowerCase());
    }
}

class BloomFilterExperiment {
    public static void main(String[] args) {
        int bitSetSize = 100000; // Adjust size as needed
        int numHashFunctions = 3; // Adjust number of hash functions

        BloomFilter bloomFilter = new BloomFilter(bitSetSize, numHashFunctions);
        DictionaryLoader.loadDictionary(bloomFilter, fileName);

        SpellChecker spellChecker = new SpellChecker(bloomFilter);

        // Test with known words
        String[] testWords = {"example", "test", "word"};
        for (String word : testWords) {
            System.out.println(word + " might be in dictionary: " + spellChecker.checkWord(word));
        }

        // Test with random words
        String[] randomWords = {"qwerty", "asdfg", "zxcvb"};
        for (String word : randomWords) {
            System.out.println(word + " might be in dictionary: " + spellChecker.checkWord(word));
        }
    }
}

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