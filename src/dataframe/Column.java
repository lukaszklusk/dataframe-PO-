package dataframe;

import java.util.*;

public class Column implements Cloneable{
    public String name;
    public Class<? extends Value> type;
    public List<Value> col;
    public int h;
    public Column(String nam, Class<? extends Value> typ){
        type = typ;
        name = nam;
        col = new ArrayList<>();
        h= 0;
    }
    public void Add(Value val){
        col.add(val);
        h++;
    }

    public void addS(String s) throws IllegalAccessException, InstantiationException {
        Value nv = type.newInstance().create(s);
        col.add(nv);
        h++;
    }

    public void Set(int num, Value val){
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
