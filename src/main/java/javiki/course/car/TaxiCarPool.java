package javiki.course.car;

import javiki.course.PointCoordinates;
import javiki.course.driver.Driver;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaxiCarPool {
    private final Map<TaxiCarType, List<TaxiCar>> allCarsByType = new HashMap<>();
    private final Map<String, TaxiCar> carById = new HashMap<>();
    private static final Random RANDOM = new Random();

    private final List<TaxiCarType> carTypes = List.of(
            new TaxiCarType("SUV", 4, CarQuality.PREMIUM),
            new TaxiCarType("Sedan", 4, CarQuality.STANDARD),
            new TaxiCarType("Hatchback", 4, CarQuality.ECONOMY),
            new TaxiCarType("Coupe", 2, CarQuality.PREMIUM),
            new TaxiCarType("Convertible", 2, CarQuality.STANDARD)
    );

    public TaxiCar createRandomCar() {
        TaxiCarType carType = carTypes.get(RANDOM.nextInt(carTypes.size()));
        String carId = "Car-" + RANDOM.nextInt(10000);
        PointCoordinates startCoordinates = new PointCoordinates(RANDOM.nextInt(101), RANDOM.nextInt(101));
        TaxiCar taxiCar = new TaxiCar(carId, null, RANDOM.nextInt(1000), CarQuality.PREMIUM, startCoordinates, carType);
        taxiCar.setIsAvailable(new AtomicBoolean(true));
        addCar(taxiCar);
        return taxiCar;
    }

    public void addCar(TaxiCar taxiCar) {
        carById.put(taxiCar.getId(), taxiCar);
        allCarsByType.computeIfAbsent(taxiCar.getTaxiCarType(), k -> new ArrayList<>()).add(taxiCar);
    }

    public void removeCar(TaxiCar taxiCar) {
        carById.remove(taxiCar.getId());
        List<TaxiCar> list = allCarsByType.get(taxiCar.getTaxiCarType());
        if (list != null) {
            list.remove(taxiCar);
            if (list.isEmpty()) {
                allCarsByType.remove(taxiCar.getTaxiCarType());
            }
        }
    }

    public List<TaxiCar> getAvailableCarsByType(TaxiCarType type) {
        List<TaxiCar> cars = allCarsByType.getOrDefault(type, Collections.emptyList());
        List<TaxiCar> availableCars = new ArrayList<>();
        for (TaxiCar car : cars) {
            if (car.getIsAvailable() != null && car.getIsAvailable().get()) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    public TaxiCar getById(String id) {
        return carById.get(id);
    }

    public TaxiCarType getRandomCarType() {
        return carTypes.get(RANDOM.nextInt(carTypes.size()));
    }
}
