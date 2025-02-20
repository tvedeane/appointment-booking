# Appointment Booking Finder

A Java Spring Boot backend that finds available time slots that don't overlap with existing bookings and match the requested criteria. The service is particularly optimized for scenarios where all bookings are exactly one hour long and booked slots never overlap with each other.

## Problem Statement

Given a set of time slots in a database where:
- Each slot has a start time and is exactly 1 hour long
- Each slot is marked as either booked (true) or available (false)
- Booked slots never overlap with other booked slots
- Available slots can overlap with any other slots
- Slots are assigned to a manager that has specific criteria (spoken language, product(s) to discuss, and internal customer rating)

Find all available slots that don't overlap with any booked slots and match the manager's criterias.

## Algorithm

The service implements a simple algorithm to efficiently find non-overlapping slots:
1. Collects all booked slot start times for a given day
2. For each available slot, checks if it overlaps with any booked slot

### Example

```text
Booked slot: 10:00-11:00
Available slots status:
✅ 9:00-10:00  (ends when booked starts)
❌ 9:30-10:30  (ends during booked)
❌ 10:00-11:00 (exact overlap)
❌ 10:30-11:30 (starts during booked)
✅ 11:00-12:00 (starts when booked ends)
```

## Getting Started


### Prerequisites
- Docker installed and running
- Java 21 or higher
- Maven
- curl (for testing) or Postman

### Database Setup

1. Navigate to the database directory:
```bash
cd db
```

2. Build the Docker image:
```bash
docker build -t enpal-coding-challenge-db .
```

3. Run the database container:
```bash
docker run --name enpal-coding-challenge-db -p 5432:5432 -d enpal-coding-challenge-db
```

### Application Setup

4. Start the Spring Boot application using either:
  - Maven command line:
   ```bash
   mvn spring-boot:run
   ```
  - Or through IntelliJ IDEA

### Testing the Endpoint

5. Send a test request:
```bash
curl -X POST 'http://localhost:3000/calendar/query' \
  -H 'Content-Type: application/json' \
  -d '{
    "date": "2024-05-03",
    "products": ["SolarPanels"],
    "language": "German",
    "rating": "Gold"
  }'
```

6. Expected response:
```json
[
  {
    "available_count": 1,
    "start_date": "2024-05-03T10:30:00.000Z"
  },
  {
    "available_count": 1,
    "start_date": "2024-05-03T11:00:00.000Z"
  },
  {
    "available_count": 1,
    "start_date": "2024-05-03T11:30:00.000Z"
  }
]
```

## Technology Stack

- Spring Boot 3 with Spring Data JPA
- Postgresql
- Maven
- For testing: Junit 5, Mockito and AssertJ

## Time Complexity Analysis

While the theoretical time complexity is `O(n * m)` where `n` is the number of non-booked slots and `m` is the number of booked slots, in practice the performance is much better due to natural constraints:

- There are only 24 hours in a day
- Assuming 30-minute slot granularity, there can be at most 48 slots per day
- One booked slot automatically blocks out several potential start times

This means that both `n` and `m` have a practical upper bound, making the algorithm effectively constant time `O(1)` for any given day. The actual performance will primarily depend on database access patterns rather than the overlap checking algorithm.

## Future Development

Potential improvements and features to consider:
- add indexes to the DB:
  - application seems to be read-heavy, so adding indexes (that generally slow down writes) is a good tradeoff
  - the columns in `sales_managers` are arrays, so we might add [GIN indexes](https://www.postgresql.org/docs/current/gin.html) on them, but they might slow down writes too much, so we can skip them for now (the arrays cannot be too big considering the column size of 100 characters)
  - querying the slots using the dates can be made faster after adding `CREATE INDEX idx_slots_dates ON slots (start_date, end_date);`
  - index on `slots.booking` is unnecessary because we use this field on the application side
- to prevent queries from slowing down over time, past entries could be removed from the `slots` table (or moved to another table if access to old data is needed) what could be done by a background job
- managing schema should be done using e.g. Flyway
- validate input on the REST API, e.g. handle empty `products` array returning clear message `"Unknown product specified"`
