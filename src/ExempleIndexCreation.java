import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

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
        IndexCreationFixed indexCreationFixed = new IndexCreationFixed();
        indexCreationFixed.createHashIndex(FilePath.PathTable2,
                1);

        System.out.println(indexCreationFixed.index);

        System.out.println(indexCreationFixed.getAddresses(0));
        System.out.println(indexCreationFixed.getAddresses(1));
        System.out.println(indexCreationFixed.getAddresses(2));
        System.out.println(indexCreationFixed.getAddresses(3));
        System.out.println(indexCreationFixed.getAddresses(4));

//        String original = "E";
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        md.update(original.getBytes());
//        byte[] digest = md.digest(); // C'est ici que vous récupérez une valeur de type BYTE qui est le résultat de la fonction de hachage
//        StringBuffer sb = new StringBuffer();
//        for (byte b : digest) {
//            System.out.println(b);
//            sb.append(String.format("%02x", b & 0xff));
//        }
//        System.out.println("original:" + original);
//        System.out.println("digested(hex):" + sb.toString());
//        System.out.println("premier élément du digest:"+digest[0]);

    }
}