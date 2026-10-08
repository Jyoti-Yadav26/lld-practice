package parking_lot_01.version2;

public class Main {

    public static void main(String[] args) {

        Vehicle bike = new Vehicle("DL01AB1234", VehicleType.BIKE);
        Vehicle car = new Vehicle("DL02XY5678", VehicleType.CAR);

        ParkingSpot spot = new ParkingSpot(
                "S1",
                1,
                SpotType.SMALL
        );

        System.out.println("Spot free: " + spot.isFree());

        spot.occupy(bike);

        System.out.println("Spot free: " + spot.isFree());
        System.out.println("Parked vehicle: " + spot.getParkedVehicle());

        spot.release();

        System.out.println("Spot free: " + spot.isFree());

        System.out.println(
                "Car can use: " + SpotRules.allowed(VehicleType.CAR)
        );
    }
}
