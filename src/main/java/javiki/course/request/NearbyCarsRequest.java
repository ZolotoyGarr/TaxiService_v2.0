package javiki.course.request;

import javiki.course.PointCoordinates;
import javiki.course.car.TaxiCarType;
import javiki.course.passenger.Passenger;

public class NearbyCarsRequest extends PassengerRequest{
    private final TaxiCarType taxiCarType;
    private final PointCoordinates requestLocation;

    public NearbyCarsRequest(Passenger passenger, TaxiCarType taxiCarType, PointCoordinates requestLocation) {
        super(passenger);
        this.taxiCarType = taxiCarType;
        this.requestLocation = requestLocation;
    }

    public TaxiCarType getTaxiCarType() {
        return taxiCarType;
    }

    public PointCoordinates getRequestLocation() {
        return requestLocation;
    }
}
