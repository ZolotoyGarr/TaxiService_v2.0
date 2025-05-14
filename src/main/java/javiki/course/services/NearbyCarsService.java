package javiki.course.services;

import javiki.course.car.TaxiCar;
import javiki.course.car.TaxiCarPool;
import javiki.course.car.TaxiCarType;
import javiki.course.request.NearbyCarsRequest;
import javiki.course.result.NearbyCarsResult;
import javiki.course.DistanceCalculator;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class NearbyCarsService {
    private static final Logger logger = Logger.getLogger(NearbyCarsService.class.getName());

    private final TaxiCarPool taxiCarPool;
    private final int MAX_DISTANCE;

    public NearbyCarsService(TaxiCarPool taxiCarPool, int maxDistance) {
        this.taxiCarPool = taxiCarPool;
        this.MAX_DISTANCE = maxDistance;
    }

    public NearbyCarsResult findNearbyCars(NearbyCarsRequest request) {
        TaxiCarType requestedType = request.getTaxiCarType();
        List<TaxiCar> carsOfType = taxiCarPool.getAvailableCarsByType(requestedType);

        List<TaxiCar> nearbyCars = carsOfType.stream()
                .filter(car -> DistanceCalculator.calculateDistance(car.getCoordinates(), request.getRequestLocation()) <= MAX_DISTANCE)
                .sorted(Comparator.comparingDouble(car -> DistanceCalculator.calculateDistance(car.getCoordinates(), request.getRequestLocation())))
                .toList();

        boolean found = !nearbyCars.isEmpty();
        int averageWaitTime = 0;
        if (found) {
            double minDistance = DistanceCalculator.calculateDistance(nearbyCars.get(0).getCoordinates(), request.getRequestLocation());
            averageWaitTime = (int) Math.ceil(minDistance / 0.5);
            logger.info("Найдено " + nearbyCars.size() + " машин типа " + requestedType.getName() + " в радиусе " + MAX_DISTANCE);
        } else {
            logger.warning("Нет доступных машин типа " + requestedType.getName() + " в радиусе " + MAX_DISTANCE);
        }

        return new NearbyCarsResult(nearbyCars, found, averageWaitTime);
    }

    public TaxiCarPool getTaxiCarPool() {
        return taxiCarPool;
    }
}
