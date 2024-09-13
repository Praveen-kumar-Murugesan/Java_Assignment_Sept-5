package com.zeetaminds.assgn5sept.ds;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SingleLinkedListTest {

    @Test
    void getFirst() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.addFirst(1);
        list.addLast(2);
        assertEquals(1, list.getFirst());
    }

    @Test
    void getLast() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.addFirst(1);
        list.addLast(2);
        assertEquals(2, list.getLast());
    }

    @Test
    void addFirst() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.addFirst(1);
        list.addFirst(2);
        assertEquals(2, list.getFirst());
        assertEquals(1, list.getLast());
        assertEquals(2, list.size());
    }

    @Test
    void addLast() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        assertEquals(1, list.getFirst());
        assertEquals(2, list.getLast());
        assertEquals(2, list.size());
    }

    @Test
    void add() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.add(1);
        list.add(2);
        assertEquals(1, list.getFirst());
        assertEquals(2, list.getLast());
        assertEquals(2, list.size());
    }

    @Test
    void removeFirst() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.addFirst(1);
        list.addFirst(2);
        list.removeFirst();
        assertEquals(1, list.getFirst());
        assertEquals(1, list.size());
    }

    @Test
    void removeLast() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.addFirst(1);
        list.addFirst(2);
        list.removeLast();
        assertEquals(2, list.getFirst());
        assertEquals(1, list.size());
    }

    @Test
    void printList() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.addFirst(3);
        list.addFirst(2);
        list.addFirst(1);
        assertEquals("1->2->3", list.printList());

    }

    @Test
    void size() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.addFirst(1);
        list.addLast(2);
        assertEquals(2, list.size());
    }

    @Test
    void reverse() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.addFirst(1);
        list.addLast(2);
        list.addLast(3);
        list.reverse();
        assertEquals(3, list.getFirst());
        assertEquals(1, list.getLast());
    }

    @Test
    void findMiddleElement() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertEquals(2, list.findMiddleElement());
    }

    @Test
    void cycle() {
//        SingleLinkedList<Integer> list = new SingleLinkedList<>();
//        list.addLast(1);
//        list.addLast(2);
//        list.addLast(3);
//        assertTrue(list.cycle());

        SingleLinkedList<Integer> listNoCycle = new SingleLinkedList<>();
        listNoCycle.addLast(1);
        listNoCycle.addLast(2);
        listNoCycle.addLast(3);
        assertFalse(listNoCycle.cycle());
    }

    @Test
    void mergeSortedLists() {
        SingleLinkedList<Integer> list1 = new SingleLinkedList<>();
        list1.addLast(1);
        list1.addLast(3);
        list1.addLast(5);

        SingleLinkedList<Integer> list2 = new SingleLinkedList<>();
        list2.addLast(2);
        list2.addLast(4);
        list2.addLast(6);

        SingleLinkedList<Integer> merged = SingleLinkedList.mergeSortedLists(list1, list2);
        assertEquals(1, merged.getFirst());
        assertEquals(6, merged.getLast());
        assertEquals(6, merged.size());
    }

    @Test
    void contains() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.addFirst(1);
        list.addLast(2);
        assertTrue(list.contains(1));
        assertFalse(list.contains(3));
    }

    @Test
    void clear() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.addFirst(1);
        list.addLast(2);
        list.clear();
        assertThrows(SingleLinkedList.NoSuchElementException.class, list::getFirst);
        assertThrows(SingleLinkedList.NoSuchElementException.class, list::getLast);
    }

    @Test
    void toDoublyLinkedList() {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.addFirst(1);
        list.addLast(2);
        list.toDoublyLinkedList();
        assertEquals(1, list.getFirst());
        assertEquals(2, list.getLast());
    }
}