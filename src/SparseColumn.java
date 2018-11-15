import java.util.*;

public class SparseColumn extends Column {
    public SparseColumn(String n,Class<? extends Value> t){
        super(n,t);
    }

    public void Add(Value val, VInteger hidden){
        try {
            if(val.neq(hidden)){
                CooValue a = new CooValue(h,((VInteger)val).val);
                col.add(a);
            }
        } catch (IncompatibleTypes incompatibleTypes) {
            incompatibleTypes.printStackTrace();
        }
        h++;
    }

}
