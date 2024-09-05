import assignment_5Sept.SingleLinkedList;

public class SingleLinkedListTest {
    public static void main(String[] args) {
        SingleLinkedList list = new SingleLinkedList();
        list.addFirst("one");
        list.addFirst("two");
        list.addLast("three");
        System.out.println(list.size());
        list.printList();
        System.out.println(list.getFirst());
        System.out.println(list.getLast());
        list.removeFirst();
        System.out.println(list.size());
        list.printList();
        list.removeLast();
        System.out.println(list.size());
        list.printList();
    }
}
