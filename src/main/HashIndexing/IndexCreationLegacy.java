package main.HashIndexing;

import main.Tuple;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

class Block {
    List<Tuple> tuples;
    int capacity;

    Block(int capacity) {
        this.tuples = new ArrayList<>();
        this.capacity = capacity;
    }

    boolean isFull() {
        return tuples.size() >= capacity;
    }

    boolean addRecord(Tuple tuple) {
        if (!isFull()) {
            tuples.add(tuple);
            return true;
        }
        return false;
    }
    List<Tuple> getRecord(){
        return tuples;
    }
}

public class IndexCreationLegacy {
    private int numberOfBlocks=2;
    private int blockSize=2;
    List<List<Block>> listOfBlocks;

    private int currentBlock;
    public IndexCreationLegacy() {

        this.listOfBlocks = new ArrayList<>();
        for (int i = 0; i < numberOfBlocks; i++) {
            List<Block> blocks = new ArrayList<>();
            blocks.add(new Block(blockSize));
            listOfBlocks.add(blocks);
        }
    }

    public void createHashIndex(String filePath, int indexed_attribute_position) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "r");
            int header_offset = 2;
            int tableSize = randomAccessFile.read();
            int tupleSize = randomAccessFile.read();
//            System.out.println("tableSize: " + tableSize + " tupleSize: " + tupleSize);

            for (int row = 0; row < tableSize; row++) {
                randomAccessFile.seek((long) row * tupleSize + header_offset);
                Tuple t = new Tuple(tupleSize);
                for (int j = 0; j < tupleSize; j++) {
                    t.val[j] = randomAccessFile.read();
                }

                // Index on key.
                int key = t.val[indexed_attribute_position];
                int hashValue = key % numberOfBlocks;
                List<Block> blockList = listOfBlocks.get(hashValue);
                this.currentBlock = blockList.size()-1;

                Block block = blockList.get(currentBlock);
                if(!block.addRecord(t)){
                    Block newBlock = new Block(blockSize);
                    newBlock.addRecord(t);
                    blockList.add(newBlock);
                }
            }

            randomAccessFile.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public int getBlockSize() {
        return blockSize;
    }

    public int getNumberOfBlocks() {
        return numberOfBlocks;
    }
    public List<Integer> getIndex(){
        List<Integer> index = new ArrayList<>();
        for(int i=0;i<listOfBlocks.size();i++){
            index.add(i);
        }
        return index;
    }
    public List<Block> getHashIndexList(int cle){
        int hashValue = cle % numberOfBlocks;
        return listOfBlocks.get(hashValue);
    }
    public List<List<Block>> getListOfBlocks() {
        return listOfBlocks;
    }

    public void displayIndex() {
        for(int i = 0; i < listOfBlocks.size(); i++) {
            System.out.print(i + " -> ");
            List<Block> blocks = listOfBlocks.get(i);
            for(int j = 0; j < blocks.size(); j++) {
                System.out.print("[");
                for(Tuple tuple : blocks.get(j).tuples) {
                    System.out.print("( " + tuple + ")");
                }
                System.out.print("]\t");
            }
            System.out.println();
        }
    }
}
