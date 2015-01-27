package netcheckers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListiner extends Thread {
    private static ServerListiner instance;
    private ServerSocket ss;

    public static ServerListiner getInstance() {
        if (instance == null) {
            instance = new ServerListiner();
        }
        return instance;
    }
    
    private ServerListiner() {
    }

    public void run() {
        try {
            ss = new ServerSocket(NetCheckers.getInstance().getPort());
            NetCheckers.getInstance().setConnectButtonText(false,true);
            Socket client = ss.accept();
            NetCheckers.getInstance().addToLog(" SYSTEM>>>Client connect: " + client.toString());
            NetCheckers.getInstance().addToLog(" SYSTEM>>>Press new game button to start new game");
            NetCheckers.getInstance().setEnableConnection(true);
            NetCheckers.getInstance().setEnableServer(true);
            NetCheckers.getInstance().serverConnect = new ServerConnect(client);
            NetCheckers.getInstance().connect = true;
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
