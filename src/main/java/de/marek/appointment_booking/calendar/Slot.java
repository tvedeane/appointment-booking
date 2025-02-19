package de.marek.appointment_booking.calendar;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Table(name = "slots")
@Entity
public final class Slot {
    @Id
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
