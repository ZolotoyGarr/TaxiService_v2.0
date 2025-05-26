package javiki.course.driver;

import javiki.course.Profile;
import javiki.course.car.TaxiCar;
import javiki.course.request.OrderTaxiRequest;

import java.util.concurrent.atomic.AtomicBoolean;

public class Driver {
    private final Profile profile;
    private TaxiCar taxiCar;
    private OrderTaxiRequest currentOrder;

    private final AtomicBoolean isAvailable = new AtomicBoolean(true);
    private int orderCounter = 0;

    public Driver(Profile profile) {
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public TaxiCar getTaxiCar() {
        return taxiCar;
    }

    public void setTaxiCar(TaxiCar taxiCar) {
        this.taxiCar = taxiCar;
    }

    public OrderTaxiRequest getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(OrderTaxiRequest currentOrder) {
        this.currentOrder = currentOrder;
    }


    public boolean compareAndSetAvailable() {
        return isAvailable.compareAndSet(true, false);
    }

    public AtomicBoolean getIsAvailable() {
        return isAvailable;
    }

    public int getOrderCounter() {
        return orderCounter;
    }

    public void incrementOrderCounter() {
        this.orderCounter++;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "name='" + profile.getName() + '\'' +
                ", car=" + (taxiCar != null ? taxiCar.getId() : "Нет машины") +
                '}';
    }
}
