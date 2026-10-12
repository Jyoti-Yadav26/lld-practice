package parking_lot_01.Version4;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.UUID;

public class ParkingLot {

    private final List<ParkingFloor> floors;
    private final SpotAllocationStrategy allocationStrategy;
    private final PricingStrategy pricingStrategy;
    private final PaymentProcessor paymentProcessor;
    private final Clock clock;

    private final Map<String, Ticket> activeTickets =
            new ConcurrentHashMap<>();

    private final Map<String, String> plateToTicket =
            new ConcurrentHashMap<>();

    private final List<SpotAvailabilityListener> listeners =
            new CopyOnWriteArrayList<>();

    private final ReentrantLock operationLock = new ReentrantLock();

    public ParkingLot(
            List<ParkingFloor> floors,
            SpotAllocationStrategy allocationStrategy,
            PricingStrategy pricingStrategy,
            PaymentProcessor paymentProcessor,
            Clock clock
    ) {
        this.floors = List.copyOf(floors);
        this.allocationStrategy = allocationStrategy;
        this.pricingStrategy = pricingStrategy;
        this.paymentProcessor = paymentProcessor;
        this.clock = clock;
    }

    public void addListener(SpotAvailabilityListener listener) {
        listeners.add(listener);

        for (ParkingFloor floor : floors) {
            for (SpotType type : SpotType.values()) {
                listener.onAvailabilityChange(
                        floor.getFloorNumber(),
                        type,
                        floor.getFreeSpotCount(type)
                );
            }
        }
    }

    public Optional<Ticket> park(Vehicle vehicle) {
        operationLock.lock();

        try {
            if (plateToTicket.containsKey(vehicle.plate())) {
                return Optional.empty();
            }

            Optional<ParkingSpot> allocated =
                    allocationStrategy.allocate(floors, vehicle);

            if (allocated.isEmpty()) {
                return Optional.empty();
            }

            ParkingSpot spot = allocated.get();

            Ticket ticket = new Ticket(
                    UUID.randomUUID().toString(),
                    vehicle,
                    spot,
                    Instant.now(clock)
            );

            activeTickets.put(ticket.getId(), ticket);
            plateToTicket.put(vehicle.plate(), ticket.getId());

            notifyAvailability(spot);

            return Optional.of(ticket);

        } finally {
            operationLock.unlock();
        }
    }

    public long pay(String ticketId) {
        operationLock.lock();

        try {
            Ticket ticket = activeTickets.get(ticketId);

            if (ticket == null) {
                throw new IllegalArgumentException(
                        "Active ticket not found"
                );
            }

            if (ticket.getStatus() != TicketStatus.ACTIVE) {
                throw new IllegalStateException(
                        "Ticket is not awaiting payment"
                );
            }

            long amount = pricingStrategy.calculatePrice(
                    ticket,
                    Instant.now(clock)
            );

            if (!paymentProcessor.processPayment(ticketId, amount)) {
                throw new IllegalStateException(
                        "Payment failed"
                );
            }

            ticket.markPaid(amount);
            return amount;

        } finally {
            operationLock.unlock();
        }
    }

    public void unpark(Ticket ticket) {
        operationLock.lock();

        try {
            Ticket stored = activeTickets.get(ticket.getId());

            if (stored == null || stored != ticket) {
                throw new IllegalArgumentException(
                        "Ticket is not active in this parking lot"
                );
            }

            if (stored.getStatus() != TicketStatus.PAID) {
                throw new IllegalStateException(
                        "Payment must be completed before exit"
                );
            }

            ParkingSpot spot = stored.getSpot();

            stored.close();

            activeTickets.remove(stored.getId());
            plateToTicket.remove(stored.getVehicle().plate());

            ParkingFloor floor = findFloor(spot);
            floor.releaseSpot(spot);

            notifyAvailability(spot);

        } finally {
            operationLock.unlock();
        }
    }

    private ParkingFloor findFloor(ParkingSpot spot) {
        return floors.stream()
                .filter(floor ->
                        floor.getFloorNumber() == spot.getFloor())
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Parking floor not found"
                        ));
    }

    private void notifyAvailability(ParkingSpot spot) {
        ParkingFloor floor = findFloor(spot);

        int freeCount = floor.getFreeSpotCount(spot.getType());

        for (SpotAvailabilityListener listener : listeners) {
            listener.onAvailabilityChange(
                    floor.getFloorNumber(),
                    spot.getType(),
                    freeCount
            );
        }
    }
}
