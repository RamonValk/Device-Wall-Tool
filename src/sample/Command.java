package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by RamonValk on 05-09-16.
 */

class Command {


    private ArrayList<String> getLine = new ArrayList<>();


    void eCommand(String cmd)
            throws IOException, InterruptedException {
        String line;
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(cmd);

        pr.waitFor();

        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));

        while ((line=buf.readLine())!=null) {
            System.out.println(line);
            getLine.add(line);

        }
    }


    ArrayList<String> getCArray() {
        return getLine;
    }

    public String getCString(int device) {
        return getLine.get(device);
    }

    public int getCArraySize() {
        return getLine.size();
    }
}
