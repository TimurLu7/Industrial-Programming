//Лу Тимур 5 группа
/* 7. Найти максимальный среди всех элементов тех строк заданной матрицы,
которые упорядочены (либо по возрастанию, либо по убыванию).*/
import java.util.Scanner;

public class Matrix {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите количество строк: ");
        int n = in.nextInt();
        System.out.print("Введите количество столбцов: ");
        int m = in.nextInt();

        int[][] matrix = new int[n][m];
        System.out.println("Введите элементы матрицы:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = in.nextInt();
            }
        }

        int Max = 0;

        for (int i = 0; i < n; i++) {
            if (isSortedAscending(matrix[i]) || isSortedDescending(matrix[i])) {
                int max = findMax(matrix[i]);
                if (Max == 0 || max > Max) {
                   Max = max;
                }
            }
        }

        if (Max != 0) {
            System.out.println("Максимальный элемент среди упорядоченных строк = " + Max);
        } else {
            System.out.println("Нет упорядоченных строк.");
        }

        in.close();
    }

    private static boolean isSortedAscending(int[] row) {
        for (int i = 0; i < row.length-1; i++) {
            if (row[i] > row[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSortedDescending(int[] row) {
        for (int i = 0; i < row.length-1; i++) {
            if (row[i] < row[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private static int findMax(int[] row) {
        int max = row[0];
        for (int i = 1; i < row.length; i++) {
            if (row[i] > max) {
                max = row[i];
            }
        }
        return max;
    }
}