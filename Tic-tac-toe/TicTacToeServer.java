import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class TicTacToeServer {
    private static final int PORT = 8888;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private Map<String, Player> waitingPlayers;
    private Map<String, GameSession> activeGames;

    public TicTacToeServer() {
        threadPool = Executors.newCachedThreadPool();
        waitingPlayers = new ConcurrentHashMap<>();
        activeGames = new ConcurrentHashMap<>();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер крестики-нолики запущен на порту " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;
        private Player player;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                String registration = in.readLine();
                if (registration == null || !registration.startsWith("REGISTER:")) {
                    out.println("ERROR:Неверная регистрация");
                    return;
                }

                String playerName = registration.substring(9);
                player = new Player(playerName, socket, out);

                out.println("WELCOME:" + playerName + "," + "none");
                System.out.println("Игрок зарегистрирован: " + playerName);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Получено от " + playerName + ": " + inputLine);
                    processCommand(inputLine);
                }

            } catch (IOException e) {
                System.err.println("Ошибка обработки клиента: " + e.getMessage());
            } finally {
                disconnectPlayer();
            }
        }

        private void processCommand(String command) {
            if (command.startsWith("FIND_GAME")) {
                findGame();
            } else if (command.startsWith("MOVE:")) {
                processMove(command);
            } else if (command.startsWith("EXIT")) {
                disconnectPlayer();
            }
        }

        private void findGame() {
            if (player.isInGame()) {
                player.sendMessage("ERROR:Вы уже в игре");
                return;
            }

            player.sendMessage("WAITING");

            synchronized (waitingPlayers) {
                if (!waitingPlayers.isEmpty()) {
                    String opponentName = waitingPlayers.keySet().iterator().next();
                    Player opponent = waitingPlayers.remove(opponentName);

                    GameSession game = new GameSession(player, opponent);
                    activeGames.put(game.getId(), game);

                    player.sendMessage("GAME_STARTED:" + opponent.getName() + ",X");
                    opponent.sendMessage("GAME_STARTED:" + player.getName() + ",O");

                    player.sendMessage("YOUR_TURN");
                    opponent.sendMessage("OPPONENT_TURN:" + player.getName());

                    System.out.println("Игра началась: " + player.getName() + " vs " + opponent.getName());
                } else {
                    waitingPlayers.put(player.getName(), player);
                    System.out.println("Игрок " + player.getName() + " ожидает соперника");
                }
            }
        }

        private void processMove(String moveCommand) {
            if (!player.isInGame()) {
                player.sendMessage("ERROR:Вы не в игре");
                return;
            }

            GameSession game = player.getGameSession();
            if (game == null) {
                player.sendMessage("ERROR:Игровая сессия не найдена");
                return;
            }

            try {
                String[] parts = moveCommand.substring(5).split(",");
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);

                if (game.makeMove(player, row, col)) {
                    // Отправка подтверждения хода
                    player.sendMessage("VALID_MOVE:" + row + "," + col + "," + player.getSymbol());

                    // Отправка обновления доски обоим игрокам
                    String boardState = game.getBoard().getBoardString();
                    player.sendMessage("GAME_UPDATE:" + boardState);
                    game.getOpponent(player).sendMessage("GAME_UPDATE:" + boardState);

                    // Проверка окончания игры
                    char winner = game.checkWinner();
                    if (winner != '-') {
                        Player winnerPlayer = (winner == 'X') ? game.getPlayerX() : game.getPlayerO();
                        winnerPlayer.sendMessage("WINNER:" + winnerPlayer.getName());
                        game.getOpponent(winnerPlayer).sendMessage("WINNER:" + winnerPlayer.getName());
                        endGame(game);
                    } else if (game.isDraw()) {
                        game.getPlayerX().sendMessage("DRAW");
                        game.getPlayerO().sendMessage("DRAW");
                        endGame(game);
                    } else {
                        // Продолжение игры
                        Player nextPlayer = game.getCurrentPlayer();
                        nextPlayer.sendMessage("YOUR_TURN");
                        game.getOpponent(nextPlayer).sendMessage("OPPONENT_TURN:" + nextPlayer.getName());
                    }
                } else {
                    player.sendMessage("INVALID_MOVE:Неверный ход");
                }

            } catch (Exception e) {
                player.sendMessage("ERROR:Неверный формат хода");
            }
        }

        private void endGame(GameSession game) {
            // Удаляем игру из активных
            activeGames.remove(game.getId());

            // Сбрасываем статус игроков
            game.getPlayerX().setGameSession(null);
            game.getPlayerO().setGameSession(null);
        }

        private void disconnectPlayer() {
            if (player != null) {
                // Удаляем из очереди ожидания
                waitingPlayers.remove(player.getName());

                // Если игрок был в игре, уведомляем соперника
                if (player.isInGame()) {
                    GameSession game = player.getGameSession();
                    if (game != null) {
                        Player opponent = game.getOpponent(player);
                        if (opponent != null) {
                            opponent.sendMessage("OPPONENT_LEFT");
                            opponent.setGameSession(null);
                        }
                        activeGames.remove(game.getId());
                    }
                }

                System.out.println("Игрок отключен: " + player.getName());
            }

            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    public static void main(String[] args) {
        TicTacToeServer server = new TicTacToeServer();
        server.start();
    }
}