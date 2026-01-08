import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Toy implements Serializable {
    private static final long serialVersionUID = 1L;

    private String toyCode;
    private String toyName;
    private String ageRange;
    private double price;
    private int quantity;
    private LocalDate receiptDate;
    private String supplier;

    public Toy(String toyCode, String toyName, String ageRange, double price,
               int quantity, LocalDate receiptDate, String supplier) {
        setToyCode(toyCode);
        setToyName(toyName);
        setAgeRange(ageRange);
        setPrice(price);
        setQuantity(quantity);
        setReceiptDate(receiptDate);
        setSupplier(supplier);
    }

    public String getToyCode() {
        return toyCode; }

    public void setToyCode(String toyCode) {
        if (toyCode == null || toyCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Код игрушки не может быть пустым");
        }
        this.toyCode = toyCode.trim();
    }

    public String getToyName() {
        return toyName; }

    public void setToyName(String toyName) {
        if (toyName == null || toyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Название игрушки не может быть пустым");
        }
        this.toyName = toyName.trim();
    }

    public String getAgeRange() {
        return ageRange; }

    public void setAgeRange(String ageRange) {
        if (ageRange == null ) {
            throw new IllegalArgumentException("Возрастные границы должны быть в формате 'число-число'");
        }
        this.ageRange = ageRange;
    }

    public double getPrice() {
        return price; }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        this.price = price;
    }

    public int getQuantity() {
        return quantity; }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Количество не может быть отрицательным");
        }
        this.quantity = quantity;
    }

    public LocalDate getReceiptDate() {
        return receiptDate; }

    public void setReceiptDate(LocalDate receiptDate) {
        if (receiptDate == null) {
            throw new IllegalArgumentException("Дата поступления не может быть 0");
        }
        this.receiptDate = receiptDate;
    }

    public String getSupplier() {
        return supplier; }

    public void setSupplier(String supplier) {
        if (supplier == null || supplier.trim().isEmpty()) {
            throw new IllegalArgumentException("Поставщик не может быть пустым");
        }
        this.supplier = supplier.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Toy toy = (Toy) o;
        return Objects.equals(toyCode, toy.toyCode);
    }

    @Override
    public String toString() {
        return String.format("Код: %s, Название: %s, Возраст: %s, Цена: %.2f, " +
                        "Кол-во: %d, Дата: %s, Поставщик: %s",
                toyCode, toyName, ageRange, price, quantity,
                receiptDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), supplier);
    }
}