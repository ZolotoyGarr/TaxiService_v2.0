package javiki.course.services;

import javiki.course.driver.DriverPool;
import javiki.course.passenger.Passenger;
import javiki.course.passenger.PassengerPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationService {
    private final PassengerPool passengerPool;
    private final DriverPool driverPool;
    private final OrderTaxiService orderTaxiService;

    public SimulationService(PassengerPool passengerPool, DriverPool driverPool, OrderTaxiService orderTaxiService) {
        this.passengerPool = passengerPool;
        this.driverPool = driverPool;
        this.orderTaxiService = orderTaxiService;
    }

    public void runSimulation(int numberOfPassengers, int numberOfDrivers) {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfPassengers);

        // Создание и добавление водителей
        for (int i = 0; i < numberOfDrivers; i++) {
            System.out.println(driverPool.createRandomDriver());
        }

        // Создание и добавление пассажиров
        for (int i = 0; i < numberOfPassengers; i++) {
            Passenger passenger = passengerPool.createPassenger(orderTaxiService);
            executorService.submit(passenger); // Пассажиры работают в потоках
        }

        System.out.println("Созданы пассажиры:");
        passengerPool.getAllPassengers().forEach(passenger -> System.out.println(passenger.getProfile().getName()));

        executorService.shutdown();

        // Ожидание завершения всех потоков
        boolean allFinished = false;
        while (!allFinished) {
            allFinished = passengerPool.getAllPassengers().stream()
                    .allMatch(p -> p.getStatus().equals(javiki.course.passenger.PassengerStatus.FINISHED));

            try {
                Thread.sleep(1000); // Проверка состояния
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Все пассажиры завершили поездки.");
        System.out.println("Симуляция завершена!");
    }
}
