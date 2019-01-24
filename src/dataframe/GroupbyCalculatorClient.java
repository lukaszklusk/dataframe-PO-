package dataframe;

import java.io.*;
import java.net.*;

public class GroupbyCalculatorClient {
    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket("localhost", 6666);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: localhost.");
            System.exit(1);
        }

        String df_str;
        while((df_str = in.readLine()) != null){
            String key = in.readLine();
            String type = in.readLine();
            DataFrame df = DataFrame.fromString(df_str);
            if(type.equals("max")) out.println(df.groupby(key).max().toString());
            else if(type.equals("min")) out.println(df.groupby(key).min().toString());
            else if(type.equals("mean")) out.println(df.groupby(key).mean().toString());
            else if(type.equals("sum")) out.println(df.groupby(key).sum().toString());
            else if(type.equals("var")) out.println(df.groupby(key).var().toString());
            else if(type.equals("std")) out.println(df.groupby(key).std().toString());
        }

        out.close();
        in.close();
        echoSocket.close();
    }
}
