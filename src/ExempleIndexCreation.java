import java.util.List;

public class ExempleIndexCreation {
    public static void main(String[] args) {
        TableDisque T4 = new TableDisque();
        T4.setFilePath("C:\\Users\\NGUYEN KE AN\\Documents\\insa\\semester-7\\SGBD\\sgbdProjet\\src\\mytable");

//         T4.randomize(4, 6);

        Tuple t = null;
        T4.open();
        System.out.println("Table T4 ****");
        while((t = T4.next())!=null){
            System.out.println(t);
        }

        IndexCreation indexCreation = new IndexCreation(2, 2);
        indexCreation.createHashIndex("C:\\Users\\NGUYEN KE AN\\Documents\\insa\\semester-7\\SGBD\\sgbdProjet\\src\\mytable",
                1);
        List<List<Block>> listOfBlocks = indexCreation.getListOfBlocks();
        for(int i = 0; i < listOfBlocks.size(); i++) {
            System.out.print(i + " -> ");
            List<Block> blocks = listOfBlocks.get(i);
            for(int j = 0; j < blocks.size(); j++) {
                System.out.print("[");
                for(Tuple tuple : blocks.get(j).tuples) {
                    System.out.print("( " + tuple + " )");
                }
                System.out.print("]\t");
            }
            System.out.println();
        }
    }
}