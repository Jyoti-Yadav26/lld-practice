package parking_lot_01.Version4;

import java.util.List;
import java.util.Optional;

public class FirstAvailableSpotStrategy
        implements SpotAllocationStrategy {

    @Override
    public Optional<ParkingSpot> allocate(
            List<ParkingFloor> floors,
            Vehicle vehicle
    ) {
        for (ParkingFloor floor : floors) {
            for (SpotType type : SpotRules.allowed(vehicle.type())) {

                Optional<ParkingSpot> spot =
                        floor.claimSpot(type);

                if (spot.isPresent()) {
                    return spot;
                }
            }
        }

        return Optional.empty();
    }
}

