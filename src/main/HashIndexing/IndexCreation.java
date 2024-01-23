package main.HashIndexing;

import main.Tuple;

import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class IndexCreation {
    public List<List<Integer>> index;
    public Set<Integer> keys;
    private int indexSize = 16;

    public IndexCreation() {
        this.index = new ArrayList<>(indexSize);
        this.keys = new HashSet<>();
        for (int i = 0; i < indexSize; i++) {
            this.index.add(new ArrayList<>());
        }
    }

    public void resizeIndex(int maxBound) {
        int numsToResize = maxBound - this.index.size() + 1;
        for(int i = 0; i < numsToResize; i++) {
            this.index.add(new ArrayList<>());
        }
    }

    private int getPositionByHashing(int key) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(Integer.toString(key).getBytes());
        byte[] digest = md.digest();
        return (digest[0] & 0xff) ;
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
                this.keys.add(key);
                // If the hashValue (position on array) is exceeded the size of array
                if(hashValue >= this.index.size() - 1) {
                    this.resizeIndex(hashValue);
                }

//                System.out.println(t + "::" + hashValue);
                this.index.get(hashValue).add(row * tupleSize + header_offset);
            }

            randomAccessFile.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public List<Integer> getAddresses(int key) {
        try {
            if (!this.keys.contains(key)){
                return null;
            }
            int hash = getPositionByHashing(key);
            return this.index.get(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
