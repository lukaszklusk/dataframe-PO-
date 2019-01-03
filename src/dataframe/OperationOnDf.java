package dataframe;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;

public class OperationOnDf implements Callable<DataFrame> {
    private int t;
    private DataFrame df;
    private ArrayList<Integer> key_id;
    public OperationOnDf(int type, DataFrame indf, ArrayList<Integer> id){
        t = type;
        df = indf;
        key_id = id;
    }

    @Override
    public DataFrame call() {
        LinkedList<Value> key_val = new LinkedList<>();
        for(int i=0;i<df.width;i++){
            if(key_id.contains(i)){
                key_val.add(df.colms[i].col.get(0));
            }
        }
        DataFrame ret_val = null;
        if(t == 0){ //max
            ret_val = df.groupby().max();
        }
        if(t == 1){ //min
            ret_val = df.groupby().min();
        }
        if(t == 2){ //sum
            ret_val = df.groupby().sum();
        }
        if(t == 3){ //mean
            ret_val = df.groupby().mean();
        }
        if(t == 4){ //std
            ret_val = df.groupby().std();
        }
        if(t == 5){ //var
            ret_val = df.groupby().var();
        }
        for(int i=0;i<df.width;i++){
            if(key_id.contains(i)){
                ret_val.colms[i].col.set(0,key_val.getFirst());
            }
        }
        return ret_val;
    }
}
