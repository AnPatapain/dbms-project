public class ExempleIndexScan {
    public static void main(String[] args) {
        TableDisque T4 = new TableDisque();
        T4.setFilePath("E:\\4A\\Mechanism_SGBD\\dbms-project\\src\\table1");
        T4.open();
        Tuple t = null;
        System.out.println("Table T4 ****");
        while((t = T4.next())!=null) {
            System.out.println(t);
        }
        T4.close();
        System.out.println("/t/t/t/t/t/t//t/t/tt/t/");
        String path = "E:\\4A\\Mechanism_SGBD\\dbms-project\\src\\table1";
        IndexScan indexScan = new IndexScan(path, 1, 4, new IndexCreation());
        Tuple t1 = null;
        indexScan.open();
        while((t1 = indexScan.next()) != null) {
            System.out.println(t1);
        }
        indexScan.close();
    }
}
