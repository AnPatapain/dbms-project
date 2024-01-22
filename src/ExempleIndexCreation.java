import java.io.File;
import java.util.List;

public class ExempleIndexCreation {
    public static void main(String[] args) {
        TableDisque T4 = new TableDisque();

        T4.setFilePath(FilePath.PathTable1);


//         T4.randomize(4, 6);

        Tuple t = null;
        T4.open();
        System.out.println("Table T4 ****");
        while((t = T4.next())!=null){
            System.out.println(t);
        }


        IndexCreation indexCreation = new IndexCreation();
        indexCreation.createHashIndex(FilePath.PathTable1,
                1);
        indexCreation.displayIndex();

    }
}