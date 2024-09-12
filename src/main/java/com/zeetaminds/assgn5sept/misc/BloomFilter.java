package com.zeetaminds.assgn5sept.misc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


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

