import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.util.Date;

class Passenger implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String email;
    private Date creationDate;

    public Passenger(String name, String email) {
        this.name = name;
        this.email = email;
        this.creationDate = new Date();
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public Date getCreationDate() { return creationDate; }

    @Override
    public String toString() {
        return "Пассажир: " + name + " (" + email + ") [создан: " + creationDate + "]";
    }
}

class Train implements Serializable {
    private static final long serialVersionUID = 1L;
    private String trainNumber;
    private String destination;
    private List<String> intermediateStations;
    private LocalDateTime departureTime;
    private double price;
    private int availableSeats;
    private Date creationDate;

    public Train(String trainNumber, String destination, List<String> intermediateStations,
                 LocalDateTime departureTime, double price, int availableSeats) {
        this.trainNumber = trainNumber;
        this.destination = destination;
        this.intermediateStations = intermediateStations;
        this.departureTime = departureTime;
        this.price = price;
        this.availableSeats = availableSeats;
        this.creationDate = new Date();
    }

    public String getTrainNumber() { return trainNumber; }
    public String getDestination() { return destination; }
    public List<String> getIntermediateStations() { return intermediateStations; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public double getPrice() { return price; }
    public int getAvailableSeats() { return availableSeats; }
    public Date getCreationDate() { return creationDate; }

    public void reduceSeats() {
        if (availableSeats > 0)
            availableSeats--;
    }

    @Override
    public String toString() {
        return "Поезд №" + trainNumber + " -> " + destination +
                ", время: " + departureTime + ", цена: " + price + " руб. [создан: " + creationDate + "]";
    }
}

class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;

    private int id;
    private Passenger passenger;
    private String destination;
    private LocalDateTime desiredTime;
    private transient String status;
    private Date creationDate;

    public Request(Passenger passenger, String destination, LocalDateTime desiredTime) {
        this.id = nextId++;
        this.passenger = passenger;
        this.destination = destination;
        this.desiredTime = desiredTime;
        this.status = "НОВАЯ";
        this.creationDate = new Date();
    }

    public int getId() { return id; }
    public Passenger getPassenger() { return passenger; }
    public String getDestination() { return destination; }
    public LocalDateTime getDesiredTime() { return desiredTime; }
    public String getStatus() { return status; }
    public Date getCreationDate() { return creationDate; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Заявка №" + id + ": " + passenger.getName() +
                " -> " + destination + " в " + desiredTime + " [" + status + "] [создана: " + creationDate + "]";
    }
}

class Bill implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private Request request;
    private Train train;
    private double amount;
    private boolean isPaid;
    private Date creationDate;

    public Bill(Request request, Train train) {
        this.id = (int) (Math.random() * 1000);
        this.request = request;
        this.train = train;
        this.amount = train.getPrice();
        this.isPaid = false;
        this.creationDate = new Date();
    }

    public void markAsPaid() {
        this.isPaid = true;
        train.reduceSeats();
        request.setStatus("ОПЛАЧЕНО");
    }

    public boolean isPaid() { return isPaid; }
    public Date getCreationDate() { return creationDate; }

    @Override
    public String toString() {
        String statusText;
        if (isPaid) {
            statusText = "ОПЛАЧЕНО";
        } else {
            statusText = "НЕ ОПЛАЧЕНО";
        }

        return "Счет №" + id + ": " + amount + " руб." +
                "\nПассажир: " + request.getPassenger().getName() +
                "\nПоезд: " + train.getTrainNumber() + " -> " + train.getDestination() +
                "\nСтатус: " + statusText + " [создан: " + creationDate + "]";
    }
}