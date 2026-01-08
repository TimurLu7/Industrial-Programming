public class NonlinearEquation {

    private static double e = 1e-8;
    private static int max = 10_000;

    public static double f(double x) {
        return x * x * x - 3.0 * x * x + 3.0;
    }

    public static double bisec(double a, double b, double e) {
        double fa = f(a);
        double fb = f(b);

        if (Math.abs(fa) <= e)
            return a;
        if (Math.abs(fb) <= e)
            return b;

        if (fa * fb > 0) {
            throw new IllegalArgumentException(
                    "На концах отрезка знак не меняется: f(a)*f(b) > 0. "
            );
        }

        double left = a;
        double right = b;
        double mid = 0.0;
        double fmid;

        for (int i = 0; i < max; i++) {
            mid = (left + right) / 2.0;
            fmid = f(mid);

            if (Math.abs(fmid) <= e || (right - left) / 2.0 < e) {
                return mid;
            }

            if (f(left) * fmid < 0) {
                right = mid;
            } else {
                left = mid;
            }
        }
        return (left + right) / 2.0;
    }

    public static void main(String[] args) {
        double a = Double.parseDouble(args[0]);
        double b = Double.parseDouble(args[1]);

        assert a != b : "a и b не должны совпадать";

        double left = Math.min(a, b);
        double right = Math.max(a, b);

        try {
            double x = bisec(left, right, e);
            System.out.printf("Приближенный корень: x = %.12f%n", x);
            System.out.printf("f(x) = %.12e%n", f(x));
        } catch (IllegalArgumentException ex) {
            System.out.println("Ошибка: " + ex.getMessage());
        }
    }
}