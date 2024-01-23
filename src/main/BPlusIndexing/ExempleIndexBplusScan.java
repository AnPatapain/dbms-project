package main.BPlusIndexing;

import main.FilePath;
import main.TableDisque;
import main.Tuple;

public class ExempleIndexBplusScan {
    public static void main(String[] args) {
        TableDisque T4 = new TableDisque();
        T4.setFilePath(FilePath.PathTable2);
        T4.open();
        Tuple t = null;
        System.out.println("Table T4 ****");
        while((t = T4.next())!=null) {
            System.out.println(t);
        }
        T4.close();

        String path = FilePath.PathTable2;

        BplusIndexScan indexScan = new BplusIndexScan(path, 1, 4, new IndexBPlusCreation(3));

        Tuple t1 = null;
        indexScan.open();
        System.out.println("Visualize the tree");
        indexScan.indexBPlusCreation.bPlusTree.display(indexScan.indexBPlusCreation.bPlusTree.root);

        System.out.println("Search tuples that haves value 4 for attribute 1 on BplusIndex");
        while((t1 = indexScan.next()) != null) {
            System.out.println(t1);
        }
        indexScan.close();
    }
}
