import java.time.LocalDate;
import java.util.*;

class Employee {
    private String name;
    private String department;
    private double salary;
    private LocalDate hireDate;

    public Employee(String name, String department, double salary, LocalDate hireDate) {
        this.name = name;
        this.department = department;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    @Override
    public String toString() {
        return String.format("%-15s %-10s %10.2f %12s",
                name, department, salary, hireDate);
    }
}

public class EmployeeSort {

    public static List<Employee> createEmployees() {
        List<Employee> employees = new ArrayList<>();

        employees.add(new Employee("Иван Иванов", "IT", 12000, LocalDate.of(2020, 3, 15)));
        employees.add(new Employee("Петр Петров", "IT", 15000, LocalDate.of(2019, 5, 20)));
        employees.add(new Employee("Сергей Сергеев", "Sales", 8000, LocalDate.of(2021, 8, 10)));
        employees.add(new Employee("Анна Смирнова", "HR", 9000, LocalDate.of(2022, 1, 15)));
        employees.add(new Employee("Мария Кузнецова", "IT", 13000, LocalDate.of(2018, 11, 30)));
        employees.add(new Employee("Алексей Алексеев", "Sales", 7500, LocalDate.of(2023, 2, 28)));
        employees.add(new Employee("Ольга Николаева", "HR", 8500, LocalDate.of(2020, 7, 5)));
        employees.add(new Employee("Дмитрий Дмитриев", "IT", 16000, LocalDate.of(2017, 4, 12)));
        employees.add(new Employee("Елена Артамонова", "Sales", 7000, LocalDate.of(2022, 6, 18)));
        employees.add(new Employee("Николай Николаев", "IT", 14000, LocalDate.of(2021, 9, 25)));

        return employees;
    }

    public static List<Employee> getEmployeesByDepartment(List<Employee> employees, String department) {
        List<Employee> result = new ArrayList<>();

        for (Employee emp : employees) {
            if (department.equals(emp.getDepartment())) {
                result.add(emp);
            }
        }

        return result;
    }

    public static Employee findEmployeeWithMaxSalary(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return null;
        }

        Employee maxSalaryEmployee = employees.get(0);

        for (Employee emp : employees) {
            if (emp.getSalary() > maxSalaryEmployee.getSalary()) {
                maxSalaryEmployee = emp;
            }
        }

        return maxSalaryEmployee;
    }

    public static double calculateAverageSalary(List<Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            return 0.0;
        }

        double totalSalary = 0.0;

        for (Employee emp : employees) {
            totalSalary += emp.getSalary();
        }

