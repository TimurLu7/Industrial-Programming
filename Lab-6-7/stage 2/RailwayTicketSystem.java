import java.util.*;
import java.time.LocalDateTime;
import java.text.MessageFormat;

public class RailwayTicketSystem {
    private FileConnector fileConnector;
    private List<Train> trains;
    private List<Request> requests;
    private List<Bill> bills;
    private Scanner scanner;
    private Translator resources;

    public RailwayTicketSystem(String language, String country) {
        Translator.initialize(language, country);
        this.resources = Translator.getInstance();
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
            System.out.println(resources.getString("data.loaded.success"));
        } catch (Exception e) {
            System.err.println(MessageFormat.format(resources.getString("data.loaded.error"), e.getMessage()));
            this.trains = new ArrayList<>();
            this.requests = new ArrayList<>();
            this.bills = new ArrayList<>();
        }
    }

    private void saveData() {
        try {
            fileConnector.saveAllData(trains, requests, bills);
        } catch (Exception e) {
            System.err.println(MessageFormat.format(resources.getString("data.save.error"), e.getMessage()));
        }
    }

    public void createRequest() {
        try {
            System.out.println("\n" + resources.getString("request.creation.title"));
            System.out.print(resources.getString("enter.name") + " ");
            String name = scanner.nextLine();
            System.out.print(resources.getString("enter.email") + " ");
            String email = scanner.nextLine();
            System.out.print(resources.getString("enter.destination") + " ");
            String destination = scanner.nextLine();
            System.out.print(resources.getString("enter.datetime") + " ");
            String dateTimeStr = scanner.nextLine();

            LocalDateTime desiredTime = LocalDateTime.parse(dateTimeStr.replace(" ", "T"));
            Passenger passenger = new Passenger(name, email);
            Request request = new Request(passenger, destination, desiredTime);

            requests.add(request);
            System.out.println(MessageFormat.format(resources.getString("request.created"), request));

            findTrainsForRequest(request);

        } catch (Exception e) {
            System.err.println(MessageFormat.format(resources.getString("request.creation.error"), e.getMessage()));
        }
    }

    private void findTrainsForRequest(Request request) {
        System.out.println("\n" + resources.getString("suitable.trains.title"));
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
            System.out.println(resources.getString("no.trains.found"));
            return;
        }

        for (int i = 0; i < suitableTrains.size(); i++) {
            System.out.println((i + 1) + ". " + suitableTrains.get(i));
        }

        System.out.print(resources.getString("choose.train") + " ");
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice > 0 && choice <= suitableTrains.size()) {
                Train selectedTrain = suitableTrains.get(choice - 1);
                createBill(request, selectedTrain);
            } else {
                System.out.println(resources.getString("invalid.choice"));
            }
        } catch (NumberFormatException e) {
            System.err.println(resources.getString("enter.number.error"));
        }
    }

    private void createBill(Request request, Train train) {
        Bill bill = new Bill(request, train);
        bills.add(bill);
        request.setStatus(resources.getString("bill.issued"));

        System.out.println("\n" + resources.getString("your.bill.title"));
        System.out.println(bill);

        System.out.print(resources.getString("pay.bill.question") + " ");
        String answer = scanner.nextLine();
        if (answer.equalsIgnoreCase("да") || answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("так")) {
            bill.markAsPaid();
            System.out.println(resources.getString("payment.success"));
        }
    }

    public void addTrain() {
        try {
            System.out.println("\n" + resources.getString("add.train.title"));
            System.out.print(resources.getString("enter.train.number") + " ");
            String number = scanner.nextLine();
            System.out.print(resources.getString("enter.destination") + " ");
            String destination = scanner.nextLine();
            System.out.print(resources.getString("enter.intermediate.stations") + " ");
            String stationsStr = scanner.nextLine();
            System.out.print(resources.getString("enter.datetime") + " ");
            String dateTimeStr = scanner.nextLine();
            System.out.print(resources.getString("enter.price") + " ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print(resources.getString("enter.seats") + " ");
            int seats = Integer.parseInt(scanner.nextLine());

            List<String> stations = Arrays.asList(stationsStr.split(","));
            LocalDateTime departureTime = LocalDateTime.parse(dateTimeStr.replace(" ", "T"));

            Train train = new Train(number, destination, stations, departureTime, price, seats);
            trains.add(train);

            System.out.println(MessageFormat.format(resources.getString("train.added"), train));

        } catch (Exception e) {
            System.err.println(MessageFormat.format(resources.getString("train.add.error"), e.getMessage()));
        }
    }

    public void viewAllData() {
        System.out.println("\n" + resources.getString("all.trains.title"));
        for (Train train : trains) {
            System.out.println(train);
        }

        System.out.println("\n" + resources.getString("all.requests.title"));
        for (Request request : requests) {
            System.out.println(request);
        }

        System.out.println("\n" + resources.getString("all.bills.title"));
        for (Bill bill : bills) {
            System.out.println(bill);
        }
    }

    public void clearAllData() {
        try {
            System.out.print("\n" + resources.getString("clear.confirmation") + " ");
            String confirmation = scanner.nextLine();

            if ("Удалить".equals(confirmation) || "Delete".equals(confirmation) || "Выдаліць".equals(confirmation)) {
                trains.clear();
                requests.clear();
                bills.clear();

                clearDataFiles();

                System.out.println(resources.getString("clear.success"));
            }
        } catch (Exception e) {
            System.err.println(MessageFormat.format(resources.getString("clear.error"), e.getMessage()));
        }
    }

    private void clearDataFiles() {
        try {
            fileConnector.saveToFile(new ArrayList<Train>(), "trains.ser");
            fileConnector.saveToFile(new ArrayList<Request>(), "requests.ser");
            fileConnector.saveToFile(new ArrayList<Bill>(), "bills.ser");
        } catch (Exception e) {
            System.err.println(MessageFormat.format(resources.getString("clear.files.error"), e.getMessage()));
        }
    }

    public void Menu() {
        while (true) {
            System.out.println("\n" + resources.getString("main.title"));
            System.out.println("1. " + resources.getString("menu.new.request"));
            System.out.println("2. " + resources.getString("menu.add.train"));
            System.out.println("3. " + resources.getString("menu.view.data"));
            System.out.println("4. " + resources.getString("menu.save.data"));
            System.out.println("5. " + resources.getString("menu.clear.data"));
            System.out.println("6. " + resources.getString("menu.exit"));
            System.out.print(resources.getString("choose.action") + " ");

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
                    System.out.println(resources.getString("goodbye"));
                    return;
                default:
                    System.out.println(resources.getString("invalid.choice"));
            }
        }
    }

    public static void main(String[] args) {
        String language = "ru";
        String country = "RU";

        if (args.length >= 2) {
            language = args[0];
            country = args[1];
        }

        System.out.println("Запуск с локализацией: " + language + "_" + country);
        RailwayTicketSystem system = new RailwayTicketSystem(language, country);
        system.Menu();
    }
}