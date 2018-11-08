import java.util.*;

public class SparseColumn extends Column {
    public SparseColumn(String n,Class<? extends Value> t){
        super(n,t);
    }

    public void sFill5(){
        CooValue n = new CooValue(3,1);
        col.add(n);
        h = 5;
    }

    public void Add(Value val, VInteger hidden){
        if(val.neq(hidden)){
            CooValue a = new CooValue(h,((VInteger)val).val);
            col.add(a);
        }
        h++;
    }

}
