package parking_lot_01.version3;

import java.time.Duration;
import java.time.Instant;

public class HourlyPricingStrategy implements PricingStrategy{

    @Override
    public long calculatePrice(Ticket ticket, Instant exitTime) {
        Instant time = ticket.getEntryTime();

        Duration duration = Duration.between(time,exitTime);
        long minutes = duration.toMinutes();
        long hours = Math.max(1, (minutes + 59) / 60);

        return 50*hours;
    }
}
