import javax.xml.crypto.Data;

public class DataFrame implements Cloneable{
    private String[] cnames;
    private String[] ctypes;
    private Column[] colms;
    private int width;
    private int heigth;
    public DataFrame(String[] col_names,String[] col_types){
        cnames = col_names;
        ctypes = col_types;
        width = col_names.length;
        colms = new Column[width];
        for(int i=0;i<width;i++){
            colms[i] = new Column(col_names[i],col_types[i]);
        }
        heigth = 0;
    }
    public void  Add(Object val,int col_id){
        colms[col_id].Add(val);
        heigth = colms[col_id].h;
    }
    public int size(){
        return heigth;
    }
    public Column get(String colname){
        for(Column n: colms){
            if(n.name == colname){
                return n;
            }
        }
        return new Column("no_name","no_type");
    }
    public DataFrame iloc(int i){
        DataFrame dfr = new DataFrame(cnames,ctypes);
        int it =0;
        for(Column n: colms){
            dfr.Add(colms[it].col.get(i),it);
        }
        return dfr;
    }
    public DataFrame iloc(int from, int to){
        DataFrame dfr = new DataFrame(cnames,ctypes);
        for(int i=from;i<=to;i++){
            int it =0;
            for(Column n: colms){
                dfr.Add(colms[it].col.get(i),it);
            }
        }
        return dfr;
    }
    public DataFrame get(String[] cols,boolean copy) throws CloneNotSupportedException{
        String[] colst = new String[cols.length];
        int it =0;
        for(Column n: colms){
            if(n.name == cols[it]){
                colst[it] = n.type;
                it++;
            }
        }
        DataFrame dfr = new DataFrame(cols,colst);
        if(copy){
            for(int i=0;i<dfr.width;i++){
                for(Column n: colms){
                    if(n.name == dfr.cnames[i]) dfr.colms[i] = (Column)n.clone();
                }
            }
        }else{
            for(int i=0;i<dfr.width;i++){
                for(Column n: colms){
                    dfr.colms[i] = n;
                }
            }
        }
        return dfr;
    }
    public static void main(String[] args){
        DataFrame df = new DataFrame(new String[]{"kol1","kol2","kol3"},new String[]{"int","double","MyType"});
        Object cc = new MyType(4);
        df.Add(cc,2);
        df.Add(0.5,1);
        df.Add(9,0);
        /*MyType bb = (MyType)df.colms[2].col.get(0);
        System.out.println(bb.Get());
        System.out.println(df.size());
        Column c = df.get("kol1");
        System.out.println(c.col.get(0));*/
        /*DataFrame df0 = df.iloc(0);
        System.out.println(df0.colms[0].col.get(0));*/

    }
}
