
import java.util.*;

// Node class represents a node in the B-tree
class Node {
    boolean isLeaf; // Flag to check if the node is a leaf
    Node[] ptr;     // Array of child pointers
    int[] key;     // Array of keys
    int size;     // Current number of keys in the node

    Map<Integer, List<Integer>> tuplePositions; // One key can have multiple record positions, one node can have multiple keys.

    Node() {
        key = new int[BplusTree_GG.bucketSize];
        ptr = new Node[BplusTree_GG.bucketSize + 1];
        tuplePositions = new HashMap<>();
    }
}

// BplusTree class represents the B-tree structure
class BplusTree_GG {
    static int bucketSize = 3; // Size of the bucket or order of the B-tree
    Node root; // Root of the B-tree

    BplusTree_GG() {
        root = null; // Initialize the B-tree with no root initially
    }

    // Method to perform deletion in the B-tree (not implemented)
    void deleteNode(int x) {
        // Implement deletion logic if needed
    }

    // Method to search for a key in the B-tree
    Node search(int key) {
        if (root == null){
            System.out.println("Root is null");
            return null;
        }
        else {
            Node current = root;
            while (!current.isLeaf) {
                for (int i = 0; i < current.size; i++) {
                    if (key < current.key[i]) {
                        current = current.ptr[i];
                        break;
                    }
                    if (i == current.size - 1) {
                        current = current.ptr[i + 1];
                        break;
                    }
                }
            }

            for (int i = 0; i < current.size; i++) {
                if (current.key[i] == key) {
                    System.out.println("Key found ");
                    return current;
                }
            }

            System.out.println("Key not found");
            return null;
        }
    }

    // Method to find the parent of a given node in the B-tree
    Node findParent(Node current, Node child) {
        Node parent = null;
        if (current.isLeaf || current.ptr[0].isLeaf)
            return null;

        for (int i = 0; i < current.size + 1; i++) {
            if (current.ptr[i] == child) {
                parent = current;
                return parent;
            } else {
                parent = findParent(current.ptr[i], child);
                if (parent != null)
                    return parent;
            }
        }
        return parent;
    }

    // Method to insert a key into the B-tree
    void insert(int key, int tuplePosition) {
        if (root == null) {
            root = new Node();
            root.key[0] = key;
            root.isLeaf = true;
            root.size = 1;

            root.tuplePositions.putIfAbsent(key, new ArrayList<>());
            root.tuplePositions.get(key).add(tuplePosition);
        } else {
            Node current = root;
            Node parent = null;

            // Find leaf node
            while (!current.isLeaf) {
                parent = current;

                for (int i = 0; i < current.size; i++) {
                    if (key < current.key[i]) {
                        current = current.ptr[i];
                        break;
                    }

                    if (i == current.size - 1) {
                        current = current.ptr[i + 1];
                        break;
                    }
                }
            }

            // Check if there is such key in leaf node or not
            boolean keyExisted = false;
            for (int i = 0; i < current.size; i++) {
                if (current.key[i] == key) {
                    current.tuplePositions.get(key).add(tuplePosition);
                    keyExisted = true;
                    break;
                }
            }

            if (!keyExisted) {
                if (current.size < bucketSize) {
                    int i = 0;

                    while (i < current.size && key > current.key[i]) {
                        i++;
                    }

                    for (int j = current.size; j > i; j--) {
                        current.key[j] = current.key[j - 1];
                    }

                    current.key[i] = key;
                    current.size++;
                    current.ptr[current.size] = current.ptr[current.size - 1];
                    current.ptr[current.size - 1] = null;

                    current.tuplePositions.putIfAbsent(key, new ArrayList<>());
                    current.tuplePositions.get(key).add(tuplePosition);
                } else {
                    Node newLeaf = new Node();
                    int[] tempNode = new int[bucketSize + 1];
                    Map<Integer, List<Integer>> copyCurrentMap = new HashMap<>(current.tuplePositions);

                    for (int i = 0; i < bucketSize; i++) {
                        tempNode[i] = current.key[i];
                    }

                    int i = 0, j;

                    while (i < bucketSize && key > tempNode[i]) {
                        i++;
                    }

                    for (j = bucketSize; j > i; j--) {
                        tempNode[j] = tempNode[j - 1];
                    }

                    tempNode[i] = key;
                    newLeaf.isLeaf = true;
                    current.size = (bucketSize + 1) / 2;
                    newLeaf.size = (bucketSize + 1) - (bucketSize + 1) / 2;

                    // Redistribute the pointer
                    current.ptr[current.size] = newLeaf;
                    newLeaf.ptr[newLeaf.size] = current.ptr[bucketSize];

                    //current.ptr[newLeaf.size] = current.ptr[bucketSize];
                    current.ptr[bucketSize] = null;

                    // Redistribute the key
                    for (i = 0; i < current.size; i++) {
                        current.key[i] = tempNode[i];
                    }

                    for (i = 0, j = current.size; i < newLeaf.size; i++, j++) {
                        newLeaf.key[i] = tempNode[j];
                    }

                    // Redistribute the tuplePositions
                    current.tuplePositions = new HashMap<>();
                    for (i = 0; i < current.size; i++) {
                        current.tuplePositions.putIfAbsent(current.key[i], new ArrayList<>());

                        if (current.key[i] == key) {
                            current.tuplePositions.get(current.key[i]).add(tuplePosition);
                        } else {
                            for (int position : copyCurrentMap.get(current.key[i])) {
                                current.tuplePositions.get(current.key[i]).add(position);
                            }
                        }
                    }

                    for (i = 0; i < newLeaf.size; i++) {
                        newLeaf.tuplePositions.putIfAbsent(newLeaf.key[i], new ArrayList<>());

                        if (newLeaf.key[i] == key) {
                            newLeaf.tuplePositions.get(newLeaf.key[i]).add(tuplePosition);
                        } else {
                            for (int position : copyCurrentMap.get(newLeaf.key[i])) {
                                newLeaf.tuplePositions.get(newLeaf.key[i]).add(position);
                            }
                        }
                    }

                    if (current == root) {
                        Node newRoot = new Node();
                        newRoot.key[0] = newLeaf.key[0];
                        newRoot.ptr[0] = current;
                        newRoot.ptr[1] = newLeaf;
                        newRoot.isLeaf = false;
                        newRoot.size = 1;
                        root = newRoot;
                    } else {
//                        System.out.println(findParent(root, current).key[0]);
                        Node parent_ = findParent(root, current);
                        this.display(root);
                        System.out.print(parent_);
                        shiftLevel(newLeaf.key[0], findParent(root, current), newLeaf);
                    }
                }
            }
        }
    }

