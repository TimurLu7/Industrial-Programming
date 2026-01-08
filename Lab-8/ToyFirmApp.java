import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ToyFirmApp {
    private ToyFileManager fileManager;
    private Scanner scanner;
    private DateTimeFormatter dateFormatter;

    public ToyFirmApp() {
        this.fileManager = new ToyFileManager();
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }

    public void run() {
        System.out.println("=== Система учета игрушек торговой фирмы ===");

        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        addTestData();
                        break;
                    case "2":
                        showAllToys();
                        break;
                    case "3":
                        showByReceiptDate();
                        break;
                    case "4":
                        showBySupplier();
                        break;
                    case "5":
                        showByAgeRange();
                        break;
                    case "6":
                        showSortedByReceiptDate();
                        break;
                    case "7":
                        searchByReceiptDateRange();
                        break;
                    case "8":
                        deleteByReceiptDate();
                        break;
                    case "9":
                        addNewToy();
                        break;
                    case "10":
                        showAllSuppliers();
                        break;
                    case "11":
                        showAllAgeRanges();
                        break;
                    case "0":
                        System.out.println("Выход из программы...");
                        return;
                    default:
                        System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }

            System.out.println("\nНажмите Enter для продолжения...");
            scanner.nextLine();
        }
    }

    private void printMenu() {
        System.out.println("\n=== Меню ===");
        System.out.println("1. Заполнить файл тестовыми данными");
        System.out.println("2. Последовательный вывод всех объектов");
        System.out.println("3. Поиск по дате поступления");
        System.out.println("4. Поиск по поставщику");
        System.out.println("5. Поиск по возрастным границам");
        System.out.println("6. Сортировка по дате поступления");
        System.out.println("7. Поиск по диапазону дат");
        System.out.println("8. Удаление по дате поступления");
        System.out.println("9. Добавить новую игрушку");
        System.out.println("10. Показать всех поставщиков");
        System.out.println("11. Показать все возрастные диапазоны");
        System.out.println("0. Выход");
        System.out.print("Выберите действие: ");
    }

    private void addTestData() throws IOException {
        Toy[] testToys = {
                new Toy("T001", "Конструктор Lego", "6-12", 2499.99, 50,
                        LocalDate.of(2024, 1, 15), "Lego Group"),
                new Toy("T002", "Кукла Barbie", "3-8", 899.50, 30,
                        LocalDate.of(2024, 2, 20), "Mattel"),
                new Toy("T003", "Машинка Hot Wheels", "4-10", 199.99, 100,
                        LocalDate.of(2024, 1, 10), "Mattel"),
                new Toy("T004", "Пазл 1000 деталей", "8-15", 459.00, 25,
                        LocalDate.of(2024, 3, 5), "Ravensburger"),
                new Toy("T005", "Настольная игра", "10-16", 1299.00, 15,
                        LocalDate.of(2024, 2, 28), "Hasbro")
        };

        for (Toy toy : testToys) {
            fileManager.saveToy(toy);
        }

        System.out.println("Добавлено 5 тестовых записей");
    }

    private void showAllToys() {
        try {
            List<Toy> toys = fileManager.readAllToys();
            if (toys.isEmpty()) {
                System.out.println("Файл пуст");
            } else {
                System.out.println("Все игрушки (" + toys.size() + "):");
                for (int i = 0; i < toys.size(); i++) {
                    System.out.println((i + 1) + ". " + toys.get(i));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }

    private void showByReceiptDate() {
        try {
            System.out.print("Введите дату поступления (дд.мм.гггг): ");
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);

            List<Toy> toys = fileManager.findByReceiptDate(date);
            if (toys.isEmpty()) {
                System.out.println("Игрушки с датой поступления " + dateStr + " не найдены");
            } else {
                System.out.println("Найдено " + toys.size() + " игрушек:");
                for (int i = 0; i < toys.size(); i++) {
                    System.out.println((i + 1) + ". " + toys.get(i));
                }
            }
        } catch (DateTimeParseException e) {
            System.err.println("Неверный формат даты. Используйте дд.мм.гггг");
        } catch (Exception e) {
            System.err.println("Ошибка при поиске: " + e.getMessage());
        }
    }

    private void showBySupplier() {
        try {
            Set<String> suppliers = fileManager.getAllSuppliers();
            if (suppliers.isEmpty()) {
                System.out.println("Нет данных о поставщиках");
                return;
            }

            System.out.println("Доступные поставщики: " + String.join(", ", suppliers));
            System.out.print("Введите название поставщика: ");
            String supplier = scanner.nextLine();

            List<Toy> toys = fileManager.findBySupplier(supplier);
            if (toys.isEmpty()) {
                System.out.println("Игрушки от поставщика '" + supplier + "' не найдены");
            } else {
                System.out.println("Найдено " + toys.size() + " игрушек:");
                for (int i = 0; i < toys.size(); i++) {
                    System.out.println((i + 1) + ". " + toys.get(i));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка при поиске: " + e.getMessage());
        }
    }

    private void showByAgeRange() {
        try {
            Set<String> ageRanges = fileManager.getAllAgeRanges();
            if (ageRanges.isEmpty()) {
                System.out.println("Нет данных о возрастных диапазонах");
                return;
            }

            System.out.println("Доступные возрастные диапазоны: " + String.join(", ", ageRanges));
            System.out.print("Введите возрастной диапазон (например, 6-12): ");
            String ageRange = scanner.nextLine();

            List<Toy> toys = fileManager.findByAgeRange(ageRange);
            if (toys.isEmpty()) {
                System.out.println("Игрушки для возрастного диапазона '" + ageRange + "' не найдены");
            } else {
                System.out.println("Найдено " + toys.size() + " игрушек:");
                for (int i = 0; i < toys.size(); i++) {
                    System.out.println((i + 1) + ". " + toys.get(i));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка при поиске: " + e.getMessage());
        }
    }

    private void showSortedByReceiptDate() {
        try {
            System.out.println("1. По возрастанию даты");
            System.out.println("2. По убыванию даты");
            System.out.print("Выберите порядок сортировки: ");
            String choice = scanner.nextLine();

            List<Toy> toys;
            if (choice.equals("1")) {
                toys = fileManager.getAllByReceiptDateAsc();
                System.out.println("Игрушки по возрастанию даты поступления:");
            } else {
                toys = fileManager.getAllByReceiptDateDesc();
                System.out.println("Игрушки по убыванию даты поступления:");
            }

            if (toys.isEmpty()) {
                System.out.println("Нет данных");
            } else {
                for (int i = 0; i < toys.size(); i++) {
                    System.out.println((i + 1) + ". " + toys.get(i));
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка при сортировке: " + e.getMessage());
        }
    }

    private void searchByReceiptDateRange() {
        try {
            System.out.println("1. Дата больше указанной");
            System.out.println("2. Дата меньше указанной");
            System.out.print("Выберите тип поиска: ");
            String choice = scanner.nextLine();

            System.out.print("Введите дату (дд.мм.гггг): ");
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);

            List<Toy> toys;
            if (choice.equals("1")) {
                toys = fileManager.findByReceiptDateAfter(date);
                System.out.println("Игрушки с датой поступления после " + dateStr + ":");
            } else {
                toys = fileManager.findByReceiptDateBefore(date);
                System.out.println("Игрушки с датой поступления до " + dateStr + ":");
            }

            if (toys.isEmpty()) {
                System.out.println("Игрушки не найдены");
            } else {
                for (int i = 0; i < toys.size(); i++) {
                    System.out.println((i + 1) + ". " + toys.get(i));
                }
            }
        } catch (DateTimeParseException e) {
            System.err.println("Неверный формат даты. Используйте дд.мм.гггг");
        } catch (Exception e) {
            System.err.println("Ошибка при поиске: " + e.getMessage());
        }
    }

    private void deleteByReceiptDate() {
        try {
            System.out.print("Введите дату поступления для удаления (дд.мм.гггг): ");
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);

            boolean deleted = fileManager.deleteByReceiptDate(date);
            if (deleted) {
                System.out.println("Все игрушки с датой поступления " + dateStr + " удалены");
            } else {
                System.out.println("Игрушки с датой поступления " + dateStr + " не найдены");
            }
        } catch (DateTimeParseException e) {
            System.err.println("Неверный формат даты. Используйте дд.мм.гггг");
        } catch (Exception e) {
            System.err.println("Ошибка при удалении: " + e.getMessage());
        }
    }

    private void addNewToy() {
        try {
            System.out.println("Добавление новой игрушки:");

            System.out.print("Код игрушки: ");
            String code = scanner.nextLine();

            System.out.print("Название игрушки: ");
            String name = scanner.nextLine();

            System.out.print("Возрастные границы (например, 6-12): ");
            String ageRange = scanner.nextLine();

            System.out.print("Цена: ");
            double price = Double.parseDouble(scanner.nextLine());

            System.out.print("Количество: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            System.out.print("Дата поступления (дд.мм.гггг): ");
            String dateStr = scanner.nextLine();
            LocalDate date = LocalDate.parse(dateStr, dateFormatter);

            System.out.print("Поставщик: ");
            String supplier = scanner.nextLine();

            Toy toy = new Toy(code, name, ageRange, price, quantity, date, supplier);
            fileManager.saveToy(toy);

            System.out.println("Игрушка успешно добавлена!");

        } catch (DateTimeParseException e) {
            System.err.println("Неверный формат даты. Используйте дд.мм.гггг");
        } catch (NumberFormatException e) {
            System.err.println("Неверный формат числа");
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка валидации: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка при добавлении: " + e.getMessage());
        }
    }

    private void showAllSuppliers() {
        try {
            Set<String> suppliers = fileManager.getAllSuppliers();
            if (suppliers.isEmpty()) {
                System.out.println("Нет данных о поставщиках");
            } else {
                System.out.println("Все поставщики:");
                for (String supplier : suppliers) {
                    System.out.println("- " + supplier);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    private void showAllAgeRanges() {
        try {
            Set<String> ageRanges = fileManager.getAllAgeRanges();
            if (ageRanges.isEmpty()) {
                System.out.println("Нет данных о возрастных диапазонах");
            } else {
                System.out.println("Все возрастные диапазоны:");
                for (String ageRange : ageRanges) {
                    System.out.println("- " + ageRange);
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ToyFirmApp app = new ToyFirmApp();
        app.run();
    }
}