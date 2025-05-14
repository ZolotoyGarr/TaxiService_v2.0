package javiki.course.passenger;

import javiki.course.*;
import javiki.course.request.OrderTaxiRequest;
import javiki.course.request.NearbyCarsRequest;
import javiki.course.result.NearbyCarsResult;
import javiki.course.services.OrderTaxiService;
import javiki.course.driver.Driver;
import javiki.course.car.TaxiCar;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class Passenger implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Passenger.class.getName());
    private Profile profile;
    private PassengerStatus status = PassengerStatus.WAITING_FOR_ACCEPTANCE;
    private PointCoordinates passengerLocation;
    private OrderTaxiRequest currentRide;
    private final OrderTaxiService orderTaxiService;
    private static final Random RANDOM = new Random();

    public Passenger(OrderTaxiService orderTaxiService) {
        this.orderTaxiService = orderTaxiService;
        this.profile = new Profile("Пассажир " + UUID.randomUUID().toString().substring(0, 5));
        this.passengerLocation = new PointCoordinates(RANDOM.nextInt(101), RANDOM.nextInt(101));
    }

    @Override
    public void run() {
        try {
            LOGGER.info(profile.getName() + ": Начинает поиск машины...");
            boolean rideAccepted = false;

            while (!rideAccepted) {
                // Создаём запрос с рандомным типом машины и текущим местоположением
                NearbyCarsRequest request = new NearbyCarsRequest(
                        this, orderTaxiService.getRandomCarType(), passengerLocation
                );

                // Вызываем метод нового сервиса, который сам найдёт и уведомит водителя
                var orderResult = orderTaxiService.orderTaxi(request);

                if (!orderResult.isFound()) {
                    LOGGER.warning(profile.getName() + ": Машины не найдены. Повторный поиск через 2 секунды...");
                    Thread.sleep(2000);
                    continue;
                }

                TaxiCar car = orderResult.getCar();
                Driver driver = car.getDriver();

                // Создаём заказ с fromPoint и toPoint (считаем, что toPoint генерируется где-то в сервисе)
                PointCoordinates fromPoint = passengerLocation;
                PointCoordinates toPoint = new PointCoordinates(
                        RANDOM.nextInt(101), RANDOM.nextInt(101)
                );

                OrderTaxiRequest order = new OrderTaxiRequest(this, car.getTaxiCarType(), fromPoint, toPoint);
                order.setDriver(driver);
                driver.setCurrentOrder(order);
                this.currentRide = order;

                LOGGER.info(profile.getName() + ": Водитель " + driver.getProfile().getName() + " принял заказ.");

                // Эмуляция времени пути до пассажира
                double distanceToPassenger = DistanceCalculator.calculateDistance(car.getCoordinates(), fromPoint);
                long timeToPassengerMs = (long)((distanceToPassenger / 20.0) * 1000);
                Thread.sleep(timeToPassengerMs);

                setStatus(PassengerStatus.IN_RIDE);
                LOGGER.info(profile.getName() + ": В пути...");

                // Эмуляция самой поездки
                double distanceRide = DistanceCalculator.calculateDistance(fromPoint, toPoint);
                long timeRideMs = (long)((distanceRide / 20.0) * 1000);
                Thread.sleep(timeRideMs);

                setStatus(PassengerStatus.FINISHED);

                // Освобождаем машину — теперь это должен делать сервис/водитель
                car.getIsAvailable().set(true);
                driver.incrementOrderCounter();

                LOGGER.info(profile.getName() + ": Поездка завершена.");
                rideAccepted = true;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warning(profile.getName() + ": Поток был прерван.");
        }
    }


    // Геттеры и сеттеры
    public Profile getProfile() {
        return profile;
    }

    public PassengerStatus getStatus() {
        return status;
    }

    public void setStatus(PassengerStatus status) {
        this.status = status;
    }

    public PointCoordinates getPassengerLocation() {
        return passengerLocation;
    }

    public void setPassengerLocation(PointCoordinates passengerLocation) {
        this.passengerLocation = passengerLocation;
    }

    public OrderTaxiRequest getCurrentOrder() {
        return currentRide;
    }

    public void setCurrentOrder(OrderTaxiRequest currentRide) {
        this.currentRide = currentRide;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "name='" + profile.getName() + '\'' +
                ", location=" + passengerLocation +
                '}';
    }
}
