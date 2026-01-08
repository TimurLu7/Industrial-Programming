package Series;

import java.util.*;

public class SeriesDemo {
    public static void main(String[] args) {
        System.out.println("ДЕМОНСТРАЦИЯ РАБОТЫ КЛАССА LINEAR\n");

        try {
            List<Linear> progressions = new ArrayList<>();

            progressions.add(new Linear(1, 2, 6));
            progressions.add(new Linear(10, -1, 5));
            progressions.add(new Linear(2.5, 0.5, 7));
            progressions.add(new Linear(0, 5, 5));

            String string = "Series{FirstElement=13.00, Difference=7.00, NumberOfElements=5}";
            progressions.add(new Linear(string));

            System.out.println("Исходный массив прогрессий:");
            printProgressions(progressions);

            testMethods(progressions.get(0));

            testIterator(progressions.get(1));

            testSorting(progressions);

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    private static void testMethods(Linear progression) {
        System.out.println("\n ТЕСТИРОВАНИЕ МЕТОДОВ ");
        System.out.println("Тестируемая прогрессия: " + progression);

        System.out.println("\nВычисление i-х элементов:");
        for (int i = 1; i <= progression.getNumber_el(); i++) {
            double element = progression.get_El(i);
            System.out.printf("a%d = %.2f%n", i, element);
        }

        double sum = progression.get_Sum();
        System.out.printf("\nСумма прогрессии: %.2f%n", sum);
    }

    private static void testIterator(Linear progression) {
        System.out.println("\n ТЕСТИРОВАНИЕ ИТЕРАТОРА ");
        System.out.println("Прогрессия для итерации: " + progression);

        System.out.println("Обход с помощью итератора:");
        int index = 1;
        for (Double element : progression) {
            System.out.printf("Элемент %d: %.2f%n", index++, element);
        }

        System.out.println("\nИспользование интерфейса Iterator:");
        Iterator<Double> iterator = progression.iterator();
        while (iterator.hasNext()) {
            System.out.printf("Элемент: %.2f%n", iterator.next());
        }
    }

    private static void testSorting(List<Linear> progressions) {
        System.out.println("\n ТЕСТИРОВАНИЕ СОРТИРОВКИ ");

        List<Linear> sortedByFirst = new ArrayList<>(progressions);
        Collections.sort(sortedByFirst);
        System.out.println("\nСортировка по первому элементу (Comparable):");
        printProgressions(sortedByFirst);

        List<Linear> sortedByDifference = new ArrayList<>(progressions);
        sortedByDifference.sort(new Series.Diff_Comparator());
        System.out.println("\nСортировка по разности:");
        printProgressions(sortedByDifference);

        List<Linear> sortedByCount = new ArrayList<>(progressions);
        sortedByCount.sort(new Series.Number_Comparator());
        System.out.println("\nСортировка по количеству элементов:");
        printProgressions(sortedByCount);

        // Сортировка с помощью Comparator по сумме
        List<Linear> sortedBySum = new ArrayList<>(progressions);
        sortedBySum.sort(new Series.Sum_Comparator());
        System.out.println("\nСортировка по сумме прогрессии:");
        printProgressions(sortedBySum);
    }

    private static void printProgressions(List<Linear> progressions) {
        for (int i = 0; i < progressions.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, progressions.get(i));
        }
    }
}