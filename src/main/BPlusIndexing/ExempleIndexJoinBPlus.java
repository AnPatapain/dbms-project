package main.BPlusIndexing;

import main.BPlusIndexing.BplusIndexJoin;
import main.DBI;
import main.FilePath;
import main.TableDisque;
import main.Tuple;

public class ExempleIndexJoinBPlus {
    public static void main(String[] args){

        TableDisque T4 = new TableDisque();
        T4.setFilePath(FilePath.MyTable1);
        T4.randomize(4,4);
        T4.open();
        Tuple t=null;
        System.out.println("table4");
        while((t = T4.next())!=null) {
            System.out.println(t);
        }
        T4.close();
        TableDisque T5 = new TableDisque();
        T5.setFilePath(FilePath.MyTable2);
        T5.randomize(4,4);
        T5.open();
        System.out.println("table5");
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
        System.out.println("Join by B+ tree");
        BplusIndexJoin bplusIndexJoin= new BplusIndexJoin(T4, FilePath.MyTable2,1,1);
        bplusIndexJoin.open();
        while((t1= bplusIndexJoin.next())!=null){
            System.out.println(t1);
        }
        bplusIndexJoin.close();
    }

}