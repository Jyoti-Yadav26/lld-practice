package parking_lot_01.Version4;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {

        // 1. Create parking spots
        List<ParkingSpot> spots = List.of(
                new ParkingSpot("S1", 1, SpotType.SMALL),
                new ParkingSpot("S2", 1, SpotType.COMPACT),
                new ParkingSpot("S3", 1, SpotType.LARGE),
                new ParkingSpot("S4", 2, SpotType.COMPACT),
                new ParkingSpot("S5", 2, SpotType.LARGE)
        );

        // 2. Create floors
        ParkingFloor floor1 = new ParkingFloor(
                1,
                spots.stream()
                        .filter(spot -> spot.getFloor() == 1)
                        .toList()
        );

        ParkingFloor floor2 = new ParkingFloor(
                2,
                spots.stream()
                        .filter(spot -> spot.getFloor() == 2)
                        .toList()
        );

        List<ParkingFloor> floors = List.of(floor1, floor2);

        // 3. Configure allocation and pricing
        SpotAllocationStrategy allocationStrategy =
                new BestFitLowestFloor();

        PricingStrategy pricingStrategy =
                new HourlyPricingStrategy(
                        Map.of(
                                VehicleType.BIKE, 20L,
                                VehicleType.CAR, 50L,
                                VehicleType.TRUCK, 100L
                        )
                );

        // 4. Configure payment
        PaymentProcessor paymentProcessor = new UpiPayment();

        // 5. Create the parking lot
        ParkingLot parkingLot = new ParkingLot(
                floors,
                allocationStrategy,
                pricingStrategy,
                paymentProcessor,
                Clock.systemUTC()
        );

        // 6. Register the display board
        DisplayBoard displayBoard = new DisplayBoard();
        parkingLot.addListener(displayBoard);

        // 7. Park a vehicle
        Vehicle vehicle = new Vehicle(
                "DL01AB1234",
                VehicleType.BIKE
        );

        Optional<Ticket> result = parkingLot.park(vehicle);

        if (result.isEmpty()) {
            System.out.println("No suitable parking spot available.");
            return;
        }

        Ticket ticket = result.get();

        System.out.println("\nVehicle parked successfully.");
        System.out.println("Ticket ID: " + ticket.getId());
        System.out.println("Spot ID: " + ticket.getSpot().getId());
        System.out.println("Status: " + ticket.getStatus());

        // 8. Process payment
        long amount = parkingLot.pay(ticket.getId());

        System.out.println("\nPayment successful.");
        System.out.println("Amount paid: Rs. " + amount);
        System.out.println("Status: " + ticket.getStatus());

        // 9. Exit the parking lot
        parkingLot.unpark(ticket);

        System.out.println("\nVehicle exited successfully.");
        System.out.println("Final status: " + ticket.getStatus());

        // 10. Verify availability
        System.out.println(
                "\nFree SMALL spots on floor 1: "
                        + displayBoard.getFreeSpotCount(
                        1, SpotType.SMALL
                )
        );
    }
}
