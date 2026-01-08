/* Лу Тимур 5 группа
35.Даны две действительные квадратные матрицы порядка n.
 Получить новую матрицу путем прибавления к элементам каждого столбца
 первой матрицы произведения элементов соответствующих строк второй матрицы.
 */
import java.util.Scanner;

public class Matrix3 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Введите порядок n: ");
        int n = in.nextInt();

        int[][] matrix1 = new int[n][n];
        System.out.println("Введите элементы 1-ой матрицы:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix1[i][j] = in.nextInt();
            }
        }
        int[][] matrix2 = new int[n][n];
        System.out.println("Введите элементы 2-ой матрицы:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix2[i][j] = in.nextInt();
            }
        }

        int[][] result = transform(matrix1, matrix2);

        System.out.println("Новая матрица:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    public static int[][] transform(int[][] matrix1, int[][] matrix2) {
        if (matrix1 == null || matrix2 == null) {
            throw new IllegalArgumentException("Матрицы не должны быть null");
        }
        int n = matrix1.length;
        if (n == 0 || matrix2.length != n) {
            throw new IllegalArgumentException("Матрицы должны быть квадратными одного порядка n>0");
        }
        for (int i = 0; i < n; i++) {
            if (matrix1[i] == null || matrix1[i].length != n || matrix2[i] == null || matrix2[i].length != n) {
                throw new IllegalArgumentException("Матрицы должны быть квадратными n x n");
            }
        }

        int[] rowProducts = new int[n];
        for (int i = 0; i < n; i++) {
            rowProducts[i] = product(matrix2[i]);
        }

        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = matrix1[i][j] + rowProducts[i];
            }
        }
        return result;
    }

    private static int product(int[] row) {
        int pr = 1;
        for (int value : row) {
            pr *= value;
        }
        return pr;
    }
    }
