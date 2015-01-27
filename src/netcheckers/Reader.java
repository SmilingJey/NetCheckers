package netcheckers;

import java.io.BufferedReader;
import java.io.IOException;

public class Reader extends Thread {

    private final BufferedReader in;
    private boolean stop = false;

    public Reader(BufferedReader in) {
        super();
        this.in = in;
    }

    public synchronized void closeReader() {
        stop = true;
    }

    public void run() {
        try {
            while (!stop) {
                NetCheckers.getInstance().parse(in.readLine());
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            NetCheckers.getInstance().addToLog(" SYSTEM>>>Client disconnect");
            NetCheckers.getInstance().disconnect();
        }
    }
}
