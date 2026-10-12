package parking_lot_01.Version4;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParkingFloor {

    private final int floorNumber;

    private final Map<SpotType, Queue<ParkingSpot>> freeSpots =
            new EnumMap<>(SpotType.class);

    public ParkingFloor(int floorNumber, List<ParkingSpot> spots) {
        this.floorNumber = floorNumber;

        for (SpotType type : SpotType.values()) {
            freeSpots.put(
                    type,
                    new ConcurrentLinkedQueue<>()
            );
        }

        for (ParkingSpot spot : spots) {
            if (spot.getFloor() != floorNumber) {
                throw new IllegalArgumentException(
                        "Spot belongs to a different floor: "
                                + spot.getId()
                );
            }

            freeSpots.get(spot.getType()).add(spot);
        }
    }

    public Optional<ParkingSpot> claimSpot(SpotType type) {
        return Optional.ofNullable(
                freeSpots.get(type).poll()
        );
    }

    public void releaseSpot(ParkingSpot spot) {
        if (spot.getFloor() != floorNumber) {
            throw new IllegalArgumentException(
                    "Spot belongs to a different floor"
            );
        }

        freeSpots.get(spot.getType()).add(spot);
    }

    public int getFreeSpotCount(SpotType type) {
        return freeSpots.get(type).size();
    }

    public int getFloorNumber() {
        return floorNumber;
    }

//    public List<ParkingSpot> getSpots() {
//        return freeSpots.values()
//                .stream()
//                .flatMap(Queue::stream)
//                .toList();
//    }
public ArrayList<Object> getSpots() {
    ArrayList<Object> result = new ArrayList<>();

    for (Queue<ParkingSpot> queue : freeSpots.values()) {
        for (ParkingSpot spot : queue) {
            result.add(spot);
        }
    }

    return result;
}
}