package parking_lot_01.Version4;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DisplayBoard implements SpotAvailabilityListener {

    private final Map<String, Integer> availability =
            new ConcurrentHashMap<>();

    @Override
    public void onAvailabilityChange(
            int floor,
            SpotType spotType,
            int freeCount
    ) {
        String key = floor + "-" + spotType;
        availability.put(key, freeCount);

        System.out.println(
                "Floor " + floor
                        + " | " + spotType
                        + " | Available: " + freeCount
        );
    }

    public int getFreeSpotCount(int floor, SpotType spotType) {
        return availability.getOrDefault(
                floor + "-" + spotType,
                0
        );
    }
}
