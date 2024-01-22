import java.util.List;

public class ExempleIndexCreation {
    public static void main(String[] args) {
        TableDisque T4 = new TableDisque();
<<<<<<< HEAD
        T4.setFilePath("E:\\4A\\Mechanism_SGBD\\dbms-project\\src\\table1");
=======
        T4.setFilePath("C:\\Users\\NGUYEN KE AN\\Documents\\insa\\semester-7\\SGBD\\sgbdProjet\\src\\mytable");
>>>>>>> 4deaaaaa7a210cddcb260374dc26ebd9d9b4cbee

//         T4.randomize(4, 6);

        Tuple t = null;
        T4.open();
        System.out.println("Table T4 ****");
        while((t = T4.next())!=null){
            System.out.println(t);
        }

<<<<<<< HEAD
        IndexCreation indexCreation = new IndexCreation();
        indexCreation.createIndex("E:\\4A\\Mechanism_SGBD\\dbms-project\\src\\table1", 1);

        System.out.println("HashIndex");
        indexCreation.getHashIndex().forEach((key, value) -> System.out.println(key + "-> " + value));
=======
        IndexCreation indexCreation = new IndexCreation(2, 2);
        indexCreation.createHashIndex("C:\\Users\\NGUYEN KE AN\\Documents\\insa\\semester-7\\SGBD\\sgbdProjet\\src\\mytable",
                1);
        indexCreation.displayIndex();
>>>>>>> 4deaaaaa7a210cddcb260374dc26ebd9d9b4cbee
    }
}