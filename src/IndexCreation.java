import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexCreation {
    Map<Integer, List<Integer>> hashIndex = new HashMap<>();

    public void createIndex(String filePath, int indexed_attribute_position) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "r");
            int header_offset = 2;
            int tableSize = randomAccessFile.read();
            int tupleSize = randomAccessFile.read();
            System.out.println("tableSize: " + tableSize + " tupleSize: " + tupleSize);

            for(int row = 0; row < tableSize; row++) {
                for(int col = 0; col < tupleSize; col++) {
                    if(col == indexed_attribute_position) {
                        randomAccessFile.seek((long) row * tupleSize + col + header_offset);
                        int col_value = randomAccessFile.read();
                        hashIndex.putIfAbsent(col_value, new ArrayList<>());
                        hashIndex.get(col_value).add(row * tupleSize + header_offset);
                    }
                }
            }

            randomAccessFile.close();
        }catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public Map<Integer, List<Integer>> getHashIndex() {
        return hashIndex;
    }
}
