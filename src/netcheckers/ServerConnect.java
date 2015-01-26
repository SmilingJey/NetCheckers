package netcheckers;

import java.net.Socket;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author APTEM
 */
public class ServerConnect {
    public boolean accept=false;
    public boolean active=false;
    public String info;
    Socket client;
    Writer writer;
    Reader reader;
    public boolean stop=false;

    public ServerConnect(Socket client) {
        super();
        this.client=client;
        writer=new Writer(this);
        reader=new Reader(this);
        writer.start();
        reader.start();
    }

    public void send_mess(String mess){
    	writer.mess=mess+'\n';
    	writer.notify_this();
    }
}
