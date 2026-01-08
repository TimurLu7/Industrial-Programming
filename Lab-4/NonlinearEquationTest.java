public class NonlinearEquationTest {
    private static double e = 1e-8;
    public static void main(String[] args) {
        System.out.println("=== ТЕСТИРОВАНИЕ КЛАССА NONLINEAREQUATION ===\n");

        NonlinearEquation equation = new NonlinearEquation();

        System.out.println("Тест 1: отрезок [1, 3])");
        try {
            double x = equation.bisec(1.0, 3.0, e);
            double fx = equation.f(x);
            System.out.printf(" корень: x = %.10f%n", x);
            System.out.printf("f(x) = %.2e%n", fx);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage() + "\n");
        }

        System.out.println("Тест 2: Отрезок [0, 2]");
        try {
            double x2 = equation.bisec(0.0, 2.0, e);
            double fx2 = equation.f(x2);
            System.out.printf("Найден корень: x = %.10f%n", x2);
            System.out.printf("f(x) = %.2e%n", fx2);
            System.out.println("Ожидаемый корень: ~1.347\n");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage() + "\n");
        }

        System.out.println("Тест 3: Ошибочный случай (отрезок [3, 4])");
        try {
            double x3 = equation.bisec(3.0, 4.0,e);
            double fx3 = equation.f(x3);
            System.out.printf("Найден корень: x = %.10f%n", x3);
            System.out.printf("f(x) = %.2e%n", fx3);
        } catch (Exception e) {
            System.out.println("Ожидаемая ошибка: " + e.getMessage() + "\n");
        }

        System.out.println("\n=== ТЕСТИРОВАНИЕ ЗАВЕРШЕНО ===");
    }
}
