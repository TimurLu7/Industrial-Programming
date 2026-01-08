import java.io.Serializable;

public class GameBoard implements Serializable {
    private static final long serialVersionUID = 1L;

    private char[][] board;
    private static final int SIZE = 3;

    public GameBoard() {
        board = new char[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = '-';
            }
        }
    }

    public boolean makeMove(int row, int col, char symbol) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || board[row][col] != '-') {
            return false;
        }
        board[row][col] = symbol;
        return true;
    }

    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == '-') {
                    return false;
                }
            }
        }
        return true;
    }

    public char checkWinner() {
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] != '-' && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                return board[i][0];
            }
        }

        for (int i = 0; i < SIZE; i++) {
            if (board[0][i] != '-' && board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
                return board[0][i];
            }
        }

        if (board[0][0] != '-' && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            return board[0][0];
        }
        if (board[0][2] != '-' && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            return board[0][2];
        }

        return '-';
    }

    public String getBoardString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sb.append(board[i][j]);
                if (j < SIZE - 1) sb.append(",");
            }
            if (i < SIZE - 1) sb.append(";");
        }
        return sb.toString();
    }

    public void setBoardFromString(String boardString) {
        String[] rows = boardString.split(";");
        for (int i = 0; i < SIZE; i++) {
            String[] cols = rows[i].split(",");
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = cols[j].charAt(0);
            }
        }
    }

    public char[][] getBoard() {
        return board;
    }
}