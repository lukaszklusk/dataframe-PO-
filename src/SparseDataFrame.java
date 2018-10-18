public class SparseDataFrame extends DataFrame {
    public SparseColumn colms[];
    public Object hidden;
    public SparseDataFrame(String[] col_names, String[] col_types,Object hide){
        super(col_names,col_types);
        colms = new SparseColumn[width];
        for(int i =0;i<width;i++){
            colms[i] = new SparseColumn(col_names[i],col_types[i]);
        }
        hidden = hide;
    }

    public SparseDataFrame(DataFrame df, Object hide){
        super(df.cnames,df.ctypes);
        colms = new SparseColumn[width];
        for(int i =0;i<width;i++){
            colms[i] = new SparseColumn(df.cnames[i],df.ctypes[i]);
        }
        hidden = hide;
        for(int i =0;i<width;i++){
            for(int j=0;j<df.colms[i].h;j++){
                colms[i].Add(df.colms[i].col.get(j),hidden);
            }
        }
    }

    public DataFrame toDense(){
        DataFrame dfr = new DataFrame(cnames,ctypes);
        int it = 0, itc, itp = 0;
        for(SparseColumn n: colms){
            CooValue tmp = (CooValue)n.col.get(itp);
            itc = tmp.pos;
            itp++;
            for(int i=0;i<n.h;i++){
                if(i == itc){
                    dfr.colms[it].Add(tmp.val);
                    if(itp < n.col.size()) {
                        tmp = (CooValue) n.col.get(itp);
                        itc = tmp.pos;
                        itp++;
                    }
                }else{
                    dfr.colms[it].Add(hidden);
                }
            }
            it++;
            itp = 0;
        }
        dfr.heigth = dfr.colms[0].h;
        return dfr;
    }

    public void sFilld(){
        for(int i=0;i<width;i++){
            colms[i].sFill5();
        }
    }
}
