package de.marek.appointment_booking.calendar;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "slots")
@Entity
public final class Slot {
    @Id
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean booked;
    @ManyToOne
    private SalesManager salesManager;

    public Slot(Long id, LocalDateTime startDate, LocalDateTime endDate, Boolean booked, SalesManager salesManager) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.booked = booked;
        this.salesManager = salesManager;
    }

    public Slot() {
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public Boolean getBooked() {
        return booked;
    }

    public SalesManager getSalesManager() {
        return salesManager;
    }
}
