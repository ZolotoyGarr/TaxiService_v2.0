package javiki.course.result;

import javiki.course.Profile;
import javiki.course.car.TaxiCar;
import javiki.course.driver.DriverDetails;

public class OrderTaxiResult {
    private final boolean isFound;
    private TaxiCar car;
    private Profile driverProfile;
    private DriverDetails driverDetails;

    public OrderTaxiResult(boolean isFound, TaxiCar car, Profile driverProfile, DriverDetails driverDetails) {
        this.isFound = isFound;
        this.car = car;
        this.driverProfile = driverProfile;
        this.driverDetails = driverDetails;
    }

    public boolean isFound() {
        return isFound;
    }

    public TaxiCar getCar() {
        return car;
    }

    public void setCar(TaxiCar car) {
        this.car = car;
    }

    public Profile getDriverProfile() {
        return driverProfile;
    }

    public void setDriverProfile(Profile driverProfile) {
        this.driverProfile = driverProfile;
    }

    public DriverDetails getDriverDetails() {
        return driverDetails;
    }

    public void setDriverDetails(DriverDetails driverDetails) {
        this.driverDetails = driverDetails;
    }
}
