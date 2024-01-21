import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class IndexScan implements Operateur{
    String filePath;
    IndexCreation indexCreation;
    int attribute; // The attribute that will be indexed
    int cle; // The value of index attribute that we want to scan
    List<Integer> tupleAddresses;
    RandomAccessFile randomAccessFile;
    int tableSize;
    int tupleSize;
    int e; // e is the current pointer on tupleAddresses list (Util for le mode pipeline)

    public IndexScan(String filePath, int attribute, int cle, IndexCreation indexCreation) {
        this.filePath = filePath;
        this.attribute = attribute;
        this.cle = cle;
        this.indexCreation = indexCreation;
    }

    @Override
    public void open() {

        this.indexCreation.createIndex(this.filePath, this.attribute);
        tupleAddresses = new ArrayList<>(this.indexCreation.getHashIndex().get(cle));
//        System.out.println(tupleAddresses);
        e = 0;
        try {
            randomAccessFile = new RandomAccessFile(filePath, "r");
            this.tableSize = randomAccessFile.read();
            this.tupleSize = randomAccessFile.read();
        }catch(IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    @Override
    public Tuple next() {
        if(e >= tupleAddresses.size()){
            return null;
        }
        int tupleAddress = tupleAddresses.get(e);
        Tuple tuple = new Tuple(this.tupleSize);
        try {
            randomAccessFile.seek(tupleAddress);
            for(int col = 0; col < this.tupleSize; col++) {
                tuple.val[col] = randomAccessFile.read();
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        e++;
        return tuple;
    }

    @Override
    public void close() {
        try {
            randomAccessFile.close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
