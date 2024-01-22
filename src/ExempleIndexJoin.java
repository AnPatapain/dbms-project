public class ExempleIndexJoin {
    public static void main(String[] args){
        String filepath1 = "E:\\4A\\Mechanism_SGBD\\dbms-project\\src\\table1";
        String filepath2 = "E:\\4A\\Mechanism_SGBD\\dbms-project\\src\\table2";
        IndexJoin indexJoin = new IndexJoin(filepath1,filepath2,1,1);
        TableDisque T4 = new TableDisque();
        T4.setFilePath("E:\\4A\\Mechanism_SGBD\\dbms-project\\src\\table1");
        TableDisque T5 = new TableDisque();
        T5.setFilePath("E:\\4A\\Mechanism_SGBD\\dbms-project\\src\\table2");
        DBI dbi = new DBI(T4,T5,1,1);
        dbi.open();
        Tuple t1 =null;
        while((t1= dbi.next())!=null){
            System.out.println(t1);
        }
        dbi.close();
        System.out.println("------------------------------------");
        indexJoin.open();
        while((t1= indexJoin.next())!=null){
            System.out.println(t1);
        }
        indexJoin.close();
    }
}
