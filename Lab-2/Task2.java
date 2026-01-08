//Лу Тимур 5 группа
/* 21. Подсчитать количество строк заданной матрицы, являющихся перестановкой чисел
-1, -2, ..., -10.
*/
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

        int[] arr = {-1, -2, -3, -4, -5, -6, -7, -8, -9, -10};
        int N = 10;

        int value = 0;
        for (int i = 0; i < n; i++) {
            if (m == N && Perestanovka(matrix[i], m, arr, N)==1 && findDuplicate(matrix[i], m)==1){
                value++;
            }
        }
        if (value != 0) {
            System.out.println("Количество строк заданной матрицы, являющихся перестановкой чисел -1, -2, ..., -10 = " + value);
        } else {
            System.out.println("Нет строк, являющихся перестановкой чисел -1, -2, ..., -10");
        }
        in.close();
    }

    private static int Perestanovka(int[] row, int m, int[] arr, int N) {
        for (int j = 0; j < m; j++)
        {
            boolean found = false;
            for (int i = 0; i < N; i++) {
                if (row[j] == arr[i]) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return -1;
            }
        }
        return 1;
    }
    private static int findDuplicate(int[] arr, int n)
    {
        for (int i = 0; i < n - 1; i++)
        {
            for (int j = i + 1; j < n; j++)
            {
                if (arr[i] == arr[j])
                {
                    return -1;
                }
            }
        }
        return 1;
    }
}