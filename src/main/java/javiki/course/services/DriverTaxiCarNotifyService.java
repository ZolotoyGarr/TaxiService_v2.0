package javiki.course.services;

import javiki.course.car.TaxiCar;
import javiki.course.driver.Driver;
import java.util.logging.Logger;

public class DriverTaxiCarNotifyService {
    private static final Logger LOGGER = Logger.getLogger(DriverTaxiCarNotifyService.class.getName());

    public boolean orderRequestToDriver(TaxiCar taxiCar) {
        Driver driver = taxiCar.getDriver();
        if (driver == null) {
            LOGGER.warning("Машина " + taxiCar.getId() + " не имеет водителя.");
            return false;
        }
        LOGGER.info("Уведомляем водителя " + driver.getProfile().getName() + " о новом заказе на машине " + taxiCar.getId());

        // Симуляция принятия/отказа с вероятностью (например 70% согласен)
        boolean accepted = Math.random() < 0.7;

        if (accepted) {
            LOGGER.info("Водитель " + driver.getProfile().getName() + " принял заказ.");
        } else {
            LOGGER.info("Водитель " + driver.getProfile().getName() + " отказался от заказа.");
        }

        return accepted;
    }

}
