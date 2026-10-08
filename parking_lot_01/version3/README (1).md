# Parking Lot - V3

## Overview

V3 introduces the **Strategy** and **Observer** design patterns to make
the Parking Lot system more flexible and easier to extend.

The main goal is to separate changing business rules such as spot
allocation and pricing from the core parking flow.

## Design

### ParkingFloor

Represents a single parking floor and manages the parking spots on that
floor.

Responsibilities:

- Store parking spots
- Find a compatible free spot
- Count free spots by spot type
- Manage floor-level parking information

### Ticket

Represents a parking ticket issued when a vehicle enters the parking
lot.

Stores:

- Ticket ID
- Vehicle
- Parking spot
- Entry time

### ParkingLot

Acts as the main coordinator of the parking system.

Responsibilities:

- Park vehicles
- Unpark vehicles
- Coordinate spot allocation
- Coordinate pricing
- Notify availability listeners

`ParkingLot` delegates variable behavior to strategies instead of
implementing those rules directly.

## Strategy Pattern

The Strategy pattern is used for behavior that may change independently.

### Spot Allocation

```text
SpotAllocationStrategy
        ↓
FirstAvailableSpotStrategy
```

`SpotAllocationStrategy` defines how a parking spot is selected.

`FirstAvailableSpotStrategy` searches the floors and returns the first
compatible free spot.

Other allocation strategies can be added later without modifying
`ParkingLot`.

Possible future strategies:

- `BestFitSpotStrategy`
- `NearestToGateStrategy`
- `EVFirstStrategy`

### Pricing

```text
PricingStrategy
        ↓
HourlyPricingStrategy
```

`PricingStrategy` defines how the parking fee is calculated.

`HourlyPricingStrategy` currently:

- Charges ₹50 per hour
- Rounds partial hours up
- Applies a minimum charge of one hour

Other pricing rules can be introduced without modifying `ParkingLot`.

Possible future strategies:

- Weekend pricing
- Flat-rate pricing
- Night pricing
- Dynamic pricing

## Observer Pattern

The Observer pattern is used to notify interested components when
parking availability changes.

```text
SpotAvailabilityListener
        ↓
DisplayBoard
```

`SpotAvailabilityListener` defines the notification contract.

`DisplayBoard` receives availability updates and displays the number of
free spots for each spot type on a floor.

Other components can subscribe in the future without changing the
parking logic.

Possible future listeners:

- Mobile application
- Admin dashboard
- Monitoring system

## Design Decisions

### Strategy vs Conditional Logic

Instead of placing allocation and pricing rules directly inside
`ParkingLot`, these behaviors are represented using interfaces.

This allows new strategies to be added without modifying existing
parking flow.

### Observer for Availability

`ParkingLot` does not directly depend on a specific display mechanism.

It notifies `SpotAvailabilityListener` implementations when spot
availability changes.

This allows additional observers to be introduced without changing
`ParkingLot`.

### Dependency Injection

`ParkingLot` receives its allocation strategy, pricing strategy, and
listeners through its constructor.

This makes the system easier to test and allows different behaviors to
be plugged in.

## Parking Flow

### Park

```text
Vehicle
   ↓
ParkingLot
   ↓
SpotAllocationStrategy
   ↓
ParkingSpot
   ↓
occupy()
   ↓
Create Ticket
   ↓
Notify Availability Listeners
```

### Unpark

```text
Ticket
   ↓
ParkingLot
   ↓
PricingStrategy
   ↓
Calculate Fee
   ↓
ParkingSpot.release()
   ↓
Notify Availability Listeners
```

## Improvements Over V2

V2:

- Basic entities and responsibilities
- Fixed spot selection logic
- No pricing abstraction
- No availability notification

V3:

- Introduces Strategy pattern for spot allocation
- Introduces Strategy pattern for pricing
- Introduces Observer pattern for availability updates
- Introduces `ParkingLot` as the system coordinator
- Makes changing behaviors easier to extend

## Limitations

V3 does not yet implement:

- Multiple entry and exit gates
- Thread-safe spot allocation
- Concurrent parking requests
- Payment processing
- Lost ticket handling
- Ticket lifecycle
- Idempotent exit/payment
- Database persistence
- REST APIs

These concerns can be addressed in later versions.

## Next Version

The next version will introduce:

- Thread-safe spot allocation
- Concurrent entry gates
- Payment methods
- Ticket lifecycle
- Idempotent exit and payment
- More robust parking flow
