public class ExempleIndexBPlus {
    public static void main(String[] args) {
        TableDisque T4 = new TableDisque();
        T4.setFilePath("C:\\Users\\NGUYEN KE AN\\Documents\\insa\\semester-7\\SGBD\\sgbdProjet\\src\\table1");

        // T4.randomize(4, 6);

        Tuple t = null;
        T4.open();
        System.out.println("Table T4 ****");
        while((t = T4.next())!=null){
            System.out.println(t);
        }

        IndexBPlus indexBPlus = new IndexBPlus(3);
        indexBPlus.createIndexBPlus("C:\\Users\\NGUYEN KE AN\\Documents\\insa\\semester-7\\SGBD\\sgbdProjet\\src\\table1",
                1);

        System.out.println("---------------BPlus Indexing Tree Visualization---------------");
        indexBPlus.visualizeBPlusTree();

        System.out.println("---------------Find addresses for tuples that have value 0 for attribute 1");
        System.out.println(indexBPlus.getbPlusTree().search(0));
    }
}
