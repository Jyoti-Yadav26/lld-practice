package parking_lot_01.Version4;

import java.time.Instant;

public class Ticket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final Instant entryTime;
    private TicketStatus status;
    private long amountPaid;

    public Ticket(String id, Vehicle vehicle, ParkingSpot spot, Instant entryTime) {
        this.ticketId = id;
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = entryTime;
        this.status = TicketStatus.ACTIVE;
        this.amountPaid =0;
    }
    public synchronized void markPaid(long amount){
        if(status!=TicketStatus.ACTIVE){
            throw new IllegalStateException("Only an active ticket can be paid");
        }
        if(amount<0){
            throw new IllegalStateException("Payment amount cannot be negative");
        }
        this.amountPaid=amount;
        this.status=TicketStatus.PAID;
    }
    public synchronized void close(){
        if(status!=TicketStatus.PAID){
            throw new IllegalStateException("Ticket must be paid before closing");
        }
        this.status=TicketStatus.CLOSED;
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

    public TicketStatus getStatus() {
        return status;
    }
    public TicketStatus setStatus(TicketStatus stat) {
        status=stat;
        return status;
    }

    public long getAmountPaid() {
        return amountPaid;
    }
}
