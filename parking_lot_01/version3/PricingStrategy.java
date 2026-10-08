package parking_lot_01.version3;

import java.time.Instant;

public interface PricingStrategy {
    long calculatePrice(Ticket ticket, Instant exitTime);
}
