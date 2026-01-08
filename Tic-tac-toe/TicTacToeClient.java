import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class TicTacToeClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String playerName;
    private char playerSymbol;
    private String opponentName;

    private JFrame frame;
    private JButton[][] buttons;
    private JTextArea textArea;
    private JPanel gamePanel;
    private JPanel controlPanel;
    private JButton findGameButton;
    private JButton exitButton;
    private JLabel statusLabel;
    private JLabel playerInfoLabel;

    private boolean myTurn;
    private boolean gameActive;

    public TicTacToeClient() {
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Крестики-Нолики");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 700);
        frame.setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        playerInfoLabel = new JLabel("Игрок: ");
        infoPanel.add(playerInfoLabel);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusLabel = new JLabel("Подключитесь к серверу");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusPanel.add(statusLabel);

        gamePanel = new JPanel(new GridLayout(3, 3, 5, 5));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gamePanel.setBackground(Color.DARK_GRAY);
        buttons = new JButton[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 80));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(e -> makeMove(row, col));
                buttons[i][j].setEnabled(false);
                gamePanel.add(buttons[i][j]);
            }
        }

        textArea = new JTextArea(5, 40);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Лог игры"));

        controlPanel = new JPanel(new FlowLayout());

        findGameButton = new JButton("Найти игру");
        findGameButton.setFont(new Font("Arial", Font.PLAIN, 14));
        findGameButton.addActionListener(e -> findGame());

        exitButton = new JButton("Выход");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
        exitButton.addActionListener(e -> disconnect());

        controlPanel.add(findGameButton);
        controlPanel.add(exitButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(infoPanel, BorderLayout.WEST);
        topPanel.add(statusPanel, BorderLayout.CENTER);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.SOUTH);
        frame.add(controlPanel, BorderLayout.AFTER_LAST_LINE);

        frame.setVisible(true);

        playerName = JOptionPane.showInputDialog(frame, "Введите ваше имя:", "Регистрация",
                JOptionPane.PLAIN_MESSAGE);
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Игрок_" + System.currentTimeMillis() % 1000;
        }

        playerInfoLabel.setText("Игрок: " + playerName);

        connectToServer();
    }

    private void connectToServer() {
        try {
            String serverAddress = JOptionPane.showInputDialog(frame,
                    "Введите адрес сервера:", "localhost");
            if (serverAddress == null || serverAddress.trim().isEmpty()) {
                serverAddress = "localhost";
            }

            socket = new Socket(serverAddress, 8888);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("REGISTER:" + playerName);

            new Thread(this::receiveMessages).start();

            updateStatus("Подключено к серверу", Color.BLUE);
            logMessage("Подключено к серверу " + serverAddress);

        } catch (IOException e) {
            updateStatus("Ошибка подключения", Color.RED);
            JOptionPane.showMessageDialog(frame, "Ошибка подключения к серверу: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                processServerMessage(message);
            }
        } catch (IOException e) {
            SwingUtilities.invokeLater(() -> {
                updateStatus("Соединение потеряно", Color.RED);
                logMessage("Соединение с сервером потеряно");
            });
        }
    }

    private void processServerMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            logMessage("Сервер: " + message);

            if (message.startsWith("WELCOME:")) {
                String[] parts = message.substring(8).split(",");
                playerName = parts[0];
                playerInfoLabel.setText("Игрок: " + playerName);
                frame.setTitle("Крестики-Нолики - " + playerName);

            } else if (message.startsWith("WAITING")) {
                updateStatus("Ожидание соперника...", Color.ORANGE);
                clearBoard();

            } else if (message.startsWith("GAME_STARTED:")) {
                String[] parts = message.substring(13).split(",");
                opponentName = parts[0];
                playerSymbol = parts[1].charAt(0);
                gameActive = true;

                updateStatus("Игра началась!", Color.GREEN);
                playerInfoLabel.setText("Игрок: " + playerName + " (" + playerSymbol + ") vs " + opponentName);

                logMessage("Игра началась! Ваш соперник: " + opponentName);
                logMessage("Ваш символ: " + playerSymbol);

                enableGameBoard();
                findGameButton.setEnabled(false);

            } else if (message.startsWith("YOUR_TURN")) {
                myTurn = true;
                updateStatus("ВАШ ХОД!", Color.GREEN);
                enableGameBoard();

            } else if (message.startsWith("OPPONENT_TURN:")) {
                myTurn = false;
                String oppName = message.substring(14);
                updateStatus("Ход соперника: " + oppName, Color.BLUE);
                disableGameBoard();

            } else if (message.startsWith("VALID_MOVE:")) {
                String[] parts = message.substring(11).split(",");
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                char symbol = parts[2].charAt(0);

                updateBoardCell(row, col, symbol);

            } else if (message.startsWith("GAME_UPDATE:")) {
                String boardState = message.substring(12);
                updateBoardFromString(boardState);

            } else if (message.startsWith("WINNER:")) {
                String winner = message.substring(7);
                gameActive = false;
                disableGameBoard();

                if (winner.equals(playerName)) {
                    updateStatus("ВЫ ПОБЕДИЛИ! 🎉", new Color(0, 150, 0));
                    highlightWinningCells();
                    JOptionPane.showMessageDialog(frame, "Поздравляем! Вы победили!",
                            "Победа!", JOptionPane.INFORMATION_MESSAGE);
                } else if (winner.equals(opponentName)) {
                    updateStatus("Вы проиграли", Color.RED);
                    JOptionPane.showMessageDialog(frame, "Соперник победил.",
                            "Поражение", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    updateStatus("Победитель: " + winner, Color.BLUE);
                }

            } else if (message.startsWith("DRAW")) {
                gameActive = false;
                disableGameBoard();
                updateStatus("НИЧЬЯ!", Color.ORANGE);
                JOptionPane.showMessageDialog(frame, "Ничья!",
                        "Результат", JOptionPane.INFORMATION_MESSAGE);

            } else if (message.startsWith("OPPONENT_LEFT")) {
                gameActive = false;
                disableGameBoard();
                updateStatus("Соперник покинул игру", Color.RED);
                logMessage("Соперник покинул игру");
                JOptionPane.showMessageDialog(frame, "Соперник покинул игру",
                        "Информация", JOptionPane.INFORMATION_MESSAGE);
                findGameButton.setEnabled(true);
                clearBoard();

            }

            else if (message.startsWith("ERROR:")) {
                String error = message.substring(6);
                updateStatus("Ошибка: " + error, Color.RED);
                JOptionPane.showMessageDialog(frame, error, "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void updateBoardCell(int row, int col, char symbol) {
        if (row >= 0 && row < 3 && col >= 0 && col < 3) {
            buttons[row][col].setText(String.valueOf(symbol));

            if (symbol == 'X') {
                buttons[row][col].setForeground(Color.RED);
                buttons[row][col].setBackground(new Color(255, 230, 230)); // светлый красный фон
            } else if (symbol == 'O') {
                buttons[row][col].setForeground(Color.BLUE);
                buttons[row][col].setBackground(new Color(230, 230, 255)); // светлый синий фон
            }

            buttons[row][col].setEnabled(false);
        }
    }

    private void updateBoardFromString(String boardString) {
        String[] rows = boardString.split(";");
        for (int i = 0; i < 3; i++) {
            String[] cols = rows[i].split(",");
            for (int j = 0; j < 3; j++) {
                char symbol = cols[j].charAt(0);
                if (symbol != '-') {
                    updateBoardCell(i, j, symbol);
                }
            }
        }
    }

    private void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setForeground(Color.BLACK);
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void highlightWinningCells() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!buttons[i][j].getText().isEmpty()) {
                    buttons[i][j].setBackground(new Color(255, 255, 200)); // желтый фон
                }
            }
        }
    }

    private void findGame() {
        out.println("FIND_GAME");
        findGameButton.setEnabled(false);
        clearBoard();
    }

    private void makeMove(int row, int col) {
        if (!myTurn || !gameActive || !buttons[row][col].getText().isEmpty()) {
            return;
        }

        out.println("MOVE:" + row + "," + col);

        buttons[row][col].setText(String.valueOf(playerSymbol));
        if (playerSymbol == 'X') {
            buttons[row][col].setForeground(Color.RED);
        } else {
            buttons[row][col].setForeground(Color.BLUE);
        }
        buttons[row][col].setEnabled(false);
    }

    private void enableGameBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(myTurn && buttons[i][j].getText().isEmpty());
            }
        }
    }

    private void disableGameBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void updateStatus(String text, Color color) {
        statusLabel.setText(text);
        statusLabel.setForeground(color);
    }

    private void logMessage(String message) {
        String timestamp = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
        textArea.append("[" + timestamp + "] " + message + "\n");
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }

    private void disconnect() {
        try {
            if (out != null) {
                out.println("EXIT");
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new TicTacToeClient());
    }
}