package parking_lot_01.Version4;


public record Vehicle(String plate, VehicleType type) {

    public Vehicle {
        if (plate == null || plate.isBlank()) {
            throw new IllegalArgumentException(
                    "Vehicle plate cannot be blank"
            );
        }

        if (type == null) {
            throw new IllegalArgumentException(
                    "Vehicle type cannot be null"
            );
        }
    }
}

