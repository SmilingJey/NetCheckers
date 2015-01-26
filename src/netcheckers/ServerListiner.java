package netcheckers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListiner extends Thread {

    static ServerListiner instance;
    int port;

    private boolean work = true;
    public ServerSocket ss;

    public ServerListiner() {
        instance = this;
    }

    public void run() {
        port = NetCheckers.getInstance().getPort();
        try {
            ss = new ServerSocket(port);
            NetCheckers.getInstance().jButtonConnect.setText("Disconnect");
            Socket client = ss.accept();
            NetCheckers.getInstance().addtolog(" SYSTEM>>>Client connect: " + client.toString());
            NetCheckers.getInstance().addtolog(" SYSTEM>>>Press new game button to start new game");
            NetCheckers.getInstance().set_enable_connect(true);
            NetCheckers.getInstance().set_enable_server(true);
            ServerConnect sc = new ServerConnect(client);
            NetCheckers.getInstance().sc = sc;
            NetCheckers.getInstance().connect = true;
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
