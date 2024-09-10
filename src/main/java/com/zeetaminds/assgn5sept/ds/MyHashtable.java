package com.zeetaminds.assgn5sept.ds;

public class MyHashtable<K, V> {

    private class Entry {
        int hash;
        K key;
        V value;

        protected Entry(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }

    private MySingleLinkedList[] table;
    private int count;

    public MyHashtable(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }
        table = new MySingleLinkedList[capacity];
        for (int i = 0; i < table.length; i++) {
            table[i] = new MySingleLinkedList();
        }
        count = 0;
    }

    public MyHashtable() {
        this(10);
    }

    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        int hash = Math.abs(key.hashCode()) % table.length;
        MySingleLinkedList list = table[hash];
        Entry entry = findEntry(list, key);

        if (entry != null) {
            V oldValue = entry.value;
            entry.value = value;
            return oldValue;
        }

        if (count >= 0.75 * table.length) {
            resize(table.length * 2);
        }

        list.addLast(new Entry(hash, key, value));
        count++;
        return null;
    }

    public V get(K key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        int hash = Math.abs(key.hashCode()) % table.length;
        MySingleLinkedList list = table[hash];
        Entry entry = findEntry(list, key);

        return entry == null ? null : entry.value;
    }

    public V remove(K key) {
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        int hash = Math.abs(key.hashCode()) % table.length;
        MySingleLinkedList list = table[hash];
        Entry entry = findEntry(list, key);

        if (entry != null) {
            list.removeElementFirstOcc(entry);
            count--;
            return entry.value;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public int size() {
        return count;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public void clear() {
        for (int i = 0; i < table.length; i++) {
            table[i].clear();
        }
        count = 0;
    }

    private void resize(int newCapacity) {
        MySingleLinkedList[] oldTable = table;
        table = new MySingleLinkedList[newCapacity];
        for (int i = 0; i < table.length; i++) {
            table[i] = new MySingleLinkedList();
        }
        count = 0;

        for (MySingleLinkedList list : oldTable) {
            MySingleLinkedList.Node node = list.first;
            while (node != null) {
                Entry entry = (Entry) node.item;
                put(entry.key, entry.value);
                node = node.next;
            }
        }
    }

    private Entry findEntry(MySingleLinkedList list, K key) {
        MySingleLinkedList.Node node = list.first;
        while (node != null) {
            Entry entry = (Entry) node.item;
            if (entry.key.equals(key)) {
                return entry;
            }
            node = node.next;
        }
        return null;
    }


    public static void main(String[] args) {
        MyHashtable<String, String> htable = new MyHashtable<>();

        htable.put("1", "ONE");
        htable.put("2", "TWO");

        System.out.println(htable.get("2"));
        System.out.println(htable.remove("1"));
        System.out.println(htable.size());
        htable.clear();
        System.out.println(htable.size());
    }
}
