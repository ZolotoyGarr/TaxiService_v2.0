package javiki.course;

import javiki.course.car.TaxiCarPool;
import javiki.course.driver.DriverPool;
import javiki.course.passenger.PassengerPool;
import javiki.course.services.DriverTaxiCarNotifyService;
import javiki.course.services.NearbyCarsService;
import javiki.course.services.SimulationService;
import javiki.course.services.OrderTaxiService;

public class Main {
    public static void main(String[] args) {
        PassengerPool passengerPool = new PassengerPool();
        TaxiCarPool taxiCarPool = new TaxiCarPool();
        DriverPool driverPool = new DriverPool(taxiCarPool);
        NearbyCarsService nearbyCarsService = new NearbyCarsService(taxiCarPool, 100);
        DriverTaxiCarNotifyService notifyService = new DriverTaxiCarNotifyService();
        OrderTaxiService orderTaxiService = new OrderTaxiService(nearbyCarsService, notifyService);
        SimulationService simulationService = new SimulationService(passengerPool, driverPool, orderTaxiService);
        simulationService.runSimulation(6, 3);
    }
}