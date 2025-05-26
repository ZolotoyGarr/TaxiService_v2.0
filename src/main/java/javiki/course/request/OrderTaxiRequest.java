package javiki.course.request;

import javiki.course.PointCoordinates;
import javiki.course.car.TaxiCarType;
import javiki.course.driver.Driver;
import javiki.course.passenger.Passenger;

import java.time.Instant;
import java.time.LocalDateTime;

public class OrderTaxiRequest extends PassengerRequest {
    private final TaxiCarType taxiCarType;
    private final PointCoordinates fromPoint;
    private final PointCoordinates toPoint;
    private Driver driver;
    private OrderStatus orderStatus;
    private int attemptCount = 0;

    public OrderTaxiRequest(Passenger passenger, TaxiCarType taxiCarType, PointCoordinates fromPoint, PointCoordinates toPoint) {
        super(passenger);
        this.taxiCarType = taxiCarType;
        this.fromPoint = fromPoint;
        this.toPoint = toPoint;
    }

    public int getAttemptCount() {
        return attemptCount;
    }

    public void incrementAttemptCount() {
        this.attemptCount++;
    }

    public TaxiCarType getTaxiCarType() {
        return taxiCarType;
    }

    public PointCoordinates getFromPoint() {
        return fromPoint;
    }

    public PointCoordinates getToPoint() {
        return toPoint;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

}
