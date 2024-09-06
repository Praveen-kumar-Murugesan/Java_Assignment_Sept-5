package assignment_5Sept;
import java.util.*;
public class SingleLinkedList {

    private static class Node {
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

    public void add(Object e, int index) {
        if(index == 0){
            addFirst(e);
            return;
        }
        if(index==size){
            addLast(e);
            return;
        }
        Node current = first;
        for(int i=0; i<index-1; i++){
            current=current.next;
        }
        Node node = new Node(e, current.next);
        current.next = node;
        size++;
    }



    public void removeFirst() {
        if(first == null){
            throw new NoSuchElementException("List is empty");
        }
        first = first.next;
        if(first==null) {
            last = null;
        }
        size--;
    }

    public void removeLast() {
        if(last==null){
            throw new NoSuchElementException("List is empty");
        }
        Object item = last.item;
        if(first==last) {
            removeFirst();
            return;
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
    }

    public void removeElementFirstOcc(Object e) {
        if(first == null){
            throw new NoSuchElementException("List is empty");
        }
        if(first.item.equals(e)){
            removeFirst();
            return;
        }
        Node current = first;
        while(current.next!= null){
            if(current.next.item.equals(e)){
                current.next = current.next.next;
                if(current.next==null) {
                    last = current;
                }
                size--;
                return;
            }
            current = current.next;
        }
    }

    public void removeElementLastOcc(Object e) {
        if(last==null){
            throw new NoSuchElementException("List is empty");
        }
        if(last.item.equals(e)){
            removeLast();
            return;
        }
        Node current = first;
        Node temp = first;
        Node prev = first;
        int flag=0;
        while(current.next!= null){
            if(current.item.equals(e)){
                temp=prev;
                size--;

            }
            current = current.next;
            if (flag==1){
                prev=prev.next;
            }
            flag=1;
        }
        current = temp;
        if(current==first){
            removeFirst();
            return;
        }
        if(current.next.next != null){
            current.next = current.next.next;
        }
        else {
            current.next=null;
        }

    }

    public void removeElementAllOcc(Object e) {
        Node current = first;
        int flag=0;
        if(last==null){
            throw new NoSuchElementException("List is empty");
        }
        while(current!=null && current.next!=null) {
            if (current.item.equals(e) && current==first) {
                    removeFirst();
                    current = first;
                    continue;
                }
                if(current.next.item.equals(e)) {
                if (current.next == last) {
                    removeLast();
                    current = last;
                    continue;
                } else {
                    current.next = current.next.next;
                    flag=1;
                }
            }
                if(flag==0) {
                    current = current.next;
                }
                flag=0;
        }
    }

    public void printListSLL(){
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

    public void printListDLL(){
        Node current = first;
        while(current!= null){
            if(current.next != null){
                System.out.print(current.item + "<->");
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

    public  void reverse() {
        if(size<2){
            return;
        }
        Node prev = null;
        Node present = first;
        Node next = present.next;
        while (present != null){
            present.next = prev;
            prev = present;
            present = next;
            if(next != null){
                next = next.next;
            }
        }
        first=prev;
    }

    public void middleElement(){
        if(size==0 || size==1){
            throw new NoSuchElementException("List size should be greater than 1");
        }
        Node current = first;
        int mid = size/2;
        for(int i=0; i<mid; i++){
            current=current.next;
        }
        System.out.println("Middle element: "+current.item);
    }

    public boolean cycle(){
        Node slow = first;
        Node fast = first;
        while(fast!=null && fast.next!=null){
            slow = slow.next;
            fast = fast.next.next;
            if(slow == fast){
                return true;
            }
        }
        return false;
    }

    public static SingleLinkedList mergeSort(SingleLinkedList list1, SingleLinkedList list2){
        SingleLinkedList merged = new SingleLinkedList();
        Node current1 = list1.first;
        Node current2 = list2.first;
        while(current1!=null && current2!=null){
            if((Integer)current1.item < (Integer)current2.item){
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

    public int findFirst(Object e){
        int index = 0;
        Node current = first;
        while(current!=null){
            if(current.item.equals(e)){
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    public int findLast(Object e){
        int index = 0;
        int temp = -1;
        Node current = first;
        while(current!=null){
            if(current.item.equals(e)){
                temp = index;
            }
            current = current.next;
            index++;
        }
        return temp;
    }

    public ArrayList<Integer> findAll(Object e){
        ArrayList<Integer> listOfIndex = new ArrayList<>();
        Node current = first;
        int index = 0;
        while(current!=null){
            if(current.item.equals(e)){
                listOfIndex.add(index);
            }
            current = current.next;
            index++;
        }
        return listOfIndex;
    }

    public void clear(){
        first = null;
        last = null;
        size = 0;
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

        SingleLinkedList list = new SingleLinkedList();

        list.addFirst("one");
        list.addLast("two");
        System.out.println(list.size());
        list.printListSLL();

    }

}
