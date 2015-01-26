package netcheckers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.StringTokenizer;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private BufferedImage bufferImage;
    private BufferedImage temp_bufferImage;
    private Graphics2D buffer_g;
    private static final int BOARD_SIZE = 8;
    private int cellSize = 20;
    public int offset = 20;
    public char abc[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
    public char abc_l[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    public boolean move = false;
    public int x_move; 
    public int y_move;
    int now_hodit;
    public int i_where, j_where;
    public int i_end, j_end;
    public boolean rotate = false;
    public String hod_line = "";
    public boolean newline = true;
    public String eat_hod = "";
    public boolean alr_eat = false;
    public boolean game_start = false;

    public BoardPanel() {
        super();
        bufferImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        buffer_g = (Graphics2D) bufferImage.getGraphics();
    }

    public void paint(Graphics g) {
        update(g);
    }

    @Override
    public void update(Graphics g) {
        drawBoard(buffer_g);
        drawCheckers(buffer_g);
        drawMove(buffer_g);
        g.drawImage(bufferImage, 0, 0, null);
    }
    
    public void resizePanel() {
        int h = this.getHeight();
        int w = this.getWidth();
        int r;
        if (h >= w) {
            r = w;
        } else {
            r = h;
        }
        cellSize = (r - offset * 2) / BOARD_SIZE;
        bufferImage = (BufferedImage) createImage(this.getWidth(), this.getHeight());
        if (bufferImage != null) {
            buffer_g = (Graphics2D) bufferImage.getGraphics();
        }
    }



    public void drawMove(Graphics2D g) {
        if (move) {
            g.setColor(new Color(0, 255, 0));
            if (!rotate) {
                g.drawRect(offset + cellSize * i_where, offset + cellSize * (BOARD_SIZE - 1 - j_where), cellSize - 1, cellSize - 1);
            } else {
                g.drawRect(offset + cellSize * (BOARD_SIZE - 1 - i_where), offset + cellSize * j_where, cellSize - 1, cellSize - 1);
            }
            int pole_j_end = -1, pole_i_end = -1;
            if (offset < x_move && x_move < offset + cellSize * BOARD_SIZE) {
                pole_j_end = (int) (x_move - offset) / cellSize;
            }
            if (offset < y_move && y_move < offset + cellSize * BOARD_SIZE) {
                pole_i_end = (int) (y_move - offset) / cellSize;
            }
            if (pole_i_end >= 0 && pole_j_end >= 0) {
                boolean chet_i = (pole_i_end % 2 == 0);
                boolean chet_j = (pole_j_end % 2 == 0);
                if (!((chet_i && chet_j) || (!chet_i && !chet_j))) {
                    g.setColor(new Color(255, 0, 0));
                    g.drawRect(offset + cellSize * pole_j_end, offset + cellSize * pole_i_end, cellSize - 1, cellSize - 1);
                }
            }
            if (offset < x_move && x_move < offset + cellSize * BOARD_SIZE) {
                i_end = get_on_pole_i(x_move);
            }
            if (offset < y_move && y_move < offset + cellSize * BOARD_SIZE) {
                j_end = get_on_pole_j(y_move);
            }

            if (now_hodit == 1) {
                g.setColor(Configframe.getInstance().getColorChekersWhite());
                g.fillOval(x_move - cellSize / 2 - 2, y_move - cellSize / 2 - 2, cellSize - 4, cellSize - 4);
                g.setColor(Configframe.getInstance().getColorCheckersBlack());
                g.drawOval(x_move - cellSize / 2 - 2, y_move - cellSize / 2 - 2, cellSize - 4, cellSize - 4);
            } else if (now_hodit == 2) {
                g.setColor(Configframe.getInstance().getColorChekersWhite());
                g.fillOval(x_move - cellSize / 2 - 2, y_move - cellSize / 2 - 2, cellSize - 4, cellSize - 4);
                g.setColor(Configframe.getInstance().getColorCheckersBlack());
                g.fillOval(x_move - cellSize / 4 - 3, y_move - cellSize / 4 - 3, (cellSize - 4) / 2, (cellSize - 4) / 2);
                g.drawOval(x_move - cellSize / 2 - 2, y_move - cellSize / 2 - 2, cellSize - 4, cellSize - 4);
            } else if (now_hodit == 3) {
                g.setColor(Configframe.getInstance().getColorCheckersBlack());
                g.fillOval(x_move - cellSize / 2 - 2, y_move - cellSize / 2 - 2, cellSize - 4, cellSize - 4);
                g.setColor(Configframe.getInstance().getColorChekersWhite());
                g.drawOval(x_move - cellSize / 2 - 2, y_move - cellSize / 2 - 2, cellSize - 4, cellSize - 4);
            } else if (now_hodit == 4) {
                g.setColor(Configframe.getInstance().getColorCheckersBlack());
                g.fillOval(x_move - cellSize / 2 - 2, y_move - cellSize / 2 - 2, cellSize - 4, cellSize - 4);
                g.setColor(Configframe.getInstance().getColorChekersWhite());
                g.fillOval(x_move - cellSize / 4 - 3, y_move - cellSize / 4 - 3, (cellSize - 4) / 2, (cellSize - 4) / 2);
                g.drawOval(x_move - cellSize / 2 - 2, y_move - cellSize / 2 - 2, cellSize - 4, cellSize - 4);
            }
        }
    }

    public void drawBoard(Graphics2D g) {
        g.setColor(Configframe.getInstance().getColorBoardWhite());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(new Color(0));
        for (int i = 0; i <= BOARD_SIZE; i++) {
            g.drawLine(offset, offset + i * cellSize, offset + cellSize * BOARD_SIZE, offset + i * cellSize);
        }
        for (int i = 0; i <= BOARD_SIZE; i++) {
            g.drawLine(offset + i * cellSize, offset, offset + i * cellSize, offset + cellSize * BOARD_SIZE);
        }

        if (!rotate) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                g.drawString(abc[i] + "", offset + (cellSize * i) + cellSize / 2, offset + (cellSize + 1) * BOARD_SIZE + 7);
            }
            for (int i = 0; i < BOARD_SIZE; i++) {
                g.drawString(abc[i] + "", offset + (cellSize * i) + cellSize / 2, offset - 7);
            }
            for (int i = 0; i < BOARD_SIZE; i++) {
                g.drawString(BOARD_SIZE - i + "", offset + (cellSize + 1) * BOARD_SIZE - 1, offset + (cellSize * i) + cellSize / 2);
            }
            for (int i = 0; i < BOARD_SIZE; i++) {
                g.drawString(BOARD_SIZE - i + "", offset - 13, offset + (cellSize * i) + cellSize / 2);
            }
        } else {
            for (int i = 0; i < BOARD_SIZE; i++) {
                g.drawString(abc[BOARD_SIZE - 1 - i] + "", offset + (cellSize * i) + cellSize / 2, offset + (cellSize + 1) * BOARD_SIZE + 7);
            }
            for (int i = 0; i < BOARD_SIZE; i++) {
                g.drawString(abc[BOARD_SIZE - 1 - i] + "", offset + (cellSize * i) + cellSize / 2, offset - 7);
            }
            for (int i = 0; i < BOARD_SIZE; i++) {
                g.drawString(i + 1 + "", offset + (cellSize + 1) * BOARD_SIZE - 1, offset + (cellSize * i) + cellSize / 2);
            }
            for (int i = 0; i < BOARD_SIZE; i++) {
                g.drawString(i + 1 + "", offset - 13, offset + (cellSize * i) + cellSize / 2);
            }
        }
        g.drawRect(offset - 3, offset - 3, cellSize * BOARD_SIZE + 6, cellSize * BOARD_SIZE + 6);
        boolean b = false;
        g.setColor(Configframe.getInstance().getColorBoardBlack());
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (b) {
                    g.fillRect(offset + cellSize * j + 1, offset + cellSize * i + 1, cellSize - 1, cellSize - 1);
                }
                b = !b;
            }
            b = !b;
        }
    }

    public void drawCheckers(Graphics2D g) {
        boolean b = true;
        if (!rotate) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (b) {
                        if (CheckersBoard.getInstance().board[i][j] == 1) {
                            g.setColor(Configframe.getInstance().getColorChekersWhite());
                            g.fillOval(offset + cellSize * j + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + 2, cellSize - 4, cellSize - 4);
                            g.setColor(Configframe.getInstance().getColorCheckersBlack());
                            g.drawOval(offset + cellSize * j + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + 2, cellSize - 4, cellSize - 4);
                        } else if (CheckersBoard.getInstance().board[i][j] == 2) {
                            g.setColor(Configframe.getInstance().getColorChekersWhite());
                            g.fillOval(offset + cellSize * j + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + 2, cellSize - 4, cellSize - 4);
                            g.setColor(Configframe.getInstance().getColorCheckersBlack());
                            g.drawOval(offset + cellSize * j + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + 2, cellSize - 4, cellSize - 4);
                            g.fillOval(offset + cellSize * j + cellSize / 4 + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + cellSize / 4 + 2, (cellSize - 4) / 2, (cellSize - 4) / 2);
                        } else if (CheckersBoard.getInstance().board[i][j] == 3) {
                            g.setColor(Configframe.getInstance().getColorCheckersBlack());
                            g.fillOval(offset + cellSize * j + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + 2, cellSize - 4, cellSize - 4);
                            g.setColor(Configframe.getInstance().getColorChekersWhite());
                            g.drawOval(offset + cellSize * j + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + 2, cellSize - 4, cellSize - 4);
                        } else if (CheckersBoard.getInstance().board[i][j] == 4) {
                            g.setColor(Configframe.getInstance().getColorCheckersBlack());
                            g.fillOval(offset + cellSize * j + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + 2, cellSize - 4, cellSize - 4);
                            g.setColor(Configframe.getInstance().getColorChekersWhite());
                            g.drawOval(offset + cellSize * j + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + 2, cellSize - 4, cellSize - 4);
                            g.fillOval(offset + cellSize * j + cellSize / 4 + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + cellSize / 4 + 2, (cellSize - 4) / 2, (cellSize - 4) / 2);
                        }
                    }
                    b = !b;
                }
                b = !b;
            }
        } else {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (b) {
                        if (CheckersBoard.getInstance().board[i][j] == 1) {
                            g.setColor(Configframe.getInstance().getColorChekersWhite());
                            g.fillOval(offset + cellSize * (BOARD_SIZE - 1 - j) + 2, offset + cellSize * i + 2, cellSize - 4, cellSize - 4);
                            g.setColor(Configframe.getInstance().getColorCheckersBlack());
                            g.drawOval(offset + cellSize * (BOARD_SIZE - 1 - j) + 2, offset + cellSize * i + 2, cellSize - 4, cellSize - 4);
                        } else if (CheckersBoard.getInstance().board[i][j] == 2) {
                            g.setColor(Configframe.getInstance().getColorChekersWhite());
                            g.fillOval(offset + cellSize * (BOARD_SIZE - 1 - j) + 2, offset + cellSize * i + 2, cellSize - 4, cellSize - 4);
                            g.setColor(Configframe.getInstance().getColorCheckersBlack());
                            g.drawOval(offset + cellSize * (BOARD_SIZE - 1 - j) + 2, offset + cellSize * i + 2, cellSize - 4, cellSize - 4);
                            g.fillOval(offset + cellSize * (BOARD_SIZE - 1 - j) + cellSize / 4 + 2, offset + cellSize * i + cellSize / 4 + 2, (cellSize - 4) / 2, (cellSize - 4) / 2);
                        } else if (CheckersBoard.getInstance().board[i][j] == 3) {
                            g.setColor(Configframe.getInstance().getColorCheckersBlack());
                            g.fillOval(offset + cellSize * (BOARD_SIZE - 1 - j) + 2, offset + cellSize * i + 2, cellSize - 4, cellSize - 4);
                            g.setColor(Configframe.getInstance().getColorChekersWhite());
                            g.drawOval(offset + cellSize * (BOARD_SIZE - 1 - j) + 2, offset + cellSize * i + 2, cellSize - 4, cellSize - 4);
                        } else if (CheckersBoard.getInstance().board[i][j] == 4) {
                            g.setColor(Configframe.getInstance().getColorCheckersBlack());
                            g.fillOval(offset + cellSize * (BOARD_SIZE - 1 - j) + 2, offset + cellSize * i + 2, cellSize - 4, cellSize - 4);
                            g.setColor(Configframe.getInstance().getColorChekersWhite());
                            g.drawOval(offset + cellSize * (BOARD_SIZE - 1 - j) + 2, offset + cellSize * i + 2, cellSize - 4, cellSize - 4);
                            g.fillOval(offset + cellSize * (BOARD_SIZE - 1 - j) + cellSize / 4 + 2, offset + cellSize * i + cellSize / 4 + 2, (cellSize - 4) / 2, (cellSize - 4) / 2);
                        }
                    }
                    b = !b;
                }
                b = !b;
            }
        }
    }

    public void movestart() {
        if (!game_start) {
            NetCheckers.getInstance().addtolog(" SYSTEM>>>You can not do move, game not started");
            return;
        }
        if (CheckersBoard.getInstance().myColor != CheckersBoard.getInstance().nowMove) {
            return;
        }
        move = false;
        int j = -1, i = -1, this_h;
        if (offset < x_move && x_move < offset + cellSize * BOARD_SIZE) {
            i = get_on_pole_i(x_move);
        }
        if (offset < y_move && y_move < offset + cellSize * BOARD_SIZE) {
            j = get_on_pole_j(y_move);
        }
        //System.out.println(j+" "+i);
        if (i >= 0 && j >= 0) {
            this_h = CheckersBoard.getInstance().board[j][i];
            //System.out.println(this_h);
            if ((CheckersBoard.getInstance().nowMove == CheckersBoard.WHITE && (this_h == 1 || this_h == 2)) || (CheckersBoard.getInstance().nowMove == CheckersBoard.BLACK && (this_h == 3 || this_h == 4))) {
                move = true;
                i_where = i;
                j_where = j;
                i_end = -1;
                j_end = -1;
                now_hodit = this_h;
                CheckersBoard.getInstance().board[j_where][i_where] = 0;
            }
        }

    }

    public int get_on_pole_i(int x_move) {
        int i = 0;
        if (rotate) {
            i = BOARD_SIZE - 1 - (int) (x_move - offset) / cellSize;
        } else {
            i = (int) (x_move - offset) / cellSize;
        }
        return i;
    }

    public int get_on_pole_j(int y_move) {
        int j = 0;
        if (rotate) {
            j = (int) (y_move - offset) / cellSize;
        } else {
            j = BOARD_SIZE - 1 - (int) (y_move - offset) / cellSize;
        }
        return j;
    }

    public void movestop() {
        if (CheckersBoard.getInstance().myColor != CheckersBoard.getInstance().nowMove) {
            return;
        }
        //System.out.println(i_where+" "+j_where);
        //System.out.println(i_end+" "+j_end);
        if ((j_end >= 0 && i_end >= 0) && move) {
            CheckersBoard.getInstance().board[j_where][i_where] = now_hodit;
            CheckersBoard.getInstance().canEat();
            CheckersBoard.getInstance().board[j_where][i_where] = 0;
            String send = "";
            if (CheckersBoard.getInstance().must_eat) {
                String h = Integer.toString(j_where) + Integer.toString(i_where) + "-"
                        + Integer.toString(j_end) + Integer.toString(i_end);
                System.out.println("move" + h);
                System.out.println("eat" + CheckersBoard.getInstance().must_eat_s);
                StringTokenizer st = new StringTokenizer(CheckersBoard.getInstance().must_eat_s, ";");
                boolean dh = false;
                while (st.hasMoreTokens()) {
                    String s = st.nextToken();
                    if (s.equals(h)) {
                        CheckersBoard.getInstance().board[j_end][i_end] = now_hodit;
                        CheckersBoard.getInstance().board[j_where + (j_end - j_where) / 2][i_where + (i_end - i_where) / 2] = 0;
                        send = ("eat "
                                + Integer.toString(i_where) + " " + Integer.toString(j_where) + " "
                                + Integer.toString(i_end) + " " + Integer.toString(j_end));

                        if (alr_eat) {
                            eat_hod = eat_hod + "-"
                                    + abc_l[i_end] + Integer.toString(j_end + 1);
                        } else {
                            eat_hod = eat_hod + abc_l[i_where] + Integer.toString(j_where + 1) + "-"
                                    + abc_l[i_end] + Integer.toString(j_end + 1);
                        }
                        alr_eat = true;
                        dh = true;
                    }
                }
                if (!dh) {
                    CheckersBoard.getInstance().board[j_where][i_where] = now_hodit;
                } else {
                    CheckersBoard.getInstance().must_eat = false;
                    CheckersBoard.getInstance().must_eat_s = "";
                    CheckersBoard.getInstance().canEatXY(j_end, i_end);
                    if (!CheckersBoard.getInstance().must_eat) {
                        CheckersBoard.getInstance().nowMove = (CheckersBoard.getInstance().nowMove == CheckersBoard.WHITE) ? CheckersBoard.BLACK : CheckersBoard.WHITE;
                        send = send + "\neatend " + eat_hod;
                        addtolog(0, 0, 0, 0, eat_hod);
                        eat_hod = "";
                        alr_eat = false;
                        NetCheckers.getInstance().setWhiteMove(CheckersBoard.getInstance().nowMove == CheckersBoard.WHITE);
                        CheckersBoard.getInstance().checkKings();
                        repaint();
                    }
                    NetCheckers.getInstance().sc.send_mess(send);
                }
            } else if (CheckersBoard.getInstance().canMove(i_where, j_where, j_end, i_end, now_hodit - CheckersBoard.getInstance().nowMove)) {
                CheckersBoard.getInstance().board[j_end][i_end] = now_hodit;
                CheckersBoard.getInstance().checkKings();
                NetCheckers.getInstance().sc.send_mess("move "
                        + Integer.toString(i_where) + " " + Integer.toString(j_where) + " "
                        + Integer.toString(i_end) + " " + Integer.toString(j_end));
                CheckersBoard.getInstance().nowMove = (CheckersBoard.getInstance().nowMove == CheckersBoard.WHITE) ? CheckersBoard.BLACK : CheckersBoard.WHITE;
                NetCheckers.getInstance().setWhiteMove(CheckersBoard.getInstance().nowMove == CheckersBoard.WHITE);
                addtolog(i_where, j_where, i_end, j_end, "");
            } else {
                CheckersBoard.getInstance().board[j_where][i_where] = now_hodit;
            }
        } else {
            if (move) {
                CheckersBoard.getInstance().board[j_where][i_where] = now_hodit;
            }
        }
        move = false;
        repaint();
    }

    public void addtolog(int i_where, int j_where, int i_end, int j_end, String ss) {
        String s;
        if (!ss.isEmpty()) {
            s = ss;
        } else {
            s = abc_l[i_where] + Integer.toString(j_where + 1) + "-"
                    + abc_l[i_end] + Integer.toString(j_end + 1);
        }
        if (newline) {
            NetCheckers.getInstance().listModel.addElement(s);
            newline = false;
        } else {
            NetCheckers.getInstance().listModel.setElementAt(
                    NetCheckers.getInstance().listModel.lastElement() + "   " + s,
                    NetCheckers.getInstance().listModel.getSize() - 1);
            newline = true;
        }
        NetCheckers.getInstance().jScrollPane1.getVerticalScrollBar().setValue(
                NetCheckers.getInstance().jScrollPane1.getVerticalScrollBar().getMaximum());
    }
}
