import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ToyFileManager {
    private static final String DATA_FILE = "toys.dat";
    private static final String INDEX_FILE = "toys_index.dat";

    private Map<LocalDate, List<Long>> receiptDateIndex = new TreeMap<>();
    private Map<String, List<Long>> supplierIndex = new TreeMap<>();
    private Map<String, List<Long>> ageRangeIndex = new TreeMap<>();

    public ToyFileManager() {
        loadIndexes();
    }

    public void saveToy(Toy toy) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(DATA_FILE, "rw")) {
            raf.seek(raf.length());
            long position = raf.getFilePointer();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(toy);
            oos.flush();

            byte[] data = baos.toByteArray();
            raf.writeInt(data.length);
            raf.write(data);

            updateIndexes(toy, position);
            saveIndexes();
        }
    }

    public Toy readToy(long position) throws IOException, ClassNotFoundException {
        try (RandomAccessFile raf = new RandomAccessFile(DATA_FILE, "r")) {
            raf.seek(position);
            int length = raf.readInt();
            byte[] data = new byte[length];
            raf.readFully(data);

            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Toy) ois.readObject();
        }
    }

    private void updateIndexes(Toy toy, long position) {
        receiptDateIndex.computeIfAbsent(toy.getReceiptDate(), k -> new ArrayList<>()).add(position);

        supplierIndex.computeIfAbsent(toy.getSupplier(), k -> new ArrayList<>()).add(position);

        ageRangeIndex.computeIfAbsent(toy.getAgeRange(), k -> new ArrayList<>()).add(position);
    }

    public List<Toy> readAllToys() throws IOException, ClassNotFoundException {
        List<Toy> toys = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(DATA_FILE, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                long position = raf.getFilePointer();
                int length = raf.readInt();
                byte[] data = new byte[length];
                raf.readFully(data);

                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais);
                Toy toy = (Toy) ois.readObject();

                toys.add(toy);
            }
        }
        return toys;
    }

    private void saveIndexes() throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(INDEX_FILE))) {
            oos.writeObject(receiptDateIndex);
            oos.writeObject(supplierIndex);
            oos.writeObject(ageRangeIndex);
        }
    }

    private void loadIndexes() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(INDEX_FILE))) {
            receiptDateIndex = (Map<LocalDate, List<Long>>) ois.readObject();
            supplierIndex = (Map<String, List<Long>>) ois.readObject();
            ageRangeIndex = (Map<String, List<Long>>) ois.readObject();
        } catch (FileNotFoundException e) {
            initializeIndexes();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка загрузки индексов: " + e.getMessage());
            initializeIndexes();
        }
    }

    private void initializeIndexes() {
        receiptDateIndex = new TreeMap<>();
        supplierIndex = new TreeMap<>();
        ageRangeIndex = new TreeMap<>();
    }

    public List<Toy> findByReceiptDate(LocalDate date) throws IOException, ClassNotFoundException {
        List<Toy> result = new ArrayList<>();
        List<Long> positions = receiptDateIndex.get(date);
        if (positions != null) {
            for (Long pos : positions) {
                result.add(readToy(pos));
            }
        }
        return result;
    }

    public List<Toy> findBySupplier(String supplier) throws IOException, ClassNotFoundException {
        List<Toy> result = new ArrayList<>();
        List<Long> positions = supplierIndex.get(supplier);
        if (positions != null) {
            for (Long pos : positions) {
                result.add(readToy(pos));
            }
        }
        return result;
    }

    public List<Toy> findByAgeRange(String ageRange) throws IOException, ClassNotFoundException {
        List<Toy> result = new ArrayList<>();
        List<Long> positions = ageRangeIndex.get(ageRange);
        if (positions != null) {
            for (Long pos : positions) {
                result.add(readToy(pos));
            }
        }
        return result;
    }

    public List<Toy> getAllByReceiptDateAsc() throws IOException, ClassNotFoundException {
        List<Toy> result = new ArrayList<>();
        for (LocalDate date : receiptDateIndex.keySet()) {
            result.addAll(findByReceiptDate(date));
        }
        return result;
    }

    public List<Toy> getAllByReceiptDateDesc() throws IOException, ClassNotFoundException {
        List<Toy> result = new ArrayList<>();
        List<LocalDate> dates = new ArrayList<>(receiptDateIndex.keySet());
        Collections.reverse(dates);
        for (LocalDate date : dates) {
            result.addAll(findByReceiptDate(date));
        }
        return result;
    }

    public List<Toy> findByReceiptDateAfter(LocalDate date) throws IOException, ClassNotFoundException {
        List<Toy> result = new ArrayList<>();
        for (LocalDate d : receiptDateIndex.keySet()) {
            if (d.isAfter(date)) {
                result.addAll(findByReceiptDate(d));
            }
        }
        return result;
    }

    public List<Toy> findByReceiptDateBefore(LocalDate date) throws IOException, ClassNotFoundException {
        List<Toy> result = new ArrayList<>();
        for (LocalDate d : receiptDateIndex.keySet()) {
            if (d.isBefore(date)) {
                result.addAll(findByReceiptDate(d));
            }
        }
        return result;
    }

    public boolean deleteByReceiptDate(LocalDate date) throws IOException, ClassNotFoundException {
        List<Long> positions = receiptDateIndex.get(date);
        if (positions == null || positions.isEmpty()) {
            return false;
        }

        for (Long pos : positions) {
            Toy toy = readToy(pos);
            supplierIndex.get(toy.getSupplier()).remove(pos);
            ageRangeIndex.get(toy.getAgeRange()).remove(pos);
        }

        receiptDateIndex.remove(date);
        saveIndexes();
        return true;
    }

    public Set<String> getAllSuppliers() {
        return supplierIndex.keySet();
    }

    public Set<String> getAllAgeRanges() {
        return ageRangeIndex.keySet();
    }

    public Set<LocalDate> getAllReceiptDates() {
        return receiptDateIndex.keySet();
    }

    public int getToyCount() throws IOException {
        int count = 0;
        try (RandomAccessFile raf = new RandomAccessFile(DATA_FILE, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                int length = raf.readInt();
                raf.skipBytes(length);
                count++;
            }
        }
        return count;
    }
}