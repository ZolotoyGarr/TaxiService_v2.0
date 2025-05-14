package javiki.course.services;

import javiki.course.car.TaxiCar;
import javiki.course.driver.Driver;
import java.util.logging.Logger;

public class DriverTaxiCarNotifyService {
    private static final Logger logger = Logger.getLogger(DriverTaxiCarNotifyService.class.getName());

    public boolean orderRequestToDriver(TaxiCar taxiCar) {
        Driver driver = taxiCar.getDriver();
        if (driver == null) {
            logger.warning("Машина " + taxiCar.getId() + " не имеет водителя.");
            return false;
        }
        // Здесь можно добавить логику уведомления водителя и его решения
        logger.info("Уведомляем водителя " + driver.getProfile().getName() + " о новом заказе на машине " + taxiCar.getId());
        // Предположим, водитель всегда принимает заказ
        return true;
    }
}
