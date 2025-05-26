package javiki.course;

import javiki.course.car.TaxiCarPool;
import javiki.course.driver.DriverPool;
import javiki.course.operator.OperatorPool;
import javiki.course.passenger.PassengerPool;
import javiki.course.services.DriverTaxiCarNotifyService;
import javiki.course.services.NearbyCarsService;
import javiki.course.services.SimulationService;
import javiki.course.services.OrderTaxiService;

public class Main {
    public static void main(String[] args) {
        TaxiCarPool taxiCarPool = new TaxiCarPool();
        DriverPool driverPool = new DriverPool(taxiCarPool);
        NearbyCarsService nearbyCarsService = new NearbyCarsService(taxiCarPool, 100);
        DriverTaxiCarNotifyService notifyService = new DriverTaxiCarNotifyService();
        OrderTaxiService orderTaxiService = new OrderTaxiService(nearbyCarsService, notifyService);
        OperatorPool operatorPool = new OperatorPool(driverPool);
        PassengerPool passengerPool = new PassengerPool(operatorPool);
        SimulationService simulationService = new SimulationService(passengerPool, driverPool, operatorPool, orderTaxiService);

        simulationService.runSimulation(6, 3);
    }
}