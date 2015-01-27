package netcheckers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerListiner extends Thread {
    private ServerSocket serverSocket;

    public ServerListiner() {
    }

    public void run() {
        try {
            serverSocket = new ServerSocket(NetCheckers.getInstance().getPort());
            NetCheckers.getInstance().setConnectButtonText(false,true);
            Socket client = serverSocket.accept();
            
            NetCheckers.getInstance().addToLog(" SYSTEM>>>Client connect: " + client.toString());
            NetCheckers.getInstance().addToLog(" SYSTEM>>>To start new game press button 'New game'");
            NetCheckers.getInstance().addToLog(" SYSTEM>>>If you want to play black checkers select 'Server - BLACK'");
            NetCheckers.getInstance().addToLog(" SYSTEM>>>You can chat with your opponent through the 'Messages'");
            NetCheckers.getInstance().setEnableConnection(true);
            NetCheckers.getInstance().setEnableServer(true);
            NetCheckers.getInstance().serverConnect = new ServerConnect(client);
            NetCheckers.getInstance().connect = true;
            
            serverSocket.close();
            NetCheckers.getInstance().startNewGame();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void closeServerListener(){
        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
