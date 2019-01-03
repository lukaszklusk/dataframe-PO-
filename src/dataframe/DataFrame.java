package dataframe;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class DataFrame implements Cloneable{
    public String[] cnames;
    public List<Class<? extends Value>> ctypes;
    public Column[] colms;
    public int width;
    public int heigth;

    public class GroupedDataFrame implements Groupby{ //klasa przechowująca zgrupowane dataframe'y
        LinkedList<DataFrame> dataframes;
        ArrayList<Integer> key_id;

        GroupedDataFrame(){
            key_id = new ArrayList<>();
            dataframes = new LinkedList<>();
        }

        @Override
        public DataFrame max() {
            DataFrame result = new DataFrame(dataframes.peekFirst().cnames,dataframes.peekFirst().ctypes);
            for(DataFrame n: dataframes){
                for(int j=0;j<n.width;j++){
                    if(key_id.contains(j)){
                        result.Add(n.colms[j].col.get(0),j);
                    }else{
                        Value maxv = new VString(); //vstring tylko zeby mi interpreter nie krzyczal
                        for(int k=0;k<n.colms[j].col.size();k++){
                            if(k==0)maxv = n.colms[j].col.get(0);
                            else{
                                try {
                                    if(n.colms[j].col.get(k).gt(maxv)) maxv = n.colms[j].col.get(k);
                                } catch (IncompatibleTypes incompatibleTypes) {
                                    incompatibleTypes.printStackTrace();
                                }
                            }
                        }
                        result.Add(maxv,j);
                    }
                }
            }
            return result;
        }

        @Override
        public DataFrame min() {
            DataFrame result = new DataFrame(dataframes.peekFirst().cnames,dataframes.peekFirst().ctypes);
            for(DataFrame n: dataframes){
                for(int j=0;j<n.width;j++){
                    if(key_id.contains(j)){
                        result.Add(n.colms[j].col.get(0),j);
                    }else{
                        Value minv = new VString(); //tylko zeby mi interpreter nie krzyczal
                        for(int k=0;k<n.colms[j].col.size();k++){
                            if(k==0)minv = n.colms[j].col.get(0);
                            else{
                                try {
                                    if(n.colms[j].col.get(k).lt(minv)) minv = n.colms[j].col.get(k);
                                } catch (IncompatibleTypes incompatibleTypes) {
                                    incompatibleTypes.printStackTrace();
                                }
                            }
                        }
                        result.Add(minv,j);
                    }
                }
            }
            return result;
        }

        @Override
        public DataFrame mean() {
            DataFrame result = new DataFrame(dataframes.peekFirst().cnames,dataframes.peekFirst().ctypes);
            for(DataFrame n: dataframes){
                for(int j=0;j<n.width;j++){
                    if(key_id.contains(j)){
                        result.Add(n.colms[j].col.get(0),j);
                    }else{
                        if(n.colms[j].col.get(0) instanceof VString || n.colms[j].col.get(0) instanceof VDatetime){
                            result.Add(new VString("NA"),j);
                        }else{
                            Value mean = new VDouble(0);
                            int k;
                            for(k=0;k<n.colms[j].col.size();k++){
                                try {
                                    mean.add(n.colms[j].col.get(k));
                                } catch (IncompatibleTypes incompatibleTypes) {
                                    incompatibleTypes.printStackTrace();
                                }
                            }
                            try {
                                mean.div(new VDouble(k));
                            } catch (IncompatibleTypes incompatibleTypes) {
                                incompatibleTypes.printStackTrace();
                            } catch (DivByZero divByZero) {
                                divByZero.printStackTrace();
                            }
                            result.Add(mean,j);
                        }
                    }
                }
            }
            return result;
        }

        @Override
        public DataFrame std() {
            DataFrame result = new DataFrame(dataframes.peekFirst().cnames,dataframes.peekFirst().ctypes);
            for(DataFrame n: dataframes){
                for(int j=0;j<n.width;j++){
                    if(key_id.contains(j)){
                        result.Add(n.colms[j].col.get(0),j);
                    }else{
                        if(n.colms[j].col.get(0) instanceof VString || n.colms[j].col.get(0) instanceof VDatetime){
                            result.Add(new VString("NA"),j);
                        }else{
                            VDouble mean = new VDouble(0);
                            int k;
                            for(k=0;k<n.colms[j].col.size();k++){
                                try {
                                    mean.add(n.colms[j].col.get(k));
                                } catch (IncompatibleTypes incompatibleTypes) {
                                    incompatibleTypes.printStackTrace();
                                }
                            }
                            try {
                                mean.div(new VDouble(k));
                            } catch (DivByZero divByZero) {
                                divByZero.printStackTrace();
                            }
                            VDouble std = new VDouble(0);
                            for(int l=0;l<n.colms[j].col.size();l++){
                                VDouble tmp = new VDouble(mean.val);
                                try {
                                    tmp.sub(n.colms[j].col.get(l));
                                } catch (IncompatibleTypes incompatibleTypes) {
                                    incompatibleTypes.printStackTrace();
                                }
                                try {
                                    std.add(tmp.mul(tmp));
                                } catch (IncompatibleTypes incompatibleTypes) {
                                    incompatibleTypes.printStackTrace();
                                }
                            }
                            try {
                                std.div(new VDouble(k-1));
                            } catch (DivByZero divByZero) {
                                divByZero.printStackTrace();
                            }
                            std.val = Math.sqrt(std.val);
                            result.Add(std,j);
                        }
                    }
                }
            }
            return result;
        }

        @Override
        public DataFrame sum() {
            DataFrame result = new DataFrame(dataframes.peekFirst().cnames,dataframes.peekFirst().ctypes);
            for(DataFrame n: dataframes){
                for(int j=0;j<n.width;j++){
                    if(key_id.contains(j)){
                        result.Add(n.colms[j].col.get(0),j);
                    }else{
                        if(n.colms[j].col.get(0) instanceof VString || n.colms[j].col.get(0) instanceof VDatetime){
                            result.Add(new VString("NA"),j);
                        }else{
                            Value sum = new VDouble(0);
                            int k;
                            for(k=0;k<n.colms[j].col.size();k++){
                                try {
                                    sum.add(n.colms[j].col.get(k));
                                } catch (IncompatibleTypes incompatibleTypes) {
                                    incompatibleTypes.printStackTrace();
                                }
                            }
                            result.Add(sum,j);
                        }
                    }
                }
            }
            return result;
        }

        @Override
        public DataFrame var() {
            DataFrame result = new DataFrame(dataframes.peekFirst().cnames,dataframes.peekFirst().ctypes);
            for(DataFrame n: dataframes){
                for(int j=0;j<n.width;j++){
                    if(key_id.contains(j)){
                        result.Add(n.colms[j].col.get(0),j);
                    }else{
                        if(n.colms[j].col.get(0) instanceof VString || n.colms[j].col.get(0) instanceof VDatetime){
                            result.Add(new VString("NA"),j);
                        }else{
                            VDouble mean = new VDouble(0);
                            int k;
                            for(k=0;k<n.colms[j].col.size();k++){
                                try {
                                    mean.add(n.colms[j].col.get(k));
                                } catch (IncompatibleTypes incompatibleTypes) {
                                    incompatibleTypes.printStackTrace();
                                }
                            }
                            try {
                                mean.div(new VDouble(k));
                            } catch (DivByZero divByZero) {
                                divByZero.printStackTrace();
                            }
                            VDouble var = new VDouble(0);
                            for(int l=0;l<n.colms[j].col.size();l++){
                                VDouble tmp = new VDouble(mean.val);
                                try {
                                    tmp.sub(n.colms[j].col.get(l));
                                } catch (IncompatibleTypes incompatibleTypes) {
                                    incompatibleTypes.printStackTrace();
                                }
                                try {
                                    var.add(tmp.mul(tmp));
                                } catch (IncompatibleTypes incompatibleTypes) {
                                    incompatibleTypes.printStackTrace();
                                }
                            }
                            try {
                                var.div(new VDouble(k-1));
                            } catch (DivByZero divByZero) {
                                divByZero.printStackTrace();
                            }
                            result.Add(var,j);
                        }
                    }
                }
            }
            return result;
        }

        @Override
        public DataFrame apply(Applyable a) {
            return null;
        }

        public void linkGroupedLists(GroupedDataFrame other){
            this.dataframes.addAll(other.dataframes);
            for(int n: other.key_id){
                if(!key_id.contains(n)){
                    key_id.add(n);
                }
            }
        }

        public ThreadedGDF toThreadedGDF(){
             ThreadedGDF tgdf = new ThreadedGDF(key_id,dataframes);
             return tgdf;
        }
    }

    public GroupedDataFrame groupby(String colname){

        GroupedDataFrame r = new GroupedDataFrame();
        int it = 0;

        while(!colms[it].name.equals(colname)) it++;
        r.key_id.add(it);
        for(int i=0;i<heigth;i++){
            if(i==0){
                DataFrame ndf = this.iloc(i);
                r.dataframes.add(ndf);
            }else{
                boolean DataFrameGroupFound = false;
                int l = 0;
                for(DataFrame n: r.dataframes){
                    try {
                        if(this.colms[it].col.get(i).eq(n.colms[it].col.get(0))){
                            DataFrameGroupFound = true;
                            break;
                        }
                    } catch (IncompatibleTypes incompatibleTypes) {
                        incompatibleTypes.printStackTrace();
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

    public GroupedDataFrame groupby(String colnames[]){
        GroupedDataFrame result = new GroupedDataFrame();
        for(int i=0;i<colnames.length;i++){
            if(i == 0){
                result = this.groupby(colnames[0]);
            }else{
                GroupedDataFrame prev_res = result;
                result = new GroupedDataFrame();
                result.key_id = prev_res.key_id;
                for(DataFrame n: prev_res.dataframes){
                    GroupedDataFrame tmp = n.groupby(colnames[i]);
                    result.linkGroupedLists(tmp);
                }
            }
        }
        return result;
    }

    public GroupedDataFrame groupby(){
        GroupedDataFrame r = new GroupedDataFrame();
        r.dataframes.add(this);
        return r;
    }

    public class ThreadedGDF implements Groupby{
        LinkedList<DataFrame> dataframes;
        ArrayList<Integer> key_id;

        public ThreadedGDF(ArrayList<Integer> a,LinkedList<DataFrame> b){
            key_id = a;
            dataframes = b;
        }

        @Override
        public DataFrame max(){
            ExecutorService executor = Executors.newFixedThreadPool(15);
            ArrayList<Future<DataFrame>> results = new ArrayList<>();
            int it =0;
            String[] cn = dataframes.peekFirst().cnames;
            List<Class<? extends Value>> ct = dataframes.peekFirst().ctypes;
            while(it < dataframes.size()){
                results.add(executor.submit(new OperationOnDf(0,dataframes.get(it),key_id)));
                it++;
            }
            DataFrame ret = new DataFrame(cn,ct);
            for(int i=0;i<it;i++){
                try {
                    DataFrame tmp = results.get(i).get();
                    for(int j=0;j<ret.width;j++){
                        ret.Add(tmp.colms[j].col.get(0),j);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            executor.shutdown();
            return ret;
        }

        @Override
        public DataFrame min() {
            ExecutorService executor = Executors.newFixedThreadPool(15);
            ArrayList<Future<DataFrame>> results = new ArrayList<>();
            int it =0;
            String[] cn = dataframes.peekFirst().cnames;
            List<Class<? extends Value>> ct = dataframes.peekFirst().ctypes;
            while(it < dataframes.size()){
                results.add(executor.submit(new OperationOnDf(1,dataframes.get(it),key_id)));
                it++;
            }
            DataFrame ret = new DataFrame(cn,ct);
            for(int i=0;i<it;i++){
                try {
                    DataFrame tmp = results.get(i).get();
                    for(int j=0;j<ret.width;j++){
                        ret.Add(tmp.colms[j].col.get(0),j);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            executor.shutdown();
            return ret;
        }

        @Override
        public DataFrame mean() {
            ExecutorService executor = Executors.newFixedThreadPool(15);
            ArrayList<Future<DataFrame>> results = new ArrayList<>();
            int it =0;
            String[] cn = dataframes.peekFirst().cnames;
            List<Class<? extends Value>> ct = dataframes.peekFirst().ctypes;
            while(it < dataframes.size()){
                results.add(executor.submit(new OperationOnDf(3,dataframes.get(it),key_id)));
                it++;
            }
            DataFrame ret = new DataFrame(cn,ct);
            for(int i=0;i<it;i++){
                try {
                    DataFrame tmp = results.get(i).get();
                    for(int j=0;j<ret.width;j++){
                        ret.Add(tmp.colms[j].col.get(0),j);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            executor.shutdown();
            return ret;
        }

        @Override
        public DataFrame std() {
            ExecutorService executor = Executors.newFixedThreadPool(15);
            ArrayList<Future<DataFrame>> results = new ArrayList<>();
            int it =0;
            String[] cn = dataframes.peekFirst().cnames;
            List<Class<? extends Value>> ct = dataframes.peekFirst().ctypes;
            while(it < dataframes.size()){
                results.add(executor.submit(new OperationOnDf(4,dataframes.get(it),key_id)));
                it++;
            }
            DataFrame ret = new DataFrame(cn,ct);
            for(int i=0;i<it;i++){
                try {
                    DataFrame tmp = results.get(i).get();
                    for(int j=0;j<ret.width;j++){
                        ret.Add(tmp.colms[j].col.get(0),j);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            executor.shutdown();
            return ret;
        }

        @Override
        public DataFrame sum() {
            ExecutorService executor = Executors.newFixedThreadPool(15);
            ArrayList<Future<DataFrame>> results = new ArrayList<>();
            int it =0;
            String[] cn = dataframes.peekFirst().cnames;
            List<Class<? extends Value>> ct = dataframes.peekFirst().ctypes;
            while(it < dataframes.size()){
                results.add(executor.submit(new OperationOnDf(2,dataframes.get(it),key_id)));
                it++;
            }
            DataFrame ret = new DataFrame(cn,ct);
            for(int i=0;i<it;i++){
                try {
                    DataFrame tmp = results.get(i).get();
                    for(int j=0;j<ret.width;j++){
                        ret.Add(tmp.colms[j].col.get(0),j);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            executor.shutdown();
            return ret;
        }

        @Override
        public DataFrame var() {
            ExecutorService executor = Executors.newFixedThreadPool(15);
            ArrayList<Future<DataFrame>> results = new ArrayList<>();
            int it =0;
            String[] cn = dataframes.peekFirst().cnames;
            List<Class<? extends Value>> ct = dataframes.peekFirst().ctypes;
            while(it < dataframes.size()){
                results.add(executor.submit(new OperationOnDf(5,dataframes.get(it),key_id)));
                it++;
            }
            DataFrame ret = new DataFrame(cn,ct);
            for(int i=0;i<it;i++){
                try {
                    DataFrame tmp = results.get(i).get();
                    for(int j=0;j<ret.width;j++){
                        ret.Add(tmp.colms[j].col.get(0),j);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            executor.shutdown();
            return ret;
        }

        @Override
        public DataFrame apply(Applyable a) {
            return null;
        }

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

    public DataFrame(String filename, List<Class<? extends Value>> col_types,boolean header) throws IOException, IllegalAccessException, InstantiationException, IncorrectWidth {
        ctypes = col_types;
        width = col_types.size();
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String firstl = br.readLine();
        int real_wdth = 0;
        for(int i=0;i<firstl.length();i++){
            if(firstl.charAt(i) == ',')real_wdth++;
        }
        if(real_wdth+1 != width){
            throw new IncorrectWidth();
        }

        br = new BufferedReader(new FileReader(file));
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
        for(String n: cnames) System.out.print(n + " ");
        System.out.println();
        for(int i=0;i<heigth;i++){
            for(int j=0;j<width;j++){
                System.out.print(colms[j].col.get(i).toString() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void toCSV(String filename) throws IOException {
        String tofile = "";
        int it = 0;
        for(String n: cnames) {
            tofile = tofile + n;
            if(it != width-1) tofile = tofile + ",";
            it++;
        }
        tofile = tofile + "\n";
        for(int i=0;i<heigth;i++){
            for(int j=0;j<width;j++){
                tofile = tofile + colms[j].col.get(i).toString();
                if(j != width-1) tofile = tofile + ",";
            }
            tofile = tofile + "\n";
        }
        tofile = tofile + "\n";
        File file = new File("./" + filename);
        if(file.createNewFile()){
            System.out.println(filename + ": OK");
            BufferedWriter writer = new BufferedWriter(new FileWriter("./" + filename));
            writer.write(tofile);
            writer.close();
        }else{
            System.out.println(filename + ": already exists");
        }
    }

    public static void main(String[] args){
        ArrayList<Class<? extends Value>> ct = new ArrayList<>();
        /*
        ct.add(dataframe.VInteger.class);
        ct.add(dataframe.VInteger.class);
        dataframe.SparseDataFrame dfs1 = new dataframe.SparseDataFrame(new String[] {"kol1","kol2"},ct , new dataframe.VInteger(0));
        dfs1.sFilld();
        dataframe.DataFrame df1 = dfs1.toDense();
        df1.print();
        String a[] = {"kol1"};
        df1.groupby(a).max().print();*/
        //GroupedDataFrame gdf = df1.groupby(a);
        //gdf.dataframes.get(0).print();
        //gdf.dataframes.get(1).print();*/

        ct.add(VString.class);
        ct.add(VDatetime.class);
        ct.add(VDouble.class);
        ct.add(VDouble.class);
        try {
            DataFrame df1 = new DataFrame("groupby.csv",ct,true);
            System.out.println("OK");
            df1.groupby("id").toThreadedGDF().mean().toCSV("asd.csv");
            df1.groupby("id").mean().print();
        } catch (IllegalAccessException | InstantiationException | IOException | IncorrectWidth e) {
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
