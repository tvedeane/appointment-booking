package de.marek.appointment_booking.calendar;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

@Table(name = "sales_managers")
@Entity
public final class SalesManager {
    @Id
    private Long id;
    @Column(name = "products", columnDefinition = "text[]")
    private List<String> products;

    public SalesManager(Long id) {
        this.id = id;
    }

    public SalesManager() {
    }

    public Long getId() {
        return id;
    }
}
