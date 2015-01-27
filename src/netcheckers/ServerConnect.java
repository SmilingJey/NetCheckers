package netcheckers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerConnect {

    public boolean accept = false;
    public boolean active = false;
    public String info;
    Socket socket;
    Writer writer;
    Reader reader;
    public boolean stop = false;

    public ServerConnect(Socket socket) {
        super();
        this.socket = socket;
        try {
            writer = new Writer(new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true));
            reader = new Reader(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            writer.start();
            reader.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        writer.sendMessage(message + '\n');
    }

    public void closeServerConnection() throws IOException {
        socket.shutdownInput();
        socket.shutdownOutput();
        writer.closeWriter();
        socket.close();
    }
}
