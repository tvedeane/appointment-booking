package de.marek.appointment_booking.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record AvailableSlotsRequest(
    LocalDate date,
    List<String> products,
    String language,
    String rating) {
}
