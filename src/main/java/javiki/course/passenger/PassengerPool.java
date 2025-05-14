package javiki.course.passenger;

import javiki.course.services.OrderTaxiService;

import java.util.ArrayList;
import java.util.List;

public class PassengerPool {
    private final List<Passenger> passengersPool = new ArrayList<>();

    // Метод для добавления пассажира в пул
    public void addPassenger(Passenger passenger) {
        passengersPool.add(passenger);
    }

    // Метод для удаления пассажира из пула
    public void removePassenger(Passenger passenger) {
        passengersPool.remove(passenger);
    }

    // Метод для создания нового пассажира и добавления его в пул
    public Passenger createPassenger(OrderTaxiService orderTaxiService) {
        Passenger passenger = new Passenger(orderTaxiService);  // Пассажир теперь инициализируется через конструктор
        this.addPassenger(passenger);  // Добавление пассажира в пул
        return passenger;
    }

    // Получение списка всех пассажиров
    public List<Passenger> getAllPassengers() {
        return passengersPool;
    }

    @Override
    public String toString() {
        return "PassengerPool{" +
                "passengersPool=" + passengersPool +
                '}';
    }
}
