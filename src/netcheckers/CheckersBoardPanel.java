package netcheckers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.StringTokenizer;
import javax.swing.JPanel;

public class CheckersBoardPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private BufferedImage bufferImage;
    private Graphics2D buffer_g;
    private static final int BOARD_SIZE = 8;
    private int cellSize = 20;
    private final int offset = 20;
    private final char abc[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
    private final char abc_l[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
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
    public boolean gameStart = false;
    public static final int WHITE = 1;
    public static final int WHITE_KING = 2;
    public static final int BLACK = 3;
    public static final int BLACK_KING = 4;
    public int nowMove = WHITE;
    public int myColor = WHITE;
    public int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    public boolean mustEat = false;
    public String mustEatString = "";
    public boolean gameEnd = false;
    public String gameEndWin = "WHITE WIN";

    public CheckersBoardPanel() {
        super();

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent e) {
                moveStop();
            }

            public void mousePressed(java.awt.event.MouseEvent e) {
                x_move = e.getX();
                y_move = e.getY();
                moveStart();
            }
        });
        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent e) {
                x_move = e.getX();
                y_move = e.getY();
                repaint();
            }
        });
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                resizePanel();
                repaint();
            }
        });

        bufferImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        buffer_g = (Graphics2D) bufferImage.getGraphics();
        initBoard();
    }

    public void paint(Graphics g) {
        update(g);
    }

    public void update(Graphics g) {
        drawBoard(buffer_g);
        drawCheckers(buffer_g);
        drawMove(buffer_g);
        drawEndGame(buffer_g);
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

    private void drawMove(Graphics2D g) {
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
                i_end = mouseXToBoardI(x_move);
            }
            if (offset < y_move && y_move < offset + cellSize * BOARD_SIZE) {
                j_end = mouseYToBoardJ(y_move);
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

    private void drawBoard(Graphics2D g) {
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

    private void drawCheckers(Graphics2D g) {
        boolean b = true;
        if (!rotate) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (b) {
                        if (board[i][j] == 1) {
                            g.setColor(Configframe.getInstance().getColorChekersWhite());
                            g.fillOval(offset + cellSize * j + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + 2, cellSize - 4, cellSize - 4);
                            g.setColor(Configframe.getInstance().getColorCheckersBlack());
                            g.drawOval(offset + cellSize * j + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + 2, cellSize - 4, cellSize - 4);
                        } else if (board[i][j] == 2) {
                            g.setColor(Configframe.getInstance().getColorChekersWhite());
                            g.fillOval(offset + cellSize * j + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + 2, cellSize - 4, cellSize - 4);
                            g.setColor(Configframe.getInstance().getColorCheckersBlack());
                            g.drawOval(offset + cellSize * j + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + 2, cellSize - 4, cellSize - 4);
                            g.fillOval(offset + cellSize * j + cellSize / 4 + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + cellSize / 4 + 2, (cellSize - 4) / 2, (cellSize - 4) / 2);
                        } else if (board[i][j] == 3) {
                            g.setColor(Configframe.getInstance().getColorCheckersBlack());
                            g.fillOval(offset + cellSize * j + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + 2, cellSize - 4, cellSize - 4);
                            g.setColor(Configframe.getInstance().getColorChekersWhite());
                            g.drawOval(offset + cellSize * j + 2, offset + cellSize * (BOARD_SIZE - 1 - i) + 2, cellSize - 4, cellSize - 4);
                        } else if (board[i][j] == 4) {
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
                        if (board[i][j] == 1) {
                            g.setColor(Configframe.getInstance().getColorChekersWhite());
                            g.fillOval(offset + cellSize * (BOARD_SIZE - 1 - j) + 2, offset + cellSize * i + 2, cellSize - 4, cellSize - 4);
                            g.setColor(Configframe.getInstance().getColorCheckersBlack());
                            g.drawOval(offset + cellSize * (BOARD_SIZE - 1 - j) + 2, offset + cellSize * i + 2, cellSize - 4, cellSize - 4);
                        } else if (board[i][j] == 2) {
                            g.setColor(Configframe.getInstance().getColorChekersWhite());
                            g.fillOval(offset + cellSize * (BOARD_SIZE - 1 - j) + 2, offset + cellSize * i + 2, cellSize - 4, cellSize - 4);
                            g.setColor(Configframe.getInstance().getColorCheckersBlack());
                            g.drawOval(offset + cellSize * (BOARD_SIZE - 1 - j) + 2, offset + cellSize * i + 2, cellSize - 4, cellSize - 4);
                            g.fillOval(offset + cellSize * (BOARD_SIZE - 1 - j) + cellSize / 4 + 2, offset + cellSize * i + cellSize / 4 + 2, (cellSize - 4) / 2, (cellSize - 4) / 2);
                        } else if (board[i][j] == 3) {
                            g.setColor(Configframe.getInstance().getColorCheckersBlack());
                            g.fillOval(offset + cellSize * (BOARD_SIZE - 1 - j) + 2, offset + cellSize * i + 2, cellSize - 4, cellSize - 4);
                            g.setColor(Configframe.getInstance().getColorChekersWhite());
                            g.drawOval(offset + cellSize * (BOARD_SIZE - 1 - j) + 2, offset + cellSize * i + 2, cellSize - 4, cellSize - 4);
                        } else if (board[i][j] == 4) {
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
    
    public void drawEndGame(Graphics2D g) {
        if (gameEnd){
            Font f = g.getFont();
            g.setColor(Color.RED);
            g.setFont(new Font("Verdana", Font.PLAIN, 60));
            g.drawString(gameEndWin, this.getWidth() / 2 - 190, this.getHeight() / 2 + 20);       
            g.setFont(f);
        }
    }

    public void moveStart() {
        if (!gameStart) {
            NetCheckers.getInstance().addToLog(" SYSTEM>>>Game has not started");
            NetCheckers.getInstance().addToLog(" SYSTEM>>>You must start server and wait for the client connection "); 
            NetCheckers.getInstance().addToLog("or connect to the server"); 
            return;
        }
        if (myColor != nowMove) {
            return;
        }
        move = false;
        int j = -1, i = -1, this_h;
        if (offset < x_move && x_move < offset + cellSize * BOARD_SIZE) {
            i = mouseXToBoardI(x_move);
        }
        if (offset < y_move && y_move < offset + cellSize * BOARD_SIZE) {
            j = mouseYToBoardJ(y_move);
        }
        if (i >= 0 && j >= 0) {
            this_h = board[j][i];
            if ((nowMove == WHITE && (this_h == 1 || this_h == 2))
                    || (nowMove == BLACK && (this_h == 3 || this_h == 4))) {
                move = true;
                i_where = i;
                j_where = j;
                i_end = -1;
                j_end = -1;
                now_hodit = this_h;
                board[j_where][i_where] = 0;
            }
        }
    }

    public void moveStop() {
        if (myColor != nowMove) {
            return;
        }
        if ((j_end >= 0 && i_end >= 0) && move) {
            board[j_where][i_where] = now_hodit;
            canEat();
            board[j_where][i_where] = 0;
            String send = "";
            if (mustEat) {
                String h = Integer.toString(j_where) + Integer.toString(i_where) + "-"
                        + Integer.toString(j_end) + Integer.toString(i_end);
                System.out.println("move " + h);
                System.out.println("eat " + mustEatString);
                StringTokenizer st = new StringTokenizer(mustEatString, ";");
                boolean dh = false;
                while (st.hasMoreTokens()) {
                    String s = st.nextToken();
                    if (s.equals(h)) {
                        board[j_end][i_end] = now_hodit;
                        board[j_where + (j_end - j_where) / 2][i_where + (i_end - i_where) / 2] = 0;
                        send = ("eat " + Integer.toString(i_where) + " " + Integer.toString(j_where) + " "
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
                    board[j_where][i_where] = now_hodit;
                } else {
                    mustEat = false;
                    mustEatString = "";
                    canEatXY(j_end, i_end);
                    if (!mustEat) {
                        nowMove = (nowMove == WHITE) ? BLACK : WHITE;
                        send = send + "\neatend " + eat_hod;
                        addToMoveHisotry(0, 0, 0, 0, eat_hod);
                        eat_hod = "";
                        alr_eat = false;
                        NetCheckers.getInstance().setWhiteMove(nowMove == WHITE);
                        checkKings();
                        repaint();
                    }
                    NetCheckers.getInstance().serverConnect.sendMessage(send);
                }
            } else if (canMove(i_where, j_where, j_end, i_end, now_hodit - nowMove)) {
                board[j_end][i_end] = now_hodit;
                checkKings();
                NetCheckers.getInstance().serverConnect.sendMessage("move "
                        + Integer.toString(i_where) + " " + Integer.toString(j_where) + " "
                        + Integer.toString(i_end) + " " + Integer.toString(j_end));
                nowMove = (nowMove == WHITE) ? BLACK : WHITE;
                NetCheckers.getInstance().setWhiteMove(nowMove == WHITE);
                addToMoveHisotry(i_where, j_where, i_end, j_end, "");
            } else {
                board[j_where][i_where] = now_hodit;
            }
        } else {
            if (move) {
                board[j_where][i_where] = now_hodit;
            }
        }
        move = false;
        
        if (!move && mustEat) NetCheckers.getInstance().addToLog(" SYSTEM>>>You must eat opponents checker!");
        
        repaint();
    }

    private int mouseXToBoardI(int mouseX) {
        if (rotate) {
            return BOARD_SIZE - 1 - (int) (mouseX - offset) / cellSize;
        } else {
            return (int) (mouseX - offset) / cellSize;
        }
    }

    private int mouseYToBoardJ(int mouseY) {
        if (rotate) {
            return (int) (mouseY - offset) / cellSize;
        } else {
            return BOARD_SIZE - 1 - (int) (mouseY - offset) / cellSize;
        }
    }

    public void addToMoveHisotry(int i_where, int j_where, int i_end, int j_end, String ss) {
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
                    NetCheckers.getInstance().listModel.lastElement() + "      " + s,
                    NetCheckers.getInstance().listModel.getSize() - 1);
            newline = true;
        }
        NetCheckers.getInstance().jScrollPane1.getVerticalScrollBar().setValue(
                NetCheckers.getInstance().jScrollPane1.getVerticalScrollBar().getMaximum());
    }

    public void initBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = 0;
            }
        }
        boolean b = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (b) {
                    board[i][j] = WHITE;
                }
                b = !b;
            }
            b = !b;
        }
        b = false;
        for (int i = BOARD_SIZE - 3; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (b) {
                    board[i][j] = BLACK;
                }
                b = !b;
            }
            b = !b;
        }
    }

    public boolean canMove(int i_where, int j_where, int j_end, int i_end, int type) {
        boolean rez;
        boolean usl1 = false, usl2 = false, usl3 = false;
        usl1 = board[j_end][i_end] == 0;
        boolean chet_i = (i_end % 2 == 0);
        boolean chet_j = (j_end % 2 == 0);
        usl2 = (chet_i && chet_j) || (!chet_i && !chet_j);

        if (type == 0) {
            usl3 = (Math.abs(-i_where + i_end) == 1) && ((nowMove == BLACK && (-j_where + j_end) == -1)
                    || (nowMove == WHITE && (-j_where + j_end) == 1));
        }
        if (type == 1) {
            usl3 = (Math.abs(i_where - i_end) == 1 && Math.abs(j_where - j_end) == 1);
        }
        //
        rez = usl1 && usl2 && usl3;
        return rez;
    }

    public void canEat() {
        mustEat = false;
        mustEatString = "";
        boolean b = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (b) {
                    canEatXY(i, j);
                }
                b = !b;
            }
            b = !b;
        }
    }

    public void canEatXY(int i, int j) {
        if (board[i][j] == nowMove) {
            if (nowMove == WHITE && i < BOARD_SIZE - 2 && j < BOARD_SIZE - 2) {
                if ((board[i + 1][j + 1] == BLACK || board[i + 1][j + 1] == BLACK + 1) && board[i + 2][j + 2] == 0) {
                    mustEat = true;
                    mustEatString = mustEatString + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i + 2) + Integer.toString(j + 2) + ";";
                }
            }

            if (nowMove == WHITE && i < BOARD_SIZE - 2 && j > 1) {
                if ((board[i + 1][j - 1] == BLACK || board[i + 1][j - 1] == BLACK + 1) && board[i + 2][j - 2] == 0) {
                    mustEat = true;
                    mustEatString = mustEatString + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i + 2) + Integer.toString(j - 2) + ";";
                }
            }

            if (nowMove == BLACK && i > 1 && j < BOARD_SIZE - 2) {
                if ((board[i - 1][j + 1] == WHITE || board[i - 1][j + 1] == WHITE + 1) && board[i - 2][j + 2] == 0) {
                    mustEat = true;
                    mustEatString = mustEatString + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i - 2) + Integer.toString(j + 2) + ";";
                }
            }

            if (nowMove == BLACK && i > 1 && j > 1) {
                if ((board[i - 1][j - 1] == WHITE || board[i - 1][j - 1] == WHITE + 1) && board[i - 2][j - 2] == 0) {
                    mustEat = true;
                    mustEatString = mustEatString + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i - 2) + Integer.toString(j - 2) + ";";
                }
            }
        }
        if (board[i][j] == (nowMove + 1)) {
            if (nowMove == WHITE && i < BOARD_SIZE - 2 && j < BOARD_SIZE - 2) {
                if ((board[i + 1][j + 1] == BLACK || board[i + 1][j + 1] == BLACK + 1) && board[i + 2][j + 2] == 0) {
                    mustEat = true;
                    mustEatString = mustEatString + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i + 2) + Integer.toString(j + 2) + ";";
                }
            }

            if (nowMove == WHITE && i < BOARD_SIZE - 2 && j > 1) {
                if ((board[i + 1][j - 1] == BLACK || board[i + 1][j - 1] == BLACK + 1) && board[i + 2][j - 2] == 0) {
                    mustEat = true;
                    mustEatString = mustEatString + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i + 2) + Integer.toString(j - 2) + ";";
                }
            }

            if (nowMove == WHITE && i > 1 && j > 1) {
                if ((board[i - 1][j - 1] == BLACK || board[i - 1][j - 1] == BLACK + 1) && board[i - 2][j - 2] == 0) {
                    mustEat = true;
                    mustEatString = mustEatString + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i - 2) + Integer.toString(j - 2) + ";";
                }
            }

            if (nowMove == WHITE && i > 1 && j < BOARD_SIZE - 2) {
                if ((board[i - 1][j + 1] == BLACK || board[i - 1][j + 1] == BLACK + 1) && board[i - 2][j + 2] == 0) {
                    mustEat = true;
                    mustEatString = mustEatString + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i - 2) + Integer.toString(j + 2) + ";";
                }
            }

            if (nowMove == BLACK && i < BOARD_SIZE - 2 && j < BOARD_SIZE - 2) {
                if ((board[i + 1][j + 1] == WHITE || board[i + 1][j + 1] == WHITE + 1) && board[i + 2][j + 2] == 0) {
                    mustEat = true;
                    mustEatString = mustEatString + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i + 2) + Integer.toString(j + 2) + ";";
                }
            }

            if (nowMove == BLACK && i < BOARD_SIZE - 2 && j > 1) {
                if ((board[i + 1][j - 1] == WHITE || board[i + 1][j - 1] == WHITE + 1) && board[i + 2][j - 2] == 0) {
                    mustEat = true;
                    mustEatString = mustEatString + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i + 2) + Integer.toString(j - 2) + ";";
                }
            }

            if (nowMove == BLACK && i > 1 && j > 1) {
                if ((board[i - 1][j - 1] == WHITE || board[i - 1][j - 1] == WHITE + 1) && board[i - 2][j - 2] == 0) {
                    mustEat = true;
                    mustEatString = mustEatString + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i - 2) + Integer.toString(j - 2) + ";";
                }
            }

            if (nowMove == BLACK && i > 1 && j < BOARD_SIZE - 2) {
                if ((board[i - 1][j + 1] == WHITE || board[i - 1][j + 1] == WHITE + 1) && board[i - 2][j + 2] == 0) {
                    mustEat = true;
                    mustEatString = mustEatString + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i - 2) + Integer.toString(j + 2) + ";";
                }
            }

        }
    }

    public void checkKings() {
        if (board[BOARD_SIZE - 1][1] == 1) {
            board[BOARD_SIZE - 1][1] = 2;
        }
        if (board[BOARD_SIZE - 1][3] == 1) {
            board[BOARD_SIZE - 1][3] = 2;
        }
        if (board[BOARD_SIZE - 1][5] == 1) {
            board[BOARD_SIZE - 1][5] = 2;
        }
        if (board[BOARD_SIZE - 1][7] == 1) {
            board[BOARD_SIZE - 1][7] = 2;
        }
        if (board[0][0] == 3) {
            board[0][0] = 4;
        }
        if (board[0][2] == 3) {
            board[0][2] = 4;
        }
        if (board[0][4] == 3) {
            board[0][4] = 4;
        }
        if (board[0][6] == 3) {
            board[0][6] = 4;
        }
    }

    public int isGameEnd() {
        boolean w = false;
        boolean b = false;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == WHITE || board[i][j] == WHITE_KING) {
                    w = true;
                }
                if (board[i][j] == BLACK || board[i][j] == BLACK_KING) {
                    b = true;
                }
                if (b && w) {
                    return 0;
                }
            }
        }
        if (b && !w) {
            return BLACK;
        }
        if (!b && w) {
            return WHITE;
        }
        return 0;
    }
}
