package parking_lot_01.version3;


import java.util.List;
import java.util.Optional;

public class ParkingFloor {
    private final int floorNumber;
    private final List<ParkingSpot> spots;

    public ParkingFloor(int floorNumber, List<ParkingSpot> spots) {
        this.floorNumber = floorNumber;
        this.spots = spots;
    }

    public Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle){
        for (int i = 0; i < spots.size(); i++) {
            if(spots.get(i).isFree()){
                if(SpotRules.allowed(vehicle.type()).contains(spots.get(i).getType())){
                    return Optional.of(spots.get(i));
                }
            }
        }
        return Optional.empty();
    }
    public int getFreeSpotCount(SpotType spotType){
        int count=0;
        for(ParkingSpot sp:spots){
            if(sp.isFree()){
                if(sp.getType()==spotType){
                    count++;
                }
            }
        }
        return count;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }
}
