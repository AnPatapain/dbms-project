package main.HashIndexing;

import main.Operateur;
import main.Tuple;

public class IndexJoin implements Operateur {
    Operateur op1;
    Operateur op2;

    String filePathTable1;
    String filePathTable2;

    int col1;
    int col2;
    Tuple t1;
    Tuple t2;
    boolean nouveauTour = true;
    boolean nouveauNestedTour = true;


    public IndexJoin(Operateur op1, String filePathTable2, int col1, int col2){
        this.col1 = col1;
        this.col2 = col2;
        this.op1 = op1;
        this.filePathTable2 = filePathTable2;
    }
    public void open(){
        op1.open();
        nouveauTour=true;
        nouveauNestedTour=true;
    }
    public Tuple next(){
        while(true){
            if(nouveauTour){
                t1= op1.next();
                nouveauTour=false;
            }
            if(t1!=null){
                if(nouveauNestedTour){
                    this.op2 = new IndexScan(filePathTable2,col2,t1.val[col1],new IndexCreation());
                    this.op2.open();
                    nouveauNestedTour=false;
                }
                t2=this.op2.next();
                if(t2==null){
                    nouveauNestedTour=true;
                    nouveauTour=true;
                }else{
                    return combineTuple(t1,t2);
                }
            }else{
                break;
            }
        }
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
