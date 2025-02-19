package de.marek.appointment_booking.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record AvailableSlotsResponse(
    @JsonProperty("available_count") Long availableCount,
    @JsonProperty("start_date") LocalDateTime startDate) {
}
