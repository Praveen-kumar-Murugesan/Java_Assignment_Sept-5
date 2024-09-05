package assignment_5Sept;

public class SingleLinkedList {

    private class Node {
        Object item;
        Node next;

        Node(Object item, Node next) {
            this.item = item;
            this.next = next;
        }
        Node(Object item) {
            this.item = item;
        }
    }


    int size;    // number of nodes in the list
    Node first; // head pointer
    Node last;  // tail pointer

    public SingleLinkedList() {
        this.size = 0;
    }

    public Object getFirst() {
        if(first == null){
            throw new NoSuchElementException("List is empty");
        }
        return first.item;
    }

    public Object getLast() {
        if(last == null){
            throw new NoSuchElementException("List is empty");
        }
        return last.item;
    }

    public void addFirst(Object e) {
        Node node = new Node(e);
        node.next = first;
        first = node;
        if(last==null) {
            last = first;
        }
        size++;
    }

    public void addLast(Object e) {
        Node node = new Node(e);
        if(last==null) {
            addFirst(e);
            return;
        }
        last.next = node;
        last = node;
        size++;

    }

    public boolean add(Object e) {
        addLast(e);
        return true;
    }



    public Object removeFirst() {
        if(first == null){
            throw new NoSuchElementException("List is empty");
        }
        Object item = first.item;
        first = first.next;
        if(first==null) {
            last = null;
        }
        size--;
        return item;
    }

    public Object removeLast() {
        if(last==null){
            throw new NoSuchElementException("List is empty");
        }
        Object item = last.item;
        if(first==last) {
            removeFirst();
            return item;
        }
        Node current = first;
        while(current.next != null){
            if(current.next.next == null) {
                last = current;
                last.next = null;
                break;
            }
            current = current.next;
        }
        size--;
        return item;
    }

    public void printList(){
        Node current = first;
        while(current!= null){
            if(current.next != null){
                System.out.print(current.item + "->");
            }else {
                System.out.print(current.item);
            }
            current = current.next;
        }
        System.out.println();
    }

    public int size() {
        return size;
    }



    public static class NoSuchElementException extends RuntimeException {

        public NoSuchElementException() {
            super();
        }
        public NoSuchElementException(String s) {
            super(s);
        }
    }


    //main
    public static void main(String[] args) {

        SingleLinkedList list = new SingleLinkedList();

        list.add("one");
        list.add("two");
        System.out.println(list.size());
        list.printList();
    }

}
