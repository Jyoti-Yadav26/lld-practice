package parking_lot_01.version3;

public class DisplayBoard implements SpotAvailabilityListener{
    @Override
    public void onAvailabilityChange(int floor, SpotType spotType, int freeCount) {
        System.out.println(
                "Floor " + floor +
                        " | " + spotType +
                        " spots available: " + freeCount
        );
    }
}
