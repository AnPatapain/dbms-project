package main.HashIndexing;

import main.*;

public class ExempleIndexJoin {
    public static void main(String[] args){

        TableDisque T4 = new TableDisque();
        T4.setFilePath(FilePath.PathTable1);
        T4.open();
        Tuple t=null;
        System.out.println("table 1");
        while((t = T4.next())!=null) {
            System.out.println(t);
        }
        T4.close();
        TableDisque T5 = new TableDisque();
        T5.setFilePath(FilePath.PathTable2);
        T5.open();
        System.out.println("table 2");
        while((t = T5.next())!=null) {
            System.out.println(t);
        }
        T5.close();
        DBI dbi = new DBI(T4,T5,1,1);
        dbi.open();
        Tuple t1 =null;
        System.out.println("Join by main.DBI");
        while((t1= dbi.next())!=null){
            System.out.println(t1);
        }
        dbi.close();
        System.out.println("------------------------------------");
        System.out.println("Join by Hash Indexing");
        IndexJoin indexJoin = new IndexJoin(T4, FilePath.PathTable2,1,1);
        indexJoin.open();
        while((t1= indexJoin.next())!=null){
            System.out.println(t1);
        }
        indexJoin.close();
    }
}
