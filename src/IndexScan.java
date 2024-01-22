import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class IndexScan implements Operateur{
    String filePath;
    IndexCreation indexCreation;
    int attribute; // The attribute that will be indexed
    int cle; // The value of index attribute that we want to scan

    RandomAccessFile randomAccessFile;
    List<Block> blockList;
    int currentBlock;
    int listBlockSize;

    public IndexScan(String filePath, int attribute, int cle, IndexCreation indexCreation) {
        this.filePath = filePath;
        this.attribute = attribute;
        this.cle = cle;
        this.indexCreation = indexCreation;
    }

    @Override
    public void open() {

        this.indexCreation.createHashIndex(this.filePath, this.attribute);
//        this.indexCreation.displayIndex();
        this.blockList = this.indexCreation.getHashIndexList(cle);
//        System.out.println(blockList);
        this.currentBlock = 0;
    }

    @Override
    public Tuple next() {
        this.listBlockSize =this.blockList.size()-1;
        while(this.currentBlock<this.listBlockSize){
            Block block = this.blockList.get(this.currentBlock);
            List<Tuple> listTuples = block.getRecord();
            while(listTuples.size()>0){
                Tuple t1 = listTuples.remove(0);
                if(t1.val[attribute] == cle) {
//                    System.out.println("Block Read"+(currentBlock);
                    return t1;
                }
            }
            this.currentBlock++;
        }
        return null;
    }

    @Override
    public void close() {

    }
}
