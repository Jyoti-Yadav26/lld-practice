# Parking Lot - V2

## Overview

V2 refactors the simple V1 implementation into separate entities with
clear responsibilities.

The main goal is to avoid the "god class" design from V1 and make the
system easier to extend.

## Design

### Vehicle

Represents a vehicle using:

- License plate
- Vehicle type

`VehicleType` is represented using an enum:

- BIKE
- CAR
- TRUCK

A `record` is used because the vehicle currently only contains data
and does not have type-specific behavior.

### ParkingSpot

Represents a single parking spot.

Responsibilities:

- Store spot ID
- Store floor number
- Store spot type
- Track the parked vehicle
- Determine whether the spot is free
- Occupy and release the spot

### SpotType

Represents the size/type of a parking spot:

- SMALL
- COMPACT
- LARGE

### SpotRules

Contains the compatibility rules between vehicle types and parking
spot types.

Example:

- BIKE → SMALL, COMPACT, LARGE
- CAR → COMPACT, LARGE
- TRUCK → LARGE

The allowed spot types are ordered from smallest to largest so that
the system can prefer the smallest suitable spot.

## Design Decisions

### Enum vs Inheritance

Vehicle types are represented using an enum instead of creating
`Car`, `Bike`, and `Truck` subclasses.

Currently, these vehicle types only differ in their compatibility
rules and do not have different behavior.

If different vehicle types acquire distinct behavior in the future,
inheritance can be introduced.

### Separation of Responsibilities

V2 separates vehicle data, parking spot state, and compatibility
rules into different components.

This makes each component easier to understand, test, and modify.

## Limitations

V2 does not yet implement:

- Parking floors
- Parking tickets
- Parking lot coordination
- Pricing strategies
- Payment methods
- Display boards
- Thread-safe spot allocation
- Injected time/clock

Spot selection is also not yet implemented at the `ParkingFloor` level.

## Next Version

The next version will introduce:

- `ParkingFloor`
- `Ticket`
- `ParkingLot`
- Spot allocation
- Entry and exit flow