package netcheckers;

import java.io.PrintWriter;

public class Writer extends Thread {

    private final PrintWriter out;
    private String message = "";
    private boolean stop = false;

    public Writer(PrintWriter out) {
        super();
        this.out = out;
    }

    public synchronized void sendMessage(String message) {
        this.message = message;
        notify();
    }

    public synchronized void closeWriter() {
        stop = true;
        notify();
    }

    public void run() {
        try {
            while (!stop) {
                synchronized (this) {
                    wait();
                }
                if (!stop) {
                    System.out.print("write " + message);
                    out.write(message);
                    out.flush();
                }
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            out.close();
        }
    }
}
