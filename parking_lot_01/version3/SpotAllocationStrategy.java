package parking_lot_01.version3;

import java.util.List;
import java.util.Optional;

public interface SpotAllocationStrategy {

    Optional<ParkingSpot> allocate(
            List<ParkingFloor> floors,
            Vehicle vehicle
    );
}
