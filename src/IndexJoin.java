import java.util.Iterator;
import java.util.Set;

public class IndexJoin implements Operateur{
    Operateur op1;
    Operateur op2;

    String filePathTable1;
    String filePathTable2;
    Iterator<Integer> indexSetOp1;
    int col1;

    int col2;
    Tuple t1;
    Tuple t2;
    boolean nouveauTour = true;
    boolean nouveauNestedTour = true;

    int cle=-1;
    public IndexJoin(String filePathTable1,String filePathTable2, int col1,int col2){
        this.col1 = col1;
        this.col2 = col2;
        this.filePathTable1 = filePathTable1;
        this.filePathTable2 = filePathTable2;
    }
    public void open(){
//        IndexCreation indexOp1 = new IndexCreation();
//        indexOp1.createIndex(filePathTable1,col1);
//        this.indexSetOp1 = indexOp1.getHashIndex().keySet().iterator();
    }
    public Tuple next(){
        while  (true) {
            if(nouveauTour && nouveauNestedTour && !this.indexSetOp1.hasNext()){
                break;
            }
            if (nouveauTour) {
                if(this.indexSetOp1.hasNext())
                    this.cle = this.indexSetOp1.next();
//                this.op1 = new IndexScan(this.filePathTable1, this.col1, this.cle, new IndexCreation());
                this.op1.open();
                this.nouveauTour = false;
            }
            if (nouveauNestedTour) {
                t1 = op1.next();
//                System.out.println("tourtourtour "+t1);
                if (t1 != null) {
//                    this.op2 = new IndexScan(this.filePathTable2, this.col2, this.cle, new IndexCreation());
                    this.op2.open();
                    this.nouveauNestedTour = false;
                } else {
                    this.nouveauTour = true;
                }

            }
            t2 = op2.next();
            if (t2 != null) {
                return this.combineTuple(t1, t2);
            } else {
                this.nouveauNestedTour = true;
            }
        }
        System.out.println("exhausted");
        return null;
    }

    public void close(){
        this.op1.close();
        this.op2.close();
    }
    private Tuple combineTuple(Tuple t1, Tuple t2){
        Tuple ret = new Tuple(t1.val.length+t2.val.length);
        for(int i=0;i<t1.val.length;i++)
            ret.val[i]=t1.val[i];
        for(int i=0;i<t2.val.length;i++)
            ret.val[i+t1.val.length]=t2.val[i];
        return ret;
    }
}
