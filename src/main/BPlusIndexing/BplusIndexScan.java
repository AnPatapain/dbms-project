package main.BPlusIndexing;

import main.Operateur;
import main.Tuple;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

public class BplusIndexScan implements Operateur {

    String filePath;
    BPlusIndexCreation BPlusIndexCreation;
    int attribute; // The attribute that will be indexed
    int cle; // The value of index attribute that we want to scan
    int cursor;
    RandomAccessFile randomAccessFile;
    int tableSize;
    int tupleSize;
    int e; // e is the current pointer on tupleAddresses list (Util for le mode pipeline)

    public BplusIndexScan(String filePath, int attribute, int cle, BPlusIndexCreation BPlusIndexCreation) {
        this.BPlusIndexCreation = BPlusIndexCreation;
        this.filePath = filePath;
        this.attribute = attribute;
        this.cle = cle;
    }

    @Override
    public void open() {
        this.BPlusIndexCreation.createIndexBPlus(filePath, attribute);
        this.cursor = 0;
        try {
            this.randomAccessFile = new RandomAccessFile(filePath, "r");
            this.tableSize = this.randomAccessFile.read();
            this.tupleSize = this.randomAccessFile.read();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Tuple next() {
        List<Double> addresses = this.BPlusIndexCreation.getbPlusTree().search(cle);

        if(addresses == null || this.cursor >= addresses.size()) {
            return null;
        }
        int tupleAddress = addresses.get(this.cursor).intValue();
        Tuple tuple = new Tuple(this.tupleSize);
        try {
            randomAccessFile.seek(tupleAddress);
            for(int col = 0; col < this.tupleSize; col++) {
                tuple.val[col] = randomAccessFile.read();
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.cursor++;
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
