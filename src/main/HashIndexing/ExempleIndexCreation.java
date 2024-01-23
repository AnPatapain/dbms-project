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
        System.out.println("Table 2 ****");
        while((t = T4.next())!=null){
            System.out.println(t);
        }


        System.out.println("Start indexing on attribute 1 of table 2");
        IndexCreation indexCreation = new IndexCreation();
        indexCreation.createHashIndex(FilePath.PathTable2, 1);

        System.out.println("keys: " + indexCreation.keys);
        System.out.println("index: " + indexCreation.index);

        System.out.println();

        System.out.println("Addresses of tuple has key 0: " + indexCreation.getAddresses(0));
        System.out.println("Addresses of tuple has key 1: " + indexCreation.getAddresses(1));
        System.out.println("Addresses of tuple has key 2: " + indexCreation.getAddresses(2));
        System.out.println("Addresses of tuple has key 3: " + indexCreation.getAddresses(3));
        System.out.println("Addresses of tuple has key 4: " + indexCreation.getAddresses(4));
    }
}