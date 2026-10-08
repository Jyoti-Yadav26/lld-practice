package parking_lot_01.src;

import java.util.HashMap;
import java.util.Map;
import static java.lang.System.currentTimeMillis;

public class ParkingLotV1{
    private final boolean[] occupied= new boolean[100];
    private final Map<String,Integer> plateToSpot= new HashMap<>();
    private final Map<String, Long> entryTime= new HashMap<>();

    public int park(String plate){
        for (int i = 0; i < occupied.length; i++) {
            if(!occupied[i]){
                occupied[i]=true;
                plateToSpot.put(plate,i);
                entryTime.put(plate,currentTimeMillis());
                return i;
            }
        }
        throw new IllegalStateException("Lot is full");
    }
    public long unPark(String plate){
        int spot= plateToSpot.remove(plate);
        occupied[spot]=false;
        long hours= (currentTimeMillis() - entryTime.remove(plate))/3600000+1;

        return hours*50;
    }

    public static class Main {
        public static void main(String[] args) {
            ParkingLotV1 p= new ParkingLotV1();
            String plate = "abcdef";
            int spot = p.park(plate);
            System.out.println("Plate No. "+plate+" parked at : "+spot);
            long payment = p.unPark(plate);
            System.out.println("Please pay: "+payment);
        }
    }
}