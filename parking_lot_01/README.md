# Parking Lot System — Low-Level Design in Java

A step-by-step implementation of a Parking Lot system demonstrating object-oriented design, SOLID principles, design patterns, and concurrency concepts in Java.

The project evolves through four versions, with each version introducing improvements to the architecture and addressing limitations in the previous implementation.

## Table of Contents

- [Overview](#overview)
- [Version Evolution](#version-evolution)
- [Features](#features)
- [Architecture](#architecture)
- [Design Patterns](#design-patterns)
- [Parking Workflow](#parking-workflow)
- [Concurrency Considerations](#concurrency-considerations)
- [Project Structure](#project-structure)
- [Tech Stack](#tech-stack)
- [How to Run](#how-to-run)
- [Future Improvements](#future-improvements)
- [Learning Objectives](#learning-objectives)

## Overview

The Parking Lot system manages parking floors, spots, vehicles, tickets, pricing, payments, and spot availability.

The main objective is to design a maintainable and extensible system in which business rules are separated from the core parking workflow.

The project focuses on:

- Object-oriented programming and encapsulation
- SOLID design principles
- Strategy and Observer design patterns
- Separation of concerns
- Thread-safe data structures
- Ticket lifecycle and payment handling
- Extensible parking allocation and pricing policies

## Version Evolution

### V1 — Basic Implementation

The initial implementation focuses on basic parking functionality.

- Park and unpark vehicles.
- Maintain parking spot availability.
- Implement the core workflow in a straightforward manner.

**Limitation:** Business logic is concentrated in a single class, making the system difficult to extend and maintain.

### V2 — Object-Oriented Design

Responsibilities are distributed across domain classes.

- `Vehicle` represents a vehicle.
- `ParkingSpot` represents a parking spot.
- `ParkingFloor` manages spots on a floor.
- `Ticket` records parking information.
- `ParkingLot` coordinates the parking workflow.

**Improvement:** Better encapsulation and separation of responsibilities.

### V3 — Strategy and Observer Patterns

The system introduces interchangeable business rules and availability notifications.

**Strategy pattern**
- `SpotAllocationStrategy` abstracts parking spot selection.
- `FirstAvailableSpotStrategy` selects the first suitable available spot.
- `PricingStrategy` abstracts parking fee calculation.
- `HourlyPricingStrategy` calculates fees based on parking duration.

**Observer pattern**
- `SpotAvailabilityListener` defines the availability notification contract.
- `DisplayBoard` receives updates when spot availability changes.

**Improvement:** Allocation, pricing, and display updates are easier to extend without tightly coupling them to `ParkingLot`.

### V4 — Concurrency-Aware Design and Payment Lifecycle

V4 extends the architecture with payment processing, explicit ticket states, and concurrency-aware components.

Key additions include:

- `TicketStatus` with `ACTIVE`, `PAID`, and `CLOSED` states.
- `PaymentProcessor` abstraction for payment processing.
- `UpiPayment` as a simulated payment implementation.
- Configurable hourly rates by vehicle type.
- Concurrent queues for available parking spots.
- `ConcurrentHashMap` for concurrent map access.
- `CopyOnWriteArrayList` for listener management.
- `Clock` injection for controllable time in tests.
- A lock to coordinate parking operations within one `ParkingLot` instance.

**Improvement:** The system models payment and exit as explicit stages and introduces stronger coordination around shared state.

> V4 is an educational LLD implementation, not a production-ready parking or payment system. Thread safety across multiple application instances, payment idempotency, persistence, and crash recovery require additional work.

## Features

- Supports bikes, cars, and trucks.
- Supports small, compact, and large parking spots.
- Organizes spots across multiple floors.
- Allocates compatible spots using interchangeable strategies.
- Calculates fees using configurable hourly rates.
- Generates a unique ticket ID for each parking session.
- Tracks ticket status and payment amount.
- Prevents exit before payment.
- Updates the display board when availability changes.
- Uses concurrency-aware data structures for shared collections.

## Architecture

### Core domain classes

| Class | Responsibility |
|---|---|
| `Vehicle` | Represents a vehicle and its registration plate. |
| `ParkingSpot` | Represents a spot's identity, floor, and type. |
| `ParkingFloor` | Manages available spots on a floor. |
| `Ticket` | Stores vehicle, spot, entry time, payment amount, and status. |
| `ParkingLot` | Coordinates allocation, parking, payment, exit, and notifications. |

### Strategy interfaces

| Interface | Implementation | Responsibility |
|---|---|---|
| `SpotAllocationStrategy` | `FirstAvailableSpotStrategy` | Selects the first available compatible spot. |
| `SpotAllocationStrategy` | `BestFitLowestFloor` | Prioritizes the preferred compatible spot type, then floor order. |
| `PricingStrategy` | `HourlyPricingStrategy` | Calculates parking fees. |

### Payment and notifications

| Component | Responsibility |
|---|---|
| `PaymentProcessor` | Defines the payment processing contract. |
| `UpiPayment` | Simulates a UPI payment. |
| `SpotAvailabilityListener` | Defines the spot availability notification contract. |
| `DisplayBoard` | Stores and displays the latest reported availability. |

## Design Patterns

### 1. Strategy Pattern

The Strategy pattern encapsulates algorithms behind interfaces.

For example, `ParkingLot` can use different implementations of `SpotAllocationStrategy` without changing its main parking workflow.

Likewise, `PricingStrategy` allows the pricing algorithm to evolve independently.

**Benefits:**
- Reduces tight coupling.
- Supports the Open/Closed Principle.
- Makes business rules easier to test and replace.

### 2. Observer Pattern

The Observer pattern notifies registered listeners when parking availability changes.

The `DisplayBoard` implements `SpotAvailabilityListener` and receives updates from `ParkingLot`.

**Benefits:**
- Separates availability changes from display logic.
- Allows additional listeners to be introduced.
- Reduces direct dependencies between components.

### 3. Dependency Injection

Strategies, payment processing, and the clock are supplied to `ParkingLot` through its constructor.

This makes implementations replaceable and improves testability.

## Parking Workflow

The system follows this general workflow:

1. A vehicle requests a parking spot.
2. The allocation strategy searches for a compatible available spot.
3. The spot is claimed and a ticket is created.
4. The display board receives an availability update.
5. The vehicle owner requests payment.
6. The pricing strategy calculates the fee.
7. The payment processor attempts the transaction.
8. On successful payment, the ticket transitions to `PAID`.
9. On exit, the ticket transitions to `CLOSED` and the spot is released.
10. The display board receives the updated availability.

### Ticket lifecycle

`ACTIVE → PAID → CLOSED`

- **ACTIVE:** The parking session is in progress and payment has not been recorded.
- **PAID:** Payment has been recorded successfully.
- **CLOSED:** The vehicle has exited and the parking session has ended.

The implementation should reject invalid transitions, such as closing an unpaid ticket.

## Concurrency Considerations

V4 introduces several concurrency-related mechanisms.

### ConcurrentLinkedQueue

Used to manage available spots. The `poll()` operation allows competing threads to claim different entries from a shared queue without both removing the same queue entry.

### ConcurrentHashMap

Used for concurrent access to maps such as ticket and availability registries.

Individual map operations are thread-safe, but multiple related operations are not automatically atomic.

### CopyOnWriteArrayList

Used for listener registration and iteration. It is useful when listener reads and iteration are more frequent than modifications.

### ReentrantLock

Coordinates related parking operations within a single `ParkingLot` instance.

**Important limitations:**

- A thread-safe collection does not make the entire business workflow atomic.
- The same spot must not be returned to an availability queue more than once.
- Payment retries must not cause duplicate charges.
- A JVM lock does not coordinate separate application instances.
- Real payment processing needs idempotency and recovery mechanisms.
- External callbacks should be handled carefully to avoid blocking or re-entrant operations while holding a lock.

These are useful areas for further improvement and interview discussion.

## Project Structure

The project contains versioned implementations to make architectural changes easy to compare.

```text
lld-practice/
├── README.md
└── src/
    └── parking_lot_01/
        ├── version1/
        ├── version2/
        ├── version3/
        └── version4/
            ├── Main.java
            ├── Vehicle.java
            ├── VehicleType.java
            ├── SpotType.java
            ├── SpotRules.java
            ├── ParkingSpot.java
            ├── ParkingFloor.java
            ├── ParkingLot.java
            ├── Ticket.java
            ├── TicketStatus.java
            ├── SpotAllocationStrategy.java
            ├── FirstAvailableSpotStrategy.java
            ├── BestFitLowestFloor.java
            ├── PricingStrategy.java
            ├── HourlyPricingStrategy.java
            ├── PaymentProcessor.java
            ├── UpiPayment.java
            ├── SpotAvailabilityListener.java
            └── DisplayBoard.java
```


## Tech Stack

- **Language:** Java
- **Concurrency:** `ConcurrentLinkedQueue`, `ConcurrentHashMap`, `CopyOnWriteArrayList`, `ReentrantLock`
- **Core concepts:** OOP, SOLID principles, interfaces, dependency injection, design patterns
- **Testing:** Manual execution through `Main.java`; automated tests can be added

## How to Run

### Prerequisites

- Java Development Kit (JDK) 17 or compatible.
- IntelliJ IDEA or another Java IDE.

### Steps

1. Clone the repository:

   ```bash
   git clone https://github.com/Jyoti-Yadav26/lld-practice.git
   ```

2. Open the project in IntelliJ IDEA.

3. Ensure the source root and package declarations match the project structure.

4. Open `Main.java` in the V4 package.

5. Run the `main()` method.

The sample program creates parking spots and floors, configures allocation and pricing, registers a display board, parks a vehicle, processes a simulated payment, and releases the spot.

If the project uses a different source directory or package layout, adjust the run configuration accordingly.

## Future Improvements

- Add automated unit tests and concurrency stress tests.
- Make spot release idempotent and prevent duplicate queue entries.
- Add payment idempotency and transaction recovery.
- Add persistent storage for tickets, vehicles, and parking spots.
- Introduce a database transaction boundary for state changes.
- Support multiple parking lot instances with shared coordination.
- Add reservation and cancellation workflows.
- Add vehicle entry and exit gates.
- Add lost-ticket handling and refunds.
- Improve error handling and structured logging.
- Add integration tests for allocation, payment failures, and concurrent requests.

## Learning Objectives

This project is intended for practicing low-level design and preparing for software engineering interviews.

By comparing V1 through V4, you can study how a basic implementation evolves toward a more modular design, how patterns address changing requirements, and why concurrency requires more than simply replacing collections with thread-safe alternatives.

The goal is not just to implement a working parking lot, but to understand the trade-offs behind its design decisions.
