package javiki.course.request;

import javiki.course.passenger.Passenger;

import java.time.Instant;
import java.util.UUID;

public class PassengerRequest {
    private final String id;
    private final Instant createdAt;
    private final Passenger passenger;

    public PassengerRequest(Passenger passenger) {
        this.id = UUID.randomUUID().toString().substring(0, 5);
        this.createdAt = Instant.now();
        this.passenger = passenger;
    }

    public String getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Passenger getPassenger() {
        return passenger;
    }
}
