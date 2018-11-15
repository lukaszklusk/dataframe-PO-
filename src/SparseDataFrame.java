import java.util.List;

public class SparseDataFrame extends DataFrame {
    public SparseColumn colms[];
    public VInteger hidden;
    public SparseDataFrame(String[] col_names, List<Class<? extends Value>> col_types,VInteger hide){
        super(col_names,col_types);
        colms = new SparseColumn[width];
        for(int i =0;i<width;i++){
            colms[i] = new SparseColumn(col_names[i], col_types.get(i));
        }
        hidden = hide;
    }

    public SparseDataFrame(DataFrame df, VInteger hide){
        super(df.cnames,df.ctypes);
        colms = new SparseColumn[width];
        for(int i =0;i<width;i++){
            colms[i] = new SparseColumn(df.cnames[i], df.ctypes.get(i));
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
                    dfr.colms[it].Add(new VInteger(tmp.val));
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
            if(i == 0){
                colms[i].col.add(new CooValue(3,1));
                colms[i].col.add(new CooValue(4,1));
                colms[i].h = 5;
            }else{
                for(int j=1;j<5;j++){
                    colms[i].col.add(new CooValue(j-1,j));
                }
                colms[i].h = 5;
            }
        }
    }

    public DataFrame iloc(int i){
        DataFrame dft = this.toDense();
        DataFrame dfr = dft.iloc(i);
        return new SparseDataFrame(dfr,hidden);
    }

    public DataFrame iloc(int from,int to){
        DataFrame dft = this.toDense();
        DataFrame dfr = dft.iloc(from,to);
        return new SparseDataFrame(dfr,hidden);
    }
}
