package parking_lot_01.Version4;

public interface SpotAvailabilityListener {
    void onAvailabilityChange(int floor, SpotType spotType, int freeCount);
}
