import org.unit.jupiter.api.Test;
import static org.unit.jupiter.api.Assertions.*;

public class Matrix3Test {

    @Test
    public void transform_basic2x2() {
        int[][] a = {
                {1, 1},
                {1, 1}
        };
        int[][] b = {
                {2, 3}, // product = 6
                {0, 5}  // product = 0
        };

        int[][] result = Matrix3.transform(a, b);

        int[][] expected = {
                {7, 7},
                {1, 1}
        };

        assertArrayEquals(expected[0], result[0]);
        assertArrayEquals(expected[1], result[1]);
    }

    @Test
    public void transform_withNegativesAndZeros() {
        int[][] a = {
                {5, -2},
                {3, 4}
        };
        int[][] b = {
                {-1, 2}, // product = -2
                {4, 0}   // product = 0
        };

        int[][] result = Matrix3.transform(a, b);

        int[][] expected = {
                {3, -4},
                {3, 4}
        };

        assertArrayEquals(expected[0], result[0]);
        assertArrayEquals(expected[1], result[1]);
    }

    @Test
    public void transform_doesNotMutateInputs() {
        int[][] a = {
                {1, 2},
                {3, 4}
        };
        int[][] aCopy = deepCopy(a);
        int[][] b = {
                {1, 1},
                {2, 2}
        };
        int[][] bCopy = deepCopy(b);

        Matrix3.transform(a, b);

        assert2dArraysEqual(aCopy, a);
        assert2dArraysEqual(bCopy, b);
    }

    @Test
    public void transform_1x1() {
        int[][] a = {{7}};
        int[][] b = {{3}}; // product = 3
        int[][] result = Matrix3.transform(a, b);
        assertEquals(10, result[0][0]);
    }

    @Test
    public void transform_nullInputs_throw() {
        assertThrows(IllegalArgumentException.class, () -> Matrix3.transform(null, new int[][]{{1}}));
        assertThrows(IllegalArgumentException.class, () -> Matrix3.transform(new int[][]{{1}}, null));
    }

    @Test
    public void transform_zeroSize_throw() {
        assertThrows(IllegalArgumentException.class, () -> Matrix3.transform(new int[][]{}, new int[][]{}));
    }

    @Test
    public void transform_sizeMismatch_throw() {
        int[][] a = {
                {1, 2, 3},
                {4, 5, 6}
        }; // 2x3 not square
        int[][] b = {
                {1, 1},
                {1, 1}
        }; // 2x2

        assertThrows(IllegalArgumentException.class, () -> Matrix3.transform(a, b));
    }

    private static int[][] deepCopy(int[][] src) {
        int[][] copy = new int[src.length][];
        for (int i = 0; i < src.length; i++) {
            copy[i] = src[i].clone();
        }
        return copy;
    }

    private static void assert2dArraysEqual(int[][] expected, int[][] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertArrayEquals(expected[i], actual[i]);
        }
    }
}



