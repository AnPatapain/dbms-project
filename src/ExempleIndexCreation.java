public class ExempleIndexCreation {
    public static void main(String[] args) {
        TableDisque T4 = new TableDisque();
        T4.setFilePath("E:\\4A\\Mechanism_SGBD\\dbms-project\\src\\table1");

        // T4.randomize(4, 4);

        Tuple t = null;
        T4.open();
        System.out.println("Table T4 ****");
        while((t = T4.next())!=null){
            System.out.println(t);
        }

        IndexCreation indexCreation = new IndexCreation();
        indexCreation.createIndex("E:\\4A\\Mechanism_SGBD\\dbms-project\\src\\table1", 1);

        System.out.println("HashIndex");
        indexCreation.getHashIndex().forEach((key, value) -> System.out.println(key + "-> " + value));
    }
}