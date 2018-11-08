import java.io.*;
import java.util.*;

public class DataFrame implements Cloneable{
    public String[] cnames;
    public List<Class<? extends Value>> ctypes;
    public Column[] colms;
    public int width;
    public int heigth;

    private class GroupedDataFrame implements Groupby{
        LinkedList<DataFrame> dataframes;

        @Override
        public DataFrame max() {
            return null;
        }

        @Override
        public DataFrame min() {
            return null;
        }

        @Override
        public DataFrame mean() {
            return null;
        }

        @Override
        public DataFrame std() {
            return null;
        }

        @Override
        public DataFrame sum() {
            return null;
        }

        @Override
        public DataFrame var() {
            return null;
        }
    }

    public GroupedDataFrame groupby(String colnames){
        GroupedDataFrame r = new GroupedDataFrame();
        int it;
        for(it=0;it<width;it++){
            if(colms[it].name == colnames) break;
        }
        for(int i=0;i<heigth;i++){
            if(i==0){
                r.dataframes = new LinkedList<>();
                DataFrame ndf = this.iloc(i);
                r.dataframes.add(ndf);
            }else{
                boolean DataFrameGroupFound = false;
                int l = 0;
                for(DataFrame n: r.dataframes){
                    if(this.colms[it].col.get(i).eq(n.colms[it].col.get(0))){
                        DataFrameGroupFound = true;
                        break;
                    }
                    l++;
                }
                if(DataFrameGroupFound){
                    for(int j=0;j<width;j++){
                        r.dataframes.get(l).Add(this.colms[j].col.get(i),j);
                    }
                }else{
                    DataFrame ndf = this.iloc(i);
                    r.dataframes.add(ndf);
                }
            }
        }
        return r;
    }

    public DataFrame(String[] col_names,List<Class<? extends Value>> col_types){
        cnames = col_names;
        ctypes = col_types;
        width = col_names.length;
        colms = new Column[width];
        for(int i=0;i<width;i++){
            colms[i] = new Column(col_names[i], col_types.get(i));
        }
        heigth = 0;
    }

    public DataFrame(String filename,List<Class<? extends Value>> col_types) throws IllegalAccessException, IOException, InstantiationException {
        new DataFrame(filename,col_types,true);
    }

    public DataFrame(String filename, List<Class<? extends Value>> col_types,boolean header) throws IOException, IllegalAccessException, InstantiationException {
        ctypes = col_types;
        width = col_types.size();
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        cnames = new String[width];
        if(!header){
            Scanner reader = new Scanner(System.in);
            System.out.println("Nazwij kolumny");
            for(int i =0;i<width;i++){
                cnames[i] = reader.next();
            }
        }else{
            String hdr = br.readLine();
            int it = 0;
            int pos = 0;
            for(int i=0;i<hdr.length();i++){
                if(hdr.charAt(i)==','){
                    cnames[it] = hdr.substring(pos,i);
                    pos = i+1;
                    it++;
                }
                if(i==hdr.length()-1){
                    cnames[it] = hdr.substring(pos);
                }
            }
        }
        colms = new Column[width];
        for(int i=0;i<width;i++){
            colms[i] = new Column(cnames[i], ctypes.get(i));
        }
        heigth =0;
        String line;
        while((line = br.readLine()) != null){
            int it =0;
            int pos =0;
            for(int i=0;i<line.length();i++){
                if(line.charAt(i)==','){
                    this.addS(line.substring(pos,i),it);
                    pos = i+1;
                    it++;
                }
                if(i==line.length()-1){
                    this.addS(line.substring(pos),it);
                }
            }
        }
    }

    public void addS(String s,int col_id) throws InstantiationException, IllegalAccessException {
        colms[col_id].addS(s);
        heigth = colms[col_id].h;
    }

    public void  Add(Value val,int col_id){
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
        return null;
    }

    public DataFrame iloc(int i){
        DataFrame dfr = new DataFrame(cnames,ctypes);
        for(int it=0;it<width;it++){
            dfr.Add(colms[it].col.get(i),it);
        }
        return dfr;
    }

    public DataFrame iloc(int from, int to){
        DataFrame dfr = new DataFrame(cnames,ctypes);
        for(int i=from;i<=to;i++){
            for(int it=0;it<width;it++){
                dfr.Add(colms[it].col.get(i),it);
            }
        }
        return dfr;
    }

    public DataFrame get(String[] cols,boolean copy) throws CloneNotSupportedException{
        List<Class<? extends Value>> colst = new ArrayList<>();
        int it =0;
        for(Column n: colms){
            if(n.name == cols[it]){
                colst.add(n.type);
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

    public void print(){
        for(int i=0;i<heigth;i++){
            for(int j=0;j<width;j++){
                System.out.print(colms[j].col.get(i).toString() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args){
        ArrayList<Class<? extends Value>> ct = new ArrayList<>();
        /*ct.add(VInteger.class);
        ct.add(VInteger.class);
        SparseDataFrame dfs1 = new SparseDataFrame(new String[] {"kol1","kol2"},ct , new VInteger(0));
        dfs1.sFilld();
        DataFrame df1 = dfs1.toDense();
        df1.print();
        GroupedDataFrame gdf = df1.groupby("kol1");
        gdf.dataframes.get(0).print();
        gdf.dataframes.get(1).print();*/
        ct.add(VDouble.class);
        ct.add(VDouble.class);
        ct.add(VDouble.class);
        try {
            DataFrame df1 = new DataFrame("data.csv",ct,true);
            df1.iloc(0,3).print();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static int powr(int a,int b){
        if(b == 0){
            return 1;
        }
        if(b == 1){
            return a;
        }
        if(b%2 == 0){
            return powr(a,b/2)*powr(a,b/2);
        }else{
            return powr(a,b-1) * a;
        }
    }
}
