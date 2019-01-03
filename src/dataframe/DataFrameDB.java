package dataframe;

import java.io.IOException;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataFrameDB extends DataFrame {

    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:dataframedb.db";

    private Connection conn;
    private Statement stat;

    public DataFrameDB(DataFrame dt){
        super(dt.cnames,dt.ctypes);
        try {
            Class.forName(DataFrameDB.DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Brak sterownika JDBC");
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB_URL);
            stat = conn.createStatement();
        } catch (SQLException e) {
            System.err.println("Problem z otwarciem polaczenia");
            e.printStackTrace();
        }

        String create = "CREATE TABLE IF NOT EXISTS dataframe (";
        for(int i=0;i<dt.width;i++){
            create += dt.cnames[i] + " ";
            if(dt.ctypes.get(i) == VInteger.class){
                create += "INT, ";
            }
            if(dt.ctypes.get(i) == VDouble.class || dt.ctypes.get(i) == VFloat.class){
                create += "NUMERIC(24,12), ";
            }
            if(dt.ctypes.get(i) == VDatetime.class){
                create += "DATE, ";
            }else{
                create += "VARCHAR(255), ";
            }
            if(i==dt.width-1){
                create = create.substring(0,create.length()-2);
                create += ");";
            }
        }
        try{
            stat.executeUpdate(create);
            for(int i=0;i<dt.heigth;i++){
                String insert = "INSERT INTO dataframe VALUES (";
                for(int j=0;j<dt.width;j++){
                    if(dt.ctypes.get(i) == VDouble.class || dt.ctypes.get(i) == VFloat.class || dt.ctypes.get(i) == VInteger.class){
                        insert = insert + dt.colms[j].col.get(j).toString() + ", ";
                    }else{
                        insert = insert + "'" + dt.colms[j].col.get(j).toString() + "', ";
                    }
                    if(i==dt.width-1){
                        insert = create.substring(0,create.length()-2);
                        insert += ");";
                    }
                }
                stat.executeUpdate(insert);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    @Override
    public void print() {
        try {
            ResultSet rs = stat.executeQuery("SELECT * FROM dataframe");
            String print = "";
            while(rs.next()){
                print = "";
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

    public static void main(String[] args){
        ArrayList<Class<? extends Value>> ct = new ArrayList<>();
        ct.add(VInteger.class);
        ct.add(VString.class);
        ct.add(VDouble.class);
        try {
            DataFrame dt1 = new DataFrame("./data1.csv",ct,true);
            DataFrameDB dtd1 = new DataFrameDB(dt1);
            dtd1.print();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IncorrectWidth incorrectWidth) {
            incorrectWidth.printStackTrace();
        }
    }
}
