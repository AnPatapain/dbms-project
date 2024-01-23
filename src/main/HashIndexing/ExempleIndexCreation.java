package main.HashIndexing;

import main.FilePath;
import main.TableDisque;
import main.Tuple;

import java.security.NoSuchAlgorithmException;

public class ExempleIndexCreation {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        TableDisque T4 = new TableDisque();


        T4.setFilePath(FilePath.PathTable2);

        Tuple t = null;
        T4.open();
        System.out.println("Table T4 ****");
        while((t = T4.next())!=null){
            System.out.println(t);
        }


        System.out.println("Start indexing");
        IndexCreation indexCreationFixed = new IndexCreation();
        indexCreationFixed.createHashIndex(FilePath.PathTable2,
                1);
        System.out.println(indexCreationFixed.keys);
        System.out.println(indexCreationFixed.index);

        System.out.println(indexCreationFixed.getAddresses(0));
        System.out.println(indexCreationFixed.getAddresses(1));
        System.out.println(indexCreationFixed.getAddresses(2));
        System.out.println(indexCreationFixed.getAddresses(3));
        System.out.println(indexCreationFixed.getAddresses(4));
    }
}