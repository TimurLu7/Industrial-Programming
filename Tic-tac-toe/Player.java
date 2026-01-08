import java.io.PrintWriter;
import java.net.Socket;

public class Player {
    private String name;
    private char symbol;
    private Socket socket;
    private PrintWriter out;
    private GameSession gameSession;
    private boolean inGame;

    public Player(String name, Socket socket, PrintWriter out) {
        this.name = name;
        this.socket = socket;
        this.out = out;
        this.inGame = false;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public Socket getSocket() {
        return socket;
    }

    public PrintWriter getWriter() {
        return out;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
        this.inGame = (gameSession != null);
    }

    public boolean isInGame() {
        return inGame;
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}