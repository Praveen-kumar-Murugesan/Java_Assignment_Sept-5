public class SingleLinkedList {

	private class Node {
		Object item;
		Node next;

		Node(Object item, Node next) {
			this.item = item;
			this.next = next;
		}
	}


	int size;    // number of nodes in the list
	Node first; // head pointer
	Node last;  // tail pointer

	public SingleLinkedList() {
	}

	public Object getFirst() {
	}

	public Object getLast() {
	}

	public void addFirst(Object e) {

		}

	public void addLast(Object e) {

	}

	public boolean add(Object e) {
	}



	public Object removeFirst() {
		}

	public Object removeLast() {
		}
	


	public int size() {
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

		list.add("one");
		list.add("two");
		System.out.println(list.size());
		list.printList();
	}

}
