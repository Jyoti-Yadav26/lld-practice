# Parking Lot - V1

## Why This Works

For one floor, one vehicle type, and one gate, this implementation is
correct and easy to understand.

The goal of V1 is to build the simplest working implementation before
introducing a more extensible LLD design.

> "Here's the simplest thing that works; now let me make it a proper design."

## Limitations

### 1. No Vehicle or Spot Types

The implementation does not distinguish between vehicle types or
parking spot types.

For example, a truck could be assigned a motorcycle spot.

### 2. Single Responsibility Violation

`ParkingLotV1` is responsible for:

- Finding parking spots
- Tracking vehicles
- Tracking entry time
- Calculating parking fees

Changing the pricing logic therefore requires modifying the parking
logic.

### 3. Not Thread-Safe

Two gates could call `park()` concurrently and both observe the same
spot as available.

For example:

Gate 1:
`occupied[7] == false`

Gate 2:
`occupied[7] == false`

Both could then assign spot 7.

### 4. Hard-Coded Rules

The pricing rule is hard-coded inside the parking lot.

Adding a new pricing strategy would require modifying the existing class,
which violates the Open/Closed Principle.

### 5. Real System Time

`System.currentTimeMillis()` is directly used inside the business logic.

This makes testing difficult because tests depend on the actual system
clock.

### 6. No Ticket Object

There is no dedicated ticket entity.

Therefore, the system cannot properly represent, validate, print, or
process a parking ticket during exit.

## Next Version

V2 will introduce a proper object-oriented design with:

- Vehicle types
- Parking spot types
- Parking floors
- Parking tickets
- Extensible pricing
- Payment methods
- Display boards
- Better testability
- Thread-safe spot allocation