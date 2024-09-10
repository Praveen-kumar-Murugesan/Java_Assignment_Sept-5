package com.zeetaminds.assgn5sept.ds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyHashtableTest {

    @Test
    void put() {
        MyHashtable<String, String> htable = new MyHashtable<>();

        assertNull(htable.put("1", "ONE"));
        assertEquals("ONE", htable.get("1"));
        assertEquals(1, htable.size());

        assertEquals("ONE", htable.put("1", "UPDATED_ONE"));
        assertEquals("UPDATED_ONE", htable.get("1"));
        assertEquals(1, htable.size());
    }

    @Test
    void get() {
        MyHashtable<String, String> htable = new MyHashtable<>();

        htable.put("1", "ONE");
        htable.put("2", "TWO");

        assertEquals("ONE", htable.get("1"));
        assertEquals("TWO", htable.get("2"));
        assertNull(htable.get("3"));
    }

    @Test
    void remove() {
        MyHashtable<String, String> htable = new MyHashtable<>();

        htable.put("1", "ONE");
        htable.put("2", "TWO");

        assertEquals("ONE", htable.remove("1"));
        assertNull(htable.get("1"));
        assertEquals(1, htable.size());
        assertNull(htable.remove("3"));
        assertEquals(1, htable.size());
    }

    @Test
    void containsKey() {
        MyHashtable<String, String> htable = new MyHashtable<>();

        htable.put("1", "ONE");
        htable.put("2", "TWO");

        assertTrue(htable.containsKey("1"));
        assertTrue(htable.containsKey("2"));
        assertFalse(htable.containsKey("3"));
    }

    @Test
    void size() {
        MyHashtable<String, String> htable = new MyHashtable<>();

        assertEquals(0, htable.size());

        htable.put("1", "ONE");
        htable.put("2", "TWO");

        assertEquals(2, htable.size());
        htable.remove("1");
        assertEquals(1, htable.size());
    }

    @Test
    void isEmpty() {
        MyHashtable<String, String> htable = new MyHashtable<>();

        assertTrue(htable.isEmpty());

        htable.put("1", "ONE");

        assertFalse(htable.isEmpty());
        htable.remove("1");
        assertTrue(htable.isEmpty());
    }

    @Test
    void clear() {
        MyHashtable<String, String> htable = new MyHashtable<>();

        htable.put("1", "ONE");
        htable.put("2", "TWO");

        assertEquals(2, htable.size());

        htable.clear();

        assertEquals(0, htable.size());
        assertTrue(htable.isEmpty());
        assertNull(htable.get("1"));
        assertNull(htable.get("2"));
    }
}
