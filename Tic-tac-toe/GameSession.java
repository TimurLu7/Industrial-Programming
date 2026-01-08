public class GameSession {
    private Player playerX;
    private Player playerO;
    private GameBoard board;
    private Player currentPlayer;
    private boolean gameActive;
    private String id;

    public GameSession(Player player1, Player player2) {
        this.playerX = player1;
        this.playerO = player2;
        playerX.setSymbol('X');
        playerO.setSymbol('O');
        this.board = new GameBoard();
        this.currentPlayer = playerX;
        this.gameActive = true;
        this.id = java.util.UUID.randomUUID().toString();

        playerX.setGameSession(this);
        playerO.setGameSession(this);
    }

    public boolean makeMove(Player player, int row, int col) {
        if (!gameActive || player != currentPlayer) {
            return false;
        }

        char symbol = player.getSymbol();
        if (board.makeMove(row, col, symbol)) {
            char winner = board.checkWinner();
            if (winner != '-') {
                gameActive = false;
                return true;
            }

            if (board.isFull()) {
                gameActive = false;
                return true;
            }

            currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
            return true;
        }

        return false;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getOpponent(Player player) {
        return (player == playerX) ? playerO : playerX;
    }

    public Player getPlayerX() {
        return playerX;
    }

    public Player getPlayerO() {
        return playerO;
    }

    public GameBoard getBoard() {
        return board;
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public String getId() {
        return id;
    }

    public char checkWinner() {
        return board.checkWinner();
    }

    public boolean isDraw() {
        return board.isFull() && board.checkWinner() == '-';
    }

    public void resetGame() {
        this.board = new GameBoard();
        this.currentPlayer = playerX;
        this.gameActive = true;
    }
}