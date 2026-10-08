package parking_lot_01.version3;

public interface SpotAvailabilityListener {
    void onAvailabilityChange(int floor,SpotType spotType,int freeCount);
}
