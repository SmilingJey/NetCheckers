package netcheckers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.StringTokenizer;

public class Reader extends Thread {

    BufferedReader in;
    ServerConnect server;
    public boolean stop = false;

    public Reader(ServerConnect server) {
        super();
        this.server = server;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(server.client.getInputStream()));
            while (!stop) {
                parse(in.readLine());
            }
            in.close();
        } catch (SocketException ee) {
            ee.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            NetCheckers.getInstance().addtolog(" SYSTEM>>>Client disconnect");
            NetCheckers.getInstance().game_disconnect();
            //Pole.getInstance().initBoard();
        }
    }

    public void parse(String mess) {
        if (mess.isEmpty()) {
            System.out.println("parse null");
            return;
        };
        System.out.print("read " + mess + '\n');
        String otvet = "";
        String command = "";
        StringTokenizer st = new StringTokenizer(mess, " ");
        if (st.hasMoreElements()) {
            command = st.nextToken().trim();
        } else {
            return;
        }
        if (command.equals("mess")) {
            NetCheckers.getInstance().addtolog(" CLIENT>>> " + mess.substring(5));
        } else if (command.equals("newgame")) {
            CheckersBoard.getInstance().initBoard();
            
            NetCheckers.getInstance().timerWhitePause = true;
            NetCheckers.getInstance().timerBlackPause = true;

            if (st.hasMoreTokens()) {
                if (st.nextToken().equals("WHITE")) {
                    CheckersBoard.getInstance().myColor = CheckersBoard.WHITE;
                    NetCheckers.getInstance().jCheckBoxRotateBoard.setSelected(false);
                    NetCheckers.getInstance().jLabel1.setText("You play WHITE");
                    CheckersBoard.getInstance().nowMove = CheckersBoard.WHITE;
                } else {
                    CheckersBoard.getInstance().myColor = CheckersBoard.BLACK;
                    NetCheckers.getInstance().jCheckBoxRotateBoard.setSelected(true);
                    NetCheckers.getInstance().jLabel1.setText("You play BLACK");
                    CheckersBoard.getInstance().nowMove = CheckersBoard.WHITE;
                }
            }
            NetCheckers.getInstance().addtolog(" SYSTEM>>>New game started, "
                    + NetCheckers.getInstance().jLabel1.getText());
            NetCheckers.getInstance().jLabelWhiteTime.setText(" WHITE: 0:00:00");
            NetCheckers.getInstance().jLabelBlackTime.setText(" BLACK: 0:00:00");
            NetCheckers.getInstance().setWhiteMove(CheckersBoard.getInstance().nowMove == CheckersBoard.WHITE);
            NetCheckers.getInstance().listModel.clear();
            NetCheckers.getInstance().pole_panel.game_start = true;
            NetCheckers.getInstance().pole_panel.newline = true;
            NetCheckers.getInstance().pole_panel.repaint();

        } else if (command.equals("move")) {
            int i_where = Integer.parseInt(st.nextToken());
            int j_where = Integer.parseInt(st.nextToken());
            int i_end = Integer.parseInt(st.nextToken());
            int j_end = Integer.parseInt(st.nextToken());
            CheckersBoard.getInstance().board[j_end][i_end] = CheckersBoard.getInstance().board[j_where][i_where];
            CheckersBoard.getInstance().board[j_where][i_where] = 0;
            CheckersBoard.getInstance().nowMove = (CheckersBoard.getInstance().nowMove == CheckersBoard.WHITE) ? CheckersBoard.BLACK : CheckersBoard.WHITE;
            //System.out.println("hoi"+CheckersBoard.getInstance().myColor);
            //System.out.println("now"+CheckersBoard.getInstance().nowMove);
            NetCheckers.getInstance().setWhiteMove(CheckersBoard.getInstance().nowMove == CheckersBoard.WHITE);
            NetCheckers.getInstance().pole_panel.addtolog(i_where, j_where, i_end, j_end, "");
            CheckersBoard.getInstance().checkKings();
            NetCheckers.getInstance().pole_panel.repaint();
        } else if (command.equals("eat")) {
            int i_where = Integer.parseInt(st.nextToken());
            int j_where = Integer.parseInt(st.nextToken());
            int i_end = Integer.parseInt(st.nextToken());
            int j_end = Integer.parseInt(st.nextToken());
            CheckersBoard.getInstance().board[j_end][i_end] = CheckersBoard.getInstance().board[j_where][i_where];
            CheckersBoard.getInstance().board[j_where][i_where] = 0;
            CheckersBoard.getInstance().board[j_where + (j_end - j_where) / 2][i_where + (i_end - i_where) / 2] = 0;
            CheckersBoard.getInstance().checkKings();
            NetCheckers.getInstance().pole_panel.repaint();

        } else if (command.equals("eatend")) {
            CheckersBoard.getInstance().nowMove = (CheckersBoard.getInstance().nowMove == CheckersBoard.WHITE) ? CheckersBoard.BLACK : CheckersBoard.WHITE;
            NetCheckers.getInstance().setWhiteMove(CheckersBoard.getInstance().nowMove == CheckersBoard.WHITE);
            NetCheckers.getInstance().pole_panel.addtolog(0, 0, 0, 0, st.nextToken());
        }
        if (!otvet.isEmpty()) {
            server.send_mess(otvet);
            int i_where = Integer.parseInt(st.nextToken());
            int j_where = Integer.parseInt(st.nextToken());
            int i_end = Integer.parseInt(st.nextToken());
            int j_end = Integer.parseInt(st.nextToken());
            CheckersBoard.getInstance().board[j_end][i_end] = CheckersBoard.getInstance().board[j_where][i_where];
            CheckersBoard.getInstance().board[j_where][i_where] = 0;
            NetCheckers.getInstance().pole_panel.repaint();
        }
    }

}
