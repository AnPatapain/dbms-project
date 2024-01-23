import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class IndexScan implements Operateur{
    String filePath;
    IndexCreationFixed indexCreation;
    int attribute; // The attribute that will be indexed
    int cle; // The value of index attribute that we want to scan

    RandomAccessFile randomAccessFile;
    List<Integer> blockList;
    int currentBlock;
    int tableSize;
    int tupleSize;
    public IndexScan(String filePath, int attribute, int cle, IndexCreationFixed indexCreationFixed) {
        this.filePath = filePath;
        this.attribute = attribute;
        this.cle = cle;
        this.indexCreation = indexCreationFixed;
    }

    @Override
    public void open() {

        this.indexCreation.createHashIndex(this.filePath, this.attribute);
        this.blockList = this.indexCreation.getAddresses(cle);
        currentBlock = 0;
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
        if(blockList==null){
            return null;
        }else{
            if(currentBlock >= blockList.size()){
                return null;
            }
            int tupleAddress = blockList.get(currentBlock);
            Tuple tuple = new Tuple(this.tupleSize);
            try {
                randomAccessFile.seek(tupleAddress);
                for(int col = 0; col < this.tupleSize; col++) {
                    tuple.val[col] = randomAccessFile.read();
                }
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
            currentBlock++;
            return tuple;
        }
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
