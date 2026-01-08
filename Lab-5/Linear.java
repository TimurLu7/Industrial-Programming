package Series;

public class Linear extends Series {

    public Linear(double first_el, double d, int number_el) {

        super(first_el, d, number_el);
    }

    public Linear(String data) {

        super(data);
    }

    @Override
    public double get_El(int i) {
        if (i <= 0 || i > number_el) {
            throw new IllegalArgumentException("Неверный индекс");
        }
        return first_el + (i - 1) * d;
    }

    @Override
    public double get_Sum() {
        return (number_el * (2 * first_el + (number_el - 1) * d)) / 2;
    }

    @Override
    public String toString() {
        return String.format("Linear[First Element=%.2f, Difference=%.2f, Number Of Elements=%d, ∑=%.2f]",
                first_el, d, number_el, get_Sum());
    }
}
