package main.BPlusIndexing;

import main.FilePath;
import main.TableDisque;
import main.Tuple;

public class ExempleBPlusIndex {
    public static void main(String[] args) {
        TableDisque T4 = new TableDisque();
        T4.setFilePath(FilePath.PathTable2);

        Tuple t = null;
        T4.open();
        System.out.println("Table 2 ****");
        while((t = T4.next())!=null){
            System.out.println(t);
        }

        BPlusIndexCreation BPlusIndexCreation = new BPlusIndexCreation(3);
        BPlusIndexCreation.createIndexBPlus(FilePath.PathTable2, 1);

        System.out.println("---------------BPlus Indexing Tree Visualization---------------");
        BPlusIndexCreation.visualizeBPlusTree();

        System.out.println("---------------Find addresses for tuples that have value 0 for attribute 1");
        System.out.println(BPlusIndexCreation.getbPlusTree().search(0));
    }
}