        return totalSalary / employees.size();
    }

    public static List<Employee> getEmployeesAboveAverage(List<Employee> employees) {
        double averageSalary = calculateAverageSalary(employees);
        List<Employee> result = new ArrayList<>();

        for (Employee emp : employees) {
            if (emp.getSalary() > averageSalary) {
                result.add(emp);
            }
        }

        return result;
    }

    public static Map<String, List<Employee>> groupEmployeesByDepartment(List<Employee> employees) {
        Map<String, List<Employee>> result = new HashMap<>();

        for (Employee emp : employees) {
            String department = emp.getDepartment();

            if (!result.containsKey(department)) {
                result.put(department, new ArrayList<>());
            }

            result.get(department).add(emp);
        }

        return result;
    }

    public static Map<String, Double> calculateTotalSalaryByDepartment(List<Employee> employees) {
        Map<String, Double> result = new HashMap<>();

        for (Employee emp : employees) {
            String department = emp.getDepartment();
            double salary = emp.getSalary();

            if (!result.containsKey(department)) {
                result.put(department, 0.0);
            }

            double currentTotal = result.get(department);
            result.put(department, currentTotal + salary);
        }

        return result;
    }

    public static List<Employee> findOldestEmployees(List<Employee> employees, int count) {
        if (employees == null || employees.isEmpty()) {
            return new ArrayList<>();
        }

        List<Employee> sortedEmployees = new ArrayList<>(employees);

        sortedEmployees.sort(new Comparator<Employee>() {
            @Override
            public int compare(Employee emp1, Employee emp2) {
                return emp1.getHireDate().compareTo(emp2.getHireDate());
            }
        });

        List<Employee> result = new ArrayList<>();
        for (int i = 0; i < Math.min(count, sortedEmployees.size()); i++) {
            result.add(sortedEmployees.get(i));
        }

        return result;
    }

    public static List<Employee> findEmployeesWithLowSalaryInDepartment(
            List<Employee> employees, String department, double minSalary) {

        List<Employee> result = new ArrayList<>();

        for (Employee emp : employees) {
            if (department.equals(emp.getDepartment()) &&
                    emp.getSalary() < minSalary) {
                result.add(emp);
            }
        }

        return result;
    }

    public static void printTable(String title, List<Employee> employees) {
        System.out.println("\n" + title);
        System.out.println("------------------------------------------------------------");
        System.out.println("Имя            Отдел       Зарплата   Дата приема");
        System.out.println("------------------------------------------------------------");

        if (employees == null || employees.isEmpty()) {
            System.out.println("Нет данных для отображения");
        } else {
            for (Employee emp : employees) {
                System.out.println(emp);
            }
        }

        System.out.println("------------------------------------------------------------");
    }

    public static void printTotalSalaryByDepartment(Map<String, Double> salaryByDept) {
        System.out.println("\n    Суммарная зарплата по отделам    ");
        System.out.println("---------------------------------------------");
        System.out.println("Отдел\t\tСуммарная зарплата");
        System.out.println("---------------------------------------------");

        for (Map.Entry<String, Double> entry : salaryByDept.entrySet()) {
            System.out.printf("%-10s %15.2f%n", entry.getKey(), entry.getValue());
        }

        System.out.println("---------------------------------------------");
    }

    public static void main(String[] args) {
        List<Employee> employees = createEmployees();

        System.out.println("    Исходный список сотрудников (10 человек)    ");
        printTable("Все сотрудники", employees);

        System.out.println("\n    1. Сотрудник с максимальной зарплатой в отделе IT    ");

        List<Employee> itEmployees = getEmployeesByDepartment(employees, "IT");

        if (itEmployees.isEmpty()) {
            System.out.println("Сотрудники в отделе IT не найдены");
        } else {
            Employee maxSalaryIT = findEmployeeWithMaxSalary(itEmployees);
            System.out.println("Сотрудник с максимальной зарплатой в IT:");
            System.out.println(maxSalaryIT);
        }

        System.out.println("\n    2. Сотрудники с зарплатой выше средней по компании    ");

        double averageSalary = calculateAverageSalary(employees);
        System.out.printf("Средняя зарплата по компании: %.2f%n", averageSalary);

        List<Employee> aboveAverageEmployees = getEmployeesAboveAverage(employees);
        printTable("Сотрудники с зарплатой выше средней", aboveAverageEmployees);

        System.out.println("\n    3. Сотрудники по отделам    ");

        Map<String, List<Employee>> employeesByDepartment = groupEmployeesByDepartment(employees);

        for (Map.Entry<String, List<Employee>> entry : employeesByDepartment.entrySet()) {
            printTable("Отдел: " + entry.getKey(), entry.getValue());
        }

        System.out.println("\n    4. Суммарные зарплаты по отделам    ");
        Map<String, Double> totalSalaryByDept = calculateTotalSalaryByDepartment(employees);
        printTotalSalaryByDepartment(totalSalaryByDept);

        System.out.println("\n    5. Топ-3 самых старых сотрудника по дате приема    ");

        List<Employee> oldestEmployees = findOldestEmployees(employees, 3);
        printTable("Топ-3 самых старых сотрудника", oldestEmployees);

        System.out.println("\n    6. Проверка зарплат в отделе Sales    ");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите минимальную зарплату: ");
        double minSalary = scanner.nextDouble();
        scanner.close();

        double min = minSalary * 2;
        System.out.printf("Порог для проверки: %.2f (2 * %.2f)%n", min, minSalary);

        List<Employee> lowSalarySales = findEmployeesWithLowSalaryInDepartment(
                employees, "Sales", min);

        if (lowSalarySales.isEmpty()) {
            System.out.println("В отделе Sales нет сотрудников с зарплатой ниже " + min);
        } else {
            printTable(
                    "Сотрудники отдела Sales с зарплатой ниже " + min,
                    lowSalarySales
            );
        }
    }
}