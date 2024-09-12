package com.zeetaminds.assgn5sept.misc;

import static com.zeetaminds.assgn5sept.misc.BloomFilter.fileName;

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
