package com.zeetaminds.assgn5sept;

public class SingleLinkedList<T extends Comparable<T>> {

    private static class Node<T> {
        T item;
        Node<T> next;
        Node<T> prev;
        Node(T item, Node<T> next) {
            this.item = item;
            this.next = next;
        }
        Node(T item, Node<T> next, Node<T> prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
        Node(T item) {
            this.item = item;
        }
    }


    int size;    // number of nodes in the list

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size cannot be negative");
        }
        this.size = size;
    }

    Node<T> first; // head pointer
    Node<T> last;  // tail pointer

    public SingleLinkedList() {
        this.size = 0;
    }

    public T getFirst() {
        if(first == null){
            throw new NoSuchElementException("List is empty");
        }
        return first.item;
    }

    public T getLast() {
        if(last == null){
            throw new NoSuchElementException("List is empty");
        }
        return last.item;
    }

    public void addFirst(T e) {
        first = new Node<>(e, first);
        if (last == null) {
            last = first;
        }
        setSize(size+1);
    }

    public void addLast(T e) {
        Node<T> node = new Node<>(e);
        if(last==null) {
            addFirst(e);
            return;
        }
        last.next = node;
        last = node;
        setSize(size+1);

    }

    public void add(T e) {
        addLast(e);
    }



    public void removeFirst() {
        if(first == null){
            throw new NoSuchElementException("List is empty");
        }
        first = first.next;
        if(first==null) {
            last = null;
        }
        setSize(size-1);
    }

    public void removeLast() {
        if(last==null){
            throw new NoSuchElementException("List is empty");
        }
        if(first==last) {
            removeFirst();
            return;
        }
        Node<T> current = first;
        while(current.next != null){
            if(current.next.next == null) {
                last = current;
                last.next = null;
                break;
            }
            current = current.next;
        }
        setSize(size-1);
    }

    public String printList(){
        Node<T> current = first;
        StringBuilder res= new StringBuilder();
        while(current!= null){
            if(current.next != null){
                res.append(current.item).append("->");
            }else {
                res.append(current.item);
            }
            current = current.next;
        }
        System.out.println();
        return res.toString().trim();
    }

    public int size() {
        return getSize();
    }

    public  void reverse() {
        if(size<2){
            return;
        }
        Node<T> prev = null;
        Node<T> present = first;
        Node<T> next = present.next;
        while (present != null){
            present.next = prev;
            prev = present;
            present = next;
            if(next != null){
                next = next.next;
            }
        }
        last=first;
        first=prev;
    }

    public T findMiddleElement(){
        if(size==0 || size==1){
            throw new NoSuchElementException("List size should be greater than 1");
        }
        Node<T> current = first;
        int mid = size/2;
        for(int i=0; i<mid; i++){
            current=current.next;
        }
        return current.item;
    }

    public boolean cycle(){
        Node<T> slow = first;
        Node<T> fast = first;
        while(fast!=null && fast.next!=null){
            slow = slow.next;
            fast = fast.next.next;
            if(slow == fast){
                return true;
            }
        }
        return false;
    }

    public static <T extends Comparable<T>> SingleLinkedList<T> mergeSortedLists(SingleLinkedList<T> list1, SingleLinkedList<T> list2){
        SingleLinkedList<T> merged = new SingleLinkedList<>();
        Node<T> current1 = list1.first;
        Node<T> current2 = list2.first;
        while(current1!=null && current2!=null){
            if(current1.item.compareTo(current2.item) < 0){
                merged.addLast(current1.item);
                current1 = current1.next;
            } else {
                merged.addLast(current2.item);
                current2 = current2.next;
            }
        }
        while(current1!=null){
            merged.addLast(current1.item);
            current1 = current1.next;
        }
        while(current2!=null){
            merged.addLast(current2.item);
            current2 = current2.next;
        }
        return merged;
    }

    public boolean contains(T e){
        Node<T> current = first;
        while(current!=null){
            if(current.item.equals(e)){
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public void clear(){
        first = null;
        last = null;
        setSize(0);
    }

    public void toDoublyLinkedList(){
        Node<T> current = first;
        Node<T> prev = null;
        while(current!= null) {
            current.prev = prev;
            prev = current;
            current = current.next;
        }
        last=prev;
    }

    public static class NoSuchElementException extends RuntimeException {

        public NoSuchElementException() {
            super();
        }
        public NoSuchElementException(String s) {
            super(s);
        }
    }

    public static void main(String[] args) {
        SingleLinkedList<Integer> list = new SingleLinkedList<>();
        list.addFirst(1);
        list.addLast(2);
        list.addLast(3);
        list.printList(); // Output: 1 -> 2 -> 3 -> 4

        System.out.println("Size: " + list.size()); // Output: Size: 4
        System.out.println("First: " + list.getFirst()); // Output: First: 1
        System.out.println("Last: " + list.getLast()); // Output: Last: 4

        list.reverse();
        String res = list.printList();
        System.out.println(res);
    }
}