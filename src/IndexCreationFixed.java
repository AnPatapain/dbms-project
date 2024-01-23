import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IndexCreationFixed {
    private List<List<Integer>> index;
    private int indexSize = 5;

    public IndexCreationFixed() {
        this.index = new ArrayList<>(indexSize);
        for (int i = 0; i < indexSize; i++) {
            this.index.add(new ArrayList<>());
        }
    }

    private int getPositionByHashing(int key) throws NoSuchAlgorithmException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(Integer.toString(key).getBytes());
        byte[] digest = md.digest();
        return (digest[0] & 0xff) % this.index.size();
    }

    public void createHashIndex(String filePath, int indexed_attribute_position) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "r");
            int header_offset = 2;
            int tableSize = randomAccessFile.read();
            int tupleSize = randomAccessFile.read();

            for (int row = 0; row < tableSize; row++) {
                randomAccessFile.seek((long) row * tupleSize + header_offset);
                Tuple t = new Tuple(tupleSize);
                for (int j = 0; j < tupleSize; j++) {
                    t.val[j] = randomAccessFile.read();
                }

                // Index on key.
                int key = t.val[indexed_attribute_position];
                int hashValue = getPositionByHashing(key);
                this.index.get(hashValue).add(row * tupleSize + header_offset);
            }

            randomAccessFile.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public List<Integer> getAddresses(int key) {
        try {
            int hash = getPositionByHashing(key);
            return this.index.get(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void displayIndex() {
        System.out.print("[ ");
        for(int i = 0; i < this.index.size(); i++) {
            System.out.print(this.index.get(i) + ", ");
        }
        System.out.print(" ]");
    }

}
