import java.util.*;
import java.time.LocalDateTime;

public class RailwayTicketSystem {
    private FileConnector fileConnector;
    private List<Train> trains;
    private List<Request> requests;
    private List<Bill> bills;
    private Scanner scanner;

    public RailwayTicketSystem() {
        this.fileConnector = new FileConnector();
        this.scanner = new Scanner(System.in);
        loadData();
    }

    private void loadData() {
        try {
            DataStorage data = fileConnector.loadAllData();
            this.trains = data.trains;
            this.requests = data.requests;
            this.bills = data.bills;
            System.out.println("Данные успешно загружены!");
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке данных: " + e.getMessage());
            this.trains = new ArrayList<>();
            this.requests = new ArrayList<>();
            this.bills = new ArrayList<>();
        }
    }

    private void saveData() {
        try {
            fileConnector.saveAllData(trains, requests, bills);
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    public void createRequest() {
        try {
            System.out.println("\n=== СОЗДАНИЕ ЗАЯВКИ ===");
            System.out.print("Введите ваше имя: ");
            String name = scanner.nextLine();
            System.out.print("Введите email: ");
            String email = scanner.nextLine();
            System.out.print("Введите станцию назначения: ");
            String destination = scanner.nextLine();
            System.out.print("Введите дату и время (гггг-мм-дд чч:мм): ");
            String dateTimeStr = scanner.nextLine();

            LocalDateTime desiredTime = LocalDateTime.parse(dateTimeStr.replace(" ", "T"));
            Passenger passenger = new Passenger(name, email);
            Request request = new Request(passenger, destination, desiredTime);

            requests.add(request);
            System.out.println("Заявка создана: " + request);

            findTrainsForRequest(request);

        } catch (Exception e) {
            System.err.println("Ошибка при создании заявки: " + e.getMessage());
        }
    }

    private void findTrainsForRequest(Request request) {
        System.out.println("\n=== ПОДХОДЯЩИЕ ПОЕЗДА ===");
        List<Train> suitableTrains = new ArrayList<>();

        for (Train train : trains) {
            if (train.getDestination().equalsIgnoreCase(request.getDestination()) &&
                    train.getDepartureTime().isAfter(request.getDesiredTime().minusHours(2)) &&
                    train.getDepartureTime().isBefore(request.getDesiredTime().plusHours(4)) &&
                    train.getAvailableSeats() > 0) {
                suitableTrains.add(train);
            }
        }

        if (suitableTrains.isEmpty()) {
            System.out.println("К сожалению, подходящих поездов не найдено.");
            return;
        }

        for (int i = 0; i < suitableTrains.size(); i++) {
            System.out.println((i + 1) + ". " + suitableTrains.get(i));
        }

        System.out.print("Выберите поезд (номер): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= suitableTrains.size()) {
                Train selectedTrain = suitableTrains.get(choice - 1);
                createBill(request, selectedTrain);
            } else {
                System.out.println("Неверный выбор.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Ошибка: введите число.");
        }
    }

    private void createBill(Request request, Train train) {
        Bill bill = new Bill(request, train);
        bills.add(bill);
        request.setStatus("СЧЕТ ВЫСТАВЛЕН");

        System.out.println("\n=== ВАШ СЧЕТ ===");
        System.out.println(bill);

        System.out.print("Оплатить счет? (да/нет): ");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("да")) {
            bill.markAsPaid();
            System.out.println("Оплата прошла успешно! Билет приобретен.");
        }
    }

    public void addTrain() {
        try {
            System.out.println("\n=== ДОБАВЛЕНИЕ ПОЕЗДА ===");
            System.out.print("Номер поезда: ");
            String number = scanner.nextLine();
            System.out.print("Станция назначения: ");
            String destination = scanner.nextLine();
            System.out.print("Промежуточные станции (через запятую): ");
            String stationsStr = scanner.nextLine();
            System.out.print("Дата и время отправления (гггг-мм-дд чч:мм): ");
            String dateTimeStr = scanner.nextLine();
            System.out.print("Цена билета: ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Количество мест: ");
            int seats = Integer.parseInt(scanner.nextLine());

            List<String> stations = Arrays.asList(stationsStr.split(","));
            LocalDateTime departureTime = LocalDateTime.parse(dateTimeStr.replace(" ", "T"));

            Train train = new Train(number, destination, stations, departureTime, price, seats);
            trains.add(train);

            System.out.println("Поезд добавлен: " + train);

        } catch (Exception e) {
            System.err.println("Ошибка при добавлении поезда: " + e.getMessage());
        }
    }

    public void viewAllData() {
        System.out.println("\n=== ВСЕ ПОЕЗДА ===");
        for (Train train : trains) {
            System.out.println(train);
        }

        System.out.println("\n=== ВСЕ ЗАЯВКИ ===");
        for (Request request : requests) {
            System.out.println(request);
        }

        System.out.println("\n=== ВСЕ СЧЕТА ===");
        for (Bill bill : bills) {
            System.out.println(bill);
        }
    }

    public void clearAllData() {
        try {
            System.out.print("\nВы уверены? (введите 'Удалить' для подтверждения): ");
            String confirmation = scanner.nextLine();

            if ("Удалить".equals(confirmation)) {
                trains.clear();
                requests.clear();
                bills.clear();

                clearDataFiles();

                System.out.println("Все данные успешно удалены!");
            }
        } catch (Exception e) {
            System.err.println("Ошибка при очистке данных: " + e.getMessage());
        }
    }

    private void clearDataFiles() {
        try {
            fileConnector.saveToFile(new ArrayList<Train>(), "trains.ser");
            fileConnector.saveToFile(new ArrayList<Request>(), "requests.ser");
            fileConnector.saveToFile(new ArrayList<Bill>(), "bills.ser");
        } catch (Exception e) {
            System.err.println("Ошибка при очистке файлов: " + e.getMessage());
        }
    }
    public void Menu() {
        while (true) {
            System.out.println("\n=== ЖЕЛЕЗНОДОРОЖНАЯ КАССА ===");
            System.out.println("1. Новая заявка (пассажир)");
            System.out.println("2. Добавить поезд (администратор)");
            System.out.println("3. Просмотреть все данные");
            System.out.println("4. Сохранить данные");
            System.out.println("5. Очистить все данные");
            System.out.println("6. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createRequest();
                    break;
                case "2":
                    addTrain();
                    break;
                case "3":
                    viewAllData();
                    break;
                case "4":
                    saveData();
                    break;
                case "5":
                    clearAllData();
                    break;
                case "6":
                    saveData();
                    System.out.println("До свидания!");
                    return;
                default:
                    System.out.println("Неверный выбор.");
            }
        }
    }

    public static void main(String[] args) {
        RailwayTicketSystem system = new RailwayTicketSystem();
        system.Menu();
    }
}