package com.zeetaminds.assgn5sept;

public class BinarySearchTree<T extends Comparable<T>> {

    protected class Node {
        T data;
        Node left;
        Node right;
        int height;

        Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }

        Node(Node left, T data, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        Node(Node left, T data) {
            this.data = data;
            this.left = left;
            this.right = null;
        }
    }

    Node root;

    public BinarySearchTree() {
        this.root = null;
    }

    public void insert(T data) {
        root = insertRec(root, data);
    }

    public Node insertRec(Node root, T data) {
        if (root == null) {
            root = new Node(data);
            return root;
        }
        if (data.compareTo(root.data) < 0) {
            root.left = insertRec(root.left, data);
        } else if (data.compareTo(root.data) > 0) {
            root.right = insertRec(root.right, data);
        }
        return root;
    }

    public String inorder() {
        return inorderRec(root).trim();
    }

    private String inorderRec(Node root) {
        if (root == null) return "";
        return inorderRec(root.left) + root.data + " " + inorderRec(root.right);
    }

    public String preorder() {
        return preorderRec(root).trim();
    }

    private String preorderRec(Node root) {
        if (root == null) return "";
        return root.data + " " + preorderRec(root.left) + preorderRec(root.right);
    }

    public String postorder() {
        return postorderRec(root).trim();
    }

    private String postorderRec(Node root) {
        if (root == null) return "";
        return postorderRec(root.left) + postorderRec(root.right) + root.data + " ";
    }

    public Node find(T data) {
        return findRec(root, data);
    }

    private Node findRec(Node root, T data) {
        if (root == null || root.data.equals(data)) {
            return root;
        }

        if (data.compareTo(root.data) < 0) {
            return findRec(root.left, data);
        }

        return findRec(root.right, data);
    }

    public void delete(T data) {
        root = deleteRec(root, data);
    }

    private Node deleteRec(Node root, T data) {
        if (root == null) return root;

        if (data.compareTo(root.data) < 0) {
            root.left = deleteRec(root.left, data);
        } else if (data.compareTo(root.data) > 0) {
            root.right = deleteRec(root.right, data);
        } else {
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;

            root.data = findMin(root.right).data;
            root.right = deleteRec(root.right, root.data);
        }

        return root;
    }

    public T findMin() {
        return findMin(root).data;
    }

    private Node findMin(Node root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    public T findMax() {
        return findMax(root).data;
    }

    private Node findMax(Node root) {
        while (root.right != null) {
            root = root.right;
        }
        return root;
    }

    public int height() {
        return heightRec(root);
    }

    private int heightRec(Node root) {
        if (root == null) return 0;

        return 1 + Math.max(heightRec(root.left), heightRec(root.right));
    }

    public boolean isBalanced() {
        return isBalancedRec(root);
    }

    private boolean isBalancedRec(Node root) {
        if (root == null) return true;

        int leftHeight = heightRec(root.left);
        int rightHeight = heightRec(root.right);

        if (Math.abs(leftHeight - rightHeight) > 1) return false;

        return isBalancedRec(root.left) && isBalancedRec(root.right);
    }

    public boolean isBST() {
        return isBSTRec(root, null, null);
    }

    private boolean isBSTRec(Node root, T min, T max) {
        if (root == null) return true;

        if ((min != null && root.data.compareTo(min) <= 0) || (max != null && root.data.compareTo(max) >= 0)) {
            return false;
        }

        return isBSTRec(root.left, min, root.data) && isBSTRec(root.right, root.data, max);
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();

        // Inserting elements
        int[] nums = {14, 2, 40, 9, 31, 25, 18, 5};
        for (int n : nums) {
            bst.insert(n);
        }

        System.out.println("Inorder traversal (should be sorted):");
        System.out.println(bst.inorder());

        System.out.println("Preorder traversal:");
        System.out.println(bst.preorder());

        System.out.println("Postorder traversal:");
        System.out.println(bst.postorder());

        // Test find method
        System.out.println("Find 25: " + (bst.find(25) != null ? "Found" : "Not Found"));
        System.out.println("Find 100: " + (bst.find(100) != null ? "Found" : "Not Found"));

        // Test findMin and findMax methods
        System.out.println("Minimum value: " + bst.findMin());
        System.out.println("Maximum value: " + bst.findMax());

        // Test height and isBalanced methods
        System.out.println("Height of the tree: " + bst.height());
        System.out.println("Is the tree balanced? " + (bst.isBalanced() ? "Yes" : "No"));

        // Delete a node
        System.out.println("Deleting node with value 25.");
        bst.delete(25);

        System.out.println("Inorder traversal after deletion:");
        bst.inorder();
        System.out.println();

        // Test height and isBalanced methods again after deletion
        System.out.println("Height of the tree after deletion: " + bst.height());
        System.out.println("Is the tree balanced after deletion? " + (bst.isBalanced() ? "Yes" : "No"));

        // Test isBST method
        System.out.println("Is the tree a valid BST? " + (bst.isBST() ? "Yes" : "No"));
    }

}