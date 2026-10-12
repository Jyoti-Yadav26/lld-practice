package parking_lot_01.Version4;

import java.time.Instant;

public interface PricingStrategy {
    long calculatePrice(Ticket ticket, Instant exitTime);
}
