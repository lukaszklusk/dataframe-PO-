package dataframe;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GroupbyRelayServer {

    public static void main(String[] args) throws IOException {
        String dtf, key, type;
        ServerSocket serverSocket = null;
        ExecutorService executor = Executors.newFixedThreadPool(15);
        ArrayList<Future<DataFrame>> results = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(6666);
        } catch (IOException e) {
            System.out.println("Could not listen on port: 6666");
            System.exit(-1);
        }

        Socket dataframeSocket = null;
        try {
            dataframeSocket = serverSocket.accept();
        }
        catch(IOException e){
            System.out.println("Accept failed");
            System.exit(-1);
        }
        PrintWriter out = new PrintWriter(dataframeSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(dataframeSocket.getInputStream()));
        dtf = in.readLine();
        key = in.readLine();
        type = in.readLine();

        DataFrame tmp = null;
        try {
            tmp = DataFrame.fromString(dtf.split("-")[0]);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        String[] gdtf = dtf.split("-");
        int it = 0;
        while (true) {
            GRSWorker w;
            try {
                results.add(executor.submit(new GRSWorker(serverSocket.accept(),gdtf[it],key,type)));
                it++;
            } catch (IOException e) {
                System.out.println("Accept failed: 6666");
                System.exit(-1);
            }
            if(it == gdtf.length)break;
        }

        DataFrame ret = new DataFrame(tmp.cnames,tmp.ctypes);
        for(int i=0;i<it;i++){
            try {
                DataFrame temp = results.get(i).get();
                for(int j=0;j<ret.width;j++){
                    ret.Add(temp.colms[j].col.get(0),j);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        out.println(ret.toString());

        /*
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        clientSocket.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            out.println(inputLine);
        }*/
        out.close();
        in.close();
        dataframeSocket.close();
        serverSocket.close();

    }

}
