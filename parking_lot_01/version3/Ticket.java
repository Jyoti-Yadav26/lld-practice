package parking_lot_01.version3;

import java.time.Instant;
import java.util.Optional;

public class Ticket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final Instant entryTime;

    public Ticket(String id, Vehicle vehicle, ParkingSpot spot, Instant entryTime) {
        this.ticketId = id;
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = entryTime;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public Instant getEntryTime() {
        return entryTime;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getId() {
        return ticketId;
    }
}
