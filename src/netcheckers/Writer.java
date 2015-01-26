package netcheckers;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Writer extends Thread {

    PrintWriter out;
    ServerConnect server;
    public String mess = "";
    public boolean stop = false;

    public Writer(ServerConnect server) {
        super();
        this.server = server;
    }

    public void run() {
        try {
            out = new PrintWriter(new OutputStreamWriter(server.client.getOutputStream()), true);
            while (!stop) {
                synchronized (this) {
                    wait();
                }
                System.out.print("write " + mess);
                out.write(mess);
                out.flush();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.close();
        }
    }

    synchronized void notify_this() {
        notify();
    }
}
