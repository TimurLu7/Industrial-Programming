import java.io.*;
import java.util.*;

class FileConnector {
    private String trainsFile = "trains.ser";
    private String requestsFile = "requests.ser";
    private String billsFile = "bills.ser";

    public <T> void saveToFile(List<T> list, String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
            System.out.println("Данные успешно сохранены в файл: " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении в файл: " + e.getMessage());
        }
    }

    public <T> List<T> loadFromFile(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (List<T>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден, создан новый список: " + filename);
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при загрузке из файла: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveAllData(List<Train> trains, List<Request> requests, List<Bill> bills) {
        saveToFile(trains, trainsFile);
        saveToFile(requests, requestsFile);
        saveToFile(bills, billsFile);
    }

    public DataStorage loadAllData() {
        List<Train> trains = loadFromFile(trainsFile);
        List<Request> requests = loadFromFile(requestsFile);
        List<Bill> bills = loadFromFile(billsFile);
        return new DataStorage(trains, requests, bills);
    }

    public <T> T find_Request_by_ID(List<T> list, int id) throws Exception {
        for (T item : list) {
            if (item instanceof Request && ((Request) item).getId() == id) {
                return item;
            }
        }
        throw new Exception("Объект с ID " + id + " не найден");
    }
}

class DataStorage {
    public List<Train> trains;
    public List<Request> requests;
    public List<Bill> bills;

    public DataStorage(List<Train> trains, List<Request> requests, List<Bill> bills) {
        this.trains = trains;
        this.requests = requests;
        this.bills = bills;
    }
}
