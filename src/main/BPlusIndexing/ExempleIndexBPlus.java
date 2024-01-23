package main.BPlusIndexing;

import main.FilePath;
import main.TableDisque;
import main.Tuple;

public class ExempleIndexBPlus {
    public static void main(String[] args) {
        TableDisque T4 = new TableDisque();
        T4.setFilePath(FilePath.PathTable2);

        // T4.randomize(4, 6);

        Tuple t = null;
        T4.open();
        System.out.println("Table T4 ****");
        while((t = T4.next())!=null){
            System.out.println(t);
        }

        IndexBPlusCreation indexBPlusCreation = new IndexBPlusCreation(3);
        indexBPlusCreation.createIndexBPlus(FilePath.PathTable2, 1);

        System.out.println("---------------BPlus Indexing Tree Visualization---------------");
        indexBPlusCreation.visualizeBPlusTree();

        System.out.println("---------------Find addresses for tuples that have value 0 for attribute 1");
        System.out.println(indexBPlusCreation.getbPlusTree().search(0));
    }
}
