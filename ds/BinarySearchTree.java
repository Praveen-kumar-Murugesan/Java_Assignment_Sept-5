public class BinarySearchTree {

	private class Node {
		int data;
		Node left;
		Node right;

		Node(int data) {
			this.data = data;
			this.left = null;
			this.right = null;
		}

		Node(Node left, int data, Node right) {
			this.data = data;
			this.left = left;
			this.right = right;
		}
	}

	Node root;

	public BinarySearchTree() {
	}

	public void insert(int data) {
	}

	public void inorder() {
	 }

	public void preorder() {
	 }


	 public void postorder() {
	 }



	public static void main(String[] args) {
		int[] nums = {14, 2, 40, 9, 31, 25, 18,5};

		BinarySearchTree bst = new BinarySearchTree();
		for(int n : nums ) {
			bst.insert(n);
		}

		//prints sorted numbers
		//inorder traversing , prints number in sorted order
		bst.inorder();

	}

}
