package dataframe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.concurrent.Callable;

public class GRSWorker implements Callable<DataFrame> {
    private Socket clientSocket;
    private String df_str;
    private String key;
    private String type;

    GRSWorker(Socket client,String s,String k,String t){
        clientSocket = client;
        df_str = s;
        key = k;
        type = t;
    }

    @Override
    public DataFrame call() {
        PrintWriter out = null;
        String res = "";
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine;

            out.println(df_str);
            out.println(key);
            out.println(type);

            res = in.readLine();

            out.close();
            in.close();
        }
        catch (IOException e){
            System.exit(-1);
        }
        DataFrame r = null;
        try {
            r = DataFrame.fromString(res);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return r;
    }
}
