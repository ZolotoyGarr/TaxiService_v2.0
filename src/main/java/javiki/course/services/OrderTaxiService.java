package javiki.course.services;

import javiki.course.car.TaxiCar;
import javiki.course.driver.Driver;
import javiki.course.driver.DriverDetails;
import javiki.course.driver.DriverPool;
import javiki.course.request.NearbyCarsRequest;
import javiki.course.result.NearbyCarsResult;
import javiki.course.result.OrderTaxiResult;
import javiki.course.car.TaxiCarType;

import java.util.List;
import java.util.logging.Logger;

public class OrderTaxiService {
    private static final Logger logger = Logger.getLogger(OrderTaxiService.class.getName());

    private final NearbyCarsService nearbyCarsService;
    private final DriverTaxiCarNotifyService notifyService;

    public OrderTaxiService(NearbyCarsService nearbyCarsService,
                            DriverTaxiCarNotifyService notifyService) {

        this.nearbyCarsService = nearbyCarsService;
        this.notifyService = notifyService;
    }

    public OrderTaxiResult orderTaxi(NearbyCarsRequest request) {
        NearbyCarsResult nearbyResult = nearbyCarsService.findNearbyCars(request);
        if (!nearbyResult.isFound()) {
            return new OrderTaxiResult(false, null, null, null);
        }

        List<TaxiCar> cars = nearbyResult.getAvailableCars();
        for (TaxiCar car : cars) {
            if (notifyService.orderRequestToDriver(car)) {
                Driver driver = car.getDriver();
                logger.info("Водитель " + driver.getProfile().getName() + " принял заказ на машине " + car.getId());

                return new OrderTaxiResult(true, car, driver.getProfile(), new DriverDetails("2020-01-01", List.of(), driver.getOrderCounter()));
            }
        }

        logger.warning("Ни один водитель не принял заказ.");
        return new OrderTaxiResult(false, null, null, null);
    }

    public TaxiCarType getRandomCarType() {
        return nearbyCarsService.getTaxiCarPool().getRandomCarType();
    }
}

