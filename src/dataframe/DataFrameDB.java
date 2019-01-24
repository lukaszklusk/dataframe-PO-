package dataframe;

import org.sqlite.SQLiteException;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.sql.*;

public class DataFrameDB extends DataFrame {

    public static final String dbURL = "jdbc:sqlite:dataframe.db";
    private Connection con;
    private Statement stat;

    public void connector(){
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Brak sterownika JDBC");
            e.printStackTrace();
        }

        try {
            con = DriverManager.getConnection(dbURL);
            stat = con.createStatement();
        } catch (SQLException e) {
            System.err.println("Problem z otwarciem polaczenia");
            e.printStackTrace();
        }
    }

    public DataFrameDB(DataFrame df){
        super(df.cnames,df.ctypes);

        this.connector();

        try {
            stat.executeUpdate("DROP TABLE IF EXISTS dataframe");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String create = "CREATE TABLE IF NOT EXISTS dataframe (\n";
        for(int i=0;i<df.width;i++){
            create += df.cnames[i] + " ";
            if(df.ctypes.get(i) == VInteger.class){
                create += "INT";
            }
            else if(df.ctypes.get(i) == VDouble.class || df.ctypes.get(i) == VFloat.class){
                create += "NUMERIC(20,10)";
            }
            else if(df.ctypes.get(i) == VDatetime.class){
                create += "DATE";
            }
            else if(df.ctypes.get(i) !=null){
                create += "VARCHAR(255)";
            }
            create += ",\n";
            if(i==df.width-1){
                create = create.substring(0,create.length()-2);
                create += ");";
            }
        }
        try{
            stat.executeUpdate(create);
            for(int i = 0; i<df.heigth; i++){
                String insert = "INSERT INTO dataframe VALUES (";
                for(int j=0;j<df.width;j++){
                    if(df.ctypes.get(j) == VDouble.class || df.ctypes.get(j) == VFloat.class || df.ctypes.get(j) == VInteger.class){
                        insert = insert + df.colms[j].col.get(i).toString() + ", ";
                    }else if(df.ctypes.get(j) != null){
                        insert = insert + "'" + df.colms[j].col.get(i).toString() + "', ";
                    }
                    if(j==df.width-1){
                        insert = insert.substring(0,insert.length()-2);
                        insert += ");";
                    }
                }
                stat.executeUpdate(insert);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void print() {
        try {
            ResultSet rs = stat.executeQuery("SELECT * FROM dataframe");
            String print = "";
            while(rs.next()){
                for(int i=0;i<width;i++){
                    print = print + rs.getString(i+1) + " ";
                }
                print = print + "\n";
            }
            System.out.println(print);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public class DatabaseGDB implements Groupby{
        DataFrameDB dfdb;
        String key;

        DatabaseGDB(DataFrameDB in,String k){
            dfdb = in;
            key = k;
        }

        @Override
        public DataFrame max() {
            String command = "SELECT ";
            for(int i=0;i<dfdb.width;i++){
                if(key.equals(cnames[i])){
                    command += cnames[i];
                }else{
                    command += "max(" + cnames[i] + ")";
                }
                if(i != dfdb.width-1){
                    command += ",";
                }
            }
            command += " FROM dataframe GROUP BY " + key + ";";
            System.out.println(command);
            DataFrame dt = new DataFrame(dfdb.cnames,dfdb.ctypes);
            try {
                ResultSet rs = dfdb.stat.executeQuery(command);
                while(rs.next()){
                    for(int i=0;i<width;i++){
                        dt.addS(rs.getString(i+1),i);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return dt;
        }

        @Override
        public DataFrame min() {
            String command = "SELECT ";
            for(int i=0;i<dfdb.width;i++){
                if(key.equals(cnames[i])){
                    command += cnames[i];
                }else{
                    command += "min(" + cnames[i] + ")";
                }
                if(i != dfdb.width-1){
                    command += ",";
                }
            }
            command += " FROM dataframe GROUP BY " + key + ";";
            System.out.println(command);
            DataFrame dt = new DataFrame(dfdb.cnames,dfdb.ctypes);
            try {
                ResultSet rs = dfdb.stat.executeQuery(command);
                while(rs.next()){
                    for(int i=0;i<width;i++){
                        dt.addS(rs.getString(i+1),i);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return dt;
        }

        @Override
        public DataFrame mean() {
            String command = "SELECT ";
            for(int i=0;i<dfdb.width;i++){
                boolean comma = true;
                if(key.equals(cnames[i])){
                    command += cnames[i];
                }else{
                    try {
                        if(ctypes.get(i).newInstance() instanceof VString || ctypes.get(i).newInstance() instanceof VDatetime){
                            comma = false;
                        }else {
                            command += "avg(" + cnames[i] + ")";
                        }
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if(i != dfdb.width-1 && comma){
                    command += ",";
                }
            }
            command += " FROM dataframe GROUP BY " + key + ";";
            System.out.println(command);
            DataFrame dt = new DataFrame(dfdb.cnames,dfdb.ctypes);
            try {
                ResultSet rs = dfdb.stat.executeQuery(command);
                while(rs.next()){
                    int ri = 0;
                    for(int i=0;i<width;i++){
                        try {
                            if(ctypes.get(i).newInstance() instanceof VString || ctypes.get(i).newInstance() instanceof VDatetime){
                                dt.Add(new VString("NA"),i);
                            }else {
                                dt.addS(rs.getString(ri+1),i);
                                ri++;
                            }
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return dt;
        }

        @Override
        public DataFrame std() {
            String command = "SELECT ";
            for(int i=0;i<dfdb.width;i++){
                boolean comma = true;
                if(key.equals(cnames[i])){
                    command += cnames[i];
                }else{
                    try {
                        if(ctypes.get(i).newInstance() instanceof VString || ctypes.get(i).newInstance() instanceof VDatetime){
                            comma = false;
                        }else {
                            command += "stdev(" + cnames[i] + ")";
                        }
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if(i != dfdb.width-1 && comma){
                    command += ",";
                }
            }
            command += " FROM dataframe GROUP BY " + key + ";";
            System.out.println(command);
            DataFrame dt = new DataFrame(dfdb.cnames,dfdb.ctypes);
            try {
                ResultSet rs = dfdb.stat.executeQuery(command);
                while(rs.next()){
                    int ri = 0;
                    for(int i=0;i<width;i++){
                        try {
                            if(ctypes.get(i).newInstance() instanceof VString || ctypes.get(i).newInstance() instanceof VDatetime){
                                dt.Add(new VString("NA"),i);
                            }else {
                                dt.addS(rs.getString(ri+1),i);
                                ri++;
                            }
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return dt;
        }

        @Override
        public DataFrame sum() {
            String command = "SELECT ";
            for(int i=0;i<dfdb.width;i++){
                boolean comma = true;
                if(key.equals(cnames[i])){
                    command += cnames[i];
                }else{
                    try {
                        if(ctypes.get(i).newInstance() instanceof VString || ctypes.get(i).newInstance() instanceof VDatetime){
                            comma = false;
                        }else {
                            command += "sum(" + cnames[i] + ")";
                        }
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if(i != dfdb.width-1 && comma){
                    command += ",";
                }
            }
            command += " FROM dataframe GROUP BY " + key + ";";
            System.out.println(command);
            DataFrame dt = new DataFrame(dfdb.cnames,dfdb.ctypes);
            try {
                ResultSet rs = dfdb.stat.executeQuery(command);
                while(rs.next()){
                    int ri = 0;
                    for(int i=0;i<width;i++){
                        try {
                            if(ctypes.get(i).newInstance() instanceof VString || ctypes.get(i).newInstance() instanceof VDatetime){
                                dt.Add(new VString("NA"),i);
                            }else {
                                dt.addS(rs.getString(ri+1),i);
                                ri++;
                            }
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return dt;
        }

        @Override
        public DataFrame var() {
            String command = "SELECT ";
            for(int i=0;i<dfdb.width;i++){
                boolean comma = true;
                if(key.equals(cnames[i])){
                    command += cnames[i];
                }else{
                    try {
                        if(ctypes.get(i).newInstance() instanceof VString || ctypes.get(i).newInstance() instanceof VDatetime){
                            comma = false;
                        }else {
                            command += "variance(" + cnames[i] + ")";
                        }
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                if(i != dfdb.width-1 && comma){
                    command += ",";
                }
            }
            command += " FROM dataframe GROUP BY " + key + ";";
            System.out.println(command);
            DataFrame dt = new DataFrame(dfdb.cnames,dfdb.ctypes);
            try {
                ResultSet rs = dfdb.stat.executeQuery(command);
                while(rs.next()){
                    int ri = 0;
                    for(int i=0;i<width;i++){
                        try {
                            if(ctypes.get(i).newInstance() instanceof VString || ctypes.get(i).newInstance() instanceof VDatetime){
                                dt.Add(new VString("NA"),i);
                            }else {
                                dt.addS(rs.getString(ri+1),i);
                                ri++;
                            }
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return dt;
        }

        @Override
        public DataFrame apply(Applyable a) {
            return null;
        }
    }

    public DatabaseGDB groupbyDB(String key){
        DatabaseGDB gdb = new DatabaseGDB(this,key);
        return gdb;
    }

    public DataFrame toDataFrame(){
        DataFrame df = new DataFrame(cnames,ctypes);
        try {
            ResultSet rs = stat.executeQuery("SELECT * FROM dataframe");
            while(rs.next()){
                for(int i=0;i<width;i++){
                    df.addS(rs.getString(i+1),i);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return df;
    }

    public void close() throws SQLException {
        stat.close();
        con.close();
    }

    public static void main(String[] args){
        ArrayList<Class<? extends Value>> ct = new ArrayList<>();
        ct.add(VInteger.class);
        ct.add(VString.class);
        ct.add(VDouble.class);
        try {
            DataFrame dt1 = new DataFrame("./data1.csv",ct,true);
            DataFrameDB dtd1 = new DataFrameDB(dt1);
            dtd1.groupbyDB("a").var().print();
            dtd1.close();
        } catch (IOException | IncorrectWidth | InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
    }
}
