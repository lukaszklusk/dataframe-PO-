import java.util.*;

public class SparseColumn extends Column {
    public SparseColumn(String n,String t){
        super(n,t);
    }

    public void sFill5(){
        CooValue n = new CooValue(3,1);
        col.add(n);
        h = 5;
    }

    public void Add(Object val, Object hidden){
        if(val != hidden){
            CooValue a = new CooValue(h,val);
            col.add(a);
        }
        h++;
    }
}
