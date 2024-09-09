package com.zeetaminds.assgn5sept;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {
    private BinarySearchTree<Integer> bst;

    @BeforeEach
    void setUp() {
        bst = new BinarySearchTree<>();
    }

    @Test
    void insert() {
        bst.insert(10);
        bst.insert(5);
        bst.insert(15);
        assertEquals("5 10 15", bst.inorder(), "Inorder traversal after inserting 10, 5, 15 should be '5 10 15'");
    }

    @Test
    void inorder() {
        int[] nums = {14, 2, 40, 9, 31, 25, 18, 5};
        for (int n : nums) {
            bst.insert(n);
        }
        assertEquals("2 5 9 14 18 25 31 40", bst.inorder(), "Inorder traversal should return sorted elements");
    }

    @Test
    void preorder() {
        int[] nums = {14, 2, 40, 9, 31, 25, 18, 5};
        for (int n : nums) {
            bst.insert(n);
        }
        assertEquals("14 2 9 5 40 31 25 18", bst.preorder(), "Preorder traversal should follow root-left-right order");
    }

    @Test
    void postorder() {
        int[] nums = {14, 2, 40, 9, 31, 25, 18, 5};
        for (int n : nums) {
            bst.insert(n);
        }
        assertEquals("5 9 2 18 25 31 40 14", bst.postorder(), "Postorder traversal should follow left-right-root order");
    }

    @Test
    void find() {
        int[] nums = {14, 2, 40, 9, 31, 25, 18, 5};
        for (int n : nums) {
            bst.insert(n);
        }
        assertNotNull(bst.find(25), "Node with value 25 should be found");
        assertNull(bst.find(100), "Node with value 100 should not be found");
    }


    @Test
    void delete() {
        int[] nums = {14, 2, 40, 9, 31, 25, 18, 5};
        for (int n : nums) {
            bst.insert(n);
        }
        bst.delete(25);
        assertEquals("2 5 9 14 18 31 40", bst.inorder(), "Inorder traversal after deleting 25 should exclude 25");
    }

    @Test
    void findMin() {
        int[] nums = {14, 2, 40, 9, 31, 25, 18, 5};
        for (int n : nums) {
            bst.insert(n);
        }
        assertEquals(2, bst.findMin(), "Minimum value in the BST should be 2");
    }

    @Test
    void findMax() {
        int[] nums = {14, 2, 40, 9, 31, 25, 18, 5};
        for (int n : nums) {
            bst.insert(n);
        }
        assertEquals(40, bst.findMax(), "Maximum value in the BST should be 40");
    }

    @Test
    void height() {
        int[] nums = {14, 2, 40, 9, 31, 25, 18, 5};
        for (int n : nums) {
            bst.insert(n);
        }
        assertEquals(4, bst.height(), "Height of the BST should be 4");
    }

    @Test
    void isBalanced() {
        int[] nums = {14, 2, 40, 9, 31, 25, 18, 5};
        for (int n : nums) {
            bst.insert(n);
        }
        assertTrue(bst.isBalanced(), "The BST should be balanced");

        // Create an unbalanced tree
        BinarySearchTree<Integer> unbalancedBst = new BinarySearchTree<>();
        unbalancedBst.insert(10);
        unbalancedBst.insert(5);
        unbalancedBst.insert(2);
        assertFalse(unbalancedBst.isBalanced(), "The unbalanced BST should not be balanced");
    }


}