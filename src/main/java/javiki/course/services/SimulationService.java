package javiki.course.services;

import javiki.course.driver.DriverPool;
import javiki.course.operator.OperatorPool;
import javiki.course.passenger.Passenger;
import javiki.course.passenger.PassengerPool;
import javiki.course.passenger.PassengerStatus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class SimulationService {
    private static final Logger LOGGER = Logger.getLogger(SimulationService.class.getName());

    private final PassengerPool passengerPool;
    private final DriverPool driverPool;
    private final OperatorPool operatorPool;
    private final OrderTaxiService orderTaxiService;

    public SimulationService(PassengerPool passengerPool, DriverPool driverPool, OperatorPool operatorPool, OrderTaxiService orderTaxiService) {
        this.passengerPool = passengerPool;
        this.driverPool = driverPool;
        this.operatorPool = operatorPool;
        this.orderTaxiService = orderTaxiService;
    }

    public void runSimulation(int numberOfPassengers, int numberOfDrivers) {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfPassengers);

        // Запуск операторов
        for (int i = 0; i < 5; i++) {
            operatorPool.processOrders();  // каждый поток — один оператор
        }

        // Создание и добавление водителей
        for (int i = 0; i < numberOfDrivers; i++) {
            LOGGER.info(driverPool.createRandomDriver().toString());
        }

        // Создание и добавление пассажиров
        for (int i = 0; i < numberOfPassengers; i++) {
            Passenger passenger = passengerPool.createPassenger(orderTaxiService);
            executorService.submit(passenger); // Пассажиры работают в потоках
        }

        LOGGER.info("Созданы пассажиры:");
        passengerPool.getAllPassengers().forEach(passenger ->
                LOGGER.info(passenger.getProfile().getName())
        );

        executorService.shutdown();

        // Ожидание завершения всех потоков
        boolean allFinished = false;
        while (!allFinished) {
            allFinished = passengerPool.getAllPassengers().stream()
                    .allMatch(p -> p.getStatus().equals(PassengerStatus.FINISHED));

            try {
                Thread.sleep(1000); // Проверка состояния
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        operatorPool.shutdown();

        LOGGER.info("Все пассажиры завершили поездки.");
        LOGGER.info("Симуляция завершена!");
    }
}
