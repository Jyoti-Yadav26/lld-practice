package parking_lot_01.Version4;

import java.util.List;
import java.util.Optional;

public class BestFitLowestFloor implements SpotAllocationStrategy {

    @Override
    public Optional<ParkingSpot> allocate(
            List<ParkingFloor> floors,
            Vehicle vehicle) {

        // Try spot types from smallest to largest.
        for (SpotType type : SpotRules.allowed(vehicle.type())) {

            // For each type, search floors in ascending order.
            for (ParkingFloor floor : floors) {

                Optional<ParkingSpot> spot = floor.claimSpot(type);

                if (spot.isPresent()) {
                    return spot;
                }
            }
        }

        return Optional.empty();
    }
}
