package parking_lot_01.version3;

public class ParkingSpot {

    private final String id;
    private final int floor;
    private final SpotType type;

    private Vehicle parked;

    public ParkingSpot(String id, int floor, SpotType type) {
        this.id = id;
        this.floor = floor;
        this.type = type;
    }

    public boolean isFree() {
        return parked == null;
    }

    public void occupy(Vehicle vehicle) {
        if (!isFree()) {
            throw new IllegalStateException("Parking spot is already occupied");
        }

        parked = vehicle;
    }

    public void release() {
        if (isFree()) {
            throw new IllegalStateException("Parking spot is already free");
        }

        parked = null;
    }

    public String getId() {
        return id;
    }

    public int getFloor() {
        return floor;
    }

    public SpotType getType() {
        return type;
    }

    public Vehicle getParkedVehicle() {
        return parked;
    }
    @Override
    public String toString() {
        return "ParkingSpot{" +
                "id='" + id + '\'' +
                ", floor=" + floor +
                ", type=" + type +
                '}';
    }
}