    // Method to shift a level in the B-tree during insertion
    void shiftLevel(int x, Node current, Node child) {
        if (current.size < bucketSize) {
            int i = 0;

            while (x > current.key[i] && i < current.size)
                i++;

            for (int j = current.size; j > i; j--)
                current.key[j] = current.key[j - 1];

            for (int j = current.size + 1; j > i + 1; j--)
                current.ptr[j] = current.ptr[j - 1];

            current.key[i] = x;
            current.size++;
            current.ptr[i + 1] = child;
        } else {
            Node newInternal = new Node();
            int[] tempKey = new int[bucketSize + 1];
            Node[] tempPtr = new Node[bucketSize + 2];

            for (int i = 0; i < bucketSize; i++)
                tempKey[i] = current.key[i];

            for (int i = 0; i < bucketSize + 1; i++)
                tempPtr[i] = current.ptr[i];

            int i = 0, j;
            while (x > tempKey[i] && i < bucketSize)
                i++;

            for (j = bucketSize + 1; j > i; j--)
                tempKey[j] = tempKey[j - 1];

            tempKey[i] = x;
            for (j = bucketSize + 2; j > i + 1; j--)
                tempPtr[j] = tempPtr[j - 1];

            tempPtr[i + 1] = child;
            newInternal.isLeaf = false;
            current.size = (bucketSize + 1) / 2;

            newInternal.size = bucketSize - (bucketSize + 1) / 2;

            for (i = 0, j = current.size + 1; i < newInternal.size; i++, j++)
                newInternal.key[i] = tempKey[j];

            for (i = 0, j = current.size + 1; i < newInternal.size + 1; i++, j++)
                newInternal.ptr[i] = tempPtr[j];

            if (current == root) {
                Node newRoot = new Node();
                newRoot.key[0] = current.key[current.size];
                newRoot.ptr[0] = current;
                newRoot.ptr[1] = newInternal;
                newRoot.isLeaf = false;
                newRoot.size = 1;
                root = newRoot;
            } else {
                shiftLevel(current.key[current.size], findParent(root, current), newInternal);
            }
        }
    }

    // Method to display the B-tree level by level using a queue
    void display(Node current) {
        if (current == null)
            return;

        Queue<Node> queue = new LinkedList<>();
        queue.add(current);

        while (!queue.isEmpty()) {
            int l = queue.size();

            for (int i = 0; i < l; i++) {
                Node tNode = queue.poll();

                if (tNode != null) {
                    for (int j = 0; j < tNode.size; j++){
                        System.out.print(tNode.key[j] + " ");
                    }

//                    if (tNode.isLeaf && tNode.ptr[tNode.size] != null) {
//                        System.out.print("-> ");
//                        for (int k = 0; k < tNode.ptr.length; k++) {
//                            if (tNode.ptr[k] != null) {
//                                for (int q = 0; q < tNode.ptr[k].key.length; q++) {
//                                    System.out.print((tNode.ptr[k].key)[q] + " ");
//                                }
//                            }
//                        }
//                    }
                    if(tNode.isLeaf) {
                        System.out.print("{ ");
                        tNode.tuplePositions.forEach((key, value) -> System.out.print(key + "-> " + value + ", " ));
                        System.out.print(" }");
                    }

                    for (int j = 0; j < tNode.size + 1; j++)
                        if (!tNode.isLeaf && tNode.ptr[j] != null)
                            queue.add(tNode.ptr[j]);

                    System.out.print("\t");
                }
            }
            System.out.println();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        BplusTree_GG bTree = new BplusTree_GG();
        System.out.println("The size of bucket is " + BplusTree_GG.bucketSize + "! ");

        // Insert some keys into the B-tree
        bTree.insert(1, 100);
        bTree.insert(2, 200);
        bTree.insert(3, 300);
        System.out.println("---------------Tree 1------------------");
        bTree.display(bTree.root);

        // Insert more keys and display the updated B-tree
        bTree.insert(4, 400);
        bTree.insert(5, 500);
        bTree.insert(3, 301);
        bTree.insert(5, 501);
//        bTree.insert(9, 900);
        System.out.println("---------------Tree 2------------------");
        bTree.display(bTree.root);
    }
}


