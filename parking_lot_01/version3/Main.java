package parking_lot_01.version3;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Vehicle bike =
                new Vehicle("DL01AB1234", VehicleType.BIKE);

        ParkingSpot small =
                new ParkingSpot("S1", 1, SpotType.SMALL);

        ParkingSpot compact =
                new ParkingSpot("C1", 1, SpotType.COMPACT);

        ParkingFloor floor =
                new ParkingFloor(
                        1,
                        List.of(small, compact)
                );

        SpotAllocationStrategy allocationStrategy =
                new FirstAvailableSpotStrategy();

        PricingStrategy pricingStrategy =
                new HourlyPricingStrategy();

        DisplayBoard displayBoard =
                new DisplayBoard();

        ParkingLot parkingLot =
                new ParkingLot(
                        List.of(floor),
                        allocationStrategy,
                        pricingStrategy,
                        List.of(displayBoard)
                );

        // Park
        Ticket ticket = parkingLot.park(bike);

        System.out.println("Ticket ID: " + ticket.getId());
        System.out.println("Vehicle: " + ticket.getVehicle());
        System.out.println("Parked at: " + ticket.getSpot());

        // Unpark
        long price = parkingLot.unpark(ticket);

        System.out.println("Please pay: ₹" + price);
        System.out.println("Spot free: " + ticket.getSpot().isFree());
    }
}