package com.zeetaminds.assgn5sept.misc;

class SpellChecker {
    private BloomFilter bloomFilter;

    public SpellChecker(BloomFilter bloomFilter) {
        this.bloomFilter = bloomFilter;
    }

    public boolean checkWord(String word) {
        return bloomFilter.mightContain(word.toLowerCase());
    }
}
