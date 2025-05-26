package javiki.course.car;

import javiki.course.PointCoordinates;
import javiki.course.driver.Driver;

import java.util.concurrent.atomic.AtomicBoolean;

public class TaxiCar {
    private final String id;
    private final int number;
    private final PointCoordinates coordinates;
    private final TaxiCarType taxiCarType;
    private Driver driver;
    private AtomicBoolean isAvailable;

    public TaxiCar(String id, Driver driver, int number, PointCoordinates coordinates, TaxiCarType taxiCarType) {
        this.id = id;
        this.driver = driver;
        this.number = number;
        this.coordinates = coordinates;
        this.taxiCarType = taxiCarType;
    }

    // Конструктор копирования
    public TaxiCar(TaxiCar other) {
        this.id = other.id;
        this.driver = other.driver;
        this.number = other.number;
        this.coordinates = new PointCoordinates(other.coordinates.getX(), other.coordinates.getY());
        this.taxiCarType = other.taxiCarType;
    }

    public PointCoordinates getCoordinates() {
        return coordinates;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }

    public String getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public TaxiCarType getTaxiCarType() {
        return taxiCarType;
    }


    public AtomicBoolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(AtomicBoolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
