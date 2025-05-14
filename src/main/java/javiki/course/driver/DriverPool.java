package javiki.course.driver;

import javiki.course.Profile;
import javiki.course.car.TaxiCar;
import javiki.course.car.TaxiCarPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DriverPool {
    private final List<Driver> driverPool = new ArrayList<>();
    private final TaxiCarPool taxiCarPool;

    private static final Logger LOGGER = Logger.getLogger(DriverPool.class.getName());

    public DriverPool(TaxiCarPool taxiCarPool) {
        this.taxiCarPool = taxiCarPool;
    }

    // Метод для создания случайного водителя с рандомным профилем
    public Driver createRandomDriver() {
        Profile randomProfile = Profile.generateRandomProfile();  // Генерация случайного профиля для водителя
        TaxiCar taxiCar = taxiCarPool.createRandomCar();  // Создание машины
        LOGGER.info("Водитель " + randomProfile.getName() + " создан с машиной: " + taxiCar.getId() + " Координаты: " + taxiCar.getCoordinates());
        Driver driver = new Driver(randomProfile);
        driver.setTaxiCar(taxiCar);  // Устанавливаем машину водителю
        taxiCar.setDriver(driver);
        addDriver(driver);  // Добавляем водителя в пул
        return driver;
    }

    // Метод для добавления водителя в пул
    public void addDriver(Driver driver) {
        driverPool.add(driver);
    }

    // Метод для удаления водителя из пула
    public void removeDriver(Driver driver) {
        driverPool.remove(driver);
    }

    public List<Driver> getAvailableDrivers() {
        return Collections.synchronizedList(
                driverPool.stream()
                        .filter(driver -> {
                            TaxiCar car = driver.getTaxiCar();
                            return car != null && car.getIsAvailable() != null && car.getIsAvailable().get();
                        })
                        .collect(Collectors.toList())
        );
    }


    // Метод для получения всех водителей
    public List<Driver> getAllDrivers() {
        return new ArrayList<>(driverPool);
    }

    @Override
    public String toString() {
        return "DriverPool{" +
                "driverPool=" + driverPool +
                '}';
    }
}
