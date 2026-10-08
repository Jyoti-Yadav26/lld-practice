package parking_lot_01.version3;

import java.util.List;

public final class SpotRules {

    private SpotRules() {
    }

    public static List<SpotType> allowed(VehicleType vehicleType) {

        return switch (vehicleType) {

            case BIKE ->
                    List.of(
                            SpotType.SMALL,
                            SpotType.COMPACT,
                            SpotType.LARGE
                    );

            case CAR ->
                    List.of(
                            SpotType.COMPACT,
                            SpotType.LARGE
                    );

            case TRUCK ->
                    List.of(
                            SpotType.LARGE
                    );
        };
    }
}
