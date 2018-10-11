import java.util.*;

public class Column implements Cloneable{
    public String name;
    public String type;
    public List<Object> col;
    public int h;
    public Column(String nam, String typ){
        type = typ;
        name = nam;
        col = new ArrayList<>();
        h= 0;
    }
    public void Add(Object val){
        col.add(val);
        h++;
    }
    public void Set(int num, Object val){
        if(num < col.size()){
            col.set(num,val);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        Column c = new Column(name,type);
        c.col = new ArrayList<>(col);
        c.h = h;
        return c;
    }
}
