import java.util.*;


public class BPlusTree {
    int m;
    InternalNode root;
    LeafNode firstLeaf;

    public BPlusTree(int m) {
        this.m = m;
        this.root = null;
    }

    // Binary search program
    private int binarySearch(DictionaryPair[] dps, int numPairs, int t) {
        Comparator<DictionaryPair> c = new Comparator<DictionaryPair>() {
            @Override
            public int compare(DictionaryPair o1, DictionaryPair o2) {
                Integer a = Integer.valueOf(o1.key);
                Integer b = Integer.valueOf(o2.key);
                return a.compareTo(b);
            }
        };
        return Arrays.binarySearch(dps, 0, numPairs, new DictionaryPair(t, new ArrayList<>()), c);
    }

    // Find the leaf node
    private LeafNode findLeafNode(int key) {

        Integer[] keys = this.root.keys;
        int i;

        for (i = 0; i < this.root.degree - 1; i++) {
            if (key < keys[i]) {
                break;
            }
        }

        Node child = this.root.childPointers[i];
        if (child instanceof LeafNode) {
            return (LeafNode) child;
        } else {
            return findLeafNode((InternalNode) child, key);
        }
    }

    // Find the leaf node
    private LeafNode findLeafNode(InternalNode node, int key) {

        Integer[] keys = node.keys;
        int i;

        for (i = 0; i < node.degree - 1; i++) {
            if (key < keys[i]) {
                break;
            }
        }
        Node childNode = node.childPointers[i];
        if (childNode instanceof LeafNode) {
            return (LeafNode) childNode;
        } else {
            return findLeafNode((InternalNode) node.childPointers[i], key);
        }
    }

    // Get the mid point
    private int getMidpoint() {
        return (int) Math.ceil((this.m + 1) / 2.0) - 1;
    }

    private boolean isEmpty() {
        return firstLeaf == null;
    }

