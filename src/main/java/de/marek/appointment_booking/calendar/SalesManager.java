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
    @Column(name = "languages", columnDefinition = "text[]")
    private List<String> languages;
    @Column(name = "customer_ratings", columnDefinition = "text[]")
    private List<String> ratings;

    public SalesManager(Long id) {
        this.id = id;
    }

    public SalesManager() {
    }

    public Long getId() {
        return id;
    }
}
