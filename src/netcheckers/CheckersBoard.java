package netcheckers;

public class CheckersBoard {

    private static CheckersBoard instance;
    public static final int WHITE = 1;
    public static final int WHITE_KING = 2;
    public static final int BLACK = 3;
    public static final int BLACK_KING = 4;
    public static final int BOARD_SIZE = 8;
    
    public int nowMove = WHITE;
    public int myColor = WHITE;
    public int[][] board = new int[8][8];
    
    public boolean must_eat = false;
    public String must_eat_s = "";
    public char abc[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    private CheckersBoard() {
        initBoard();
    }

    public static CheckersBoard getInstance() {
        if (instance == null) {
            instance = new CheckersBoard();
        }
        return instance;
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

    public void canEat() {
        must_eat = false;
        must_eat_s = "";
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
                    must_eat = true;
                    must_eat_s = must_eat_s + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i + 2) + Integer.toString(j + 2) + ";";
                }
            }

            if (nowMove == WHITE && i < BOARD_SIZE - 2 && j > 1) {
                if ((board[i + 1][j - 1] == BLACK || board[i + 1][j - 1] == BLACK + 1) && board[i + 2][j - 2] == 0) {
                    must_eat = true;
                    must_eat_s = must_eat_s + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i + 2) + Integer.toString(j - 2) + ";";
                }
            }

            if (nowMove == BLACK && i > 1 && j < BOARD_SIZE - 2) {
                if ((board[i - 1][j + 1] == WHITE || board[i - 1][j + 1] == WHITE + 1) && board[i - 2][j + 2] == 0) {
                    must_eat = true;
                    must_eat_s = must_eat_s + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i - 2) + Integer.toString(j + 2) + ";";
                }
            }

            if (nowMove == BLACK && i > 1 && j > 1) {
                if ((board[i - 1][j - 1] == WHITE || board[i - 1][j - 1] == WHITE + 1) && board[i - 2][j - 2] == 0) {
                    must_eat = true;
                    must_eat_s = must_eat_s + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i - 2) + Integer.toString(j - 2) + ";";
                }
            }
        }
        if (board[i][j] == (nowMove + 1)) {
            if (nowMove == WHITE && i < BOARD_SIZE - 2 && j < BOARD_SIZE - 2) {
                if ((board[i + 1][j + 1] == BLACK || board[i + 1][j + 1] == BLACK + 1) && board[i + 2][j + 2] == 0) {
                    must_eat = true;
                    must_eat_s = must_eat_s + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i + 2) + Integer.toString(j + 2) + ";";
                }
            }

            if (nowMove == WHITE && i < BOARD_SIZE - 2 && j > 1) {
                if ((board[i + 1][j - 1] == BLACK || board[i + 1][j - 1] == BLACK + 1) && board[i + 2][j - 2] == 0) {
                    must_eat = true;
                    must_eat_s = must_eat_s + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i + 2) + Integer.toString(j - 2) + ";";
                }
            }

            if (nowMove == WHITE && i > 1 && j > 1) {
                if ((board[i - 1][j - 1] == BLACK || board[i - 1][j - 1] == BLACK + 1) && board[i - 2][j - 2] == 0) {
                    must_eat = true;
                    must_eat_s = must_eat_s + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i - 2) + Integer.toString(j - 2) + ";";
                }
            }

            if (nowMove == WHITE && i > 1 && j < BOARD_SIZE - 2) {
                if ((board[i - 1][j + 1] == BLACK || board[i - 1][j + 1] == BLACK + 1) && board[i - 2][j + 2] == 0) {
                    must_eat = true;
                    must_eat_s = must_eat_s + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i - 2) + Integer.toString(j + 2) + ";";
                }
            }

            if (nowMove == BLACK && i < BOARD_SIZE - 2 && j < BOARD_SIZE - 2) {
                if ((board[i + 1][j + 1] == WHITE || board[i + 1][j + 1] == WHITE + 1) && board[i + 2][j + 2] == 0) {
                    must_eat = true;
                    must_eat_s = must_eat_s + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i + 2) + Integer.toString(j + 2) + ";";
                }
            }

            if (nowMove == BLACK && i < BOARD_SIZE - 2 && j > 1) {
                if ((board[i + 1][j - 1] == WHITE || board[i + 1][j - 1] == WHITE + 1) && board[i + 2][j - 2] == 0) {
                    must_eat = true;
                    must_eat_s = must_eat_s + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i + 2) + Integer.toString(j - 2) + ";";
                }
            }

            if (nowMove == BLACK && i > 1 && j > 1) {
                if ((board[i - 1][j - 1] == WHITE || board[i - 1][j - 1] == WHITE + 1) && board[i - 2][j - 2] == 0) {
                    must_eat = true;
                    must_eat_s = must_eat_s + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i - 2) + Integer.toString(j - 2) + ";";
                }
            }

            if (nowMove == BLACK && i > 1 && j < BOARD_SIZE - 2) {
                if ((board[i - 1][j + 1] == WHITE || board[i - 1][j + 1] == WHITE + 1) && board[i - 2][j + 2] == 0) {
                    must_eat = true;
                    must_eat_s = must_eat_s + Integer.toString(i) + Integer.toString(j) + "-"
                            + Integer.toString(i - 2) + Integer.toString(j + 2) + ";";
                }
            }

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
                if (b && w) return 0;
            }
        }
        if (b && !w) {
            return CheckersBoard.BLACK;
        }
        if (!b && w) {
            return CheckersBoard.WHITE;
        }
        return 0;
    }
}
