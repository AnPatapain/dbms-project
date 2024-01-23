public class ExempleIndexBplusScan {
    public static void main(String[] args) {
        TableDisque T4 = new TableDisque();
        T4.setFilePath("C:\\Users\\NGUYEN KE AN\\Documents\\insa\\semester-7\\SGBD\\sgbdProjet\\src\\table1");
        T4.open();
        Tuple t = null;
        System.out.println("Table T4 ****");
        while((t = T4.next())!=null) {
            System.out.println(t);
        }
        T4.close();

        String path = "C:\\Users\\NGUYEN KE AN\\Documents\\insa\\semester-7\\SGBD\\sgbdProjet\\src\\table1";

        System.out.println("Search tuples that haves value 0 for attribute 1 on BplusIndex");
        BplusIndexScan indexScan = new BplusIndexScan(path, 1, 0, new IndexBPlus(3));
        Tuple t1 = null;
        indexScan.open();
        while((t1 = indexScan.next()) != null) {
            System.out.println(t1);
        }
        indexScan.close();
    }
}
