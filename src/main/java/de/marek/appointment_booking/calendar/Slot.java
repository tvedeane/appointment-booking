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
    private Boolean booked;
    private Long salesManagerId;

    public Slot(Long id, LocalDateTime startDate, LocalDateTime endDate, Boolean booked, Long salesManagerId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.booked = booked;
        this.salesManagerId = salesManagerId;
    }

    public Slot() {
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public Boolean getBooked() {
        return booked;
    }

    public Long getSalesManagerId() {
        return salesManagerId;
    }
}