    private int linearNullSearch(DictionaryPair[] dps) {
        for (int i = 0; i < dps.length; i++) {
            if (dps[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private int linearNullSearch(Node[] pointers) {
        for (int i = 0; i < pointers.length; i++) {
            if (pointers[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private void sortDictionary(DictionaryPair[] dictionary) {
        Arrays.sort(dictionary, new Comparator<DictionaryPair>() {
            @Override
            public int compare(DictionaryPair o1, DictionaryPair o2) {
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 == null) {
                    return 1;
                }
                if (o2 == null) {
                    return -1;
                }
                return o1.compareTo(o2);
            }
        });
    }

    private Node[] splitChildPointers(InternalNode in, int split) {

        Node[] pointers = in.childPointers;
        Node[] halfPointers = new Node[this.m + 1];

        for (int i = split + 1; i < pointers.length; i++) {
            halfPointers[i - split - 1] = pointers[i];
            in.removePointer(i);
        }

        return halfPointers;
    }

    private DictionaryPair[] splitDictionary(LeafNode ln, int split) {

        DictionaryPair[] dictionary = ln.dictionary;

        DictionaryPair[] halfDict = new DictionaryPair[this.m];

        for (int i = split; i < dictionary.length; i++) {
            halfDict[i - split] = dictionary[i];
            ln.delete(i);
        }

        return halfDict;
    }

    private void splitInternalNode(InternalNode in) {

        InternalNode parent = in.parent;

        int midpoint = getMidpoint();
        int newParentKey = in.keys[midpoint];
        Integer[] halfKeys = splitKeys(in.keys, midpoint);
        Node[] halfPointers = splitChildPointers(in, midpoint);

        in.degree = linearNullSearch(in.childPointers);

        InternalNode sibling = new InternalNode(this.m, halfKeys, halfPointers);
        for (Node pointer : halfPointers) {
            if (pointer != null) {
                pointer.parent = sibling;
            }
        }

        sibling.rightSibling = in.rightSibling;
        if (sibling.rightSibling != null) {
            sibling.rightSibling.leftSibling = sibling;
        }
        in.rightSibling = sibling;
        sibling.leftSibling = in;

        if (parent == null) {

            Integer[] keys = new Integer[this.m];
            keys[0] = newParentKey;
            InternalNode newRoot = new InternalNode(this.m, keys);
            newRoot.appendChildPointer(in);
            newRoot.appendChildPointer(sibling);
            this.root = newRoot;

            in.parent = newRoot;
            sibling.parent = newRoot;

        } else {

            parent.keys[parent.degree - 1] = newParentKey;
            Arrays.sort(parent.keys, 0, parent.degree);

            int pointerIndex = parent.findIndexOfPointer(in) + 1;
            parent.insertChildPointer(sibling, pointerIndex);
            sibling.parent = parent;
        }
    }

    private Integer[] splitKeys(Integer[] keys, int split) {

        Integer[] halfKeys = new Integer[this.m];

        keys[split] = null;

        for (int i = split + 1; i < keys.length; i++) {
            halfKeys[i - split - 1] = keys[i];
            keys[i] = null;
        }

        return halfKeys;
    }

    public void insert(int key, double value) {
        if (isEmpty()) {

            LeafNode ln = new LeafNode(this.m, new DictionaryPair(key, new ArrayList<>(Arrays.asList(value))));

            this.firstLeaf = ln;

        } else {
            LeafNode ln = (this.root == null) ? this.firstLeaf : findLeafNode(key);

            if (!ln.insert(new DictionaryPair(key, new ArrayList<>(Arrays.asList(value))))) {

                ln.dictionary[ln.numPairs] = new DictionaryPair(key, new ArrayList<>(Arrays.asList(value)));
                ln.numPairs++;
                sortDictionary(ln.dictionary);

                int midpoint = getMidpoint();
                DictionaryPair[] halfDict = splitDictionary(ln, midpoint);

                if (ln.parent == null) {
                    Integer[] parent_keys = new Integer[this.m];
                    parent_keys[0] = halfDict[0].key;
                    InternalNode parent = new InternalNode(this.m, parent_keys);
                    ln.parent = parent;
                    parent.appendChildPointer(ln);

                } else {
                    int newParentKey = halfDict[0].key;
                    ln.parent.keys[ln.parent.degree - 1] = newParentKey;
                    Arrays.sort(ln.parent.keys, 0, ln.parent.degree);
                }

                LeafNode newLeafNode = new LeafNode(this.m, halfDict, ln.parent);

                int pointerIndex = ln.parent.findIndexOfPointer(ln) + 1;
                ln.parent.insertChildPointer(newLeafNode, pointerIndex);

                newLeafNode.rightSibling = ln.rightSibling;
                if (newLeafNode.rightSibling != null) {
                    newLeafNode.rightSibling.leftSibling = newLeafNode;
                }
                ln.rightSibling = newLeafNode;
                newLeafNode.leftSibling = ln;

                if (this.root == null) {
                    this.root = ln.parent;
                } else {
                    InternalNode in = ln.parent;
                    while (in != null) {
                        if (in.isOverfull()) {
                            splitInternalNode(in);
                        } else {
                            break;
                        }
                        in = in.parent;
                    }
                }
            }
        }
    }

    public List<Double> search(int key) {

        if (isEmpty()) {
            return null;
        }

        LeafNode ln = (this.root == null) ? this.firstLeaf : findLeafNode(key);

        DictionaryPair[] dps = ln.dictionary;
        int index = binarySearch(dps, ln.numPairs, key);

        if (index < 0) {
            return null;
        } else {
            return dps[index].values;
        }
    }

    void display(InternalNode current) {
        if (current == null) return;

        Queue<Node> queue = new LinkedList<>();
        queue.add(current);

        while (!queue.isEmpty()) {
            int l = queue.size();

            for (int i = 0; i < l; i++) {
                Node tNode = queue.poll();
                if (tNode != null) {
                    if (tNode instanceof InternalNode) {
                        for (Integer key : ((InternalNode) tNode).keys) {
                            System.out.print(key + " ");
                        }
                        for (Node node : ((InternalNode) tNode).childPointers) {
                            queue.add(node);
                        }
                    } else if (tNode instanceof LeafNode) {
                        for (DictionaryPair pair : ((LeafNode) tNode).dictionary) {
                            if (pair != null) {
                                System.out.print("(" + pair.key + ":" + pair.values + ")");
                            }
                        }
                    }
                }
                System.out.print("\t");
            }
            System.out.println();
        }
    }

    public class Node {
        InternalNode parent;
    }

    private class InternalNode extends Node {
        int maxDegree;
        int degree;
        InternalNode leftSibling;
        InternalNode rightSibling;
        Integer[] keys;
        Node[] childPointers;

        private void appendChildPointer(Node pointer) {
            this.childPointers[degree] = pointer;
            this.degree++;
        }

        private int findIndexOfPointer(Node pointer) {
            for (int i = 0; i < childPointers.length; i++) {
                if (childPointers[i] == pointer) {
                    return i;
                }
            }
            return -1;
        }

        private void insertChildPointer(Node pointer, int index) {
            for (int i = degree - 1; i >= index; i--) {
                childPointers[i + 1] = childPointers[i];
            }
            this.childPointers[index] = pointer;
            this.degree++;
        }

        private boolean isOverfull() {
            return this.degree == maxDegree + 1;
        }


        private void removePointer(int index) {
            this.childPointers[index] = null;
            this.degree--;
        }

        private InternalNode(int m, Integer[] keys) {
            this.maxDegree = m;
            this.degree = 0;
            this.keys = keys;
            this.childPointers = new Node[this.maxDegree + 1];
        }

        private InternalNode(int m, Integer[] keys, Node[] pointers) {
            this.maxDegree = m;
            this.degree = linearNullSearch(pointers);
            this.keys = keys;
            this.childPointers = pointers;
        }
    }

    public class LeafNode extends Node {
        int maxNumPairs;
        int numPairs;
        LeafNode leftSibling;
        LeafNode rightSibling;
        DictionaryPair[] dictionary;

        public void delete(int index) {
            this.dictionary[index] = null;
            numPairs--;
        }

        public boolean insert(DictionaryPair dp) {
            boolean keyExisted_ = false;
            for (int i = 0; i < numPairs; i++) {
                if (this.dictionary[i].key == dp.key) {
                    keyExisted_ = true;
                    break;
                }
            }
            if (this.isFull() && !keyExisted_) {
                return false;
            } else {
                boolean keyExisted = false;
                for (int i = 0; i < numPairs; i++) {
                    if (this.dictionary[i].key == dp.key) {
                        this.dictionary[i].values.add(dp.values.get(0));
                        keyExisted = true;
                        break;
                    }
                }
                if(!keyExisted) {
                    this.dictionary[numPairs] = dp;
                    numPairs++;
                    Arrays.sort(this.dictionary, 0, numPairs);
                }
                return true;
            }
        }

        public boolean isFull() {

            return numPairs == maxNumPairs;
        }


        public LeafNode(int m, DictionaryPair dp) {
            this.maxNumPairs = m - 1;
            this.dictionary = new DictionaryPair[m];
            this.numPairs = 0;
            this.insert(dp);
        }

        public LeafNode(int m, DictionaryPair[] dps, InternalNode parent) {
            this.maxNumPairs = m - 1;
            this.dictionary = dps;
            this.numPairs = linearNullSearch(dps);
            this.parent = parent;
        }

        public void displayDictionary() {
            Arrays.stream(this.dictionary).toList().forEach((dictionaryPair) -> {
                if(dictionaryPair != null) {
                    System.out.print(dictionaryPair.key + ": " + dictionaryPair.values);
                }
            });
            System.out.println();
        }
    }

    public class DictionaryPair implements Comparable<DictionaryPair> {
        int key;
        List<Double> values;

        public DictionaryPair(int key, List<Double> values) {
            this.key = key;
            this.values = values;
        }

        public int compareTo(DictionaryPair o) {
            if (key == o.key) {
                return 0;
            } else if (key > o.key) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    public static void main(String[] args) {
        BPlusTree bpt = null;
        bpt = new BPlusTree(3);

        bpt.insert(4, 2);
        bpt.display(bpt.root);

        bpt.insert(4, 6);
        bpt.display(bpt.root);

        bpt.insert(3, 10);
        bpt.display(bpt.root);

        bpt.insert(3, 14);
        bpt.display(bpt.root);

        bpt.insert(4, 18);
        bpt.display(bpt.root);

        bpt.insert(2, 22);
        bpt.display(bpt.root);


        if (bpt.search(4) != null) {
            System.out.println("Found: " + bpt.search(4));
        } else {
            System.out.println("Not Found");
        }
    }
}