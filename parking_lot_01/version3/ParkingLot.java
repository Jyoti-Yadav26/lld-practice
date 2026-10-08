package parking_lot_01.version3;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class ParkingLot {
    private final List<ParkingFloor> floors;
    private final SpotAllocationStrategy allocationStrategy;
    private final PricingStrategy pricingStrategy;
    private final List<SpotAvailabilityListener> listeners;

    public ParkingLot(List<ParkingFloor> floors, SpotAllocationStrategy allocationStrategy, PricingStrategy pricingStrategy, List<SpotAvailabilityListener> listeners) {
        this.floors = floors;
        this.allocationStrategy = allocationStrategy;
        this.pricingStrategy = pricingStrategy;
        this.listeners = listeners;
    }

    public Ticket park(Vehicle vehicle){
        Optional<ParkingSpot> spot = allocationStrategy.allocate(floors,vehicle);

        if(spot.isEmpty()){
            throw new IllegalStateException("No Free Spot Available");
        }

        ParkingSpot parkingSpot=spot.get();
        parkingSpot.occupy(vehicle);
        notifyAvailabilityChange(parkingSpot);

        return new Ticket("1",vehicle,parkingSpot,Instant.now());
    }
    public long unpark(Ticket ticket){
        Instant exit = Instant.now();

        long price = pricingStrategy.calculatePrice(ticket,exit);
        ParkingSpot spot = ticket.getSpot();
        spot.release();
        notifyAvailabilityChange(spot);

        return price;
    }
    private ParkingFloor findFloor(ParkingSpot targetSpot) {

        for (ParkingFloor floor : floors) {
            if (floor.getSpots().contains(targetSpot)) {
                return floor;
            }
        }

        throw new IllegalStateException("Floor not found");
    }

    private void notifyAvailabilityChange(ParkingSpot spot) {

        ParkingFloor floor = findFloor(spot);

        int freeCount = floor.getFreeSpotCount(spot.getType());

        for (SpotAvailabilityListener listener : listeners) {
            listener.onAvailabilityChange(
                    floor.getFloorNumber(),
                    spot.getType(),
                    freeCount
            );
        }
    }
}
