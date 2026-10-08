package parking_lot_01.version3;

import java.util.List;
import java.util.Optional;

public class FirstAvailableSpotStrategy implements SpotAllocationStrategy{

    @Override
    public Optional<ParkingSpot> allocate(List<ParkingFloor> floors, Vehicle vehicle) {
        for(ParkingFloor sp:floors){
            Optional<ParkingSpot> spot= sp.findAvailableSpot(vehicle);
            if(spot.isPresent()){
                return spot;
            }
        }
        return Optional.empty();
    }
}
