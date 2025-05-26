package javiki.course.passenger;

import javiki.course.*;
import javiki.course.operator.OperatorPool;
import javiki.course.request.OrderTaxiRequest;
import javiki.course.request.NearbyCarsRequest;
import javiki.course.services.OrderTaxiService;
import javiki.course.driver.Driver;
import javiki.course.car.TaxiCar;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

public class Passenger implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Passenger.class.getName());
    private Profile profile;
    private PassengerStatus status = PassengerStatus.WAITING_FOR_ACCEPTANCE;
    private PointCoordinates passengerLocation;
    private OrderTaxiRequest currentRide;
    private final OrderTaxiService orderTaxiService;
    private static final Random RANDOM = new Random();
    private final OperatorPool operatorPool;

    public Passenger(OrderTaxiService orderTaxiService, OperatorPool operatorPool) {
        this.orderTaxiService = orderTaxiService;
        this.operatorPool = operatorPool;
        this.profile = new Profile("Пассажир " + UUID.randomUUID().toString().substring(0, 5));
        this.passengerLocation = new PointCoordinates(RANDOM.nextInt(101), RANDOM.nextInt(101));
    }


    @Override
    public void run() {
        try {
            LOGGER.info(profile.getName() + ": Запрашивает такси у оператора...");

            boolean rideAccepted = false;

            while (!rideAccepted) {
                // Формируем запрос
                NearbyCarsRequest request = new NearbyCarsRequest(
                        this, orderTaxiService.getRandomCarType(), passengerLocation
                );
                OrderTaxiRequest order = new OrderTaxiRequest(this, request.getTaxiCarType(), passengerLocation,
                        new PointCoordinates(RANDOM.nextInt(101), RANDOM.nextInt(101)));

                operatorPool.receiveOrder(order);
                this.currentRide = order; // сохранить ссылку, чтобы потом ждать назначения водителя

                // Ожидаем, пока оператор присвоит водителя (можно просто подождать и проверять наличие driver'а)
                while (currentRide == null || currentRide.getDriver() == null) {
                    Thread.sleep(1000);
                }

                Driver driver = currentRide.getDriver();
                TaxiCar car = driver.getTaxiCar();

                LOGGER.info(profile.getName() + ": Назначен водитель " + driver.getProfile().getName());

                PointCoordinates fromPoint = passengerLocation;
                PointCoordinates toPoint = currentRide.getToPoint();

                // Эмуляция ожидания машины
                double distanceToPassenger = DistanceCalculator.calculateDistance(car.getCoordinates(), fromPoint);
                Thread.sleep((long) ((distanceToPassenger / 20.0) * 1000));

                setStatus(PassengerStatus.IN_RIDE);
                LOGGER.info(profile.getName() + ": В пути...");

                // Поездка
                double rideDistance = DistanceCalculator.calculateDistance(fromPoint, toPoint);
                Thread.sleep((long) ((rideDistance / 20.0) * 1000));

                setStatus(PassengerStatus.FINISHED);
                car.getIsAvailable().set(true);
                driver.incrementOrderCounter();
                driver.getIsAvailable().set(true); // <- ВОЗВРАЩАЕМ ВОДИТЕЛЯ В ПУЛ ДОСТУПНЫХ
                driver.setCurrentOrder(null);

                LOGGER.info(profile.getName() + ": Поездка завершена.");
                rideAccepted = true;
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warning(profile.getName() + ": Прерван поток.");
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
