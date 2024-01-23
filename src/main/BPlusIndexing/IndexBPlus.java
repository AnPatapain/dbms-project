package main.BPlusIndexing;


import main.BPlusIndexing.BPlusTree;
import main.Tuple;

import java.io.RandomAccessFile;

public class IndexBPlus {
    BPlusTree bPlusTree;
    public IndexBPlus(int maxDegree) {
        this.bPlusTree = new BPlusTree(maxDegree);
    }

    public void createIndexBPlus(String filePath, int indexed_attribute_position) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "r");
            int header_offset = 2;
            int tableSize = randomAccessFile.read();
            int tupleSize = randomAccessFile.read();

            for (int row = 0; row < tableSize; row++) {
                // Position the file pointer at the tuple's position
                int tuplePosition = row * tupleSize + header_offset;
                randomAccessFile.seek((long) tuplePosition);

                // Read file and create tuple
                Tuple t = new Tuple(tupleSize);
                for (int j = 0; j < tupleSize; j++) {
                    t.val[j] = randomAccessFile.read();
                }

                // Index on key. Insert (key, tuplePosition) into main.BPlusIndexing.BPlusTree.
                int key = t.val[indexed_attribute_position];
//                System.out.println(key + "::" + tuplePosition);
                this.bPlusTree.insert(key, tuplePosition);
            }

            randomAccessFile.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public BPlusTree getbPlusTree() {
        return bPlusTree;
    }

    public void visualizeBPlusTree() {
        bPlusTree.display(bPlusTree.root);
    }
}
