package javiki.course.operator;

import javiki.course.DistanceCalculator;
import javiki.course.driver.Driver;
import javiki.course.driver.DriverPool;
import javiki.course.passenger.Passenger;
import javiki.course.request.OrderTaxiRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class OperatorPool {
    private final Semaphore operators = new Semaphore(5, true);  // 5 операторов
    //изменил List на Queue (List O(n) из-за сдвига элементов, а Queue O(1), так как берется последний
    //Почему нет "сдвига", как у ArrayList?
    //Потому что ты используешь LinkedList, а она устроена не как массив, а как цепочка связанных объектов (узлов).
    //
    //🧩 Внутреннее устройство LinkedList:
    //[HEAD] → [elem1] → [elem2] → [elem3] → ... → [TAIL]
    //Удаление головы (poll) — просто сдвиг "указателя" на следующий элемент.
    //
    //Нет необходимости сдвигать все остальные, как в массиве (ArrayList).
    //
    //🔧 Операции:
    //
    //addLast() — O(1)
    //
    //poll() или removeFirst() — O(1)
    private final Queue<OrderTaxiRequest> pendingOrders = new LinkedList<>();
    private final DriverPool driverPool;
    private volatile boolean running = true;

    private static final Logger LOGGER = Logger.getLogger(OperatorPool.class.getName());

    public OperatorPool(DriverPool driverPool) {
        this.driverPool = driverPool;
    }

    public void processOrders() {
        new Thread(() -> {
            while (running) {
                OrderTaxiRequest order;
                // Извлекаем заказ из очереди — потокобезопасно
                synchronized (this) {
                    while (pendingOrders.isEmpty() && running) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    if (!running) return;

                    // Забираем заказ из очереди
                    order = pendingOrders.poll();
                }

                try {
                    // Ждем оператора
                    operators.acquire();

                    // Обрабатываем заказ
                    processSingleOrder(order);

                    // Симулируем звонок 20 секунд
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    releaseOperator();
                }
            }
        }).start();
    }

    private void processSingleOrder(OrderTaxiRequest order) {
        Passenger passenger = order.getPassenger();

        List<Driver> availableDrivers = driverPool.getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            LOGGER.warning("Нет доступных водителей для пассажира: " + passenger.getProfile().getName());
            requeueOrder(order);
            return;
        }

        Driver driver = findNearestAvailableDriver(passenger, availableDrivers);
        if (driver == null || !driver.compareAndSetAvailable()) {
            LOGGER.warning("Водитель уже занят, возвращаем заказ в очередь.");
            requeueOrder(order);
            return;
        }

        LOGGER.info("Оператор назначил водителя " + driver.getProfile().getName() +
                " пассажиру " + passenger.getProfile().getName());

        order.setDriver(driver);
        driver.setCurrentOrder(order);
        order.setOrderStatus(javiki.course.request.OrderStatus.ACCEPTED);
    }


    // Поиск ближайшего доступного водителя
    private Driver findNearestAvailableDriver(Passenger passenger, List<Driver> availableDrivers) {
        double minDistance = Double.MAX_VALUE;
        Driver nearestDriver = null;

        for (Driver driver : availableDrivers) {
            double distance = DistanceCalculator.calculateDistance(
                    passenger.getPassengerLocation(),
                    driver.getTaxiCar().getCoordinates()
            );

            if (distance < minDistance && driver.getIsAvailable().get()) {
                minDistance = distance;
                nearestDriver = driver;
            }
        }
        return nearestDriver;
    }

    //возвращает ордер обратно в pendingOrders
    private void requeueOrder(OrderTaxiRequest order) {
        synchronized (this) {
            pendingOrders.add(order);
            notifyAll();
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void receiveOrder(OrderTaxiRequest  order) {
        pendingOrders.add(order);
        notifyAll(); // разбудить поток оператора, если он ждал
    }

    public boolean acquireOperator() {
        return operators.tryAcquire();
    }

    public void releaseOperator() {
        operators.release();
    }

    public void shutdown() {
        running = false;
        synchronized (this) {
            notifyAll(); // разбудить потоки, чтобы они завершились
        }
    }
}
