public class ExempleIndexJoin {
    public static void main(String[] args){

        IndexJoin indexJoin = new IndexJoin(FilePath.PathTable1,FilePath.PathTable2,1,1);
        TableDisque T4 = new TableDisque();
        T4.setFilePath(FilePath.PathTable1);
        TableDisque T5 = new TableDisque();
        T5.setFilePath(FilePath.PathTable2);
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
