import assignment_5Sept.SingleLinkedList;

public class SingleLinkedListTest {
    public static void main(String[] args) {
//        SingleLinkedList list = new SingleLinkedList();
//        list.addFirst("one");
//        list.addFirst("two");
//        list.addLast("three");
//        list.addFirst("four");
//        list.addLast("five");
//        System.out.println(list.size());
//        list.printListSLL();
//        System.out.println(list.getFirst());
//        System.out.println(list.getLast());
//        list.removeFirst();
//        System.out.println(list.size());
//        list.printListSLL();
//        list.removeLast();
//        System.out.println(list.size());
//        list.printListSLL();
//        list.reverse();
//        list.printListSLL();
//        list.middleElement();
//        System.out.println(list.cycle());

        SingleLinkedList list1 = new SingleLinkedList();
        list1.addFirst(1);
        list1.addLast(7);
        SingleLinkedList list2 = new SingleLinkedList();
        list2.addFirst(4);
        list2.addLast(5);
        list2.addLast(5);
//        SingleLinkedList list = SingleLinkedList.mergeSort(list1, list2);
//        list.printListSLL();
//        System.out.println(list.findLast(4));
//        System.out.println(list.findAll(4));
        list2.addLast(4);
        list2.addLast(5);
        list2.addLast(4);
        list2.addLast(4);
        list2.printListSLL();
        list2.removeElementAllOcc(4);
//        list2.removeFirst();
//        list2.removeFirst();
//        list2.removeFirst();
        list2.printListSLL();
    }
}
