package parking_lot_01.Version4;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class HourlyPricingStrategy implements PricingStrategy {

    private final Map<VehicleType, Long> ratePerHour;

    public HourlyPricingStrategy(Map<VehicleType, Long> ratePerHour) {
        this.ratePerHour = Map.copyOf(ratePerHour);
    }

    @Override
    public long calculatePrice(Ticket ticket, Instant exitTime) {

        long minutes = Duration.between(
                ticket.getEntryTime(),
                exitTime
        ).toMinutes();

        long hours = Math.max(1, (minutes + 59) / 60);

        long rate = ratePerHour.get(ticket.getVehicle().type());

        return Math.multiplyExact(hours, rate);
    }
}

